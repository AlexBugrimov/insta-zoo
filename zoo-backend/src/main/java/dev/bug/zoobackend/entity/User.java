package dev.bug.zoobackend.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.*;

@Entity
@EqualsAndHashCode(callSuper = true)
@Data
public class User extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(unique = true, updatable = true)
    private String username;

    @Column(nullable = false)
    private String lastname;

    @Column(unique = true)
    private String email;

    @Column(columnDefinition = "text")
    private String bio;

    @Column(length = 3000)
    private String password;

    @ElementCollection(targetClass = Role.class)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "user", orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @Transient
    private Collection<? extends GrantedAuthority> authorities;
}

