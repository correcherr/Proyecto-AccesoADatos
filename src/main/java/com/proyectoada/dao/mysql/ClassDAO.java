package com.proyectoada.dao.mysql;

import com.proyectoada.connection.Conexion;
import com.proyectoada.model.mysql.Class;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClassDAO {
    
    public void createClass(Class classObj) throws SQLException {
        String sql = "INSERT INTO classes (class_name, educationalStage, numberOfStudents) VALUES (?, ?, ?)";
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, classObj.getClassName());
            stmt.setString(2, classObj.getEducationalStage());
            stmt.setInt(3, classObj.getNumberOfStudents());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        classObj.setClassId(generatedKeys.getInt(1));
                    }
                }
            }
        }
    }
    
    public List<Class> getAllClasses() throws SQLException {
        List<Class> classes = new ArrayList<>();
        String sql = "SELECT * FROM classes ORDER BY class_name";
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Class classObj = new Class();
                classObj.setClassId(rs.getInt("class_Id"));
                classObj.setClassName(rs.getString("class_name"));
                classObj.setEducationalStage(rs.getString("educationalStage"));
                classObj.setNumberOfStudents(rs.getInt("numberOfStudents"));
                classes.add(classObj);
            }
        }
        return classes;
    }
    
    public Class getClassById(int id) throws SQLException {
        String sql = "SELECT * FROM classes WHERE class_Id = ?";
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Class classObj = new Class();
                    classObj.setClassId(rs.getInt("class_Id"));
                    classObj.setClassName(rs.getString("class_name"));
                    classObj.setEducationalStage(rs.getString("educationalStage"));
                    classObj.setNumberOfStudents(rs.getInt("numberOfStudents"));
                    return classObj;
                }
            }
        }
        return null;
    }
    
    public void deleteClass(int classId) throws SQLException {
        String sql = "DELETE FROM classes WHERE class_Id = ?";
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, classId);
            stmt.executeUpdate();
        }
    }
    
    public boolean hasTrips(int classId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM trips WHERE class_Id = ?";
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, classId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    public boolean existsClassName(String className) throws SQLException {
        String sql = "SELECT COUNT(*) FROM classes WHERE class_name = ?";
        
        try (Connection conn = Conexion.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, className);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}