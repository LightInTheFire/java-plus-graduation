package ru.yandex.practicum.collector.mapper;

import java.time.Instant;

import ru.practicum.ewm.stats.avro.UserActionAvro;
import ru.yandex.practicum.stats.proto.UserActionProto;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ValueMapping;
import org.mapstruct.ValueMappings;

import com.google.protobuf.Timestamp;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserEventMapper {

    @ValueMappings({@ValueMapping(source = "ACTION_VIEW", target = "VIEW"),
        @ValueMapping(source = "ACTION_REGISTER", target = "REGISTER"),
        @ValueMapping(source = "ACTION_LIKE", target = "LIKE"),
        @ValueMapping(source = "UNRECOGNIZED", target = MappingConstants.NULL)})
    UserActionAvro toUserActionAvro(UserActionProto userActionProto);

    default Instant mapTimestamp(Timestamp timestamp) {
        return Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());
    }
}
