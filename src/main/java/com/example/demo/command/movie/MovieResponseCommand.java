package com.example.demo.command.movie;

import com.example.demo.model.Movie;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel("Movie response")
public class MovieResponseCommand {

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

    @ApiModelProperty("Creators ID")
    private Long creator;

    public MovieResponseCommand(Movie movie) {
        this.id = movie.getId();
        this.title = movie.getTitle();
        this.rating = movie.getRating();
        this.description = movie.getDescription();
        this.imageUrl = movie.getImageUrl();
        if (movie.getCreator() != null) this.creator = movie.getCreator().getId();
    }
}
