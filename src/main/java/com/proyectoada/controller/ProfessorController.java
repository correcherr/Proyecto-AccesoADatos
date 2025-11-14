package com.proyectoada.controller;

import com.proyectoada.service.ProfessorService;
import com.proyectoada.view.ConsoleView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class ProfessorController {
    private Scanner scanner;
    private ConsoleView view;
    private ProfessorService professorService;
    
    public ProfessorController() {
        this.scanner = new Scanner(System.in);
        this.view = new ConsoleView();
        this.professorService = new ProfessorService();
    }
    
    public void createProfessor() {
        System.out.println("\n=== ALTA DE PROFESOR ===");
        
        System.out.print("Nombre: ");
        String name = scanner.nextLine();
        
        System.out.print("Apellidos: ");
        String surname = scanner.nextLine();
        
        LocalDate birthDate = getDateInput("Fecha de nacimiento (dd/MM/yyyy): ");
        if (birthDate == null) return;
        
        System.out.print("Teléfono: ");
        String phone = scanner.nextLine();
        
        boolean success = professorService.createProfessor(name, surname, birthDate, phone);
        if (success) {
            System.out.println("Profesor creado exitosamente.");
        } else {
            System.out.println("Error al crear el profesor.");
        }
    }
    
    private LocalDate getDateInput(String message) {
        while (true) {
            try {
                System.out.print(message);
                String dateStr = scanner.nextLine();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                return LocalDate.parse(dateStr, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Error: Formato de fecha inválido. Use dd/MM/yyyy");
            }
        }
    }
}