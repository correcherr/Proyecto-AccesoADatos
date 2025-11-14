package com.proyectoada.model.mysql;

import java.time.LocalDate;
import java.util.List;

public class Trip {
    private int tripId;
    private int groupId;
    private String destination;
    private int duration;
    private LocalDate date;
    private double cost;
    private boolean status;
    private List<Professor> accompanyingProfessors;
    private String groupName;

    public Trip() {
    }

    public Trip(int groupId, String destination, int duration, LocalDate date, double cost) {
        this.groupId = groupId;
        this.destination = destination;
        this.duration = duration;
        this.date = date;
        this.cost = cost;
        this.status = false;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isFinished() {
        return status;
    }

    public List<Professor> getAccompanyingProfessors() {
        return accompanyingProfessors;
    }

    public void setAccompanyingProfessors(List<Professor> accompanyingProfessors) {
        this.accompanyingProfessors = accompanyingProfessors;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public String toString() {
        String statusStr = status ? "Finalizada" : "Pendiente";
        return tripId + " - " + destination + " (" + date + ") - " + statusStr;
    }
}