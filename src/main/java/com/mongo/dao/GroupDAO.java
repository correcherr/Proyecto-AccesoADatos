package com.mongo.dao;

import com.mongo.connection.Conexion;
import com.mongo.model.mysql.Group;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GroupDAO {

    public void createGroup(Group group) throws SQLException {
        String sql = "INSERT INTO groups (group_name, educationalStage, numberOfStudents) VALUES (?, ?, ?)";

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
                    }
                }
            }
        }
    }

    public List<Group> getAllGroups() throws SQLException {
        List<Group> groups = new ArrayList<>();
        String sql = "SELECT * FROM groups ORDER BY group_name";

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
        }
        return groups;
    }

    public Group getGroupById(int id) throws SQLException {
        String sql = "SELECT * FROM groups WHERE group_Id = ?";

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
                    return group;
                }
            }
        }
        return null;
    }

    public void deleteGroup(int groupId) throws SQLException {
        String sql = "DELETE FROM groups WHERE group_Id = ?";

        try (Connection conn = Conexion.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, groupId);
            stmt.executeUpdate();
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
        String sql = "SELECT COUNT(*) FROM groups WHERE group_name = ?";

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