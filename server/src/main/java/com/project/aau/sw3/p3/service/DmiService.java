package com.project.aau.sw3.p3.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.aau.sw3.p3.model.TotalPrecipitation;
import com.project.aau.sw3.p3.model.DmiPoint;
import com.project.aau.sw3.p3.model.GridCell;
import com.project.aau.sw3.p3.repository.GridRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.core.type.TypeReference;
import com.project.aau.sw3.p3.repository.DmiPointRepo;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class DmiService {

    private final DmiPointRepo dmiPointRepo;
    private final GridRepo gridRepo;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    //constructor
    public DmiService(DmiPointRepo dmiPointRepo, GridRepo gridRepo, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.dmiPointRepo = dmiPointRepo;
        this.gridRepo = gridRepo;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    // API Url der henter data ( 4 parametre)
    private static final String DMI_URL =
            "https://dmigw.govcloud.dk/v1/forecastedr/collections/harmonie_dini_sf/position"
                    + "?coords=POINT(10.2039 56.1629)"
                    + "&parameter-name=total-precipitation"
                    + "&api-key=39d54b14-ff57-4612-85de-f66333bd4b03";

    // endpoint for the bbox
    private static final String DMI_URL2 =
            "https://dmigw.govcloud.dk/v1/forecastedr/collections/harmonie_dini_sf"
                    + "/bbox?bbox=10.0689697,56.1045981,10.2639771,56.197728"
                    + "&parameter-name=total-precipitation,latitude,longitude"
                    + "&crs=crs84&f=GeoJSON&api-key=39d54b14-ff57-4612-85de-f66333bd4b03";

    //getting the gdal path from the application-local.yaml file
    @Value("${gdal.path}")
    private String gdalPath;

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
            //System.out.println("JSON type: " + data.get("type"));

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

            //System.out.println("Number of values: " + tp.getValues().size());
            //System.out.println("First value: " + tp.getValues().get(0));


            //just an example: will look at the type
            //System.out.println("JSON type: " + root.get("type"));

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

            //loop through features and save each grid cell
            for (int i = 0; i < features.size(); i++) {
                Map<String, Object> featureObject = features.get(i);

                //get "geometry" part from the "features"-object
                Map<String, Object> geometry = (Map<String, Object>) featureObject.get("geometry");

                //get "coordinates" from the "geometry"-object
                List<Object> coordinates = (List<Object>) geometry.get("coordinates");

                //convert coordinate-objects to doubles
                double xCoordinate = ((Number) coordinates.get(0)).doubleValue();
                double yCoordinate = ((Number) coordinates.get(1)).doubleValue();

                //get "properties" from "features" part
                Map<String, Object> properties = (Map<String, Object>) featureObject.get("properties");

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

                //create GridCell object
                GridCell gridCell = new GridCell(xCoordinate, yCoordinate, precipitation, timeStep);

                //save in db
                gridRepo.save(gridCell);
            }
        } catch (Exception e) {
            //error message in console. tells where in the code it went wrong
            e.printStackTrace();
        }
    }

    public ObjectNode buildDmiGrid() {
        ObjectNode featureCollection = objectMapper.createObjectNode();

        //create empty featureCollection to adhere to GeoJSON format (needed in frontend)
        featureCollection.put("type", "FeatureCollection");

        //create JSON-array to adhere to GeoJSON format
        ArrayNode features = objectMapper.createArrayNode();

        //add "type" and "geometry" to adhere to GeoJSON format
        gridRepo.findAll().forEach(gridCell -> {

            //"type"
            ObjectNode feature = objectMapper.createObjectNode();
            feature.put("type", "Feature");

            //"geometry" includes a new "type"-element and a "coordinates"-element
            ObjectNode geometry = objectMapper.createObjectNode();
            geometry.put("type", "Point");
            ArrayNode coordinates = objectMapper.createArrayNode();
            coordinates.add(gridCell.getxCoordinate());
            coordinates.add(gridCell.getyCoordinate());
            geometry.set("coordinates", coordinates);

            ObjectNode properties = objectMapper.createObjectNode();
            Double precipitation = gridCell.getPrecipitation();
            properties.put("total-precipitation", precipitation);
            LocalDateTime timeStep = gridCell.getTimeStep();
            properties.put("step", timeStep.toString());

            feature.set("geometry", geometry);
            feature.set("properties", properties);

            features.add(feature);

            /*try {
                JsonNode featureNode = objectMapper.readTree(feature);
                features.add(featureNode);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }*/
        });

        featureCollection.set("features", features);
        return featureCollection;
    }

    public void projectGrids(){
        ObjectNode dmiGrid = buildDmiGrid();
        ObjectMapper mapper = new ObjectMapper();

        try {
            //create a file object
            File file = new File("jsonValues.json");

            //write json to a file, "file" is the file to save it in, "dmiGrid" is the json to save
            mapper.writeValue(file, dmiGrid);

            //.out.println("File saved to: " + file.getAbsolutePath());
        } catch (IOException e) {
            //when using writeValue, we have to catch for IOException
            throw new RuntimeException(e);
        }

        //finding all timesteps in DB
        List<LocalDateTime> timeSteps = gridRepo.findAllTimeSteps();

        for (int i = 0; i < timeSteps.size(); i++) {
            String fileName = "grid" + i + ".tif";
            File outputFile = new File("client/public/grids/" + fileName);
            File parent = outputFile.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();  // opret mapper, hvis de ikke findes
            }
            if (!parent.canWrite()) {
                try {
                    throw new IOException("Mappen kan ikke skrives til: " + parent.getAbsolutePath());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            System.out.println(outputFile.getAbsolutePath());

            //running a terminal command in jave
            try {
                //this terminal command takes a geoJSON-file (test.json) and creates a geoTiff (test_wgs84_nearest12.tif)
                String[] args = new String[]{
                        gdalPath,
                        "-sql", "SELECT * FROM jsonValues WHERE step = '" + timeSteps.get(i) + "'",
                        "-a", "nearest:radius1=0.05:radius2=0.05:nodata=-9999",
                        "-txe", "10.0689697", "10.2639771",
                        "-tye", "56.1045981", "56.197728",
                        "-tr", "0.001", "0.001",
                        "-of", "GTiff",
                        "-ot", "Float32",
                        "-co", "COMPRESS=LZW",
                        "-a_srs", "EPSG:4326",
                        "-l", "jsonValues",
                        "-zfield", "total-precipitation",
                        "jsonValues.json",
                        outputFile.getAbsolutePath()
                };

                //ProcessBuilder makes it possible to start external programs from Java
                //this includes commandline programs like GDAL
                Process proc = new ProcessBuilder(args).start();

                //wait for gdal to finish and print exit code


                int exit = proc.waitFor();
                System.out.println("GDAL exit code: " + exit);

                //if gdal fails, print message
                if (exit != 0) {
                    System.err.println("GDAL failed!");
                }

            } catch (IOException | InterruptedException e) {
                //prints detailed error message
                e.printStackTrace();
            }
        }
    }
}
