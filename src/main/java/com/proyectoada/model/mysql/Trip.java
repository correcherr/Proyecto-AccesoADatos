package com.proyectoada.model.mysql;

import java.time.LocalDate;
import java.util.List;

public class Trip {
    private int tripId;
    private int classId;
    private String destination;
    private int duration;
    private LocalDate date;
    private double cost;
    private boolean isFinished;
    private List<Professor> accompanyingProfessors;
    private String className; // Para mostrar en listados
    
    public Trip() {}
    
    public Trip(int classId, String destination, int duration, LocalDate date, double cost) {
        this.classId = classId;
        this.destination = destination;
        this.duration = duration;
        this.date = date;
        this.cost = cost;
        this.isFinished = false;
    }
    
    // Getters y Setters
    public int getTripId() { return tripId; }
    public void setTripId(int tripId) { this.tripId = tripId; }
    
    public int getClassId() { return classId; }
    public void setClassId(int classId) { this.classId = classId; }
    
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
    
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    
    public double getCost() { return cost; }
    public void setCost(double cost) { this.cost = cost; }
    
    public boolean isFinished() { return isFinished; }
    public void setFinished(boolean finished) { isFinished = finished; }
    
    public List<Professor> getAccompanyingProfessors() { return accompanyingProfessors; }
    public void setAccompanyingProfessors(List<Professor> accompanyingProfessors) { 
        this.accompanyingProfessors = accompanyingProfessors; 
    }
    
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    
    @Override
    public String toString() {
        return tripId + " - " + destination + " (" + date + ") - " + 
               (isFinished ? "FINALIZADA" : "PENDIENTE");
    }
}