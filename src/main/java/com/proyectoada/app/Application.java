package com.proyectoada.app;

import com.proyectoada.controller.MainMenuController;
import com.proyectoada.connection.Conexion;
import com.proyectoada.connection.MongoConnection;

public class Application {
    private MainMenuController menuController;
    
    public Application() {
        this.menuController = new MainMenuController();
    }
    
    public void start() {
        // Inicializar conexiones
        try {
            Conexion.getConnection(); // Test MySQL
            MongoConnection.getDatabase(); // Test MongoDB
            System.out.println("Conexiones establecidas correctamente.");
            
            // Iniciar menú principal
            menuController.showMainMenu();
            
        } catch (Exception e) {
            System.err.println("Error al conectar con las bases de datos: " + e.getMessage());
        } finally {
            // Cerrar conexiones al salir
            Conexion.closeConnection();
            MongoConnection.closeConnection();
        }
    }
}