package com.example.demo.bootstrap;

import com.example.demo.model.Movie;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserAndMovieBootstrap implements CommandLineRunner {

    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserAndMovieBootstrap(MovieRepository movieRepository, UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        var admin = addUser("admin", "password", Role.ADMIN);
        var basic = addUser("user", "password", Role.BASIC);

        addMovie("The Godfather", 9.0f, "saddasdsdas", "http://www.google.com", admin);
        addMovie("Star Wars: New Hope", 8.21f, "asasddsasda", "http://www.google.com", basic);
        addMovie("Pulp Fiction", 7.8f, "dsadsadsadsadsa", "http://www.google.com", basic);
        addMovie("The Shawshank Redemption", 5.89f, "saddssdsdadsa", "" +
                "http://www.google.com", admin);
        addMovie("Forrest Gump", 8.45f, "dadsdsaasd", "http://www.google.com", basic);
        addMovie("The Matrix", 4.34f, "asddasasddsa", "http://www.google.com", basic);
        addMovie("Shrek", 7.43f, "adsdssdassdadsa", "http://www.google.com", admin);
        addMovie("Fight Club", 9.45f, "asdsddsdsdas", "http://www.google.com", admin);
        addMovie("Titanic", 2.0f, "saddasdsdas", "http://www.google.com", admin);
        addMovie("The Sixth Sense", 6.19f, "asasddsasda", "http://www.google.com", basic);
        addMovie("Shutter Island", 5.48f, "dsadsadsadsadsa", "http://www.google.com", basic);
        addMovie("Inception", 7.89f, "saddssdsdadsa", "" +
                "http://www.google.com", admin);
        addMovie("Se7en", 9.45f, "dadsdsaasd", "http://www.google.com", basic);
        addMovie("Cast Away", 5.34f, "asddasasddsa", "http://www.google.com", basic);
        addMovie("Gran Torino", 2.43f, "adsdssdassdadsa", "http://www.google.com", admin);
        addMovie("The Shining", 8.12f, "asdsddsdsdas", "http://www.google.com", admin);
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
        var user = User.builder()
                .username(username.toLowerCase())
                .password(bCryptPasswordEncoder.encode(password))
                .role(role)
                .build();
        return userRepository.save(user);
    }
}
