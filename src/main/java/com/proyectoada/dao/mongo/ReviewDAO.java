package com.proyectoada.dao.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.proyectoada.connection.MongoConnection;
import com.proyectoada.model.mongo.Review;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {
    private MongoCollection<Document> collection;
    
    public ReviewDAO() {
        MongoDatabase database = MongoConnection.getDatabase();
        this.collection = database.getCollection("reviews");
    }
    
    public void createReview(Review review) {
        Document doc = new Document("tripId", review.getTripId())
                .append("comment", review.getComment())
                .append("rating", review.getRating())
                .append("reviewDate", review.getReviewDate());
        collection.insertOne(doc);
    }
    
    public List<Review> getReviewsByRatingRange(int minRating, int maxRating) {
        List<Review> reviews = new ArrayList<>();
        collection.find(Filters.and(
            Filters.gte("rating", minRating),
            Filters.lte("rating", maxRating)
        )).forEach(doc -> {
            Review review = new Review(
                doc.getInteger("tripId"),
                doc.getString("comment"),
                doc.getInteger("rating"),
                doc.getDate("reviewDate")
            );
            reviews.add(review);
        });
        return reviews;
    }
    
    public boolean existsReviewForTrip(int tripId) {
        return collection.find(Filters.eq("tripId", tripId)).first() != null;
    }
}