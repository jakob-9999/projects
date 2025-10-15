package com.project.aau.sw3.p3.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
// import java.time.LocalDateTime;


 //Entity class representing precipitation data fetched from DMI.
 //Spring Boot automatically creates a corresponding table in PostgreSQL.

@Entity
public class PrecipitationData {

    // Unique ID for each row in the database
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Type of precipitation (e.g. rain, snow)
    private String precipitationType;

    // Amount of precipitation in mm
    //private double precipitation;

    // Timestamp for the forecast or observation
    //private LocalDateTime timeStamp;

    // Empty constructor required by JPA
    public PrecipitationData() {}


    public PrecipitationData(String precipitationType) {
        this.precipitationType = precipitationType;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    //public double getPrecipitation() { return precipitation; }
    //public void setPrecipitation(double precipitation) { this.precipitation = precipitation; }

    public String getPrecipitationType() { return precipitationType; }
    public void setPrecipitationType(String precipitationType) { this.precipitationType = precipitationType; }

    //public LocalDateTime getTimeStamp() { return timeStamp; }
    //public void setTimeStamp(LocalDateTime timeStamp) { this.timeStamp = timeStamp; }
}
