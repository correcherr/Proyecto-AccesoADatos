package com.proyectoada.model.mysql;

import java.time.LocalDate;

public class Professor {
    private int teacherId;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private String phone;
    
    public Professor() {}
    
    public Professor(String name, String surname, LocalDate birthDate, String phone) {
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.phone = phone;
    }
    
    // Getters y Setters
    public int getTeacherId() { return teacherId; }
    public void setTeacherId(int teacherId) { this.teacherId = teacherId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }
    
    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    @Override
    public String toString() {
        return teacherId + " - " + name + " " + surname + " (" + phone + ")";
    }
}