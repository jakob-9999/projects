package com.project.aau.sw3.p3.model;

import java.util.List;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "bBoxPoint")
public class BBoxPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //primary key

    private Double xCoordinate;
    private Double yCoordinate;
    private Double precipitation;
    private LocalDateTime timeStep;

    public BBoxPoint() {
    }

    public BBoxPoint(Double xCoordinate, Double yCoordinate, Double precipitation, LocalDateTime timeStep) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.precipitation = precipitation;
        this.timeStep = timeStep;
    }

    public Double getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(Double xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public Double getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(Double yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public Double getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(Double precipitation) {
        this.precipitation = precipitation;
    }

    public LocalDateTime getTimeStep() {
        return timeStep;
    }

    public void setTimeStep(LocalDateTime timeStep) {
        this.timeStep = timeStep;
    }
}