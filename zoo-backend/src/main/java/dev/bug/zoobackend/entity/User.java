package dev.bug.zoobackend.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;

import java.util.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class User extends BaseEntity {

    private String name;
    private String username;
    private String lastname;
    private String email;
    private String bio;
    private String password;

    private Set<Role> roles = new HashSet<>();
    private List<Post> posts = new ArrayList<>();
    private Collection<? extends GrantedAuthority> authorities;
}

