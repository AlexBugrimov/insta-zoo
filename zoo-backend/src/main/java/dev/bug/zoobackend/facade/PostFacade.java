package dev.bug.zoobackend.facade;

import dev.bug.zoobackend.dto.PostDto;
import dev.bug.zoobackend.entity.Post;
import org.springframework.stereotype.Component;

@Component
public class PostFacade {

    public PostDto postToPostDto(Post post) {
        var postDto = new PostDto();
        postDto.setUsername(post.getUser().getUsername());
        postDto.setId(post.getId());
        postDto.setCaption(post.getCaption());
        postDto.setLikes(post.getLikes());
        postDto.setUsersLiked(post.getLikedUsers());
        postDto.setLocation(post.getLocation());
        postDto.setTitle(post.getTitle());
        return postDto;
    }
}
