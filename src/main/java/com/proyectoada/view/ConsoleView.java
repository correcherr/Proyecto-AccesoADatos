package com.proyectoada.view;

import com.proyectoada.model.mysql.*;
import com.proyectoada.model.mongo.Review;

import java.util.List;

public class ConsoleView {
    
    public void showMainMenu() {
        System.out.println("\n=== GESTIÓN DE EXCURSIONES IES SERPIS ===");
        System.out.println("1. Alta profesor");
        System.out.println("2. Alta grupo");
        System.out.println("3. Baixa grupo");
        System.out.println("4. Alta excursió");
        System.out.println("5. Baixa excursió");
        System.out.println("6. Modificació excursió");
        System.out.println("7. Llistat total d'excursions amb professors");
        System.out.println("8. Llistat d'excursions finalitzades entre dates");
        System.out.println("9. Fer ressenya");
        System.out.println("10. Llistat de ressenyes per rang de puntuació");
        System.out.println("11. Eixir");
        System.out.println("===========================================");
    }
    
    public void showProfessors(List<Professor> professors) {
        if (professors.isEmpty()) {
            System.out.println("No hay profesores registrados.");
            return;
        }
        
        System.out.println("\n--- LISTA DE PROFESORES ---");
        System.out.println("ID | Nombre y Apellidos | Teléfono");
        System.out.println("----------------------------------");
        for (Professor professor : professors) {
            System.out.printf("%-3d | %-20s | %s\n", 
                professor.getTeacherId(),
                professor.getName() + " " + professor.getSurname(),
                professor.getPhone());
        }
    }
    
    public void showClasses(List<Class> classes) {
        if (classes.isEmpty()) {
            System.out.println("No hay grupos registrados.");
            return;
        }
        
        System.out.println("\n--- LISTA DE GRUPOS ---");
        System.out.println("ID | Nombre | Etapa | Nº Alumnos");
        System.out.println("--------------------------------");
        for (Class classObj : classes) {
            System.out.printf("%-3d | %-7s | %-12s | %d\n",
                classObj.getClassId(),
                classObj.getClassName(),
                classObj.getEducationalStage(),
                classObj.getNumberOfStudents());
        }
    }
    
    public void showTripsWithProfessors(List<Trip> trips) {
        if (trips.isEmpty()) {
            System.out.println("No hay excursiones registradas.");
            return;
        }
        
        System.out.println("\n--- LISTA DE EXCURSIONES ---");
        System.out.println("ID | Destino | Fecha | Duración | Coste | Estado | Grupo | Profesores");
        System.out.println("------------------------------------------------------------------------");
        for (Trip trip : trips) {
            System.out.printf("%-3d | %-15s | %-10s | %-4dh | %-6.2f€ | %-8s | %-6s | %s\n",
                trip.getTripId(),
                trip.getDestination(),
                trip.getDate(),
                trip.getDuration(),
                trip.getCost(),
                trip.isFinished() ? "FINALIZADA" : "PENDIENTE",
                trip.getClassName(),
                getProfessorsString(tripService.getProfessorsForTrip(trip.getTripId())));
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
    
    public void showReviews(List<Review> reviews) {
        if (reviews.isEmpty()) {
            System.out.println("No hay reseñas en el rango especificado.");
            return;
        }
        
        System.out.println("\n--- LISTA DE RESEÑAS ---");
        for (Review review : reviews) {
            System.out.println(review);
            System.out.println("----------------------------");
        }
    }
}