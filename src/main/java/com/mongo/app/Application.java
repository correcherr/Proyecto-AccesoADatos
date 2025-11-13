package com.mongo.app;

import com.mongo.controller.MainMenuController;
import com.mongo.connection.Conexion;
import com.mongo.connection.MongoConnection;

public class Application {
    private MainMenuController menuController;

    public Application() {
        this.menuController = new MainMenuController();
    }

    public void start() {
        try {
            Conexion.getConnection();
            MongoConnection.getDatabase();
            System.out.println("Conexiones establecidas correctamente.");

            menuController.showMainMenu();

        } catch (Exception e) {
            System.err.println("Error al conectar con las bases de datos: " + e.getMessage());
        } finally {
            Conexion.closeConnection();
            MongoConnection.closeConnection();
        }
    }
}