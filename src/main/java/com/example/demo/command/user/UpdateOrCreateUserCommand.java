package com.example.demo.command.user;

import com.example.demo.validation.constraint.UniqueUsernameConstraint;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel("User update or create model")
public class UpdateOrCreateUserCommand {

    @NotNull(message = "Username is required")
    @UniqueUsernameConstraint(message = "Username already exist")
    @Pattern(regexp = "(A-Za-z0-9)", message = "Invalid username")
    @Size(min = 5, max = 25, message = "Invalid length")
    @ApiModelProperty("Username")
    private String username;

    @NotNull(message = "Password is required")
    @Size(min = 5, message = "Password too short")
    @ApiModelProperty("Password")
    private String password;
}
