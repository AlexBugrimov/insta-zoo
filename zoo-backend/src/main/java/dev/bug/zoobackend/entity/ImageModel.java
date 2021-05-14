package dev.bug.zoobackend.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ImageModel extends BaseEntity {

    private String name;
    private byte[] imageBytes;
    private Long userId;
    private Long postId;
}
