package com.example.demo.controller;

import com.example.demo.command.movie.MovieResponseCommand;
import com.example.demo.command.movie.PatchMovieCommand;
import com.example.demo.command.movie.UpdateOrCreateMovieCommand;
import com.example.demo.model.Role;
import com.example.demo.service.MovieService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("api/movie")
public class MovieController {

    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/all")
    @ApiOperation("Fetch all movies. Available for ADMIN users.")
    @Secured({Role.ADMIN})
    public Iterable<MovieResponseCommand> getAll() {
        return movieService.getAll();
    }

    @GetMapping
    @ApiOperation("Query movie page. Available for all.")
    public Iterable<MovieResponseCommand> getByCriteria(

            @ApiParam(value = "Movie title.", allowEmptyValue = true)
            @RequestParam(required = false)
                    String title,

            @ApiParam(value = "Lowest Rating.", allowEmptyValue = true)
            @RequestParam(required = false)
                    Float minRating,

            @ApiParam(value = "Highest Rating.", allowEmptyValue = true)
            @RequestParam(required = false)
                    Float maxRating,

            @ApiParam(value = "Page number.", defaultValue = "0")
            @RequestParam(defaultValue = "0")
                    Integer page,

            @ApiParam(value = "Sort by which field.", defaultValue = "title")
            @RequestParam(defaultValue = "title")
                    String sortBy,

            @ApiParam(value = "Sort descending or ascending.", defaultValue = "true")
            @RequestParam(defaultValue = "true")
                    Boolean desc,

            @ApiParam(value = "Creators ID.", allowEmptyValue = true)
            @RequestParam(required = false)
                    Long creatorId,

            @ApiParam(value = "Creators username.", allowEmptyValue = true)
            @RequestParam(required = false)
                    String creatorUsername) {

        return movieService.findByCriteria(title, minRating, maxRating, creatorId, creatorUsername, page, sortBy, desc);
    }

    @GetMapping("/{id}")
    @ApiOperation("Get movie by id. Available for all")
    public MovieResponseCommand getById(

            @ApiParam("Movie ID.")
            @PathVariable
                    Long id) {

        return movieService.getById(id);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete movie by id. Available for ADMIN and BASIC users")
    @Secured({Role.BASIC, Role.ADMIN})
    public void deleteById(

            @ApiParam("Movie ID.")
            @PathVariable
                    Long id,
            Principal principal) {

        movieService.deleteById(id, principal.getName());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Create new movie. Available for ADMIN and BASIC users")
    @Secured({Role.BASIC, Role.ADMIN})
    public MovieResponseCommand createMovie(

            @Valid
            @RequestBody
                    UpdateOrCreateMovieCommand movie,
            Principal principal) {

        return movieService.create(movie, principal.getName());
    }

    @PutMapping("/{id}")
    @ApiOperation("Update movie. Available for ADMIN and BASIC users")
    @Secured({Role.BASIC, Role.ADMIN})
    public MovieResponseCommand update(

            @PathVariable
                    Long id,
            @Valid
            @RequestBody
                    UpdateOrCreateMovieCommand movie,
            Principal principal) {

        return movieService.update(id, movie, principal.getName());
    }

    @PatchMapping("/{id}")
    @ApiOperation("Patch movie. Available for Admin and BASIC users")
    @Secured({Role.BASIC, Role.ADMIN})
    public MovieResponseCommand patch(

            @PathVariable
                    Long id,
            @Valid
            @RequestBody
                    PatchMovieCommand movie,
            Principal principal) {

        return movieService.patch(id, movie, principal.getName());
    }

    @DeleteMapping
    @ApiOperation("Delete all movies. Available for ADMIN users")
    @Secured(Role.ADMIN)
    public void deleteAll() {
        movieService.deleteAll();
    }
}
