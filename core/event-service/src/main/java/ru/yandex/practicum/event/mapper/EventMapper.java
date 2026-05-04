package ru.yandex.practicum.event.mapper;

import java.time.LocalDateTime;

import ru.yandex.practicum.category.mapper.CategoryMapper;
import ru.yandex.practicum.category.model.Category;
import ru.yandex.practicum.event.dto.*;
import ru.yandex.practicum.event.model.Event;
import ru.yandex.practicum.event.model.EventInfo;
import ru.yandex.practicum.event.model.Location;
import ru.yandex.practicum.user.dto.UserShortDto;

import lombok.experimental.UtilityClass;

@UtilityClass
public class EventMapper {

    public Event mapToEntity(NewEventDto newEventDto, Category category, UserShortDto userDto, Location location) {
        return new Event(
            null,
            newEventDto.annotation(),
            category,
            LocalDateTime.now(),
            newEventDto.description(),
            newEventDto.eventDate(),
            userDto.id(),
            location,
            newEventDto.paid(),
            newEventDto.participantLimit(),
            null,
            newEventDto.requestModeration(),
            EventState.PENDING,
            newEventDto.title());
    }

    public EventInfoDto maptoEventInfoDto(EventInfo eventInfo) {
        return new EventInfoDto(
            eventInfo.getId(),
            eventInfo.getInitiatorId(),
            eventInfo.getParticipantLimit(),
            eventInfo.isRequestModeration(),
            eventInfo.getState());
    }

    public EventFullDto mapToFullDto(Event event, UserShortDto userDto, long confirmedRequests, Long views,
        Long commentaries) {
        return new EventFullDto(
            event.getAnnotation(),
            CategoryMapper.mapToDto(event.getCategory()),
            confirmedRequests,
            event.getCreatedOn(),
            event.getDescription(),
            event.getEventDate(),
            event.getId(),
            userDto,
            LocationMapper.mapToDto(event.getLocation()),
            event.getPaid(),
            event.getParticipantLimit(),
            event.getPublishedOn(),
            event.getRequestModeration(),
            event.getState(),
            event.getTitle(),
            views,
            commentaries);
    }

    public EventShortDto mapToShortDto(Event event, UserShortDto userDto, long confirmedRequests, Long views,
        Long commentaries) {
        return new EventShortDto(
            event.getAnnotation(),
            CategoryMapper.mapToDto(event.getCategory()),
            confirmedRequests,
            event.getEventDate(),
            event.getId(),
            userDto,
            event.getPaid(),
            event.getTitle(),
            views,
            commentaries);
    }

    public void updateEventFromDto(Event event, UpdateEventAdminRequest updateDto, Category newCategory) {
        if (updateDto.hasStateAction()) {
            switch (updateDto.stateAction()) {
                case PUBLISH_EVENT -> {
                    event.setState(EventState.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                }
                case REJECT_EVENT -> event.setState(EventState.CANCELED);
            }
        }

        updateCommonFields(event, updateDto, newCategory);
    }

    public void updateEventFromDto(Event event, UpdateEventUserRequest updateDto, Category newCategory) {
        if (updateDto.hasStateAction()) {
            switch (updateDto.stateAction()) {
                case SEND_TO_REVIEW -> event.setState(EventState.PENDING);
                case CANCEL_REVIEW -> event.setState(EventState.CANCELED);
            }
        }

        updateCommonFields(event, updateDto, newCategory);
    }

    private void updateCommonFields(Event event, UpdatableEvent updateDto, Category newCategory) {
        if (updateDto.hasAnnotation()) {
            event.setAnnotation(updateDto.annotation());
        }

        if (updateDto.hasEventDate()) {
            event.setEventDate(updateDto.eventDate());
        }

        if (updateDto.hasCategory()) {
            event.setCategory(newCategory);
        }

        if (updateDto.hasLocation()) {
            Location location = LocationMapper.mapToEntity(updateDto.location());
            event.setLocation(location);
        }

        if (updateDto.hasParticipantLimit()) {
            event.setParticipantLimit(updateDto.participantLimit());
        }

        if (updateDto.hasPaid()) {
            event.setPaid(updateDto.paid());
        }

        if (updateDto.hasRequestModeration()) {
            event.setRequestModeration(updateDto.requestModeration());
        }

        if (updateDto.hasTitle()) {
            event.setTitle(updateDto.title());
        }

        if (updateDto.hasDescription()) {
            event.setDescription(updateDto.description());
        }
    }
}
