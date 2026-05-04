package ru.yandex.practicum.request.service;

import java.util.List;
import java.util.Map;

import ru.yandex.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.yandex.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.yandex.practicum.request.dto.ParticipationRequestDto;

public interface ParticipationRequestService {

    ParticipationRequestDto createRequest(Long userId, Long eventId);

    List<ParticipationRequestDto> getUserRequests(Long userId);

    ParticipationRequestDto cancelRequest(Long userId, Long requestId);

    List<ParticipationRequestDto> getEventRequestsByInitiator(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateEventRequestsStatus(Long userId, Long eventId,
        EventRequestStatusUpdateRequest request);

    Map<Long, Long> countConfirmedByEventIds(List<Long> eventIds);
}
