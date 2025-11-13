package com.mongo.controller;

import com.mongo.view.ConsolaView;
import com.mongo.model.mysql.*;
import com.mongo.model.mongo.Review;
import com.mongo.service.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MainController {
    private ConsolaView view;
    private ProfessorService professorService;
    private GroupService groupService;
    private TripService tripService;
    private ReviewService reviewService;
    
    public MainController() {
        this.view = new ConsolaView();
        this.professorService = new ProfessorService();
        this.groupService = new GroupService();
        this.tripService = new TripService();
        this.reviewService = new ReviewService();
    }
    
    public void showMainMenu() {
        int option;
        do {
            option = view.menu();
            
            switch (option) {
                case 1:
                    createProfessor();
                    break;
                case 2:
                    createGroup();
                    break;
                case 3:
                    deleteGroup();
                    break;
                case 4:
                    createTrip();
                    break;
                case 5:
                    deleteTrip();
                    break;
                case 6:
                    updateTripDate();
                    break;
                case 7:
                    listAllTripsWithProfessors();
                    break;
                case 8:
                    listFinishedTripsBetweenDates();
                    break;
                case 9:
                    createReview();
                    break;
                case 10:
                    listReviewsByRatingRange();
                    break;
                case 11:
                    showExitMessage();
                    break;
                default:
                    view.error("Opción no válida. Intente nuevamente.");
            }
            

            
        } while (option != 11);
    }
    
    // ========== MÉTODOS DE PROFESOR ==========
    
    private void createProfessor() {
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
    
    // ========== MÉTODOS DE GRUPO ==========
    
    private void createGroup() {
        view.info("\n=== ALTA DE GRUPO ===");
        
        String groupName = view.pedir("Nombre del grupo (ej: 1DAMC)");
        String educationalStage = view.pedir("Etapa educativa (ESO/Batxillerat/FP)");
        int numberOfStudents = view.pedirEntero("Número de estudiantes");
        
        boolean success = groupService.createGroup(groupName, educationalStage, numberOfStudents);
        if (success) {
            view.info("Grupo creado exitosamente.");
        } else {
            view.error("Error al crear el grupo.");
        }
    }
    
    private void deleteGroup() {
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
    
    // ========== MÉTODOS DE EXCURSIÓN ==========
    
    private void createTrip() {
        view.info("\n=== ALTA DE EXCURSIÓN ===");
        
        // Seleccionar grupo
        List<Group> groups = groupService.getAllGroups();
        if (groups.isEmpty()) {
            view.error("No hay grupos registrados. Debe crear un grupo primero.");
            return;
        }
        showGroups(groups);
        int groupId = view.pedirEntero("Seleccione el ID del grupo");
        
        String destination = view.pedir("Destino");
        int duration = view.pedirEntero("Duración (horas)");
        LocalDate date = view.pedirFecha("Fecha de la excursión (YYYY-MM-DD)");
        double cost = view.pedirEntero("Coste");
        
        // Asignar profesores acompañantes
        List<Professor> selectedProfessors = assignProfessorsToTrip();
        if (selectedProfessors.isEmpty()) {
            view.error("Debe asignar al menos un profesor.");
            return;
        }
        
        boolean success = tripService.createTrip(groupId, destination, duration, date, cost, selectedProfessors);
        if (success) {
            view.info("Excursión creada exitosamente.");
        } else {
            view.error("Error al crear la excursión.");
        }
    }
    
    private List<Professor> assignProfessorsToTrip() {
        List<Professor> selectedProfessors = new ArrayList<>();
        List<Professor> allProfessors = professorService.getAllProfessors();
        
        if (allProfessors.isEmpty()) {
            view.error("No hay profesores registrados. Debe crear profesores primero.");
            return selectedProfessors;
        }
        
        view.info("\n--- ASIGNACIÓN DE PROFESORES ACOMPAÑANTES ---");
        view.info("Lista de profesores disponibles:");
        showProfessors(allProfessors);
        
        view.info("Seleccione los IDs de los profesores acompañantes (0 para terminar):");
        
        while (true) {
            int professorId = view.pedirEntero("ID del profesor");
            
            if (professorId == 0) {
                if (selectedProfessors.isEmpty()) {
                    view.error("Debe seleccionar al menos un profesor.");
                    continue;
                }
                break;
            }
            
            Professor professor = professorService.getProfessorById(professorId);
            if (professor != null) {
                if (!selectedProfessors.contains(professor)) {
                    selectedProfessors.add(professor);
                    view.info("Profesor " + professor.getName() + " " + professor.getSurname() + " añadido.");
                } else {
                    view.error("Este profesor ya está asignado.");
                }
            } else {
                view.error("ID de profesor no válido.");
            }
        }
        
        return selectedProfessors;
    }
    
    private void deleteTrip() {
        view.info("\n=== BAJA DE EXCURSIÓN ===");
        
        List<Trip> trips = tripService.getAllTripsWithProfessors();
        if (trips.isEmpty()) {
            view.info("No hay excursiones registradas.");
            return;
        }
        
        showTripsWithProfessors(trips);
        int tripId = view.pedirEntero("Seleccione el ID de la excursión a eliminar");
        
        boolean success = tripService.deleteTrip(tripId);
        if (success) {
            view.info("Excursión eliminada exitosamente.");
        } else {
            view.error("Error al eliminar la excursión.");
        }
    }
    
    private void updateTripDate() {
        view.info("\n=== MODIFICACIÓN DE EXCURSIÓN ===");
        
        List<Trip> trips = tripService.getAllTripsWithProfessors();
        if (trips.isEmpty()) {
            view.info("No hay excursiones registradas.");
            return;
        }
        
        showTripsWithProfessors(trips);
        int tripId = view.pedirEntero("Seleccione el ID de la excursión a modificar");
        
        LocalDate newDate = view.pedirFecha("Nueva fecha (YYYY-MM-DD)");
        
        boolean success = tripService.updateTripDate(tripId, newDate);
        if (success) {
            view.info("Fecha de excursión actualizada exitosamente.");
        } else {
            view.error("Error al actualizar la fecha de la excursión.");
        }
    }
    
    private void listAllTripsWithProfessors() {
        view.info("\n=== LISTADO TOTAL DE EXCURSIONES ===");
        List<Trip> trips = tripService.getAllTripsWithProfessors();
        showTripsWithProfessors(trips);
    }
    
    private void listFinishedTripsBetweenDates() {
        view.info("\n=== EXCURSIONES FINALIZADAS ENTRE FECHAS ===");
        
        LocalDate startDate = view.pedirFecha("Fecha inicial (YYYY-MM-DD)");
        LocalDate endDate = view.pedirFecha("Fecha final (YYYY-MM-DD)");
        
        List<Trip> trips = tripService.getFinishedTripsBetweenDates(startDate, endDate);
        showTripsWithProfessors(trips);
    }
    
    // ========== MÉTODOS DE RESEÑAS (MongoDB) ==========
    
    private void createReview() {
        view.info("\n=== REALIZAR RESEÑA ===");
        
        List<Trip> trips = tripService.getAllTripsWithProfessors();
        if (trips.isEmpty()) {
            view.info("No hay excursiones registradas.");
            return;
        }
        
        // Mostrar solo excursiones no finalizadas
        List<Trip> unfinishedTrips = trips.stream()
                .filter(trip -> !trip.isFinished())
                .toList();
        
        if (unfinishedTrips.isEmpty()) {
            view.info("No hay excursiones pendientes de finalizar.");
            return;
        }
        
        showTripsWithProfessors(unfinishedTrips);
        int tripId = view.pedirEntero("Seleccione el ID de la excursión para hacer la reseña");
        
        // Verificar si ya existe reseña
        if (reviewService.existsReviewForTrip(tripId)) {
            view.error("Ya existe una reseña para esta excursión.");
            return;
        }
        
        String comment = view.pedir("Comentario");
        int rating = view.pedirEntero("Puntuación (1-10)");
        
        // Crear reseña en MongoDB
        boolean reviewSuccess = reviewService.createReview(tripId, comment, rating);
        if (reviewSuccess) {
            // Marcar excursión como finalizada en MySQL
            boolean tripSuccess = tripService.markTripAsFinished(tripId);
            if (tripSuccess) {
                view.info("Reseña creada y excursión marcada como finalizada exitosamente.");
            } else {
                view.error("Reseña creada pero error al marcar la excursión como finalizada.");
            }
        } else {
            view.error("Error al crear la reseña.");
        }
    }
    
    private void listReviewsByRatingRange() {
        view.info("\n=== RESEÑAS POR RANGO DE PUNTUACIÓN ===");
        
        String rangeInput = view.pedir("Ingrese el rango de puntuación (formato: min,max)");
        
        try {
            String[] parts = rangeInput.split(",");
            if (parts.length != 2) {
                view.error("Formato incorrecto. Use min,max");
                return;
            }
            
            int minRating = Integer.parseInt(parts[0].trim());
            int maxRating = Integer.parseInt(parts[1].trim());
            
            List<Review> reviews = reviewService.getReviewsByRatingRange(minRating, maxRating);
            showReviews(reviews);
            
        } catch (NumberFormatException e) {
            view.error("Los valores deben ser números.");
        }
    }
    
    // ========== MÉTODOS DE VISUALIZACIÓN ==========
    
    private void showProfessors(List<Professor> professors) {
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
    
    private void showGroups(List<Group> groups) {
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
    
    private void showTripsWithProfessors(List<Trip> trips) {
        if (trips.isEmpty()) {
            view.info("No hay excursiones registradas.");
            return;
        }
        
        view.info("\n--- LISTA DE EXCURSIONES ---");
        view.info("ID | Destino | Fecha | Duración | Coste | Estado | Grupo");
        view.info("----------------------------------------------------------------");
        for (Trip trip : trips) {
            List<Professor> professors = tripService.getProfessorsForTrip(trip.getTripId());
            String professorsStr = getProfessorsString(professors);
            
            view.info(String.format("%-3d | %-15s | %-10s | %-4dh | %-6.2f€ | %-8s | %-6s | %s",
                trip.getTripId(),
                trip.getDestination(),
                trip.getDate(),
                trip.getDuration(),
                trip.getCost(),
                trip.isFinished() ? "FINALIZADA" : "PENDIENTE",
                trip.getGroupName(),
                professorsStr));
        }
    }
    
    private void showReviews(List<Review> reviews) {
        if (reviews.isEmpty()) {
            view.info("No hay reseñas en el rango especificado.");
            return;
        }
        
        view.info("\n--- LISTA DE RESEÑAS ---");
        for (Review review : reviews) {
            view.info(review.toString());
            view.info("----------------------------");
        }
    }
    
    private String getProfessorsString(List<Professor> professors) {
        if (professors == null || professors.isEmpty()) {
            return "Sin profesores";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < professors.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(professors.get(i).getName().charAt(0)).append(". ").append(professors.get(i).getSurname());
        }
        return sb.toString();
    }
    
    private void showExitMessage() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm 'del dia' dd/MM/yyyy");
        String formattedDate = now.format(formatter);
        view.info("\nAdeu. Sessió finalitzada a les " + formattedDate);
    }
}