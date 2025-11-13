package com.mongo.model.mysql;

import java.time.LocalDate;

public class Professor {
    private int professorId;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private String phone;

    public Professor() {
    }

    public Professor(String name, String surname, LocalDate birthDate, String phone) {
        this.name = name;
        this.surname = surname;
        this.birthDate = birthDate;
        this.phone = phone;
    }

    // Getters y Setters
    public int getProfessorId() {
        return professorId;
    }

    public void setProfessorId(int teacherId) {
        this.professorId = teacherId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return professorId + " - " + name + " " + surname + " (" + phone + ")";
    }
}