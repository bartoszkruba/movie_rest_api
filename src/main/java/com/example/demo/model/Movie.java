package com.example.demo.model;

import com.example.demo.command.movie.UpdateOrCreateMovieCommand;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Movie {

    public Movie(UpdateOrCreateMovieCommand command) {
        this.title = command.getTitle();
        this.rating = command.getRating();
        this.description = command.getDescription();
        this.imageUrl = command.getImageUrl();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("ID")
    private Long id;

    @ApiModelProperty("Title")
    private String title;

    @ApiModelProperty("Rating")
    private Float rating;

    @ApiModelProperty("Description")
    private String description;

    @ApiModelProperty("Image URL")
    private String imageUrl;

    @ManyToOne
    @JsonIgnore
    private User creator;
}
