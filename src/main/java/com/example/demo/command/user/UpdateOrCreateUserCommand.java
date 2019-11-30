package com.example.demo.command.user;

import com.example.demo.validation.constraint.UniqueUsernameConstraint;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateOrCreateUserCommand {

    @NotNull(message = "Username is required")
    @UniqueUsernameConstraint(message = "Username already exist")
    @Pattern(regexp = "(A-Za-z0-9)", message = "Invalid username")
    @Min(value = 5, message = "Username too short")
    @Max(value = 25, message = "Username too long")
    private String username;

    @NotNull(message = "Password is required")
    @Min(value = 5, message = "Password too short")
    private String password;
}
