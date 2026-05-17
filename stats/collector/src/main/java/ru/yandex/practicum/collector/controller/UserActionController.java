package ru.yandex.practicum.collector.controller;

import ru.practicum.ewm.stats.avro.UserActionAvro;
import ru.yandex.practicum.collector.mapper.UserEventMapper;
import ru.yandex.practicum.collector.producer.UserActionProducer;
import ru.yandex.practicum.stats.proto.UserActionControllerGrpc;
import ru.yandex.practicum.stats.proto.UserActionProto;

import com.google.protobuf.Empty;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class UserActionController extends UserActionControllerGrpc.UserActionControllerImplBase {

    private final UserActionProducer userActionProducer;
    private final UserEventMapper userEventMapper;

    @Override
    public void collectUserAction(UserActionProto request, StreamObserver<Empty> responseObserver) {
        try {
            UserActionAvro userActionAvro = userEventMapper.toUserActionAvro(request);

            userActionProducer.sendEvent(userActionAvro);

            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("Exception during handling event", e);
            responseObserver.onError(
                Status.INTERNAL.withDescription("Failed to process user event")
                    .withCause(e)
                    .asRuntimeException());
        }
    }
}
