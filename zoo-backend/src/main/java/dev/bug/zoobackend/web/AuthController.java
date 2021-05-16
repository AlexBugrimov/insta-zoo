package dev.bug.zoobackend.web;

import dev.bug.zoobackend.payload.request.SignupRequest;
import dev.bug.zoobackend.payload.response.JwtTokenSuccessResponse;
import dev.bug.zoobackend.payload.response.MessageResponse;
import dev.bug.zoobackend.security.JwtTokenProvider;
import dev.bug.zoobackend.security.SecurityConstants;
import dev.bug.zoobackend.service.UserService;
import dev.bug.zoobackend.validations.ResponseErrorValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
@PreAuthorize("permitAll()")
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final ResponseErrorValidation responseErrorValidation;
    private final UserService userService;

    @Autowired
    public AuthController(JwtTokenProvider jwtTokenProvider,
                          AuthenticationManager authenticationManager,
                          ResponseErrorValidation responseErrorValidation,
                          UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.responseErrorValidation = responseErrorValidation;
        this.userService = userService;
    }

    @PostMapping("/signin")
    public ResponseEntity<Object> authenticationUser(@Valid @RequestBody SignupRequest signupRequest,
                                                     BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        var authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                signupRequest.getUsername(),
                signupRequest.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String jwt = SecurityConstants.TOKEN_PREFIX + jwtTokenProvider.generateToken(authenticate);
        return ResponseEntity.ok(new JwtTokenSuccessResponse(true, jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> registerUser(@Valid @RequestBody SignupRequest signupRequest,
                                               BindingResult bindingResult) {
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (!ObjectUtils.isEmpty(errors)) {
            return errors;
        }
        userService.createUser(signupRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new MessageResponse("User registered successfully"));
    }
}
