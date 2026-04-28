package ru.yandex.practicum.comment.mapper;

import java.time.LocalDateTime;

import ru.yandex.practicum.comment.dto.CommentDto;
import ru.yandex.practicum.comment.dto.NewCommentDto;
import ru.yandex.practicum.comment.model.Comment;
import ru.yandex.practicum.event.model.Event;
import ru.yandex.practicum.user.mapper.UserMapper;
import ru.yandex.practicum.user.model.User;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CommentMapper {

    public CommentDto toCommentDto(Comment comment, User author) {
        return new CommentDto(
            comment.getId(),
            comment.getText(),
            UserMapper.mapToUserShortDto(author),
            comment.getCreated(),
            comment.isEdited());
    }

    public Comment toEntity(NewCommentDto newCommentDto, User author, Event event) {
        return new Comment(null, newCommentDto.text(), author, event, LocalDateTime.now(), false);
    }
}
