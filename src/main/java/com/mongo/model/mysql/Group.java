package com.mongo.model.mysql;

public class Group {
    private int groupId;
    private String groupName;
    private String educationalStage;
    private int numberOfStudents;

    public Group() {
    }

    public Group(String groupName, String educationalStage, int numberOfStudents) {
        this.groupName = groupName;
        this.educationalStage = educationalStage;
        this.numberOfStudents = numberOfStudents;
    }

    // Getters y Setters
    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getEducationalStage() {
        return educationalStage;
    }

    public void setEducationalStage(String educationalStage) {
        this.educationalStage = educationalStage;
    }

    public int getNumberOfStudents() {
        return numberOfStudents;
    }

    public void setNumberOfStudents(int numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
    }

    @Override
    public String toString() {
        return groupId + " - " + groupName + " (" + educationalStage + ") - " + numberOfStudents + " alumnos";
    }
}