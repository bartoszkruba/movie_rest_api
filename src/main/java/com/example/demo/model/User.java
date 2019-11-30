package com.example.demo.model;

import com.example.demo.command.user.UpdateOrCreateUserCommand;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    public User(UpdateOrCreateUserCommand user) {
        this.username = user.getUsername();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @JsonIgnore
    private String password;

    private String role;

    @JsonIgnore
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @Builder.Default
    private Collection<Movie> movies = new ArrayList<>();
}
