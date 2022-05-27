package com.diabetesapp.dao;

import com.diabetesapp.model.DiabetesRecord;
import com.diabetesapp.repositories.DiabetesRecordsRepository;
import javax.inject.Singleton;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
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
                        .bloodGlucoseLevel(resultSet.getDouble(2))
                        .carbIntake(resultSet.getDouble(3))
                        .medicationDose(resultSet.getDouble(4))
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
                        .bloodGlucoseLevel(resultSet.getDouble(2))
                        .carbIntake(resultSet.getDouble(3))
                        .medicationDose(resultSet.getDouble(4))
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
    public void add(DiabetesRecord record) {
        Connection connection = openConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " +
                    "daily_diabetes_records(blood_glucose_level, carb_intake, medication_dose, date_recorded)" +
                    "VALUES(?, ?, ?, ?);");
            preparedStatement.setDouble(1, record.getBloodGlucoseLevel());
            preparedStatement.setDouble(2, record.getCarbIntake());
            preparedStatement.setDouble(3, record.getMedicationDose());
            preparedStatement.setDate(4, new Date(getPresentDate()));
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            System.err.println(e);
        }
        closeConnection(connection);
    }

    private long getPresentDate() {
        Calendar calendar = Calendar.getInstance();
        long date = calendar.getTimeInMillis();
        return date;
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
            preparedStatement.setDouble(1, record.getBloodGlucoseLevel());
            preparedStatement.setDouble(2, record.getCarbIntake());
            preparedStatement.setDouble(3, record.getMedicationDose());
            preparedStatement.setDate(4, record.getDateRecorded());
            preparedStatement.setInt(5, record.getDiabetesRecordId());
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

    @Override
    public List<DiabetesRecord> listSpecified(Date startingDate, Date endingDate) {
        Connection connection = openConnection();
        List<DiabetesRecord> list = new ArrayList<>();
        try {
            PreparedStatement preparedStatement
                    = connection.prepareStatement("SELECT * FROM daily_diabetes_records " +
                    "WHERE date_recorded BETWEEN ? AND ?;");
            preparedStatement.setDate(1, startingDate);
            preparedStatement.setDate(2, endingDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                list.add(DiabetesRecord.builder()
                        .diabetesRecordId(resultSet.getInt(1))
                        .bloodGlucoseLevel(resultSet.getDouble(2))
                        .carbIntake(resultSet.getDouble(3))
                        .medicationDose(resultSet.getDouble(4))
                        .dateRecorded(resultSet.getDate(5))
                        .build());
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
        closeConnection(connection);
        return list;
    }

    @Override
    public double averageBloodGlucose(Date startingDate, Date endingDate) {
        Connection connection = openConnection();
        double average = 0.0;
        try {
            PreparedStatement preparedStatement
                    = connection.prepareStatement("SELECT AVG(blood_glucose_level) FROM daily_diabetes_records " +
                    "WHERE date_recorded BETWEEN ? AND ?;");
            preparedStatement.setDate(1, startingDate);
            preparedStatement.setDate(2, endingDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                average = resultSet.getDouble(1);
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
        closeConnection(connection);
        return average;
    }

    @Override
    public double averageBloodGlucose() {
        Connection connection = openConnection();
        double average = 0.0;
        try {
            PreparedStatement preparedStatement
                    = connection.prepareStatement("SELECT AVG(blood_glucose_level) FROM daily_diabetes_records;");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                average = resultSet.getDouble(1);
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
        closeConnection(connection);
        return average;
    }

    @Override
    public double averageCarbIntake(Date startingDate, Date endingDate) {
        Connection connection = openConnection();
        double average = 0.0;
        try {
            PreparedStatement preparedStatement
                    = connection.prepareStatement("SELECT AVG(carb_intake) FROM daily_diabetes_records " +
                    "WHERE date_recorded BETWEEN ? AND ?;");
            preparedStatement.setDate(1, startingDate);
            preparedStatement.setDate(2, endingDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                average = resultSet.getDouble(1);
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
        closeConnection(connection);
        return average;
    }

    @Override
    public double averageCarbIntake() {
        Connection connection = openConnection();
        double average = 0.0;
        try {
            PreparedStatement preparedStatement
                    = connection.prepareStatement("SELECT AVG(carb_intake) FROM daily_diabetes_records;");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                average = resultSet.getDouble(1);
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
        closeConnection(connection);
        return average;
    }

}
