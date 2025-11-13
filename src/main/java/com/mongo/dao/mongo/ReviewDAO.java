package com.mongo.dao.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongo.connection.MongoConnection;
import com.mongo.dao.Dao;
import com.mongo.model.mongo.Review;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReviewDAO implements Dao<Review, Integer> {
    private MongoCollection<Document> collection;

    public ReviewDAO() {
        MongoDatabase database = MongoConnection.getDatabase();
        this.collection = database.getCollection("reviews");
    }

    @Override
    public Integer create(Review review) {
        Document doc = new Document("tripId", review.getTripId())
                .append("comment", review.getComment())
                .append("rating", review.getRating())
                .append("reviewDate", review.getReviewDate());
        try {
            collection.insertOne(doc);
            return review.getTripId();
        } catch (Exception e) {
            System.err.println("Error al crear reseña: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Optional<Review> findById(Integer id) {
        try {
            Document doc = collection.find(Filters.eq("tripId", id)).first();
            if (doc != null) {
                Review review = new Review(
                        doc.getInteger("tripId"),
                        doc.getString("comment"),
                        doc.getInteger("rating"),
                        doc.getDate("reviewDate"));
                return Optional.of(review);
            }
            return Optional.empty();
        } catch (Exception e) {
            System.err.println("Error al obtener reseña: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<Review> findAll() {
        try {
            List<Review> reviews = new ArrayList<>();
            collection.find().forEach(doc -> {
                Review review = new Review(
                        doc.getInteger("tripId"),
                        doc.getString("comment"),
                        doc.getInteger("rating"),
                        doc.getDate("reviewDate"));
                reviews.add(review);
            });
            return reviews;
        } catch (Exception e) {
            System.err.println("Error al obtener reseñas: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public boolean update(Review t) {
        System.err.println("No se pueden modificar reseñas.");
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        System.err.println("No se pueden eliminar reseñas.");
        return false;
    }

    public List<Review> getReviewsByRatingRange(int minRating, int maxRating) {
        List<Review> reviews = new ArrayList<>();
        try {
            collection.find(Filters.and(
                    Filters.gte("rating", minRating),
                    Filters.lte("rating", maxRating))).forEach(doc -> {
                        Review review = new Review(
                                doc.getInteger("tripId"),
                                doc.getString("comment"),
                                doc.getInteger("rating"),
                                doc.getDate("reviewDate"));
                        reviews.add(review);
                    });
        } catch (Exception e) {
            System.err.println("Error al obtener reseñas por rango: " + e.getMessage());
        }
        return reviews;
    }

    public boolean existsReviewForTrip(int tripId) {
        return collection.find(Filters.eq("tripId", tripId)).first() != null;
    }
}
