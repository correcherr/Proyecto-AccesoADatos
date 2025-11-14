package com.proyectoada.dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.proyectoada.connection.Conexion;
import com.proyectoada.model.mysql.Professor;
import com.proyectoada.model.mysql.Trip;

public class TripDAO implements Dao<Trip, Integer> {

    @Override
    public Integer create(Trip trip) {
        Connection conn = null;
        try {
            conn = Conexion.getConnection();
            conn.setAutoCommit(false);

            String sqlTrip = "INSERT INTO trips (group_Id, trip_destination, trip_duration, trip_date, trip_cost, trip_status) VALUES (?, ?, ?, ?, ?, ?)";
            int tripId;

            try (PreparedStatement stmt = conn.prepareStatement(sqlTrip, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, trip.getGroupId());
                stmt.setString(2, trip.getDestination());
                stmt.setInt(3, trip.getDuration());
                stmt.setDate(4, Date.valueOf(trip.getDate()));
                stmt.setDouble(5, trip.getCost());
                stmt.setString(6, trip.isStatus() ? "Finalizada" : "Pendiente");

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

            if (trip.getAccompanyingProfessors() != null && !trip.getAccompanyingProfessors().isEmpty()) {
                String sqlProfessors = "INSERT INTO professorsgoing (trip_id, professor_id) VALUES (?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(sqlProfessors)) {
                    for (Professor professor : trip.getAccompanyingProfessors()) {
                        stmt.setInt(1, tripId);
                        stmt.setInt(2, professor.getProfessorId());
                        stmt.addBatch();
                    }
                    stmt.executeBatch();
                }
            }

            return tripId;

        } catch (SQLException e) {
    
            System.err.println("Error al crear excursión: " + e.getMessage());
            return null;
        } 
    }

    @Override
    public Optional<Trip> findById(Integer id) {
        String sql = "SELECT * FROM trips WHERE trips_id = ?";

        try (Connection conn = Conexion.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Trip trip = new Trip();
                    trip.setTripId(rs.getInt("trips_id"));
                    trip.setGroupId(rs.getInt("group_Id"));
                    trip.setDestination(rs.getString("trip_destination"));
                    trip.setDuration(rs.getInt("trip_duration"));
                    trip.setDate(rs.getDate("trip_date").toLocalDate());
                    trip.setCost(rs.getDouble("trip_cost"));
                    String s = rs.getString("trip_status");
                    trip.setStatus(s != null && ("Finalizada".equalsIgnoreCase(s) || "Pendiente".equalsIgnoreCase(s)));
                    return Optional.of(trip);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener excursión: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Trip> findAll() {
        List<Trip> trips = new ArrayList<>();
        String sql = """
            SELECT t.trips_id, t.trip_destination, t.trip_duration, t.trip_date, 
                   t.trip_cost, t.trip_status, g.group_name, g.educationalStage
            FROM trips t
            JOIN `Groups` g ON t.group_Id = g.group_Id
            ORDER BY t.trip_date
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
                String s = rs.getString("trip_status");
                trip.setStatus(s != null && ("Finalizada".equalsIgnoreCase(s) || "Pendiente".equalsIgnoreCase(s)));
                trip.setGroupName(rs.getString("group_name"));
                trips.add(trip);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener excursiones: " + e.getMessage());
        }
        return trips;
    }

    @Override
    public boolean update(Trip t) {
        String sql = "UPDATE trips SET trip_date = ? WHERE trips_id = ? AND trip_status = 'Pendiente'";

        try (Connection conn = Conexion.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(t.getDate()));
            stmt.setInt(2, t.getTripId());
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar excursión: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM trips WHERE trips_id = ? AND trip_status = 'Pendiente'";

        try (Connection conn = Conexion.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar excursión: " + e.getMessage());
            return false;
        }
    }

    public List<Trip> getFinishedTripsBetweenDates(LocalDate startDate, LocalDate endDate) throws SQLException {
        List<Trip> trips = new ArrayList<>();
        String sql = """
            SELECT t.*, g.group_name
            FROM trips t
            JOIN `Groups` g ON t.group_Id = g.group_Id
            WHERE t.trip_status = 'Finalizada' AND t.trip_date BETWEEN ? AND ?
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
                    String s = rs.getString("trip_status");
                    trip.setStatus(s != null && ("Finalizada".equalsIgnoreCase(s) || "Pendiente".equalsIgnoreCase(s)));
                    trip.setGroupName(rs.getString("group_name"));
                    trips.add(trip);
                }
            }
        }
        return trips;
    }

    public void markTripAsFinished(int tripId) throws SQLException {
        String sql = "UPDATE trips SET trip_status = 'Finalizada' WHERE trips_id = ?";

        try (Connection conn = Conexion.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, tripId);
            stmt.executeUpdate();
        }
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