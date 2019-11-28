package com.example.demo.model;

import com.example.demo.validation.constraint.ValidURLConstraint;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("ID")
    private Long id;

    @NotEmpty(message = "Title cannot be empty")
    @ApiModelProperty("Title")
    private String title;

    @Min(value = 0, message = "Value is too low.")
    @Max(value = 10, message = "Value it too high")
    @ApiModelProperty("Rating")
    private Float rating;

    @Size(min = 10, message = "Description must be at least 10 characters long")
    @ApiModelProperty("Description")
    private String description;

    @ApiModelProperty("Image URL")
    @ValidURLConstraint(message = "Invalid URL.")
    private String imageUrl;

    @ManyToOne
    @JsonIgnore
    private User creator;
}
