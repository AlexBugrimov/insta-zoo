package dev.bug.zoobackend.web;

import dev.bug.zoobackend.dto.CommentDto;
import dev.bug.zoobackend.facade.CommentFacade;
import dev.bug.zoobackend.payload.response.MessageResponse;
import dev.bug.zoobackend.service.CommentService;
import dev.bug.zoobackend.validations.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/comment")
@CrossOrigin
public class CommentController {

    private final CommentService commentService;
    private final CommentFacade commentFacade;
    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public CommentController(CommentService commentService, CommentFacade commentFacade, ResponseErrorValidation responseErrorValidation) {
        this.commentService = commentService;
        this.commentFacade = commentFacade;
        this.responseErrorValidation = responseErrorValidation;
    }

    @PostMapping("/{postId}/create")
    public ResponseEntity<Object> createComment(@Valid @RequestBody CommentDto commentDto,
                                                @PathVariable("postId") String postId,
                                                BindingResult bindingResult,
                                                Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        var comment = commentService.saveComment(Long.parseLong(postId), commentDto, principal);
        var createdComment = commentFacade.commentToCommentDto(comment);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}/all")
    public ResponseEntity<List<CommentDto>> getAllCommentsToPost(@PathVariable("postId") String postId) {
        var comments = commentService.getAllCommentsForPost(Long.parseLong(postId))
                .stream()
                .map(commentFacade::commentToCommentDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId/delete}")
    public ResponseEntity<MessageResponse> deleteComment(@PathVariable("commentId") String commentId) {
        commentService.deleteComment(Long.parseLong(commentId));
        return new ResponseEntity<>(new MessageResponse("Comment was deleted"), HttpStatus.NO_CONTENT);
    }
}
