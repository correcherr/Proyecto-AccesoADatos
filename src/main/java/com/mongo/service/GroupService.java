package com.mongo.service;

import com.mongo.dao.GroupDAO;
import com.mongo.model.mysql.Group;

import java.sql.SQLException;
import java.util.List;

public class GroupService {
    private GroupDAO groupDAO;

    public GroupService() {
        this.groupDAO = new GroupDAO();
    }

    public boolean createGroup(String groupName, String educationalStage, int numberOfStudents) {
        try {
            // Validaciones
            if (groupName == null || groupName.trim().isEmpty()) {
                System.out.println("Error: El nombre del grupo no puede estar vacío.");
                return false;
            }
            if (!educationalStage.equals("ESO") && !educationalStage.equals("Batxillerat")
                    && !educationalStage.equals("FP")) {
                System.out.println("Error: La etapa educativa debe ser ESO, Batxillerat o FP.");
                return false;
            }
            if (numberOfStudents <= 0) {
                System.out.println("Error: El número de estudiantes debe ser mayor que 0.");
                return false;
            }

            // Verificar duplicados
            if (groupDAO.existsGroupName(groupName.trim())) {
                System.out.println("Error: Ya existe un grupo con este nombre.");
                return false;
            }

            Group group = new Group(groupName.trim(), educationalStage, numberOfStudents);
            groupDAO.createGroup(group);
            System.out.println("Grupo creado exitosamente con ID: " + group.getGroupId());
            return true;

        } catch (SQLException e) {
            System.err.println("Error al crear grupo: " + e.getMessage());
            return false;
        }
    }

    public List<Group> getAllGroups() {
        try {
            return groupDAO.getAllGroups();
        } catch (SQLException e) {
            System.err.println("Error al obtener grupos: " + e.getMessage());
            return List.of();
        }
    }

    public boolean deleteGroup(int groupId) {
        try {
            // Verificar si tiene excursiones asociadas
            if (groupDAO.hasTrips(groupId)) {
                System.out.println("Error: No se puede eliminar el grupo porque tiene excursiones asociadas.");
                return false;
            }

            groupDAO.deleteGroup(groupId);
            System.out.println("Grupo eliminado exitosamente.");
            return true;

        } catch (SQLException e) {
            System.err.println("Error al eliminar grupo: " + e.getMessage());
            return false;
        }
    }

    public Group getGroupById(int id) {
        try {
            return groupDAO.getGroupById(id);
        } catch (SQLException e) {
            System.err.println("Error al obtener grupo: " + e.getMessage());
            return null;
        }
    }
}