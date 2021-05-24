package dev.bug.zoobackend.facade;

import dev.bug.zoobackend.dto.CommentDto;
import dev.bug.zoobackend.entity.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentFacade {

    public CommentDto commentToCommentDto(Comment comment) {
        var commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setUsername(comment.getUsername());
        commentDto.setMessage(comment.getMessage());
        return commentDto;
    }
}
