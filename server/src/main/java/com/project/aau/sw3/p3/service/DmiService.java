package com.project.aau.sw3.p3.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.aau.sw3.p3.model.TotalPrecipitation;
import com.project.aau.sw3.p3.model.DmiPoint;
import com.project.aau.sw3.p3.model.BBoxPoint;
import com.project.aau.sw3.p3.repository.BBoxRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.core.type.TypeReference;
import com.project.aau.sw3.p3.repository.DmiPointRepo;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;


@Service
public class DmiService {

    private final DmiPointRepo dmiPointRepo;
    private final BBoxRepo bBoxRepo;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    //constructor
    public DmiService(DmiPointRepo dmiPointRepo, BBoxRepo bBoxRepo, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.dmiPointRepo = dmiPointRepo;
        this.bBoxRepo = bBoxRepo;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    // API Url der henter data ( 4 parametre)
    private static final String DMI_URL =
            "https://dmigw.govcloud.dk/v1/forecastedr/collections/harmonie_dini_sf/position"
                    + "?coords=POINT(10.2039 56.1629)"
                    + "&parameter-name=total-precipitation"
                    + "&api-key=39d54b14-ff57-4612-85de-f66333bd4b03";

    private static final String DMI_URL2 =
            "https://dmigw.govcloud.dk/v1/forecastedr/collections/harmonie_dini_sf" +
                    "/bbox?bbox=10.154828,56.123953,10.261859,56.179219&" +
                    "parameter-name=total-precipitation,latitude,longitude&" +
                    "crs=crs84&f=GeoJSON&datetime=2025-11-07T22:00:00.000Z&" +
                    "api-key=39d54b14-ff57-4612-85de-f66333bd4b03";
    //datetime=2025-11-07T22:00:00.000Z skal slettes/ændres

    public Map <String, Object> fetchDmiData() {
        try {
            // For HTTP requests
            // GET-request to DMI’s API, get answer as String)
            ResponseEntity<String> response = restTemplate.getForEntity(DMI_URL, String.class);

            // Save as JSON String
            String json = response.getBody();

            //converts JSON to a Map
            Map<String, Object> data = objectMapper.readValue(json, Map.class);

            System.out.println("DMI API Response:");
            //System.out.println(json);

            //just an example: will look at the type
            System.out.println("JSON type: " + data.get("type"));

            // return the map in browser
            return data;

        } catch (Exception e) {
            //returns error as JSON
            return Map.of("error", "Fejl ved hentning af DMI data: " + e.getMessage());
        }
    }

    public TotalPrecipitation fetchTotalPrecipitation() {
        try {
            // For HTTP requests
            // GET-request to DMI’s API, get answer as String)
            ResponseEntity<String> response = restTemplate.getForEntity(DMI_URL, String.class);

            // Save as JSON String
            String json = response.getBody();

            System.out.println("DMI API Response:");
            //System.out.println(json);


            //converts the whole JSON to a Map
            Map<String, Object> root = objectMapper.readValue(json, Map.class);

            //get the "ranges" part from the json
            Map<String, Object> ranges = (Map<String, Object>) root.get("ranges");

            //get the "total-precipitation" part, from the ranges part
            Object totalPrecipObj = ranges.get("total-precipitation");

            //convert to the model class "TotalPrecipitation"
            TotalPrecipitation tp = objectMapper.convertValue(totalPrecipObj, TotalPrecipitation.class);

            System.out.println("Number of values: " + tp.getValues().size());
            System.out.println("First value: " + tp.getValues().get(0));


            //just an example: will look at the type
            System.out.println("JSON type: " + root.get("type"));

            // return the map in browser
            return tp;

        } catch (Exception e) {
            //error message in console. tells where in the code it went wrong
            e.printStackTrace();
            return null;
        }
    }

    public void saveDmiDiniPoint() {
        try {
            // For HTTP requests
            // GET-request to DMI’s API, get answer as String)
            ResponseEntity<String> response = restTemplate.getForEntity(DMI_URL, String.class);

            // Save as JSON String
            String json = response.getBody();

            //Create ObjectMapper to convert JSON strings into Java objects
            //converts the whole JSON to a Map
            Map<String, Object> root = objectMapper.readValue(json, Map.class);

            //get the "ranges" part from the json
            Map<String, Object> ranges = (Map<String, Object>) root.get("ranges");

            //get the "total-precipitation" part from the "ranges"-part
            Map<String, Object> totalPrecipitation = (Map<String, Object>) ranges.get("total-precipitation");

            //get the "values" from "total-precipitation" part, and save it as a list
            //totalPrecipitation.get("values") returns List<Object>, not List<Double>. Must be converted
            //mapper.convertValue takes two arguments: 1) what to convert, 2) what I want it converted to
            //TypeReference is an anonymous type that tells Jackson what type i want
            List<Double> precipitationValues = objectMapper.convertValue(totalPrecipitation.get("values"),
                    new TypeReference<List<Double>>() {}
            );

            //get the "domain" part from the json
            Map<String, Object> domain = (Map<String, Object>) root.get("domain");

            //get the "axes" part from the "domain"-part
            Map<String, Object> axes = (Map<String, Object>) domain.get("axes");

            //get the "x" part from the "axes"-part
            Map<String, Object> x = (Map<String, Object>) axes.get("x");

            //get the "values" from "x" part, convert to List<Double> and save it
            List<Double> xValues = objectMapper.convertValue(x.get("values"),
                    new TypeReference<List<Double>>() {}
            );

            //get the "bounds" from "x" part, convert to List<Double> and save it
            List<Double> xBounds = objectMapper.convertValue(x.get("bounds"),
                    new TypeReference<List<Double>>() {}
            );

            //get the "y" part from the "axes"-part
            Map<String, Object> y = (Map<String, Object>) axes.get("y");

            //get the "values" from "y" part, convert to List<Double> and save it
            List<Double> yValues = objectMapper.convertValue(y.get("values"),
                    new TypeReference<List<Double>>() {}
            );

            //get the "bounds" from "x" part, convert to List<Double> and save it
            List<Double> yBounds = objectMapper.convertValue(y.get("bounds"),
                    new TypeReference<List<Double>>() {}
            );

            //get the "t" part from the "axes"-part
            Map<String, Object> t = (Map<String, Object>) axes.get("t");

            //get the "valurs" from "t" part, convert to List<String> and save it. List<String> because time values are strings in json
            List<String> tValues = objectMapper.convertValue(t.get("values"),
                    new TypeReference<List<String>>() {}
            );

            //create DmiPoint object
            DmiPoint dmiPoint = new DmiPoint(precipitationValues, xValues, xBounds, yValues, yBounds, tValues);

            //save in db
            dmiPointRepo.save(dmiPoint);

        } catch (Exception e) {
            //error message in console. tells where in the code it went wrong
            e.printStackTrace();
        }
    }

    public void saveBBox() {
        try {
            // GET-request bbox api, get answer as String
            ResponseEntity<String> response = restTemplate.getForEntity(DMI_URL2, String.class);

            // Save as JSON String
            String json = response.getBody();

            //Create ObjectMapper to convert JSON strings into Java objects
            //converts the whole JSON to a Map
            Map<String, Object> root = objectMapper.readValue(json, Map.class);

            //"features" is a list of feature-objects
            List<Map<String, Object>> features = (List<Map<String, Object>>) root.get("features");

            //get the first feature object
            Map<String, Object> firstFeatureObject = features.get(0);

            //get "geometry" part from the "features"-object
            Map<String, Object> geometry = (Map<String, Object>) firstFeatureObject.get("geometry");

            //get "coordinates" from the "geometry"-object
            List<Object> coordinates = (List<Object>) geometry.get("coordinates");

            //convert coordinate-objects to doubles
            double xCoordinate = ((Number) coordinates.get(0)).doubleValue();
            double yCoordinate = ((Number) coordinates.get(1)).doubleValue();

            //get "properties" from "features" part
            Map<String, Object> properties = (Map<String, Object>) firstFeatureObject.get("properties");

            //get "total-precipitation" from the "properties"-object
            Object totalPrecipitation = properties.get("total-precipitation");

            //convert totalPrecipitation-object to double
            double precipitation = ((Number) totalPrecipitation).doubleValue();


            //get "step" from "properties"
            Object step = properties.get("step");

            //convert step-object to a String
            String stepString = step.toString();

            //convert step-string to date and time
            LocalDateTime timeStep = ZonedDateTime.parse(stepString).toLocalDateTime();

            //create BBox object
            BBoxPoint bBoxPoint = new BBoxPoint(xCoordinate, yCoordinate, precipitation, timeStep);

            //save in db
            bBoxRepo.save(bBoxPoint);

        } catch (Exception e) {
            //error message in console. tells where in the code it went wrong
            e.printStackTrace();
        }
    }
}
