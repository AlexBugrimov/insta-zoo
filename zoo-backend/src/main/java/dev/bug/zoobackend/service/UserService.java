package dev.bug.zoobackend.service;

import dev.bug.zoobackend.dto.UserDto;
import dev.bug.zoobackend.entity.User;
import dev.bug.zoobackend.entity.enums.RoleEnum;
import dev.bug.zoobackend.exceptions.UserExistException;
import dev.bug.zoobackend.payload.request.SignupRequest;
import dev.bug.zoobackend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(SignupRequest userIn) {
        var user = new User();
        user.setEmail(userIn.getEmail());
        user.setName(userIn.getFirstname());
        user.setLastname(userIn.getLastname());
        user.setUsername(userIn.getUsername());
        user.setPassword(passwordEncoder.encode(userIn.getPassword()));
        user.getRoles().add(RoleEnum.ROLE_USER);
        try {
            log.info("Saving user {}", userIn.getEmail());
            return userRepository.save(user);
        } catch (Exception ex) {
            log.error("Error during registration. {}", ex.getMessage());
            throw new UserExistException("The user " + user.getUsername() + " already exist. Please check credentials");
        }
    }

    public User updateUser(UserDto userDto, Principal principal) {
        var user = getUserByPrincipal(principal);
        user.setName(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setBio(user.getBio());
        return userRepository.save(user);
    }

    public User getCurrentUser(Principal principal) {
        return getUserByPrincipal(principal);
    }

    private User getUserByPrincipal(Principal principal) {
        String username = principal.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("USer not found with username " + username));
    }

    public User getUserById(Long userId) {
        return userRepository.findUserById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
