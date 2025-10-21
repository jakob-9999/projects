package com.project.aau.sw3.p3.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

//this entity represents the sewerData tabel in the db
@Entity
public class SewerData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //primary key

    @Column(columnDefinition = "TEXT") //tells jpa (Java Persistence API) that the column in db should contain large amount of text
    private String coordinates;        //saves coordinates as json-string
    private String sewerType;          //vaerd1201a

    //no argument contructor is required for JPA to work
    public SewerData() {
    }

    //constructor
    public SewerData(String coordinates, String sewerType) {
        this.coordinates = coordinates;
        this.sewerType = sewerType;
    }

    //getters and setters
    public String getCoordinates() {
        return coordinates;
    }

    public String getSewerType() {
        return sewerType;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public void setSewerType(String sewerType) {
        this.sewerType = sewerType;
    }
}