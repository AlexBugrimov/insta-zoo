package dev.bug.zoobackend.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
public class ImageModel extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] imageBytes;

    @JsonIgnore
    private Long userId;

    @JsonIgnore
    private Long postId;
}
