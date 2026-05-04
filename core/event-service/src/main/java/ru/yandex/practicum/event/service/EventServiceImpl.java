package ru.yandex.practicum.event.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.*;

import jakarta.servlet.http.HttpServletRequest;

import ru.yandex.practicum.category.model.Category;
import ru.yandex.practicum.category.repository.CategoryRepository;
import ru.yandex.practicum.comment.client.CommentClient;
import ru.yandex.practicum.dto.ViewStatsDto;
import ru.yandex.practicum.event.controller.EventSortBy;
import ru.yandex.practicum.event.dto.EventState;
import ru.yandex.practicum.event.dto.*;
import ru.yandex.practicum.event.mapper.EventMapper;
import ru.yandex.practicum.event.mapper.LocationMapper;
import ru.yandex.practicum.event.model.Event;
import ru.yandex.practicum.event.model.EventInfo;
import ru.yandex.practicum.event.model.Location;
import ru.yandex.practicum.event.repository.EventRepository;
import ru.yandex.practicum.exception.ForbiddenAccessException;
import ru.yandex.practicum.exception.IllegalEventUpdateException;
import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.exception.ValidationException;
import ru.yandex.practicum.request.client.RequestClient;
import ru.yandex.practicum.stats.client.StatsClient;
import ru.yandex.practicum.user.client.UserClient;
import ru.yandex.practicum.user.dto.UserShortDto;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    private static final Duration MIN_TIME_BEFORE_EVENT = Duration.ofHours(2);
    private static final String EVENTS_URI = "/events/%d";
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserClient userClient;
    private final StatsClient statsClient;
    private final CommentClient commentClient;
    private final RequestClient requestClient;

    @Override
    public EventFullDto getById(Long eventId, HttpServletRequest request) {
        Optional<Event> eventOptional = eventRepository.findByIdAndState(eventId, EventState.PUBLISHED);
        Event event = eventOptional.orElseThrow(NotFoundException.supplier("Event with id=%d not found", eventId));

        statsClient.hit(request);
        String uri = request.getRequestURI();

        ViewStatsDto statsDto = getStatsForEvent(event, uri);

        Set<Event> eventSet = Set.of(event);

        Map<Long, Long> confirmedRequests = getConfirmedRequests(eventSet);

        Map<Long, Long> commentariesCount = getCommentariesCount(eventSet);

        UserShortDto user = userClient.getUser(event.getInitiatorId());

        return EventMapper.mapToFullDto(
            event,
            user,
            confirmedRequests.getOrDefault(event.getId(), 0L),
            statsDto.hits(),
            commentariesCount.getOrDefault(event.getId(), 0L));
    }

    @Override
    public Collection<EventShortDto> getEvents(EventsPublicGetRequest getRequest) {
        Page<Event> events = eventRepository
            .findAll(EventRepository.createPredicate(getRequest), getRequest.getPageable());

        statsClient.hit(getRequest.httpRequest());

        Set<Event> eventsSet = events.stream()
            .collect(Collectors.toSet());

        Map<Long, Long> statsForEvents = getStatsMapForEvents(events);

        Map<Long, Long> confirmedRequests = getConfirmedRequests(eventsSet);

        Map<Long, Long> commentariesCount = getCommentariesCount(eventsSet);

        Map<Long, UserShortDto> userShortDtoMap = getInitiatorsForEvents(events);

        List<EventShortDto> eventsList = events.stream()
            .map(
                event -> EventMapper.mapToShortDto(
                    event,
                    userShortDtoMap.get(event.getInitiatorId()),
                    confirmedRequests.getOrDefault(event.getId(), 0L),
                    statsForEvents.get(event.getId()),
                    commentariesCount.getOrDefault(event.getId(), 0L)))
            .toList();

        if (EventSortBy.VIEWS.equals(getRequest.sort())) {
            return eventsList.stream()
                .sorted(Comparator.comparing(EventShortDto::views))
                .toList();
        }

        return eventsList;
    }

    @Override
    public Collection<EventFullDto> getEvents(EventsAdminGetRequest getRequest) {
        Page<Event> events = eventRepository
            .findAll(EventRepository.createPredicate(getRequest), getRequest.getPageable());

        Map<Long, Long> statsForEvents = getStatsMapForEvents(events);

        Map<Long, Long> confirmedRequests = getConfirmedRequests(
            events.stream()
                .collect(Collectors.toSet()));

        Map<Long, UserShortDto> userShortDtoMap = getInitiatorsForEvents(events);

        return events.stream()
            .map(
                event -> EventMapper.mapToFullDto(
                    event,
                    userShortDtoMap.get(event.getInitiatorId()),
                    confirmedRequests.getOrDefault(event.getId(), 0L),
                    statsForEvents.get(event.getId()),
                    null))
            .toList();
    }

    private Map<Long, UserShortDto> getInitiatorsForEvents(Page<Event> events) {
        List<Long> eventInitiators = events.stream()
            .map(Event::getInitiatorId)
            .toList();
        List<UserShortDto> usersByIds = userClient.getUsersByIds(eventInitiators);
        return usersByIds.stream()
            .collect(Collectors.toMap(UserShortDto::id, Function.identity()));
    }

    @Override
    public Collection<EventShortDto> getEvents(EventsPrivateGetRequest getRequest) {
        userClient.getUser(getRequest.userId());

        Page<Event> events = eventRepository.findByInitiatorId(getRequest.userId(), getRequest.getPageable());

        Map<Long, Long> statsForEvents = getStatsMapForEvents(events);

        Map<Long, Long> confirmedRequests = getConfirmedRequests(
            events.stream()
                .collect(Collectors.toSet()));

        Map<Long, UserShortDto> userShortDtoMap = getInitiatorsForEvents(events);

        return events.stream()
            .map(
                event -> EventMapper.mapToShortDto(
                    event,
                    userShortDtoMap.get(event.getInitiatorId()),
                    confirmedRequests.getOrDefault(event.getId(), 0L),
                    statsForEvents.get(event.getId()),
                    null))
            .toList();
    }

    @Override
    @Transactional
    public EventFullDto createEvent(Long userId, NewEventDto newEventDto) {
        Location location = LocationMapper.mapToEntity(newEventDto.location());
        Category category = getCategoryByIdOrThrow(newEventDto.category());
        UserShortDto user = userClient.getUser(userId);
        Event event = EventMapper.mapToEntity(newEventDto, category, user, location);

        LocalDateTime now = LocalDateTime.now();
        if (event.getEventDate()
            .isBefore(now.plus(MIN_TIME_BEFORE_EVENT))) {
            throw new ValidationException(
                "The event must be scheduled at least %d hours from now.".formatted(MIN_TIME_BEFORE_EVENT.toHours()));
        }
        Event saved = eventRepository.save(event);

        return EventMapper.mapToFullDto(saved, user, 0, 0L, 0L);
    }

    @Override
    public EventFullDto getByUserById(Long userId, Long eventId) {
        UserShortDto user = userClient.getUser(userId);

        Event event = getEventByIdOrThrow(eventId);

        if (!event.getInitiatorId()
            .equals(user.id())) {
            throw new ForbiddenAccessException("You can't view event that's not yours");
        }

        ViewStatsDto statsDto = getStatsForEvent(event, EVENTS_URI.formatted(eventId));

        Map<Long, Long> confirmedRequests = getConfirmedRequests(Set.of(event));

        return EventMapper
            .mapToFullDto(event, user, confirmedRequests.getOrDefault(event.getId(), 0L), statsDto.hits(), null);
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest updateRequest) {
        Event event = getEventByIdOrThrow(eventId);

        if ((event.getState()
            .equals(EventState.PUBLISHED)
            || event.getState()
                .equals(EventState.CANCELED))
            && updateRequest.hasStateAction()) {
            throw new IllegalEventUpdateException(
                "Forbidden to update event that already %s".formatted(
                    event.getState()
                        .toString()));
        }

        Category newCategory = null;
        if (updateRequest.hasCategory()) {
            newCategory = getCategoryByIdOrThrow(updateRequest.category());
        }
        EventMapper.updateEventFromDto(event, updateRequest, newCategory);

        Event saved = eventRepository.save(event);

        Map<Long, Long> confirmedRequests = getConfirmedRequests(Set.of(event));

        UserShortDto user = userClient.getUser(event.getInitiatorId());

        return EventMapper.mapToFullDto(saved, user, confirmedRequests.getOrDefault(event.getId(), 0L), null, null);
    }

    @Override
    @Transactional
    public EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventUserRequest updateRequest) {
        Event event = getEventByIdOrThrow(eventId);
        UserShortDto user = userClient.getUser(userId);

        if (!event.getInitiatorId()
            .equals(user.id())) {
            throw new ForbiddenAccessException("You can't update event that's not yours");
        }

        if ((event.getState()
            .equals(EventState.PUBLISHED)
            || event.getState()
                .equals(EventState.CANCELED))
            && !updateRequest.hasStateAction()) {
            throw new IllegalEventUpdateException(
                "Forbidden to update event that already %s".formatted(
                    event.getState()
                        .toString()));
        }

        Category newCategory = null;
        if (updateRequest.hasCategory()) {
            newCategory = getCategoryByIdOrThrow(updateRequest.category());
        }
        EventMapper.updateEventFromDto(event, updateRequest, newCategory);

        Event saved = eventRepository.save(event);

        Map<Long, Long> confirmedRequests = getConfirmedRequests(Set.of(event));

        return EventMapper.mapToFullDto(saved, user, confirmedRequests.getOrDefault(event.getId(), 0L), null, null);
    }

    @Override
    public boolean existsById(Long id) {
        return eventRepository.existsById(id);
    }

    @Override
    public EventInfoDto getEventById(Long eventId) {
        Optional<EventInfo> eventInfoById = eventRepository.findEventInfoById(eventId);
        EventInfo eventInfo = eventInfoById
            .orElseThrow(NotFoundException.supplier("Event with id %d not found", eventId));

        return EventMapper.maptoEventInfoDto(eventInfo);
    }

    private Map<Long, Long> getCommentariesCount(Set<Event> events) {
        if (events.isEmpty()) {
            return Map.of();
        }

        List<Long> eventIds = events.stream()
            .map(Event::getId)
            .toList();

        return commentClient.countComments(eventIds);
    }

    private Map<Long, Long> getStatsMapForEvents(Page<Event> events) {
        if (events.isEmpty()) {
            return Collections.emptyMap();
        }
        List<String> listOfUris = events.stream()
            .map(event -> EVENTS_URI.formatted(event.getId()))
            .toList();
        LocalDateTime minimalPublishDate = events.stream()
            .map(Event::getPublishedOn)
            .filter(Objects::nonNull)
            .min(LocalDateTime::compareTo)
            .orElse(LocalDateTime.of(1000, 1, 1, 1, 1));

        return getStatsForEvents(listOfUris, minimalPublishDate).stream()
            .collect(
                Collectors.toMap(
                    statsDto -> Long.valueOf(
                        statsDto.uri()
                            .substring(
                                statsDto.uri()
                                    .lastIndexOf('/') + 1)),
                    ViewStatsDto::hits));
    }

    private Map<Long, Long> getConfirmedRequests(Set<Event> events) {
        if (events.isEmpty()) {
            return Map.of();
        }

        List<Long> eventIds = events.stream()
            .map(Event::getId)
            .toList();

        return requestClient.countConfirmed(eventIds);
    }

    private List<ViewStatsDto> getStatsForEvents(List<String> uris, LocalDateTime startDate) {
        try {
            return statsClient.getStats(startDate, LocalDateTime.now(), uris, true);
        } catch (RestClientException e) {
            log.error("Error during getting stats for events", e);
        }
        return List.of();
    }

    private ViewStatsDto getStatsForEvent(Event event, String uri) {
        if (event.getPublishedOn() == null) {
            return new ViewStatsDto(null, null, null);
        }

        LocalDateTime startDate = event.getPublishedOn()
            .minusSeconds(10);
        LocalDateTime endDate = LocalDateTime.now()
            .plusSeconds(10);
        ViewStatsDto statsDto;

        try {
            statsDto = statsClient.getStats(startDate, endDate, List.of(uri), true)
                .getFirst();
        } catch (NoSuchElementException e) {
            log.trace("No stats for event with id={} found", event.getId());
            statsDto = new ViewStatsDto(null, null, 0L);
        } catch (RestClientException e) {
            log.error("Error during getting stats for event with id={}", event.getId(), e);
            statsDto = new ViewStatsDto(null, null, null);
        }
        return statsDto;
    }

    private Event getEventByIdOrThrow(Long eventId) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        return eventOptional.orElseThrow(NotFoundException.supplier("Event with id=%d not found", eventId));
    }

    private Category getCategoryByIdOrThrow(Long categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        return optionalCategory.orElseThrow(NotFoundException.supplier("Category with id=%d not found", categoryId));
    }
}
