package com.company.User;

import com.company.Repositories.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private UserRoles role;

    public User(String username, String password, String email, UserRoles role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public boolean isAdmin() {
        return role.name().equals("ADMIN");
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public UserRoles getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role.name() +
                '}';
    }
}
