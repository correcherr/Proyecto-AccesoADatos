package com.proyectoada.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.proyectoada.connection.Conexion;
import com.proyectoada.model.mysql.Group;

public class GroupDAO implements Dao<Group, Integer> {

    @Override
    public Integer create(Group group) {
        String sql = "INSERT INTO `Groups` (group_name, educationalStage, numberOfStudents) VALUES (?, ?, ?)";

        try (Connection conn = Conexion.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, group.getGroupName());
            stmt.setString(2, group.getEducationalStage());
            stmt.setInt(3, group.getNumberOfStudents());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        group.setGroupId(generatedKeys.getInt(1));
                        return group.getGroupId();
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al crear grupo: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Optional<Group> findById(Integer id) {
        String sql = "SELECT * FROM `Groups` WHERE group_Id = ?";

        try (Connection conn = Conexion.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Group group = new Group();
                    group.setGroupId(rs.getInt("group_Id"));
                    group.setGroupName(rs.getString("group_name"));
                    group.setEducationalStage(rs.getString("educationalStage"));
                    group.setNumberOfStudents(rs.getInt("numberOfStudents"));
                    return Optional.of(group);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener grupo: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public List<Group> findAll() {
        List<Group> groups = new ArrayList<>();
        String sql = "SELECT * FROM `Groups` ORDER BY group_name";

        try (Connection conn = Conexion.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Group group = new Group();
                group.setGroupId(rs.getInt("group_Id"));
                group.setGroupName(rs.getString("group_name"));
                group.setEducationalStage(rs.getString("educationalStage"));
                group.setNumberOfStudents(rs.getInt("numberOfStudents"));
                groups.add(group);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener grupos: " + e.getMessage());
        }
        return groups;
    }

    @Override
    public boolean update(Group t) {
        // No hace falta para grupos
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM `Groups` WHERE group_Id = ?";

        try (Connection conn = Conexion.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al eliminar grupo: " + e.getMessage());
            return false;
        }
    }

    public boolean hasTrips(int groupId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM trips WHERE group_Id = ?";

        try (Connection conn = Conexion.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, groupId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public boolean existsGroupName(String groupName) throws SQLException {
        String sql = "SELECT COUNT(*) FROM `Groups` WHERE group_name = ?";

        try (Connection conn = Conexion.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, groupName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}