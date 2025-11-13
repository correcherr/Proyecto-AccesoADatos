package com.mongo.controller;

import com.mongo.view.ConsolaView;
import com.mongo.model.mysql.Professor;
import com.mongo.service.ProfessorService;

import java.time.LocalDate;
import java.util.List;

public class ProfessorController {
    private ConsolaView view;
    private ProfessorService professorService;
    
    public ProfessorController(ConsolaView view, ProfessorService professorService) {
        this.view = view;
        this.professorService = professorService;
    }
    
    public void createProfessor() {
        view.info("\n=== ALTA DE PROFESOR ===");
        
        String name = view.pedir("Nombre");
        String surname = view.pedir("Apellidos");
        LocalDate birthDate = view.pedirFecha("Fecha de nacimiento (YYYY-MM-DD)");
        String phone = view.pedir("Teléfono");
        
        boolean success = professorService.createProfessor(name, surname, birthDate, phone);
        if (success) {
            view.info("Profesor creado exitosamente.");
        } else {
            view.error("Error al crear el profesor.");
        }
    }
    
    public List<Professor> getAllProfessors() {
        return professorService.getAllProfessors();
    }
    
    public Professor getProfessorById(int id) {
        return professorService.getProfessorById(id);
    }
    
    public void showProfessors(List<Professor> professors) {
        if (professors.isEmpty()) {
            view.info("No hay profesores registrados.");
            return;
        }
        
        view.info("\n--- LISTA DE PROFESORES ---");
        view.info("ID | Nombre y Apellidos | Teléfono");
        view.info("----------------------------------");
        for (Professor professor : professors) {
            view.info(String.format("%-3d | %-20s | %s",
                    professor.getProfessorId(),
                    professor.getName() + " " + professor.getSurname(),
                    professor.getPhone()));
        }
    }
}