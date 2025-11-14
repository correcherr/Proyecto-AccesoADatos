package com.proyectoada.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import com.proyectoada.dao.TripDAO;
import com.proyectoada.model.mysql.Professor;
import com.proyectoada.model.mysql.Trip;

public class TripService {
    private TripDAO tripDAO;

    public TripService() {
        this.tripDAO = new TripDAO();
    }

    private boolean validateTripFields(String destination, int duration, LocalDate date, double cost,
            List<Professor> professors) {
        if (destination == null || destination.trim().isEmpty()) {
            System.out.println("Error: El destino no puede estar vacío.");
            return false;
        }
        if (duration <= 0) {
            System.out.println("Error: La duración debe ser mayor que 0.");
            return false;
        }
        if (!validateDate(date)) {
            return false;
        }
        if (cost < 0) {
            System.out.println("Error: El coste no puede ser negativo.");
            return false;
        }
        if (professors == null || professors.isEmpty()) {
            System.out.println("Error: Debe asignar al menos un profesor acompañante.");
            return false;
        }
        return true;
    }

    private boolean validateDate(LocalDate date) {
        LocalDate maxDate = LocalDate.of(2026, 6, 18);

        if (date == null) {
            System.out.println("Error: La fecha no puede estar vacía.");
            return false;
        }
        if (!date.isAfter(LocalDate.now())) {
            System.out.println("Error: La fecha debe ser posterior a la fecha actual.");
            return false;
        }
        if (!date.isBefore(maxDate)) {
            System.out.println("Error: La fecha debe ser anterior al 18/06/2026.");
            return false;
        }
        return true;
    }

    private boolean validateTripForModification(int tripId) throws SQLException {
        Trip trip = tripDAO.findById(tripId).orElse(null);
        if (trip == null) {
            System.out.println("Error: No se encontró la excursión.");
            return false;
        }
        if (trip.isFinished()) {
            System.out.println("Error: No se puede modificar una excursión finalizada.");
            return false;
        }
        return true;
    }

    public boolean createTrip(int groupId, String destination, int duration, LocalDate date,
            double cost, List<Professor> accompanyingProfessors) {
        if (!validateTripFields(destination, duration, date, cost, accompanyingProfessors)) {
            return false;
        }

        Trip trip = new Trip(groupId, destination.trim(), duration, date, cost);
        trip.setAccompanyingProfessors(accompanyingProfessors);
        Integer tripId = tripDAO.create(trip);
        if (tripId != null) {
            System.out.println("Excursión creada exitosamente con ID: " + tripId);
            return true;
        }
        return false;
    }

    public boolean updateTripDate(int tripId, LocalDate newDate) {
        try {
            if (!validateTripForModification(tripId)) {
                return false;
            }
            if (!validateDate(newDate)) {
                return false;
            }

            Trip trip = new Trip();
            trip.setTripId(tripId);
            trip.setDate(newDate);
            if (tripDAO.update(trip)) {
                System.out.println("Fecha de excursión actualizada exitosamente.");
                return true;
            }
            return false;

        } catch (SQLException e) {
            System.err.println("Error al actualizar excursión: " + e.getMessage());
            return false;
        }
    }

    public List<Trip> getAllTripsWithProfessors() {
        return tripDAO.findAll();
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
            Trip trip = tripDAO.findById(tripId).orElse(null);
            if (trip == null) {
                System.out.println("Error: No se encontró la excursión.");
                return false;
            }
            if (trip.isFinished()) {
                System.out.println("Error: No se puede eliminar una excursión finalizada.");
                return false;
            }

            if (tripDAO.delete(tripId)) {
                System.out.println("Excursión eliminada exitosamente.");
                return true;
            }
            return false;

        } catch (Exception e) {
            System.err.println("Error al eliminar excursión: " + e.getMessage());
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
        return tripDAO.findById(tripId).orElse(null);
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