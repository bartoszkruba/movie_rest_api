package com.example.demo.service;

import com.example.demo.command.user.PatchUserCommand;
import com.example.demo.command.user.UpdateOrCreateUserCommand;
import com.example.demo.command.user.UserResponseCommand;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.UnauthorizedException;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    public Iterable<UserResponseCommand> getAll() {
        return userRepository.findAll().stream().map(UserResponseCommand::new).collect(Collectors.toList());
    }


    public UserResponseCommand create(String username, String password, String role) {
        var user = User.builder()
                .username(username.toLowerCase())
                .password(bCryptPasswordEncoder.encode(password))
                .role(role)
                .build();
        return new UserResponseCommand(userRepository.save(user));
    }


    public UserResponseCommand getById(Long id) {
        return new UserResponseCommand(this.queryUserById(id));
    }


    public UserResponseCommand create(UpdateOrCreateUserCommand user) {
        var created = new User(user);
        created.setRole(Role.BASIC);
        created.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        return new UserResponseCommand(userRepository.save(created));
    }


    public UserResponseCommand update(Long id, UpdateOrCreateUserCommand user, String username) {
        var found = this.queryUserById(id);
        var currentUser = userRepository.findByUsername(username.toLowerCase()).orElseThrow(UnauthorizedException::new);

        if (!currentUser.getUsername().equals(found.getUsername()) && !currentUser.getRole().equals(Role.ADMIN))
            throw new UnauthorizedException();

        found.setUsername(user.getUsername());
        found.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        return new UserResponseCommand(userRepository.save(found));
    }


    public UserResponseCommand patch(Long id, PatchUserCommand user, String username) {
        var found = this.queryUserById(id);
        var currentUser = userRepository.findByUsername(username.toLowerCase()).orElseThrow(UnauthorizedException::new);

        if (!currentUser.getUsername().equals(found.getUsername()) && !currentUser.getRole().equals(Role.ADMIN))
            throw new UnauthorizedException();

        if (user.getUsername() != null) found.setUsername(user.getUsername());
        if (user.getPassword() != null) found.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        return new UserResponseCommand(userRepository.save(found));
    }


    public void delete(Long id, String username) {
        var found = this.queryUserById(id);
        var currentUser = userRepository.findByUsername(username.toLowerCase()).orElseThrow(UnauthorizedException::new);

        if (!currentUser.getUsername().equals(found.getUsername()) && !currentUser.getRole().equals(Role.ADMIN))
            throw new UnauthorizedException();

        userRepository.delete(found);
    }


    private User queryUserById(Long id) {
        return userRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

}