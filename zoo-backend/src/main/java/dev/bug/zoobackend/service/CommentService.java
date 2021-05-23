package dev.bug.zoobackend.service;

import dev.bug.zoobackend.dto.CommentDto;
import dev.bug.zoobackend.entity.Comment;
import dev.bug.zoobackend.entity.User;
import dev.bug.zoobackend.exceptions.PostNotFoundException;
import dev.bug.zoobackend.repository.CommentRepository;
import dev.bug.zoobackend.repository.PostRepository;
import dev.bug.zoobackend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Slf4j
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public Comment saveComment(Long postId, CommentDto commentDto, Principal principal) {
        var user = getUserByPrincipal(principal);
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found for user: " + user.getEmail()));
        var comment = new Comment();
        comment.setPost(post);
        comment.setUserId(user.getId());
        comment.setUsername(user.getUsername());
        comment.setMessage(commentDto.getMessage());

        log.info("Saving comment for post: {}", post.getId());
        return commentRepository.save(comment);
    }

    public List<Comment> getAllCommentsForPost(Long postId) {
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post cannot be found"));
        return commentRepository.findAllByPost(post);
    }

    public void deleteComment(Long commentId) {
        var comment = commentRepository.findById(commentId);
        comment.ifPresent(commentRepository::delete);
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("USer not found with username " + username));
    }
}
