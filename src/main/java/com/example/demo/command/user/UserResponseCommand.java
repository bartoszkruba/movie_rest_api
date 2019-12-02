package com.example.demo.command.user;

import com.example.demo.model.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel("User response")
@Builder
public class UserResponseCommand {

    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("Username")
    private String username;

    @ApiModelProperty("Role")
    private String role;

    @ApiModelProperty("Created movies (IDs)")
    @Builder.Default
    private Collection<Long> movies = new ArrayList<>();

    public UserResponseCommand(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.role = user.getRole();
        user.getMovies().forEach(movie -> this.movies.add(movie.getId()));
    }
}
