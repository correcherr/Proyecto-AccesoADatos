package com.mongo.service;

import com.mongo.dao.ProfessorDAO;
import com.mongo.model.mysql.Professor;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ProfessorService {
    private ProfessorDAO professorDAO;

    public ProfessorService() {
        this.professorDAO = new ProfessorDAO();
    }

    public boolean createProfessor(String name, String surname, LocalDate birthDate, String phone) {
        try {
            // Validaciones
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
            if (phone == null || phone.trim().isEmpty() || phone.length() != 9) {
                System.out.println("Error: El teléfono no puede estar vacío y tiene que tener exactamente 9 dígitos.");
                return false;
            }

            // Verificar duplicados por teléfono
            if (professorDAO.existsProfessorWithPhone(phone)) {
                System.out.println("Error: Ya existe un profesor con este número de teléfono.");
                return false;
            }

            Professor professor = new Professor(name.trim(), surname.trim(), birthDate, phone.trim());
            professorDAO.createProfessor(professor);
            System.out.println("Profesor creado exitosamente con ID: " + professor.getProfessorId());
            return true;

        } catch (SQLException e) {
            System.err.println("Error al crear profesor: " + e.getMessage());
            return false;
        }
    }

    public List<Professor> getAllProfessors() {
        try {
            return professorDAO.getAllProfessors();
        } catch (SQLException e) {
            System.err.println("Error al obtener profesores: " + e.getMessage());
            return List.of();
        }
    }

    public Professor getProfessorById(int id) {
        try {
            return professorDAO.getProfessorById(id);
        } catch (SQLException e) {
            System.err.println("Error al obtener profesor: " + e.getMessage());
            return null;
        }
    }
}