package dev.bug.zoobackend.web;

import dev.bug.zoobackend.dto.UserDto;
import dev.bug.zoobackend.facade.UserFacade;
import dev.bug.zoobackend.service.UserService;
import dev.bug.zoobackend.validations.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("api/user")
@CrossOrigin
public class UserController {

    private final UserService userService;
    private final UserFacade userFacade;
    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public UserController(UserService userService,
                          UserFacade userFacade,
                          ResponseErrorValidation responseErrorValidation) {
        this.userService = userService;
        this.userFacade = userFacade;
        this.responseErrorValidation = responseErrorValidation;
    }

    @GetMapping("/")
    public ResponseEntity<UserDto> getCurrentUser(Principal principal) {
        var user = userService.getCurrentUser(principal);
        var userDto = userFacade.userToUserDto(user);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserProfile(@PathVariable("userId") String userId) {
        var user = userService.getUserById(Long.parseLong(userId));
        var userDto = userFacade.userToUserDto(user);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UserDto userDto,
                                             BindingResult bindingResult,
                                             Principal principal) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        var user = userService.updateUser(userDto, principal);
        var userUpdated = userFacade.userToUserDto(user);
        return new ResponseEntity<>(userUpdated, HttpStatus.OK);
    }
}
