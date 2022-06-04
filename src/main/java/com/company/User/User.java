package com.company.User;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class User {
    private int id;
    private String username;
    @JsonIgnore
    private String password;
    private String email;
    private UserRoles role;

    public User(String username, String password, String email, UserRoles role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public User(int id, String username, String password, String email, UserRoles role) {
        this.id = id;
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

    public int getId() {
        return id;
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
