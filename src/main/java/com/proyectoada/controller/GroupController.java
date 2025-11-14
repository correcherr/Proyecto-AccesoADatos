package com.proyectoada.controller;

import java.util.List;

import com.proyectoada.model.mysql.Group;
import com.proyectoada.service.GroupService;
import com.proyectoada.view.ConsolaView;

public class GroupController {
    private ConsolaView view;
    private GroupService groupService;
    
    public GroupController(ConsolaView view, GroupService groupService) {
        this.view = view;
        this.groupService = groupService;
    }
    
    public void createGroup() {
        view.info("\n=== ALTA DE GRUPO ===");
        
        String groupName = view.pedir("Nombre del grupo");
        String educationalStage = view.pedir("Etapa educativa (ESO/Batxillerat/FP)");
        int numberOfStudents = view.pedirEntero("Número de estudiantes");
        
        boolean success = groupService.createGroup(groupName, educationalStage, numberOfStudents);
        if (success) {
            view.info("Grupo creado exitosamente.");
        } else {
            view.error("Error al crear el grupo.");
        }
    }
    
    public void deleteGroup() {
        view.info("\n=== BAJA DE GRUPO ===");
        
        List<Group> groups = groupService.getAllGroups();
        if (groups.isEmpty()) {
            view.info("No hay grupos registrados.");
            return;
        }
        
        showGroups(groups);
        int groupId = view.pedirEntero("Seleccione el ID del grupo a eliminar");
        
        boolean success = groupService.deleteGroup(groupId);
        if (success) {
            view.info("Grupo eliminado exitosamente.");
        } else {
            view.error("Error al eliminar el grupo.");
        }
    }
    
    public List<Group> getAllGroups() {
        return groupService.getAllGroups();
    }
    
    public Group getGroupById(int id) {
        return groupService.getGroupById(id);
    }
    
    public void showGroups(List<Group> groups) {
        if (groups.isEmpty()) {
            view.info("No hay grupos registrados.");
            return;
        }
        
        view.info("\n--- LISTA DE GRUPOS ---");
        view.info("ID | Nombre | Etapa | Nº Alumnos");
        view.info("--------------------------------");
        for (Group group : groups) {
            view.info(String.format("%-3d | %-7s | %-12s | %d",
                    group.getGroupId(),
                    group.getGroupName(),
                    group.getEducationalStage(),
                    group.getNumberOfStudents()));
        }
    }
}