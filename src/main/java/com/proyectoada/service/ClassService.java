package com.proyectoada.service;

import com.proyectoada.dao.mysql.ClassDAO;
import com.proyectoada.model.mysql.Class;

import java.sql.SQLException;
import java.util.List;

public class ClassService {
    private ClassDAO classDAO;
    
    public ClassService() {
        this.classDAO = new ClassDAO();
    }
    
    public boolean createClass(String className, String educationalStage, int numberOfStudents) {
        try {
            // Validaciones
            if (className == null || className.trim().isEmpty()) {
                System.out.println("Error: El nombre de la clase no puede estar vacío.");
                return false;
            }
            if (!educationalStage.equals("ESO") && !educationalStage.equals("Batxillerat") && !educationalStage.equals("FP")) {
                System.out.println("Error: La etapa educativa debe ser ESO, Batxillerat o FP.");
                return false;
            }
            if (numberOfStudents <= 0) {
                System.out.println("Error: El número de estudiantes debe ser mayor que 0.");
                return false;
            }
            
            // Verificar duplicados
            if (classDAO.existsClassName(className.trim())) {
                System.out.println("Error: Ya existe una clase con este nombre.");
                return false;
            }
            
            Class classObj = new Class(className.trim(), educationalStage, numberOfStudents);
            classDAO.createClass(classObj);
            System.out.println("Clase creada exitosamente con ID: " + classObj.getClassId());
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error al crear clase: " + e.getMessage());
            return false;
        }
    }
    
    public List<Class> getAllClasses() {
        try {
            return classDAO.getAllClasses();
        } catch (SQLException e) {
            System.err.println("Error al obtener clases: " + e.getMessage());
            return List.of();
        }
    }
    
    public boolean deleteClass(int classId) {
        try {
            // Verificar si tiene excursiones asociadas
            if (classDAO.hasTrips(classId)) {
                System.out.println("Error: No se puede eliminar la clase porque tiene excursiones asociadas.");
                return false;
            }
            
            classDAO.deleteClass(classId);
            System.out.println("Clase eliminada exitosamente.");
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar clase: " + e.getMessage());
            return false;
        }
    }
    
    public Class getClassById(int id) {
        try {
            return classDAO.getClassById(id);
        } catch (SQLException e) {
            System.err.println("Error al obtener clase: " + e.getMessage());
            return null;
        }
    }
}