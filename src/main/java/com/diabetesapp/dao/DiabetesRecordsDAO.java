package com.diabetesapp.dao;

import com.diabetesapp.model.DiabetesRecord;
import com.diabetesapp.repositories.DiabetesRecordsRepository;
import javax.inject.Singleton;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class DiabetesRecordsDAO implements DiabetesRecordsRepository {

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

    @Override
    public List<DiabetesRecord> list() {
        Connection connection = openConnection();
        List<DiabetesRecord> diabetesRecords = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM daily_diabetes_records;");
            while (resultSet.next()) {
                diabetesRecords.add(DiabetesRecord.builder()
                        .diabetesRecordId(resultSet.getInt(1))
                        .bloodGlucoseLevel(resultSet.getInt(2))
                        .carbIntake(resultSet.getInt(3))
                        .medicationDose(resultSet.getInt(4))
                        .dateRecorded(resultSet.getDate(5))
                        .build());
            }
            statement.close();
        } catch (SQLException e) {
            System.err.println(e);
        }
        closeConnection(connection);
        return diabetesRecords;
    }

    @Override
    public DiabetesRecord get(int recordId) {
        Connection connection = openConnection();
        DiabetesRecord record = null;
        try {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("SELECT * FROM daily_diabetes_records WHERE record_id = ?;");
            preparedStatement.setInt(1, recordId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                record = DiabetesRecord.builder()
                        .diabetesRecordId(resultSet.getInt(1))
                        .bloodGlucoseLevel(resultSet.getInt(2))
                        .carbIntake(resultSet.getInt(3))
                        .medicationDose(resultSet.getInt(4))
                        .dateRecorded(resultSet.getDate(5))
                        .build();
            }
            preparedStatement.close();
        } catch (SQLException e) {
            System.err.println(e);
        }
        closeConnection(connection);
        return record;
    }

    @Override
    public void add(DiabetesRecord record, int userId) {
        Connection connection = openConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " +
                    "daily_diabetes_records(blood_glucose_level, carb_intake, medication_dose, date_recorded, user_id)" +
                    "VALUES(?, ?, ?, ?, ?);");
            preparedStatement.setInt(1, record.getBloodGlucoseLevel());
            preparedStatement.setInt(2, record.getCarbIntake());
            preparedStatement.setInt(3, record.getMedicationDose());
            preparedStatement.setDate(4, (Date) record.getDateRecorded());
            preparedStatement.setInt(5, record.getUserId());
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            System.err.println(e);
        }
        closeConnection(connection);
    }

    @Override
    public void update(DiabetesRecord record) {
        Connection connection = openConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE daily_diabetes_records " +
                    "SET blood_glucose_level = ?, " +
                    "carb_intake = ?, " +
                    "medication_dose = ?, " +
                    "date_recorded = ? " +
                    "WHERE record_id = ?;");
            preparedStatement.setInt(1, record.getBloodGlucoseLevel());
            preparedStatement.setInt(2, record.getCarbIntake());
            preparedStatement.setInt(3, record.getMedicationDose());
            preparedStatement.setDate(4, (Date) record.getDateRecorded());
            preparedStatement.setInt(5, record.getUserId());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            System.err.println(e);
        }
        closeConnection(connection);
    }

    @Override
    public void delete(int recordId) {
        Connection connection = openConnection();
        try {
            Statement statement = connection.createStatement();
            statement.execute("DELETE FROM daily_diabetes_records " +
                    "WHERE record_id = " + recordId + ";");
            statement.close();
        } catch (SQLException e) {
            System.err.println(e);
        }
        closeConnection(connection);
    }
}