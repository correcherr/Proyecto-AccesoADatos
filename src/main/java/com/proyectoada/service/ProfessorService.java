package com.proyectoada.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import com.proyectoada.dao.ProfessorDAO;
import com.proyectoada.model.mysql.Professor;

public class ProfessorService {
    private ProfessorDAO professorDAO;

    public ProfessorService() {
        this.professorDAO = new ProfessorDAO();
    }
    private boolean validateFieldsProfessor(String name, String surname, LocalDate birthDate, String phone) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Error: El nombre no puede estar vacío.");
            return false;
        }
        if (surname == null || surname.trim().isEmpty()) {
            System.out.println("Error: Los apellidos no pueden estar vacíos.");
            return false;
        }
        if (birthDate == null || birthDate.isAfter(LocalDate.now())) {
            System.out.println("Error: La fecha de nacimiento debe ser anterior a la fecha actual.");
            return false;
        }
        if (phone == null || phone.trim().isEmpty()) {
            System.out.println("Error: El teléfono no puede estar vacío.");
            return false;
        }
        return true;
    }
    
    public boolean createProfessor(String name, String surname, LocalDate birthDate, String phone) {
        try {
            if (!validateFieldsProfessor(name, surname, birthDate, phone)) {
                return false;
            }
            
            if (professorDAO.existsProfessorWithPhone(phone)) {
                System.out.println("Error: Ya existe un profesor con este número de teléfono.");
                return false;
            }
            
            Professor professor = new Professor(name.trim(), surname.trim(), birthDate, phone.trim());
            Integer professorId = professorDAO.create(professor);
            if (professorId != null) {
                System.out.println("Profesor creado exitosamente con ID: " + professorId);
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            System.err.println("Error al crear profesor: " + e.getMessage());
            return false;
        }
    }

    public List<Professor> getAllProfessors() {
        return professorDAO.findAll();
    }

    public Professor getProfessorById(int id) {
        return professorDAO.findById(id).orElse(null);
    }
}