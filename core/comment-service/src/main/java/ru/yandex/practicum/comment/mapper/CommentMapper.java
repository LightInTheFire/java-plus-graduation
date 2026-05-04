package ru.yandex.practicum.comment.mapper;

import java.time.LocalDateTime;

import ru.yandex.practicum.comment.dto.CommentDto;
import ru.yandex.practicum.comment.dto.NewCommentDto;
import ru.yandex.practicum.comment.model.Comment;
import ru.yandex.practicum.user.dto.UserShortDto;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CommentMapper {

    public CommentDto toCommentDto(Comment comment, UserShortDto author) {
        return new CommentDto(comment.getId(), comment.getText(), author, comment.getCreated(), comment.isEdited());
    }

    public Comment toEntity(NewCommentDto newCommentDto, Long authorId, Long eventId) {
        return new Comment(null, newCommentDto.text(), authorId, eventId, LocalDateTime.now(), false);
    }
}
