package com.proyectoada.model.mongo;

import java.util.Date;

public class Review {
    private int tripId;
    private String comment;
    private int rating;
    private Date reviewDate;

    public Review() {
    }

    public Review(int tripId, String comment, int rating, Date reviewDate) {
        this.tripId = tripId;
        this.comment = comment;
        this.rating = rating;
        this.reviewDate = reviewDate;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    @Override
    public String toString() {
        return "Excursión ID: " + tripId +
                " | Puntuación: " + rating + "/10" +
                " | Fecha reseña: " + reviewDate +
                "\nComentario: " + comment;
    }
}
