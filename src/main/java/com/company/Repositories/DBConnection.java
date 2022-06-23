package com.company.Repositories;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static DBConnection dbConnection;
    private String url;
    private String user;
    private String password;
    private Connection connection;

    private DBConnection() throws IOException {
        Properties props = new Properties();
        props.load(ClassLoader.getSystemResourceAsStream("config/db.properties"));
        this.user = props.getProperty("user");
        this.password = props.getProperty("password");
        this.url = String.format("jdbc:mysql://%s:%s/%s", props.getProperty("server"), props.getProperty("port"), props.getProperty("database"));
    }

    public synchronized static DBConnection getDbConnection() throws IOException {
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
