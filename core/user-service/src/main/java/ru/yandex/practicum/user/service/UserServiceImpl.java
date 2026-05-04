package ru.yandex.practicum.user.service;

import java.util.Collection;
import java.util.List;

import ru.yandex.practicum.exception.NotFoundException;
import ru.yandex.practicum.user.dto.NewUserRequest;
import ru.yandex.practicum.user.dto.UserDto;
import ru.yandex.practicum.user.dto.UserShortDto;
import ru.yandex.practicum.user.mapper.UserMapper;
import ru.yandex.practicum.user.model.User;
import ru.yandex.practicum.user.repository.UserRepository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Collection<UserDto> getUsersPaged(UsersGetRequest request) {
        Pageable pageable = PageRequest.of(request.from(), request.size());

        if (request.hasIds()) {
            return userRepository.findAllByIdIn(request.ids(), pageable)
                .stream()
                .map(UserMapper::mapToUserDto)
                .toList();
        }

        return userRepository.findAll(pageable)
            .stream()
            .map(UserMapper::mapToUserDto)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserShortDto getUserById(Long id) {
        User user = getUserByIdOrThrow(id);
        return UserMapper.mapToUserShortDto(user);
    }

    @Override
    public UserDto createUser(NewUserRequest newUserRequest) {
        User user = UserMapper.mapToEntity(newUserRequest);
        User savedUser = userRepository.save(user);
        return UserMapper.mapToUserDto(savedUser);
    }

    @Override
    public void deleteUserById(Long userId) {
        getUserByIdOrThrow(userId);
        userRepository.deleteById(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserShortDto> getUsersByIds(List<Long> userIds) {
        List<User> usersByIds = userRepository.findAllById(userIds);
        return usersByIds.stream()
            .map(UserMapper::mapToUserShortDto)
            .toList();
    }

    private User getUserByIdOrThrow(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(NotFoundException.supplier("User with id=%d not found", userId));
    }
}
