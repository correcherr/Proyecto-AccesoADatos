package com.mongo.dao;

import com.mongo.connection.Conexion;
import com.mongo.model.mysql.Professor;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProfessorDAO {

    public void createProfessor(Professor professor) throws SQLException {
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
                    }
                }
            }
        }
    }

    public List<Professor> getAllProfessors() throws SQLException {
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
        }
        return professors;
    }

    public Professor getProfessorById(int id) throws SQLException {
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
                    return professor;
                }
            }
        }
        return null;
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