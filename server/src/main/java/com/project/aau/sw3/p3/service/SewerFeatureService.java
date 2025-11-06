package com.project.aau.sw3.p3.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.project.aau.sw3.p3.model.SewerFeature;
import com.project.aau.sw3.p3.repository.SewerFeatureRepo;
import org.locationtech.proj4j.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

@Service
public class SewerFeatureService {

    private final SewerFeatureRepo sewerFeatureRepo;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    // Initialize everything needed for transforming coordinates from EPSG:25832 -> EPSG:4326
    private static final CoordinateTransform TRANSFORM_25832_TO_4326;
    static {
        CRSFactory crsFactory = new CRSFactory();
        CoordinateReferenceSystem UTM = crsFactory.createFromParameters("epsg:25832",
                "+proj=utm +zone=32 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs");
        CoordinateReferenceSystem WGS84 = crsFactory.createFromParameters("epsg:4326",
                "+proj=longlat +datum=WGS84 +no_defs");
        CoordinateTransformFactory ctFactory = new CoordinateTransformFactory();
        TRANSFORM_25832_TO_4326 = ctFactory.createTransform(UTM, WGS84);
        if (TRANSFORM_25832_TO_4326 == null) {
            throw new IllegalStateException("Failed to build EPSG:25832 -> EPSG:4326");
        }
    }

    public SewerFeatureService(SewerFeatureRepo sewerFeatureRepo, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.sewerFeatureRepo = sewerFeatureRepo;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    // Currently hardcoded to a bbox around Aarhus municipality using EPSG:25832 CRS
    private static final String URL = "https://geoserver.plandata.dk/geoserver/wfs"
            + "?service=WFS&version=2.0.0&request=GetFeature"
            + "&typeNames=pdk:theme_pdk_kloakopland_vedtaget_v"
            + "&outputFormat=application/json"
            + "&bbox=566487.6742,6218236.0206,578426.1126,6228805.4170,EPSG:25832";

    public void createSewagelandFeatures() {

        // GET-request to plandata’s API
        ResponseEntity<String> response = restTemplate.getForEntity(URL, String.class);

        // Store the response's body from the API call as a String
        String json = response.getBody();

        JsonNode root = null;
        try {
            root = objectMapper.readTree(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        JsonNode features = root.get("features");

        ArrayList<SewerFeature> sewerFeatures = new ArrayList<>();
        for (JsonNode feature : features) {
            ObjectNode geometry = (ObjectNode) feature.get("geometry");

            ObjectNode properties = (ObjectNode) feature.get("properties");
            String vaerd1201a = properties.get("vaerd1201a").asText(null);

            if (vaerd1201a == null) {
                continue;
            }

            reprojectGeometry25832To4326(geometry);

            ObjectNode neededProperties = objectMapper.createObjectNode()
                    .put("vaerd1201a", vaerd1201a)
                    .put("CRS", "EPSG:4326");
            ObjectNode newFeature = objectMapper.createObjectNode();
            newFeature.put("type", "Feature");
            newFeature.set("geometry", geometry);
            newFeature.set("properties", neededProperties);
            SewerFeature sewerFeature = new SewerFeature(newFeature);
            sewerFeatures.add(sewerFeature);
        }

        // We always want the newest data when we fetch this, so we just delete what is already in the DB
        sewerFeatureRepo.deleteAll(sewerFeatureRepo.findAll());
        sewerFeatureRepo.saveAll(sewerFeatures);
    }

    public ObjectNode buildSewerFeatureCollection() {
        ObjectNode featureCollection = objectMapper.createObjectNode();

        featureCollection.put("type", "FeatureCollection");
        ArrayNode features = objectMapper.createArrayNode();

        sewerFeatureRepo.findAll().forEach(sewerFeature -> {
            String feature = sewerFeature.getFeature().toString();
            try {
                JsonNode featureNode = objectMapper.readTree(feature);
                features.add(featureNode);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });

        featureCollection.set("features", features);
        return featureCollection;
    }

    /**
     * Reprojects a GeoJSON geometry's coordinates from EPSG:25832 to EPSG:4326 via recursion.
     */
    private void reprojectGeometry25832To4326(ObjectNode geometry) {
        if (geometry == null) return;
        JsonNode coords = geometry.get("coordinates");
        if (coords == null || !coords.isArray()) return;

        ArrayNode reprojected = reprojectCoordinatesRecursive((ArrayNode) coords);
        geometry.set("coordinates", reprojected);
    }

    /**
     * Recursively reprojects coordinates array. If the node is a single position [x, y],
     * it transforms X/Y; otherwise, it maps over nested arrays until the array is so small it can transform.
     */
    private ArrayNode reprojectCoordinatesRecursive(ArrayNode node) {

        if (node.size() > 0 && node.get(0).isNumber()) {
            // This is a single position
            double x = node.get(0).asDouble(); // Easting
            double y = node.get(1).asDouble(); // Northing
            ProjCoordinate src = new ProjCoordinate(x, y);
            ProjCoordinate dst = new ProjCoordinate();
            TRANSFORM_25832_TO_4326.transform(src, dst); // dst.x=lon, dst.y=lat

            ArrayNode out = objectMapper.createArrayNode();
            out.add(dst.x); // lon
            out.add(dst.y); // lat

            return out;
        } else {
            // Nested array -> recurse
            ArrayNode out = objectMapper.createArrayNode();
            for (JsonNode child : node) {
                out.add(reprojectCoordinatesRecursive((ArrayNode) child));
            }
            return out;
        }
    }
}