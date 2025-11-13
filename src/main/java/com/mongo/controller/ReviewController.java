package com.mongo.controller;

import com.mongo.view.ConsolaView;
import com.mongo.model.mongo.Review;
import com.mongo.model.mysql.Trip;
import com.mongo.service.ReviewService;
import com.mongo.service.TripService;

import java.util.List;

public class ReviewController {
    private ConsolaView view;
    private ReviewService reviewService;
    private TripService tripService;
    
    public ReviewController(ConsolaView view, ReviewService reviewService, TripService tripService) {
        this.view = view;
        this.reviewService = reviewService;
        this.tripService = tripService;
    }
    
    public void createReview() {
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
        
        TripController tripController = new TripController(view, tripService, null, null);
        tripController.showTripsWithProfessors(unfinishedTrips);
        
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
    
    public void listReviewsByRatingRange() {
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
    
    public void showReviews(List<Review> reviews) {
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
}