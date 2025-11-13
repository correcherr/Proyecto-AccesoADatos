package com.mongo.service;

import com.mongo.dao.TripDAO;
import com.mongo.model.mysql.Professor;
import com.mongo.model.mysql.Trip;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class TripService {
    private TripDAO tripDAO;

    public TripService() {
        this.tripDAO = new TripDAO();
    }

    public boolean createTrip(int groupId, String destination, int duration, LocalDate date,
            double cost, List<Professor> accompanyingProfessors) {
        try {
            // Validaciones
            if (destination == null || destination.trim().isEmpty()) {
                System.out.println("Error: El destino no puede estar vacío.");
                return false;
            }
            if (duration <= 0) {
                System.out.println("Error: La duración debe ser mayor que 0.");
                return false;
            }
            if (date == null || date.isBefore(LocalDate.now())) {
                System.out.println("Error: La fecha debe ser posterior a la fecha actual.");
                return false;
            }
            if (date.isAfter(LocalDate.of(2026, 6, 18))) {
                System.out.println("Error: La fecha debe ser anterior al 18/06/2026.");
                return false;
            }
            if (cost < 0) {
                System.out.println("Error: El coste no puede ser negativo.");
                return false;
            }
            if (accompanyingProfessors == null || accompanyingProfessors.isEmpty()) {
                System.out.println("Error: Debe asignar al menos un profesor acompañante.");
                return false;
            }

            Trip trip = new Trip(groupId, destination.trim(), duration, date, cost);
            trip.setAccompanyingProfessors(accompanyingProfessors);
            tripDAO.createTrip(trip);
            System.out.println("Excursión creada exitosamente con ID: " + trip.getTripId());
            return true;

        } catch (SQLException e) {
            System.err.println("Error al crear excursión: " + e.getMessage());
            return false;
        }
    }

    public List<Trip> getAllTripsWithProfessors() {
        try {
            return tripDAO.getAllTripsWithProfessors();
        } catch (SQLException e) {
            System.err.println("Error al obtener excursiones: " + e.getMessage());
            return List.of();
        }
    }

    public List<Trip> getFinishedTripsBetweenDates(LocalDate startDate, LocalDate endDate) {
        try {
            if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
                System.out.println("Error: Las fechas no son válidas.");
                return List.of();
            }
            return tripDAO.getFinishedTripsBetweenDates(startDate, endDate);
        } catch (SQLException e) {
            System.err.println("Error al obtener excursiones: " + e.getMessage());
            return List.of();
        }
    }

    public boolean deleteTrip(int tripId) {
        try {
            Trip trip = tripDAO.getTripById(tripId);
            if (trip == null) {
                System.out.println("Error: No se encontró la excursión.");
                return false;
            }
            if (trip.isFinished()) {
                System.out.println("Error: No se puede eliminar una excursión finalizada.");
                return false;
            }

            tripDAO.deleteTrip(tripId);
            System.out.println("Excursión eliminada exitosamente.");
            return true;

        } catch (SQLException e) {
            System.err.println("Error al eliminar excursión: " + e.getMessage());
            return false;
        }
    }

    public boolean updateTripDate(int tripId, LocalDate newDate) {
        try {
            Trip trip = tripDAO.getTripById(tripId);
            if (trip == null) {
                System.out.println("Error: No se encontró la excursión.");
                return false;
            }
            if (trip.isFinished()) {
                System.out.println("Error: No se puede modificar una excursión finalizada.");
                return false;
            }
            if (newDate == null || newDate.isBefore(LocalDate.now())) {
                System.out.println("Error: La nueva fecha debe ser posterior a la fecha actual.");
                return false;
            }
            if (newDate.isAfter(LocalDate.of(2026, 6, 18))) {
                System.out.println("Error: La fecha debe ser anterior al 18/06/2026.");
                return false;
            }

            tripDAO.updateTripDate(tripId, newDate);
            System.out.println("Fecha de excursión actualizada exitosamente.");
            return true;

        } catch (SQLException e) {
            System.err.println("Error al actualizar excursión: " + e.getMessage());
            return false;
        }
    }

    public boolean markTripAsFinished(int tripId) {
        try {
            tripDAO.markTripAsFinished(tripId);
            System.out.println("Excursión marcada como finalizada.");
            return true;
        } catch (SQLException e) {
            System.err.println("Error al marcar excursión como finalizada: " + e.getMessage());
            return false;
        }
    }

    public Trip getTripById(int tripId) {
        try {
            return tripDAO.getTripById(tripId);
        } catch (SQLException e) {
            System.err.println("Error al obtener excursión: " + e.getMessage());
            return null;
        }
    }

    public List<Professor> getProfessorsForTrip(int tripId) {
        try {
            return tripDAO.getProfessorsForTrip(tripId);
        } catch (SQLException e) {
            System.err.println("Error al obtener profesores de la excursión: " + e.getMessage());
            return List.of();
        }
    }
}