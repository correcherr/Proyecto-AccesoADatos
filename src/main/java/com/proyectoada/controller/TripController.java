package com.proyectoada.controller;

import com.proyectoada.model.mysql.*;
import com.proyectoada.model.mongo.Review;
import com.proyectoada.service.*;
import com.proyectoada.view.ConsoleView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TripController {
    private Scanner scanner;
    private ConsoleView view;
    private TripService tripService;
    private ClassService classService;
    private ProfessorService professorService;
    private ReviewService reviewService;
    
    public TripController() {
        this.scanner = new Scanner(System.in);
        this.view = new ConsoleView();
        this.tripService = new TripService();
        this.classService = new ClassService();
        this.professorService = new ProfessorService();
        this.reviewService = new ReviewService();
    }
    
    public void createTrip() {
        System.out.println("\n=== ALTA DE EXCURSIÓN ===");
        
        // Seleccionar grupo
        List<Class> classes = classService.getAllClasses();
        if (classes.isEmpty()) {
            System.out.println("Error: No hay grupos registrados. Debe crear un grupo primero.");
            return;
        }
        view.showClasses(classes);
        System.out.print("Seleccione el ID del grupo: ");
        int classId = getIntInput();
        
        System.out.print("Destino: ");
        String destination = scanner.nextLine();
        
        System.out.print("Duración (horas): ");
        int duration = getIntInput();
        
        LocalDate date = getDateInput("Fecha de la excursión (dd/MM/yyyy): ");
        if (date == null) return;
        
        System.out.print("Coste: ");
        double cost = getDoubleInput();
        
        // Asignar profesores acompañantes
        List<Professor> selectedProfessors = assignProfessorsToTrip();
        if (selectedProfessors.isEmpty()) {
            System.out.println("Error: Debe asignar al menos un profesor.");
            return;
        }
        
        boolean success = tripService.createTrip(classId, destination, duration, date, cost, selectedProfessors);
        if (success) {
            System.out.println("Excursión creada exitosamente.");
        } else {
            System.out.println("Error al crear la excursión.");
        }
    }
    
    private List<Professor> assignProfessorsToTrip() {
        List<Professor> selectedProfessors = new ArrayList<>();
        List<Professor> allProfessors = professorService.getAllProfessors();
        
        if (allProfessors.isEmpty()) {
            System.out.println("Error: No hay profesores registrados. Debe crear profesores primero.");
            return selectedProfessors;
        }
        
        System.out.println("\n--- ASIGNACIÓN DE PROFESORES ACOMPAÑANTES ---");
        System.out.println("Lista de profesores disponibles:");
        view.showProfessors(allProfessors);
        
        System.out.println("Seleccione los IDs de los profesores acompañantes (0 para terminar):");
        
        while (true) {
            System.out.print("ID del profesor: ");
            int professorId = getIntInput();
            
            if (professorId == 0) {
                if (selectedProfessors.isEmpty()) {
                    System.out.println("Debe seleccionar al menos un profesor.");
                    continue;
                }
                break;
            }
            
            Professor professor = professorService.getProfessorById(professorId);
            if (professor != null) {
                if (!selectedProfessors.contains(professor)) {
                    selectedProfessors.add(professor);
                    System.out.println("Profesor " + professor.getName() + " " + professor.getSurname() + " añadido.");
                } else {
                    System.out.println("Este profesor ya está asignado.");
                }
            } else {
                System.out.println("ID de profesor no válido.");
            }
        }
        
        return selectedProfessors;
    }
    
    public void deleteTrip() {
        System.out.println("\n=== BAJA DE EXCURSIÓN ===");
        
        List<Trip> trips = tripService.getAllTripsWithProfessors();
        if (trips.isEmpty()) {
            System.out.println("No hay excursiones registradas.");
            return;
        }
        
        view.showTripsWithProfessors(trips);
        System.out.print("Seleccione el ID de la excursión a eliminar: ");
        int tripId = getIntInput();
        
        boolean success = tripService.deleteTrip(tripId);
        if (success) {
            System.out.println("Excursión eliminada exitosamente.");
        } else {
            System.out.println("Error al eliminar la excursión.");
        }
    }
    
    public void updateTripDate() {
        System.out.println("\n=== MODIFICACIÓN DE EXCURSIÓN ===");
        
        List<Trip> trips = tripService.getAllTripsWithProfessors();
        if (trips.isEmpty()) {
            System.out.println("No hay excursiones registradas.");
            return;
        }
        
        view.showTripsWithProfessors(trips);
        System.out.print("Seleccione el ID de la excursión a modificar: ");
        int tripId = getIntInput();
        
        LocalDate newDate = getDateInput("Nueva fecha (dd/MM/yyyy): ");
        if (newDate == null) return;
        
        boolean success = tripService.updateTripDate(tripId, newDate);
        if (success) {
            System.out.println("Fecha de excursión actualizada exitosamente.");
        } else {
            System.out.println("Error al actualizar la fecha de la excursión.");
        }
    }
    
    public void listAllTripsWithProfessors() {
        System.out.println("\n=== LISTADO TOTAL DE EXCURSIONES ===");
        List<Trip> trips = tripService.getAllTripsWithProfessors();
        view.showTripsWithProfessors(trips);
    }
    
    public void listFinishedTripsBetweenDates() {
        System.out.println("\n=== EXCURSIONES FINALIZADAS ENTRE FECHAS ===");
        
        LocalDate startDate = getDateInput("Fecha inicial (dd/MM/yyyy): ");
        if (startDate == null) return;
        
        LocalDate endDate = getDateInput("Fecha final (dd/MM/yyyy): ");
        if (endDate == null) return;
        
        List<Trip> trips = tripService.getFinishedTripsBetweenDates(startDate, endDate);
        view.showTripsWithProfessors(trips);
    }
    
    public void createReview() {
        System.out.println("\n=== REALIZAR RESEÑA ===");
        
        List<Trip> trips = tripService.getAllTripsWithProfessors();
        if (trips.isEmpty()) {
            System.out.println("No hay excursiones registradas.");
            return;
        }
        
        // Mostrar solo excursiones no finalizadas
        List<Trip> unfinishedTrips = trips.stream()
                .filter(trip -> !trip.isFinished())
                .toList();
        
        if (unfinishedTrips.isEmpty()) {
            System.out.println("No hay excursiones pendientes de finalizar.");
            return;
        }
        
        view.showTripsWithProfessors(unfinishedTrips);
        System.out.print("Seleccione el ID de la excursión para hacer la reseña: ");
        int tripId = getIntInput();
        
        // Verificar si ya existe reseña
        if (reviewService.existsReviewForTrip(tripId)) {
            System.out.println("Error: Ya existe una reseña para esta excursión.");
            return;
        }
        
        System.out.print("Comentario: ");
        String comment = scanner.nextLine();
        
        System.out.print("Puntuación (1-10): ");
        int rating = getIntInput();
        
        // Crear reseña en MongoDB
        boolean reviewSuccess = reviewService.createReview(tripId, comment, rating);
        if (reviewSuccess) {
            // Marcar excursión como finalizada en MySQL
            boolean tripSuccess = tripService.markTripAsFinished(tripId);
            if (tripSuccess) {
                System.out.println("Reseña creada y excursión marcada como finalizada exitosamente.");
            } else {
                System.out.println("Reseña creada pero error al marcar la excursión como finalizada.");
            }
        } else {
            System.out.println("Error al crear la reseña.");
        }
    }
    
    public void listReviewsByRatingRange() {
        System.out.println("\n=== RESEÑAS POR RANGO DE PUNTUACIÓN ===");
        
        System.out.print("Ingrese el rango de puntuación (formato: min,max): ");
        String rangeInput = scanner.nextLine();
        
        try {
            String[] parts = rangeInput.split(",");
            if (parts.length != 2) {
                System.out.println("Error: Formato incorrecto. Use min,max");
                return;
            }
            
            int minRating = Integer.parseInt(parts[0].trim());
            int maxRating = Integer.parseInt(parts[1].trim());
            
            List<Review> reviews = reviewService.getReviewsByRatingRange(minRating, maxRating);
            view.showReviews(reviews);
            
        } catch (NumberFormatException e) {
            System.out.println("Error: Los valores deben ser números.");
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
    
    private double getDoubleInput() {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar un número válido.");
            }
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