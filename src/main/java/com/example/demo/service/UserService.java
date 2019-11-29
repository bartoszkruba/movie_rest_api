package com.example.demo.service;

import com.example.demo.command.user.PatchUserCommand;
import com.example.demo.command.user.UpdateOrCreateUserCommand;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.UnauthorizedException;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public Iterable<User> getAll() {
        return userRepository.findAll();
    }

    public User create(String username, String password, String role) {
        var user = User.builder()
                .username(username.toLowerCase())
                .password(bCryptPasswordEncoder.encode(password))
                .role(role)
                .build();
        return userRepository.save(user);
    }

    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    public User create(UpdateOrCreateUserCommand user) {
        var created = new User(user);
        created.setRole(Role.BASIC);
        created.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        return userRepository.save(created);
    }

    public User update(Long id, UpdateOrCreateUserCommand user, String username) {
        var found = this.getById(id);
        var currentUser = userRepository.findByUsername(username.toLowerCase()).orElseThrow(UnauthorizedException::new);

        if (!currentUser.getUsername().equals(found.getUsername()) && !currentUser.getRole().equals(Role.ADMIN))
            throw new UnauthorizedException();

        found.setUsername(user.getUsername());
        found.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        return userRepository.save(found);
    }

    public User patch(Long id, PatchUserCommand user, String username) {
        var found = this.getById(id);
        var currentUser = userRepository.findByUsername(username.toLowerCase()).orElseThrow(UnauthorizedException::new);

        if (!currentUser.getUsername().equals(found.getUsername()) && !currentUser.getRole().equals(Role.ADMIN))
            throw new UnauthorizedException();

        if (user.getUsername() != null) found.setUsername(user.getUsername());
        if (user.getPassword() != null) found.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        return userRepository.save(found);
    }

    public void delete(Long id, String username) {
        var found = this.getById(id);
        var currentUser = userRepository.findByUsername(username.toLowerCase()).orElseThrow(UnauthorizedException::new);

        if (!currentUser.getUsername().equals(found.getUsername()) && !currentUser.getRole().equals(Role.ADMIN))
            throw new UnauthorizedException();

        userRepository.delete(found);
    }

}