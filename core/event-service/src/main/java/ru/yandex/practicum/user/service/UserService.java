package ru.yandex.practicum.user.service;

import java.util.Collection;

import ru.yandex.practicum.user.dto.NewUserRequest;
import ru.yandex.practicum.user.dto.UserDto;

public interface UserService {

    Collection<UserDto> getUsersPaged(UsersGetRequest request);

    UserDto createUser(NewUserRequest newUserRequest);

    void deleteUserById(Long userId);
}
