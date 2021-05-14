package dev.bug.zoobackend.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Comment extends BaseEntity {

    private Post post;
    private String username;
    private Long userId;
    private String message;
}
