package com.mongo.controller;

import java.time.LocalDate;

import com.mongo.service.GroupService;
import com.mongo.service.ProfessorService;
import com.mongo.service.ReviewService;
import com.mongo.service.TripService;
import com.mongo.view.ConsolaView;

public class MainController {
    private GroupService groupService;
    private ProfessorService professorService;
    private TripService tripService;
    private ReviewService reviewService;
    private ConsolaView view;

    public MainController(GroupService groupService, ProfessorService professorService, TripService tripService,
            ReviewService reviewService, ConsolaView view) {
        this.groupService = groupService;
        this.professorService = professorService;
        this.tripService = tripService;
        this.reviewService = reviewService;
        this.view = view;
    }

    public void run() {
        int op;
        do {
            op = view.menu();
            try {
                switch (op) {
                    case 1 -> newProfessor();
                    case 2 -> newGroup();
                    case 3 -> deleteGroup();
                    case 4 -> newTrip();
                    case 5 -> deleteTrip();
                    case 6 -> editTrip();
                    case 7 -> listTripsnProfessors();
                    case 8 -> listDoneTrips();
                    case 9 -> doReview();
                    case 10 -> listReviewsFilter();
                    case 0 -> view.info("¡Hasta luego!");
                    default -> view.error("Opción inválida, inténtalo de nuevo.");
                }

            } catch (Exception e) {
                view.error("Atención!" + e.getMessage());
            }
        } while (op != 0);
    }

    public void newProfessor() {
        String name = view.pedir("Nom del profesor");
        String surname = view.pedir("Cognom del profesor");
        ;
        LocalDate date = view.pedirFecha("Data de naixement");
        String phone = view.pedir("Nombre de teléfon");

    }

    public void newGroup() {

    }

    public void deleteGroup() {

    }

    public void newTrip() {

    }

    public void deleteTrip() {

    }

    public void editTrip() {

    }

    public void listTripsnProfessors() {

    }

    public void listDoneTrips() {

    }

    public void doReview() {

    }

    public void listReviewsFilter() {

    }
}
