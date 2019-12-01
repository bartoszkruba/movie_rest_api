package com.example.demo.service;

import com.example.demo.command.movie.MovieResponseCommand;
import com.example.demo.command.movie.PatchMovieCommand;
import com.example.demo.command.movie.UpdateOrCreateMovieCommand;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.UnauthorizedException;
import com.example.demo.model.Movie;
import com.example.demo.model.Role;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final UserRepository userRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository, UserRepository userRepository) {
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
    }

    public Iterable<MovieResponseCommand> getAll() {
        return movieRepository.findAll().stream()
                .map(MovieResponseCommand::new)
                .collect(Collectors.toList());
    }

    public MovieResponseCommand getById(Long id) {
        return new MovieResponseCommand(movieRepository.findById(id).orElseThrow(ResourceNotFoundException::new));
    }

    public void deleteById(Long id, String username) {
        var movie = movieRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        var user = userRepository.findByUsername(username.toLowerCase()).orElseThrow(UnauthorizedException::new);

        if (!user.getUsername().equals(movie.getCreator().getUsername()) && !user.getRole().equals(Role.ADMIN))
            throw new UnauthorizedException();

        movieRepository.delete(movie);
    }

    public MovieResponseCommand create(UpdateOrCreateMovieCommand movie, String username) {

        var user = userRepository.findByUsername(username.toLowerCase()).orElseThrow(UnauthorizedException::new);

        var created = new Movie(movie);
        created.setCreator(user);
        created = movieRepository.save(created);
        user.getMovies().add(created);
        return new MovieResponseCommand(created);
    }

    public MovieResponseCommand update(Long id, UpdateOrCreateMovieCommand movie, String username) {

        var found = movieRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        var user = userRepository.findByUsername(username.toLowerCase()).orElseThrow(UnauthorizedException::new);

        if (!user.getUsername().equals(found.getCreator().getUsername()) && !user.getRole().equals(Role.ADMIN))
            throw new UnauthorizedException();

        found.setTitle(movie.getTitle());
        found.setRating(movie.getRating());
        found.setDescription(movie.getDescription());
        found.setImageUrl(movie.getImageUrl());

        return new MovieResponseCommand(movieRepository.save(found));
    }

    public MovieResponseCommand patch(Long id, PatchMovieCommand movie, String username) {
        var found = movieRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        var user = userRepository.findByUsername(username.toLowerCase()).orElseThrow(UnauthorizedException::new);
        if (!user.getUsername().equals(found.getCreator().getUsername()) && !user.getRole().equals(Role.ADMIN))
            throw new UnauthorizedException();

        if (movie.getTitle() != null) found.setTitle(movie.getTitle());
        if (movie.getRating() != null) found.setRating(movie.getRating());
        if (movie.getDescription() != null) found.setDescription(movie.getDescription());
        if (movie.getImageUrl() != null) found.setImageUrl(movie.getImageUrl());

        return new MovieResponseCommand(movieRepository.save(found));
    }

    public void deleteAll() {
        movieRepository.deleteAll();
    }

    public Iterable<MovieResponseCommand>
    findByCriteria(String title, Float minRating, Float maxRating, Integer page) {

        Pageable pageRequest = PageRequest.of(page, 10);

        return movieRepository.findAll((Specification<Movie>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (title != null)
                predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("title"), title)));

            if (minRating != null)
                predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("rating"), minRating)));

            if (maxRating != null)
                predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get("rating"), maxRating)));

            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        }, pageRequest).map(MovieResponseCommand::new);
    }
}
