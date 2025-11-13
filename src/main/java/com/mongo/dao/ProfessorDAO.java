package com.mongo.dao;

import com.mongo.connection.Conexion;
import com.mongo.model.mysql.Professor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProfessorDAO implements Dao<Professor, Integer> {

    @Override
    public Integer create(Professor professor) {
        String sql = "INSERT INTO professors (professor_name, professor_surname, professor_birthDate, professor_phone) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexion.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, professor.getName());
            stmt.setString(2, professor.getSurname());
            stmt.setDate(3, Date.valueOf(professor.getBirthDate()));
            stmt.setString(4, professor.getPhone());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        professor.setProfessorId(generatedKeys.getInt(1));
                        return professor.getProfessorId();
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al crear profesor: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Optional<Professor> findById(Integer id) {
        String sql = "SELECT * FROM professors WHERE professor_Id = ?";

        try (Connection conn = Conexion.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Professor professor = new Professor();
                    professor.setProfessorId(rs.getInt("professor_Id"));
                    professor.setName(rs.getString("professor_name"));
                    professor.setSurname(rs.getString("professor_surname"));
                    professor.setBirthDate(rs.getDate("professor_birthDate").toLocalDate());
                    professor.setPhone(rs.getString("professor_phone"));
                    return Optional.of(professor);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener profesor: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Professor> findAll() {
        List<Professor> professors = new ArrayList<>();
        String sql = "SELECT * FROM professors ORDER BY professor_name, professor_surname";

        try (Connection conn = Conexion.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Professor professor = new Professor();
                professor.setProfessorId(rs.getInt("professor_Id"));
                professor.setName(rs.getString("professor_name"));
                professor.setSurname(rs.getString("professor_surname"));
                professor.setBirthDate(rs.getDate("professor_birthDate").toLocalDate());
                professor.setPhone(rs.getString("professor_phone"));
                professors.add(professor);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener profesores: " + e.getMessage());
        }
        return professors;
    }

    @Override
    public boolean update(Professor t) {
        // No implementado para profesores
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        // No implementado para profesores
        return false;
    }

    public boolean existsProfessorWithPhone(String phone) throws SQLException {
        String sql = "SELECT COUNT(*) FROM professors WHERE professor_phone = ?";

        try (Connection conn = Conexion.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, phone);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}