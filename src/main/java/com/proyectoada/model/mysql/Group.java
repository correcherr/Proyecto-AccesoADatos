package com.proyectoada.model.mysql;

public class Group {
    private int classId;
    private String className;
    private String educationalStage;
    private int numberOfStudents;
    
    public Group() {}
    
    public Group(String className, String educationalStage, int numberOfStudents) {
        this.className = className;
        this.educationalStage = educationalStage;
        this.numberOfStudents = numberOfStudents;
    }
    
    // Getters y Setters
    public int getClassId() { return classId; }
    public void setClassId(int classId) { this.classId = classId; }
    
    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
    
    public String getEducationalStage() { return educationalStage; }
    public void setEducationalStage(String educationalStage) { this.educationalStage = educationalStage; }
    
    public int getNumberOfStudents() { return numberOfStudents; }
    public void setNumberOfStudents(int numberOfStudents) { this.numberOfStudents = numberOfStudents; }
    
    @Override
    public String toString() {
        return classId + " - " + className + " (" + educationalStage + ") - " + numberOfStudents + " alumnos";
    }
}