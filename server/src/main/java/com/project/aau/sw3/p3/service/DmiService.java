package com.project.aau.sw3.p3.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.aau.sw3.p3.model.GridCell;
import com.project.aau.sw3.p3.repository.GridRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.time.ZonedDateTime;
import java.time.LocalDateTime;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;


@Service
public class DmiService {

    private final GridRepo gridRepo;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public DmiService(GridRepo gridRepo, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.gridRepo = gridRepo;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    // Endpoint for the bbox
    private static final String DMI_URL2 =
            "https://dmigw.govcloud.dk/v1/forecastedr/collections/harmonie_dini_sf"
                    + "/bbox?bbox=10.0689697,56.1045981,10.2639771,56.197728"
                    + "&parameter-name=total-precipitation,latitude,longitude"
                    + "&crs=crs84&f=GeoJSON&api-key=39d54b14-ff57-4612-85de-f66333bd4b03";

    //Getting the gdal path from the application-local.yaml file
    @Value("${gdal.path}")
    private String gdalPath;

    public void saveBBox() {
        try {
            //GET-request bbox api, get answer as String
            ResponseEntity<String> response = restTemplate.getForEntity(DMI_URL2, String.class);

            //Save as JSON String
            String json = response.getBody();

            //Create ObjectMapper to convert JSON strings into Java objects
            //Converts the whole JSON to a Map
            Map<String, Object> root = objectMapper.readValue(json, Map.class);

            //"features" is a list of feature-objects
            List<Map<String, Object>> features = (List<Map<String, Object>>) root.get("features");

            //Loop through features and save each grid cell
            for (int i = 0; i < features.size(); i++) {
                Map<String, Object> featureObject = features.get(i);

                //Get "geometry" part from the "features"-object
                Map<String, Object> geometry = (Map<String, Object>) featureObject.get("geometry");

                //Get "coordinates" from the "geometry"-object
                List<Object> coordinates = (List<Object>) geometry.get("coordinates");

                //Convert coordinate-objects to doubles
                double xCoordinate = ((Number) coordinates.get(0)).doubleValue();
                double yCoordinate = ((Number) coordinates.get(1)).doubleValue();

                //Get "properties" from "features" part
                Map<String, Object> properties = (Map<String, Object>) featureObject.get("properties");

                //Get "total-precipitation" from the "properties"-object
                Object totalPrecipitation = properties.get("total-precipitation");

                //Convert totalPrecipitation-object to double
                double precipitation = ((Number) totalPrecipitation).doubleValue();

                //Get "step" from "properties"
                Object step = properties.get("step");

                //Convert step-object to a String
                String stepString = step.toString();

                //Convert step-string to date and time
                LocalDateTime timeStep = ZonedDateTime.parse(stepString).toLocalDateTime();

                //Create GridCell object
                GridCell gridCell = new GridCell(xCoordinate, yCoordinate, precipitation, timeStep);

                //Save in db
                gridRepo.save(gridCell);
            }
        } catch (Exception e) {
            //Error message in console. tells where in the code it went wrong
            e.printStackTrace();
        }
    }

    public ObjectNode buildDmiGrid() {
        ObjectNode featureCollection = objectMapper.createObjectNode();

        //Create empty featureCollection to adhere to GeoJSON format (needed in frontend)
        featureCollection.put("type", "FeatureCollection");

        //Create JSON-array to adhere to GeoJSON format
        ArrayNode features = objectMapper.createArrayNode();

        //Add "type" and "geometry" to adhere to GeoJSON format
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
        });

        featureCollection.set("features", features);
        return featureCollection;
    }

    public void projectGrids() throws IOException {
        ObjectNode dmiGrid = buildDmiGrid();
        ObjectMapper mapper = new ObjectMapper();

        //Create a file object
        File inputFile = new File("precipitationGridCells.json");
        try {
            //Write json to a file, "file" is the file to save it in, "dmiGrid" is the json to save
            mapper.writeValue(inputFile, dmiGrid);

        } catch (IOException e) {
            //When using writeValue, we have to catch for IOException
            throw new RuntimeException(e);
        }

        //Finding all timesteps in DB
        List<LocalDateTime> timeSteps = gridRepo.findAllTimeSteps();

        String basePath = System.getProperty("user.dir");
        System.out.println("base path: " + basePath);

        File gridDir;
        //If the application is started in the terminal using maven
        if (basePath.endsWith("server")) {
            //Go one level up and normalize the path by using getCanonicalFile(), which means it removed any "." or ".." in the path
            // this means it goes from "AAU-SW3-P3/server/../client/public/grids/" to "AAU-SW3-P3/client/public/grids/"
            //getCanonicalFile, return  the full path to the given directory
            gridDir = new File(basePath, "../client/public/grids/").getCanonicalFile();
        } else {
            //Use the path relative to the project root
            gridDir = new File(basePath, "client/public/grids/").getCanonicalFile();
        }

        for (int i = 0; i < timeSteps.size(); i++) {
            String fileName = "grid" + i + ".tif";
            File outputFile = new File(gridDir, fileName);
            System.out.println(outputFile);
            File parent = outputFile.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();  //Create directory if it doesn't exist
            }
            if (!parent.canWrite()) {
                try {
                    throw new IOException("Directory can not be written to: " + parent.getAbsolutePath());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            try {
                //This terminal command takes a geoJSON-file (inputFile = precipitationGridCells.json) and creates a geoTiff (outputFile)
                //For more information about the GDAL command can be find in README.md
                String[] args = new String[]{
                        gdalPath,
                        "-sql", "SELECT * FROM precipitationGridCells WHERE step = '" + timeSteps.get(i) + "'",
                        "-a", "nearest:radius1=0.05:radius2=0.05:nodata=-9999",
                        "-txe", "10.0689697", "10.2639771",
                        "-tye", "56.1045981", "56.197728",
                        "-tr", "0.001", "0.001",
                        "-of", "GTiff",
                        "-ot", "Float32",
                        "-co", "COMPRESS=LZW",
                        "-a_srs", "EPSG:4326",
                        "-l", "precipitationGridCells",
                        "-zfield", "total-precipitation",
                        inputFile.getAbsolutePath(),
                        outputFile.getAbsolutePath()
                };

                //ProcessBuilder makes it possible to start external programs from Java
                //This includes commandline programs like GDAL
                Process proc = new ProcessBuilder(args).start();

                //Wait for gdal to finish and print exit code
                int exit = proc.waitFor();
                System.out.println("GDAL exit code: " + exit);

                //If gdal fails, print message
                if (exit != 0) {
                    System.err.println("GDAL failed!");
                }

            } catch (IOException | InterruptedException e) {
                //Prints detailed error message
                e.printStackTrace();
            }
        }
    }

    public void loadGridOnStartup() {
        if (gridRepo.count() == 0) {
            System.out.println("Grid is empty. fetching DMI bbox grid...");
            saveBBox();
        } else {
            System.out.println("Grid already exists. skipping DMI fetch.");
        }
    }
}
