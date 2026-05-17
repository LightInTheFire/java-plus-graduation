package ru.yandex.practicum.analyzer.controller;

import ru.yandex.practicum.analyzer.service.RecommendationFacade;
import ru.yandex.practicum.stats.proto.*;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class RecommendationsController extends RecommendationsControllerGrpc.RecommendationsControllerImplBase {

    private final RecommendationFacade facade;

    @Override
    public void getRecommendationsForUser(UserRecommendationsRequestProto request,
        StreamObserver<RecommendedEventProto> responseObserver) {
        handle(
            responseObserver,
            "recommendations failed",
            () -> facade.getForUser(request.getUserId(), request.getMaxResults())
                .forEach(responseObserver::onNext));

    }

    @Override
    public void getSimilarEvents(SimilarEventsRequestProto request,
        StreamObserver<RecommendedEventProto> responseObserver) {
        handle(
            responseObserver,
            "similar events failed",
            () -> facade.getSimilar(request.getEventId(), request.getUserId(), request.getMaxResults())
                .forEach(responseObserver::onNext));

    }

    @Override
    public void getInteractionsCount(InteractionsCountRequestProto request,
        StreamObserver<RecommendedEventProto> responseObserver) {
        handle(
            responseObserver,
            "interactions failed",
            () -> facade.getInteractions(request.getEventIdList())
                .forEach(responseObserver::onNext));

    }

    private void handle(StreamObserver<RecommendedEventProto> responseObserver, String errorMessage, Runnable action) {
        try {
            action.run();
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error(errorMessage, e);

            responseObserver.onError(
                Status.INTERNAL.withDescription(errorMessage)
                    .withCause(e)
                    .asRuntimeException());
        }
    }
}
