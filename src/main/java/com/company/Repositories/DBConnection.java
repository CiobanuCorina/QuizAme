package com.company.Repositories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static DBConnection dbConnection;
    private String url = "jdbc:mysql://localhost:3306/quiz_game";
    private String user = "root";
    private String password = "";
    private Connection connection;

    private DBConnection() {}

    public synchronized static DBConnection getDbConnection() {
        if(DBConnection.dbConnection == null) {
            dbConnection = new DBConnection();
            dbConnection.connect();
        }
        return dbConnection;
    }

    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connection succeeded");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
