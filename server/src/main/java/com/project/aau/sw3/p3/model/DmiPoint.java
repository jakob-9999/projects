package com.project.aau.sw3.p3.model;

import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;

@Entity
public class DmiPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //primary key

    @Column(columnDefinition = "TEXT")
    //saves precipitationValues as json-string
    private String precipitationValues;

    //tells jpa (Java Persistence API) that the column in db should contain large amount of text
    @Column(columnDefinition = "TEXT")
    //saves x-values as json-string
    private String xValues;

    @Column(columnDefinition = "TEXT")
    //saves x-bounds as json-string
    private String xBounds;

    @Column(columnDefinition = "TEXT")
    //saves y-values as json-string
    private String yValues;

    @Column(columnDefinition = "TEXT")
    //saves y-bounds as json-string
    private String yBounds;

    @Column(columnDefinition = "TEXT")
    //saves timeValues as json-string
    private String timeValues;

    public DmiPoint() {
    }

    public DmiPoint(String precipitationValues, String xValues, String xBounds, String yValues, String yBounds, String timeValues) {
        this.precipitationValues = precipitationValues;
        this.xValues = xValues;
        this.xBounds = xBounds;
        this.yValues = yValues;
        this.yBounds = yBounds;
        this.timeValues = timeValues;
    }

    public String getPrecipitationValues() {
        return precipitationValues;
    }

    public void setPrecipitationValues(String precipitationValues) {
        this.precipitationValues = precipitationValues;
    }

    public String getxValues() {
        return xValues;
    }

    public void setxValues(String xValues) {
        this.xValues = xValues;
    }

    public String getxBounds() {
        return xBounds;
    }

    public void setxBounds(String xBounds) {
        this.xBounds = xBounds;
    }

    public String getyValues() {
        return yValues;
    }

    public void setyValues(String yValues) {
        this.yValues = yValues;
    }

    public String getyBounds() {
        return yBounds;
    }

    public void setyBounds(String yBounds) {
        this.yBounds = yBounds;
    }

    public String getTimeValues() {
        return timeValues;
    }

    public void setTimeValues(String timeValues) {
        this.timeValues = timeValues;
    }
}



