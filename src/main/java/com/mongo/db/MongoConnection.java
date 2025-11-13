package com.mongo.db;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoConnection {

    private static final String URI = "mongodb://localhost:27017";
    private static final String DB_NAME = "testdb";

    private static MongoClient client;

    public static MongoDatabase getDatabase() {
        if (client == null) {
            client = MongoClients.create(URI);
        }
        MongoDatabase db = client.getDatabase(DB_NAME);
        ensureBooksCollection(db);
        return db;
    }

    private static void ensureBooksCollection(MongoDatabase db) {
        boolean exists = false;

        for (String name : db.listCollectionNames()) {
            if (name.equals("books")) {
                exists = true;
                break;
            }
        }

        if (!exists) {
            System.out.println("La colection books no existeix, es crearà...");
            db.createCollection("books");
            System.out.println("Col.leció books creada correctamente.");
        }
    }

    public static void close() {
        if (client != null)
            client.close();
    }
}