package com.proyectoada.service;

import java.util.Date;
import java.util.List;

import com.proyectoada.dao.mongo.ReviewDAO;
import com.proyectoada.model.mongo.Review;

public class ReviewService {
    private ReviewDAO reviewDAO;
    
    public ReviewService() {
        this.reviewDAO = new ReviewDAO();
    }
    
    private boolean validarCamposReview(String comment, int rating) {
        if (comment == null || comment.trim().isEmpty()) {
            System.out.println("Error: El comentario no puede estar vacío.");
            return false;
        }
        if (rating < 1 || rating > 10) {
            System.out.println("Error: La puntuación debe estar entre 1 y 10.");
            return false;
        }
        return true;
    }
    
    public boolean createReview(int tripId, String comment, int rating) {
        try {
            if (!validarCamposReview(comment, rating)) {
                return false;
            }
            
            Review review = new Review(tripId, comment.trim(), rating, new Date());
            Integer result = reviewDAO.create(review);
            if (result != null) {
                System.out.println("Reseña creada exitosamente.");
                return true;
            }
            return false;
            
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