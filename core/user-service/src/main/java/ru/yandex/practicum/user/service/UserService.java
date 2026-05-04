package ru.yandex.practicum.user.service;

import java.util.Collection;
import java.util.List;

import ru.yandex.practicum.user.dto.NewUserRequest;
import ru.yandex.practicum.user.dto.UserDto;
import ru.yandex.practicum.user.dto.UserShortDto;

public interface UserService {

    Collection<UserDto> getUsersPaged(UsersGetRequest request);

    UserShortDto getUserById(Long id);

    UserDto createUser(NewUserRequest newUserRequest);

    void deleteUserById(Long userId);

    List<UserShortDto> getUsersByIds(List<Long> userIds);
}
