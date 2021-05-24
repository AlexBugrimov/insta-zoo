package dev.bug.zoobackend.facade;

import dev.bug.zoobackend.dto.UserDto;
import dev.bug.zoobackend.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserFacade {

    public UserDto userToUserDto(User user) {
        var userDto = new UserDto();
        userDto.setId(userDto.getId());
        userDto.setFirstname(user.getName());
        userDto.setLastname(user.getLastname());
        userDto.setUsername(user.getUsername());
        userDto.setBio(user.getBio());
        return userDto;
    }
}
