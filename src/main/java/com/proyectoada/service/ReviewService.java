package com.proyectoada.service;

import com.proyectoada.dao.mongo.ReviewDAO;
import com.proyectoada.model.mongo.Review;

import java.util.Date;
import java.util.List;

public class ReviewService {
    private ReviewDAO reviewDAO;
    
    public ReviewService() {
        this.reviewDAO = new ReviewDAO();
    }
    
    public boolean createReview(int tripId, String comment, int rating) {
        try {
            // Validaciones
            if (comment == null || comment.trim().isEmpty()) {
                System.out.println("Error: El comentario no puede estar vacío.");
                return false;
            }
            if (rating < 1 || rating > 10) {
                System.out.println("Error: La puntuación debe estar entre 1 y 10.");
                return false;
            }
            
            // Verificar si ya existe reseña para esta excursión
            if (reviewDAO.existsReviewForTrip(tripId)) {
                System.out.println("Error: Ya existe una reseña para esta excursión.");
                return false;
            }
            
            Review review = new Review(tripId, comment.trim(), rating, new Date());
            reviewDAO.createReview(review);
            System.out.println("Reseña creada exitosamente.");
            return true;
            
        } catch (Exception e) {
            System.err.println("Error al crear reseña: " + e.getMessage());
            return false;
        }
    }
    
    public List<Review> getReviewsByRatingRange(int minRating, int maxRating) {
        try {
            if (minRating < 1 || maxRating > 10 || minRating > maxRating) {
                System.out.println("Error: El rango de puntuación debe estar entre 1 y 10.");
                return List.of();
            }
            return reviewDAO.getReviewsByRatingRange(minRating, maxRating);
        } catch (Exception e) {
            System.err.println("Error al obtener reseñas: " + e.getMessage());
            return List.of();
        }
    }
    
    public boolean existsReviewForTrip(int tripId) {
        try {
            return reviewDAO.existsReviewForTrip(tripId);
        } catch (Exception e) {
            System.err.println("Error al verificar reseña: " + e.getMessage());
            return false;
        }
    }
}