package com.mongo.dao;

import com.mongo.connection.Conexion;
import com.mongo.model.mysql.Professor;
import com.mongo.model.mysql.Trip;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TripDAO {

    public void createTrip(Trip trip) throws SQLException {
        Connection conn = null;
        try {
            conn = Conexion.getConnection();
            conn.setAutoCommit(false);

            // Insertar excursión
            String sqlTrip = "INSERT INTO trips (class_Id, trip_destination, trip_duration, trip_date, trip_cost) VALUES (?, ?, ?, ?, ?)";
            int tripId;

            try (PreparedStatement stmt = conn.prepareStatement(sqlTrip, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, trip.getGroupId());
                stmt.setString(2, trip.getDestination());
                stmt.setInt(3, trip.getDuration());
                stmt.setDate(4, Date.valueOf(trip.getDate()));
                stmt.setDouble(5, trip.getCost());

                int affectedRows = stmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Error al crear la excursión, ninguna fila afectada.");
                }

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        tripId = generatedKeys.getInt(1);
                        trip.setTripId(tripId);
                    } else {
                        throw new SQLException("Error al obtener ID de la excursión.");
                    }
                }
            }

            // Insertar profesores acompañantes
            if (trip.getAccompanyingProfessors() != null && !trip.getAccompanyingProfessors().isEmpty()) {
                String sqlprofessors = "INSERT INTO professorsgoing (trip_id, professor_id) VALUES (?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sqlprofessors)) {
                    for (Professor professor : trip.getAccompanyingProfessors()) {
                        stmt.setInt(1, tripId);
                        stmt.setInt(2, professor.getProfessorId());
                        stmt.addBatch();
                    }
                    stmt.executeBatch();
                }
            }

            conn.commit();

        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
            }
        }
    }

    public List<Trip> getAllTripsWithProfessors() throws SQLException {
        List<Trip> trips = new ArrayList<>();
        String sql = """
                SELECT t.trips_id, t.trip_destination, t.trip_duration, t.trip_date, t.trip_cost, t.trip_status,
                       c.class_name as group_name,
                       GROUP_CONCAT(CONCAT(prof.professor_name, ' ', prof.professor_surname) SEPARATOR ', ') as professors
                FROM trips t
                JOIN classes c ON t.class_Id = c.class_Id
                LEFT JOIN professorsgoing tg ON t.trips_id = tg.trip_id
                LEFT JOIN professors prof ON tg.professor_id = prof.professor_Id
                GROUP BY t.trips_id
                ORDER BY t.trip_date DESC
                """;

        try (Connection conn = Conexion.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Trip trip = new Trip();
                trip.setTripId(rs.getInt("trips_id"));
                trip.setDestination(rs.getString("trip_destination"));
                trip.setDuration(rs.getInt("trip_duration"));
                trip.setDate(rs.getDate("trip_date").toLocalDate());
                trip.setCost(rs.getDouble("trip_cost"));
                trip.setFinished(rs.getBoolean("trip_status"));
                trip.setGroupName(rs.getString("group_name"));
                trips.add(trip);
            }
        }
        return trips;
    }

    public List<Trip> getFinishedTripsBetweenDates(LocalDate startDate, LocalDate endDate) throws SQLException {
        List<Trip> trips = new ArrayList<>();
        String sql = """
                SELECT t.*, c.class_name as group_name
                FROM trips t
                JOIN classes c ON t.class_Id = c.class_Id
                WHERE t.status = true AND t.trip_date BETWEEN ? AND ?
                ORDER BY t.trip_date
                """;

        try (Connection conn = Conexion.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(startDate));
            stmt.setDate(2, Date.valueOf(endDate));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Trip trip = new Trip();
                    trip.setTripId(rs.getInt("trips_id"));
                    trip.setDestination(rs.getString("trip_destination"));
                    trip.setDuration(rs.getInt("trip_duration"));
                    trip.setDate(rs.getDate("trip_date").toLocalDate());
                    trip.setCost(rs.getDouble("trip_cost"));
                    trip.setFinished(rs.getBoolean("trip_status"));
                    trip.setGroupName(rs.getString("group_name"));
                    trips.add(trip);
                }
            }
        }
        return trips;
    }

    public void deleteTrip(int tripId) throws SQLException {
        String sql = "DELETE FROM trips WHERE trips_id = ? AND trip_status = false";

        try (Connection conn = Conexion.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, tripId);
            stmt.executeUpdate();
        }
    }

    public void updateTripDate(int tripId, LocalDate newDate) throws SQLException {
        String sql = "UPDATE trips SET trip_date = ? WHERE trips_id = ? AND trip_status = false";

        try (Connection conn = Conexion.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(newDate));
            stmt.setInt(2, tripId);
            stmt.executeUpdate();
        }
    }

    public void markTripAsFinished(int tripId) throws SQLException {
        String sql = "UPDATE trips SET trip_status = true WHERE trips_id = ?";

        try (Connection conn = Conexion.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, tripId);
            stmt.executeUpdate();
        }
    }

    public Trip getTripById(int tripId) throws SQLException {
        String sql = "SELECT * FROM trips WHERE trips_id = ?";

        try (Connection conn = Conexion.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, tripId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Trip trip = new Trip();
                    trip.setTripId(rs.getInt("trips_id"));
                    trip.setGroupId(rs.getInt("class_Id"));
                    trip.setDestination(rs.getString("trip_destination"));
                    trip.setDuration(rs.getInt("trip_duration"));
                    trip.setDate(rs.getDate("trip_date").toLocalDate());
                    trip.setCost(rs.getDouble("trip_cost"));
                    trip.setFinished(rs.getBoolean("trip_status"));
                    return trip;
                }
            }
        }
        return null;
    }

    public List<Professor> getProfessorsForTrip(int tripId) throws SQLException {
        List<Professor> professors = new ArrayList<>();
        String sql = """
                SELECT p.* FROM professors p
                JOIN professorsgoing tg ON p.professor_Id = tg.professor_id
                WHERE tg.trip_id = ?
                """;

        try (Connection conn = Conexion.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, tripId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Professor professor = new Professor();
                    professor.setProfessorId(rs.getInt("professor_Id"));
                    professor.setName(rs.getString("professor_name"));
                    professor.setSurname(rs.getString("professor_surname"));
                    professor.setBirthDate(rs.getDate("professor_birthDate").toLocalDate());
                    professor.setPhone(rs.getString("professor_phone"));
                    professors.add(professor);
                }
            }
        }
        return professors;
    }
}