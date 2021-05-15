package dev.bug.zoobackend.payload.request;

import dev.bug.zoobackend.annotations.PasswordMatches;
import dev.bug.zoobackend.annotations.ValidEmail;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@PasswordMatches
public class SignupRequest {

    @Email(message = "It should have email format")
    @NotBlank(message = "User email is required")
    @ValidEmail
    private String email;

    @NotEmpty(message = "Please enter year name")
    private String firstname;

    @NotEmpty(message = "Please enter year lastname")
    private String lastname;

    @NotEmpty(message = "Please enter year username")
    private String username;

    @NotEmpty(message = "Password is required")
    @Size(min = 6)
    private String password;

    private String confirmPassword;
}
