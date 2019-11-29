package com.example.demo.bootstrap;

import com.example.demo.model.Movie;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.MovieRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UserAndMovieBootstrap implements CommandLineRunner {

    private final MovieRepository movieRepository;
    private final UserService userService;

    @Autowired
    public UserAndMovieBootstrap(MovieRepository movieRepository, UserService userService) {
        this.movieRepository = movieRepository;
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        var admin = addUser("admin", "password", Role.ADMIN);
        var basic = addUser("user", "password", Role.BASIC);

        addMovie("Godfather", 9.0f, "saddasdsdas", "http://www.google.com", admin);
        addMovie("Star Wars: New Hope", 8.21f, "asasddsasda", "http://www.google.com", basic);
        addMovie("Pulp Fiction", 7.8f, "dsadsadsadsadsa", "http://www.google.com", basic);
    }

    private void addMovie(String title, Float rating, String description, String imageUrl, User creator) {
        movieRepository.save(Movie.builder()
                .title(title)
                .rating(rating)
                .description(description)
                .imageUrl(imageUrl)
                .creator(creator)
                .build());
    }

    private User addUser(String username, String password, String role) {
        return userService.registerNewUser(username, password, role);
    }
}
