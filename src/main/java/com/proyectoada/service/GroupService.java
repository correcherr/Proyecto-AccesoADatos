package com.proyectoada.service;

import java.sql.SQLException;
import java.util.List;

import com.proyectoada.dao.GroupDAO;
import com.proyectoada.model.mysql.Group;

public class GroupService {
    private GroupDAO groupDAO;

    public GroupService() {
        this.groupDAO = new GroupDAO();
    }

    private boolean validateFieldsGroups(String groupName, String educationalStage, int numberOfStudents) {
        if (groupName == null || groupName.trim().isEmpty()) {
            System.out.println("Error: El nombre del grupo no puede estar vacío.");
            return false;
        }
        if (!educationalStage.equals("Primaria") && !educationalStage.equals("ESO") && !educationalStage.equals("Bachillerato") && !educationalStage.equals("FP")) {
            System.out.println("Error: La etapa educativa debe ser Primaria, ESO, Bachillerato o FP.");
            return false;
        }
        if (numberOfStudents <= 0) {
            System.out.println("Error: El número de estudiantes debe ser mayor que 0.");
            return false;
        }
        return true;
    }
    
    public boolean createGroup(String groupName, String educationalStage, int numberOfStudents) {
        try {
            if (!validateFieldsGroups(groupName, educationalStage, numberOfStudents)) {
                return false;
            }
            
            if (groupDAO.existsGroupName(groupName.trim())) {
                System.out.println("Error: Ya existe un grupo con este nombre.");
                return false;
            }
            
            Group group = new Group(groupName.trim(), educationalStage, numberOfStudents);
            Integer groupId = groupDAO.create(group);
            if (groupId != null) {
                System.out.println("Grupo creado exitosamente con ID: " + groupId);
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            System.err.println("Error al crear grupo: " + e.getMessage());
            return false;
        }
    }

    public List<Group> getAllGroups() {
        return groupDAO.findAll();
    }

    public boolean deleteGroup(int groupId) {
        try {
            if (groupDAO.hasTrips(groupId)) {
                System.out.println("Error: No se puede eliminar el grupo porque tiene excursiones asociadas.");
                return false;
            }

            if (groupDAO.delete(groupId)) {
                return true;
            }
            return false;

        } catch (SQLException e) {
            System.err.println("Error al eliminar grupo: " + e.getMessage());
            return false;
        }
    }

    public Group getGroupById(int id) {
        return groupDAO.findById(id).orElse(null);
    }
}