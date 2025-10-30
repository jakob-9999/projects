package com.project.aau.sw3.p3.model;

import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Table;

@Entity
@Table(name = "dmiPoint")
public class DmiPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //primary key

    //@ElementCollection makes it possible to store a list in a separate table linked to this entity
    @ElementCollection
    //saves precipitationValues as List<Double>
    private List<Double> precipitationValues;


    @ElementCollection
    //saves x-values as List<Double>
    private List<Double> xValues;

    @ElementCollection
    //saves x-bounds as List<Double>
    private List<Double> xBounds;

    @ElementCollection
    //saves y-values as List<Double>
    private List<Double> yValues;

    @ElementCollection
    //saves y-bounds as List<Double>
    private List<Double> yBounds;

    @ElementCollection
    //saves timeValues as List<Double>
    private List<String> timeValues;

    public DmiPoint() {
    }

    public DmiPoint(List<Double> precipitationValues, List<Double> xValues, List<Double> xBounds, List<Double> yValues, List<Double> yBounds, List<String> timeValues) {
        this.precipitationValues = precipitationValues;
        this.xValues = xValues;
        this.xBounds = xBounds;
        this.yValues = yValues;
        this.yBounds = yBounds;
        this.timeValues = timeValues;
    }

    public List<Double> getPrecipitationValues() {
        return precipitationValues;
    }

    public void setPrecipitationValues(List<Double> precipitationValues) {
        this.precipitationValues = precipitationValues;
    }

    public List<Double> getxValues() {
        return xValues;
    }

    public void setxValues(List<Double> xValues) {
        this.xValues = xValues;
    }

    public List<Double> getxBounds() {
        return xBounds;
    }

    public void setxBounds(List<Double> xBounds) {
        this.xBounds = xBounds;
    }

    public List<Double> getyValues() {
        return yValues;
    }

    public void setyValues(List<Double> yValues) {
        this.yValues = yValues;
    }

    public List<Double> getyBounds() {
        return yBounds;
    }

    public void setyBounds(List<Double> yBounds) {
        this.yBounds = yBounds;
    }

    public List<String> getTimeValues() {
        return timeValues;
    }

    public void setTimeValues(List<String> timeValues) {
        this.timeValues = timeValues;
    }
}