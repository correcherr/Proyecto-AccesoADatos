package com.proyectoada.app;

import com.proyectoada.connection.Conexion;
import com.proyectoada.connection.MongoConnection;
import com.proyectoada.controller.MainController;

public class Application {
    private MainController menuController;

    public Application() {
        this.menuController = new MainController();
    }

    public void start() {
        try {
            Conexion.getConnection();
            MongoConnection.getDatabase();
            System.out.println("Conexiones establecidas correctamente.");

            menuController.menu();

        } catch (Exception e) {
            System.err.println("Error al conectar con las bases de datos: " + e.getMessage());
        } finally {
            Conexion.closeConnection();
            MongoConnection.closeConnection();
        }
    }
}