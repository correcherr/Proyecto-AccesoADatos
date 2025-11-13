package com.mongo.controller;

import com.mongo.view.ConsolaView;
import com.mongo.model.mysql.Group;
import com.mongo.model.mysql.Professor;
import com.mongo.model.mysql.Trip;
import com.mongo.service.GroupService;
import com.mongo.service.ProfessorService;
import com.mongo.service.TripService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TripController {
    private ConsolaView view;
    private TripService tripService;
    private GroupService groupService;
    private ProfessorService professorService;
    
    public TripController(ConsolaView view, TripService tripService, GroupService groupService, ProfessorService professorService) {
        this.view = view;
        this.tripService = tripService;
        this.groupService = groupService;
        this.professorService = professorService;
    }
    
    public void createTrip() {
        view.info("\n=== ALTA DE EXCURSIÓN ===");
        
        // Seleccionar grupo
        List<Group> groups = groupService.getAllGroups();
        if (groups.isEmpty()) {
            view.error("No hay grupos registrados. Debe crear un grupo primero.");
            return;
        }
        
        GroupController groupController = new GroupController(view, groupService);
        groupController.showGroups(groups);
        
        int groupId = view.pedirEntero("Seleccione el ID del grupo");
        String destination = view.pedir("Destino");
        int duration = view.pedirEntero("Duración (horas)");
        LocalDate date = view.pedirFecha("Fecha de la excursión (YYYY-MM-DD)");
        double cost = view.pedirDouble("Coste");
        
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
        
        ProfessorController professorController = new ProfessorController(view, professorService);
        professorController.showProfessors(allProfessors);
        
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
    
    public void deleteTrip() {
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
    
    public void updateTripDate() {
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
    
    public void listAllTripsWithProfessors() {
        view.info("\n=== LISTADO TOTAL DE EXCURSIONES ===");
        List<Trip> trips = tripService.getAllTripsWithProfessors();
        showTripsWithProfessors(trips);
    }
    
    public void listFinishedTripsBetweenDates() {
        view.info("\n=== EXCURSIONES FINALIZADAS ENTRE FECHAS ===");
        
        LocalDate startDate = view.pedirFecha("Fecha inicial (YYYY-MM-DD)");
        LocalDate endDate = view.pedirFecha("Fecha final (YYYY-MM-DD)");
        
        List<Trip> trips = tripService.getFinishedTripsBetweenDates(startDate, endDate);
        showTripsWithProfessors(trips);
    }
    
    public void showTripsWithProfessors(List<Trip> trips) {
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
            
            view.info(String.format("%-3d | %-15s | %-10s | %-4dh | %-6.2f | %-8s | %-6s | %s",
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
    
    public boolean markTripAsFinished(int tripId) {
        return tripService.markTripAsFinished(tripId);
    }
    
    public List<Trip> getAllTrips() {
        return tripService.getAllTripsWithProfessors();
    }
}