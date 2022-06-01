package com.company.Repositories;

import com.company.User.User;
import com.company.User.UserRoles;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {
    Connection connection = DBConnection.getDbConnection().getConnection();
    User user;

    public UserRepository(User user) {
        this.user = user;
    }

    public UserRepository() {}

    public boolean addUser() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO quiz_game.user(username, password, email, role) VALUES(?, ?, ?, ?)");
        preparedStatement.setObject(1, user.getUsername());
        preparedStatement.setObject(2, user.getPassword());
        preparedStatement.setObject(3, user.getEmail());
        preparedStatement.setObject(4, user.getRole().name());
        return preparedStatement.executeUpdate() == 1;
    }

    public User getUser(String username, String password) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT u.id, u.username, u.password, u.email, u.role FROM quiz_game.user u WHERE u.username = ? AND u.password = ?");
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        System.out.println(resultSet.getString("role"));
        return new User(resultSet.getInt("id"),
                resultSet.getString("username"),
                resultSet.getString("password"),
                resultSet.getString("email"),
                UserRoles.valueOf(resultSet.getString("role")));
    }
}
