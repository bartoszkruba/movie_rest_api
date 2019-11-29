package com.example.demo.command.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateOrCreateUserCommand {

    @Pattern(regexp = "(A-Za-z0-9)", message = "Invalid username")
    @Min(value = 5, message = "Username too short")
    @Max(value = 25, message = "Username too long")
    private String username;

    @Min(value = 5, message = "Password too short")
    private String password;
}
