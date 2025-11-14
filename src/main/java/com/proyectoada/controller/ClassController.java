package com.proyectoada.controller;

import com.proyectoada.model.mysql.Class;
import com.proyectoada.service.ClassService;
import com.proyectoada.view.ConsoleView;

import java.util.List;
import java.util.Scanner;

public class ClassController {
    private Scanner scanner;
    private ConsoleView view;
    private ClassService classService;
    
    public ClassController() {
        this.scanner = new Scanner(System.in);
        this.view = new ConsoleView();
        this.classService = new ClassService();
    }
    
    public void createClass() {
        System.out.println("\n=== ALTA DE GRUPO ===");
        
        System.out.print("Nombre del grupo (ej: 1DAMC): ");
        String className = scanner.nextLine();
        
        System.out.print("Etapa educativa (ESO/Batxillerat/FP): ");
        String educationalStage = scanner.nextLine();
        
        System.out.print("Número de estudiantes: ");
        int numberOfStudents = getIntInput();
        
        boolean success = classService.createClass(className, educationalStage, numberOfStudents);
        if (success) {
            System.out.println("Grupo creado exitosamente.");
        } else {
            System.out.println("Error al crear el grupo.");
        }
    }
    
    public void deleteClass() {
        System.out.println("\n=== BAJA DE GRUPO ===");
        
        List<Class> classes = classService.getAllClasses();
        if (classes.isEmpty()) {
            System.out.println("No hay grupos registrados.");
            return;
        }
        
        view.showClasses(classes);
        System.out.print("Seleccione el ID del grupo a eliminar: ");
        int classId = getIntInput();
        
        boolean success = classService.deleteClass(classId);
        if (success) {
            System.out.println("Grupo eliminado exitosamente.");
        } else {
            System.out.println("Error al eliminar el grupo.");
        }
    }
    
    private int getIntInput() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar un número válido.");
            }
        }
    }
}