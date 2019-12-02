package com.example.demo.controller;

import com.example.demo.command.user.PatchUserCommand;
import com.example.demo.command.user.UpdateOrCreateUserCommand;
import com.example.demo.command.user.UserResponseCommand;
import com.example.demo.model.Role;
import com.example.demo.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("api/user")
@Api("Endpoints for users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @ApiOperation("Get all users. Available for ADMIN users.")
    @Secured({Role.ADMIN})
    public Iterable<UserResponseCommand> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    @ApiOperation("Get user by id. Available for all.")
    public UserResponseCommand getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Create new user. Available for all.")
    public UserResponseCommand create(@Valid @RequestBody UpdateOrCreateUserCommand user) {
        return userService.create(user);
    }

    @PutMapping("/{id}")
    @ApiOperation("Update user. Available for ADMIN and BASIC users.")
    @Secured({Role.ADMIN, Role.BASIC})
    public UserResponseCommand update(@PathVariable Long id, @Valid @RequestBody UpdateOrCreateUserCommand user,
                                      Principal principal) {
        return userService.update(id, user, principal.getName());
    }

    @PatchMapping("/{id}")
    @ApiOperation("Patch user. Available for Admin and BASIC users.")
    @Secured({Role.ADMIN, Role.BASIC})
    public UserResponseCommand patch(@PathVariable Long id, @Valid @RequestBody PatchUserCommand user, Principal principal) {
        return userService.patch(id, user, principal.getName());
    }

    @DeleteMapping("/id")
    @ApiOperation("Delete user. Available for Admin and BASIC users.")
    @Secured({Role.ADMIN, Role.BASIC})
    public void delete(@PathVariable Long id, Principal principal) {
        userService.delete(id, principal.getName());
    }
}
