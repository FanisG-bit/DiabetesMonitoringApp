package com.diabetesapp.dao;

import com.diabetesapp.model.User;

import javax.inject.Singleton;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class UsersDAO {

    private final String URL = "jdbc:postgresql://localhost:5432/DiabetesMonitoringApp_db";
    private final String USER = "newuser";
    private final String PASSWORD = "9876543!@";

    private Connection openConnection() {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Error opening database connection.");
            System.out.println(e);
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }
        return connection;
    }

    private void closeConnection(Connection connection) {
        try {
            if (!connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection.");
        }
    }

    public boolean areCredentialsValid(String username, String password) {
        Connection connection = openConnection();
        List<User> users = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
            while (resultSet.next()) {
                users.add(User.builder()
                        .username(resultSet.getString(2))
                        .password(resultSet.getString(3))
                        .build());
            }
            for(User u : users) {
                if(username.equals(u.getUsername()) && password.equals(u.getPassword())) {
                    closeConnection(connection);
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        closeConnection(connection);
        return false;
    }

}
