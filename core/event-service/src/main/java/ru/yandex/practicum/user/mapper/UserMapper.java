package ru.yandex.practicum.user.mapper;

import ru.yandex.practicum.user.dto.NewUserRequest;
import ru.yandex.practicum.user.dto.UserDto;
import ru.yandex.practicum.user.dto.UserShortDto;
import ru.yandex.practicum.user.model.User;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {

    public User mapToEntity(NewUserRequest dto) {
        return new User(null, dto.name(), dto.email());
    }

    public UserDto mapToUserDto(User user) {
        return new UserDto(user.getEmail(), user.getId(), user.getName());
    }

    public UserShortDto mapToUserShortDto(User user) {
        return new UserShortDto(user.getId(), user.getName());
    }
}
