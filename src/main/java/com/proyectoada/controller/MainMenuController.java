package com.proyectoada.controller;

import com.proyectoada.view.ConsoleView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class MainMenuController {
    private Scanner scanner;
    private ConsoleView view;
    private ProfessorController professorController;
    private ClassController classController;
    private TripController tripController;
    
    public MainMenuController() {
        this.scanner = new Scanner(System.in);
        this.view = new ConsoleView();
        this.professorController = new ProfessorController();
        this.classController = new ClassController();
        this.tripController = new TripController();
    }
    
    public void showMainMenu() {
        int option;
        do {
            view.showMainMenu();
            option = getIntInput("Seleccione una opción: ");
            
            switch (option) {
                case 1:
                    professorController.createProfessor();
                    break;
                case 2:
                    classController.createClass();
                    break;
                case 3:
                    classController.deleteClass();
                    break;
                case 4:
                    tripController.createTrip();
                    break;
                case 5:
                    tripController.deleteTrip();
                    break;
                case 6:
                    tripController.updateTripDate();
                    break;
                case 7:
                    tripController.listAllTripsWithProfessors();
                    break;
                case 8:
                    tripController.listFinishedTripsBetweenDates();
                    break;
                case 9:
                    tripController.createReview();
                    break;
                case 10:
                    tripController.listReviewsByRatingRange();
                    break;
                case 11:
                    showExitMessage();
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
            
            if (option != 11) {
                System.out.println("\nPresione Enter para continuar...");
                scanner.nextLine();
            }
            
        } while (option != 11);
    }
    
    private int getIntInput(String message) {
        while (true) {
            try {
                System.out.print(message);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar un número válido.");
            }
        }
    }
    
    private void showExitMessage() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm 'del dia' dd/MM/yyyy");
        String formattedDate = now.format(formatter);
        System.out.println("\nAdeu. Sessió finalitzada a les " + formattedDate);
    }
}