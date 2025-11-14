package com.proyectoada.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.proyectoada.service.*;
import com.proyectoada.view.ConsolaView;

public class MainController {
    private ConsolaView view;
    private ProfessorController professorController;
    private GroupController groupController;
    private TripController tripController;
    private ReviewController reviewController;

    public MainController() {
        this.view = new ConsolaView();

        // Inicializar servicios
        ProfessorService professorService = new ProfessorService();
        GroupService groupService = new GroupService();
        TripService tripService = new TripService();
        ReviewService reviewService = new ReviewService();

        // Inicializar controllers
        this.professorController = new ProfessorController(view, professorService);
        this.groupController = new GroupController(view, groupService);
        this.tripController = new TripController(view, tripService, groupService, professorService);
        this.reviewController = new ReviewController(view, reviewService, tripService);
    }

    public void menu() {
        int option;
        do {
            option = view.menu();

            switch (option) {
                case 1 -> professorController.createProfessor();
                case 2 -> groupController.createGroup();
                case 3 -> groupController.deleteGroup();
                case 4 -> tripController.createTrip();
                case 5 -> tripController.deleteTrip();
                case 6 -> tripController.updateTripDate();
                case 7 -> tripController.listAllTripsWithProfessors();
                case 8 -> tripController.listFinishedTripsBetweenDates();
                case 9 -> reviewController.createReview();
                case 10 -> reviewController.listReviewsByRatingRange();
                case 11 -> exitMessage();
                default -> view.error("Opción no válida. Intente nuevamente.");
            }

        } while (option != 11);
    }

    private void exitMessage() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm 'del dia' dd/MM/yyyy");
        String formattedDate = now.format(formatter);
        view.info("\nAdeu. Sessió finalitzada a les " + formattedDate);
    }

}