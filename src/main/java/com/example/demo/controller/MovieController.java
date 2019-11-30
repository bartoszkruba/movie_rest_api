package com.example.demo.controller;

import com.example.demo.command.movie.MovieResponseCommand;
import com.example.demo.command.movie.PatchMovieCommand;
import com.example.demo.command.movie.UpdateOrCreateMovieCommand;
import com.example.demo.model.Movie;
import com.example.demo.model.Role;
import com.example.demo.service.MovieService;
import io.swagger.annotations.ApiOperation;
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
    @ApiOperation("Fetch all movies. Available for all")
    public Iterable<MovieResponseCommand> getAll() {
        return movieService.getAll();
    }

    @GetMapping
    @ApiOperation("Find movies page by query")
    public Iterable<Movie> getByCriteria(@RequestParam(required = false) String title,
                                         @RequestParam(required = false) Float rating,
                                         @RequestParam(required = false) String description,
                                         @RequestParam(required = false) String imageUrl,
                                         @RequestParam(defaultValue = "0") Integer page) {
        return movieService.findByCriteria(title, rating, description, imageUrl, page);
    }

    @GetMapping("/{id}")
    @ApiOperation("Get movie by id. Available for all")
    public MovieResponseCommand getById(@PathVariable Long id) {
        return movieService.getById(id);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Delete movie by id. Available for ADMIN and BASIC users")
    @Secured({Role.BASIC, Role.ADMIN})
    public void deleteById(@PathVariable Long id, Principal principal) {
        movieService.deleteById(id, principal.getName());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Create new movie. Available for ADMIN and BASIC users")
    @Secured({Role.BASIC, Role.ADMIN})
    public MovieResponseCommand createMovie(@Valid @RequestBody UpdateOrCreateMovieCommand movie, Principal principal) {
        return movieService.create(movie, principal.getName());
    }

    @PutMapping("/{id}")
    @ApiOperation("Update movie. Available for ADMIN and BASIC users")
    @Secured({Role.BASIC, Role.ADMIN})
    public MovieResponseCommand update(@PathVariable Long id, @Valid @RequestBody UpdateOrCreateMovieCommand movie, Principal principal) {
        return movieService.update(id, movie, principal.getName());
    }

    @PatchMapping("/{id}")
    @ApiOperation("Patch movie. Available for Admin and BASIC users")
    @Secured({Role.BASIC, Role.ADMIN})
    public MovieResponseCommand patch(@PathVariable Long id, @Valid @RequestBody PatchMovieCommand movie, Principal principal) {
        return movieService.patch(id, movie, principal.getName());
    }

    @DeleteMapping
    @ApiOperation("Delete all movies. Available for ADMIN users")
    @Secured(Role.ADMIN)
    public void deleteAll() {
        movieService.deleteAll();
    }
}
