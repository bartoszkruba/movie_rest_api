package com.example.demo.command.movie;

import com.example.demo.validation.constraint.ValidURLConstraint;
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
@ApiModel("Movie update or create model")
public class UpdateOrCreateMovieCommand {

    @NotNull
    @NotEmpty(message = "Title cannot be empty")
    @ApiModelProperty("Title")
    private String title;

    @NotNull
    @Min(value = 0, message = "Value is too low.")
    @Max(value = 10, message = "Value it too high")
    @ApiModelProperty("Rating")
    private Float rating;

    @NotNull
    @Size(min = 10, message = "Description must be at least 10 characters long")
    @ApiModelProperty("Description")
    private String description;

    @NotNull
    @ApiModelProperty("Image URL")
    @ValidURLConstraint(message = "Invalid URL.")
    private String imageUrl;
}

