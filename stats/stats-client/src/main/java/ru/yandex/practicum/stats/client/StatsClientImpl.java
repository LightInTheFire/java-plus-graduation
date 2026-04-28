package ru.yandex.practicum.stats.client;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;

import ru.yandex.practicum.dto.EndpointHitDto;
import ru.yandex.practicum.dto.ViewStatsDto;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.*;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StatsClientImpl implements StatsClient {

    private final RestClient restClient;
    private final String statsServiceId;
    private final String app;
    private final RetryTemplate retryTemplate;
    private final DiscoveryClient discoveryClient;
    private final DateTimeFormatter formatter;

    @Override
    public void hit(EndpointHitDto dto) {
        URI uri = makeUri("/hit");

        restClient.post()
            .uri(uri)
            .contentType(MediaType.APPLICATION_JSON)
            .body(dto)
            .retrieve()
            .toBodilessEntity();
    }

    @Override
    public void hit(HttpServletRequest request) {
        EndpointHitDto dto = EndpointHitDto.builder()
            .app(app)
            .uri(request.getRequestURI())
            .ip(request.getRemoteAddr())
            .timestamp(LocalDateTime.now())
            .build();

        hit(dto);
    }

    @Override
    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        URI baseUri = makeUri("/stats");
        UriComponentsBuilder b = UriComponentsBuilder.fromHttpUrl(baseUri.toString())
            .queryParam("start", start.format(formatter))
            .queryParam("end", end.format(formatter))
            .queryParam("unique", unique);

        if (uris != null && !uris.isEmpty()) {
            for (String uri : uris) {
                b.queryParam("uris", uri);
            }
        }

        URI finalUri = b.build()
            .toUri();

        ViewStatsDto[] body = restClient.get()
            .uri(finalUri)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .body(ViewStatsDto[].class);

        return body == null ? List.of() : List.of(body);
    }

    private URI makeUri(String path) {
        ServiceInstance instanceInfo = retryTemplate.execute(context -> getInstance());
        return URI.create("http://" + instanceInfo.getHost() + ":" + instanceInfo.getPort() + path);
    }

    private ServiceInstance getInstance() {
        try {
            return discoveryClient.getInstances(statsServiceId)
                .getFirst();
        } catch (Exception e) {
            throw new StatsServerUnavailableException("No instances for stats server found with id: " + statsServiceId);
        }
    }
}
