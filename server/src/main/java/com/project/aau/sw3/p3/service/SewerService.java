package com.project.aau.sw3.p3.service;

import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.project.aau.sw3.p3.model.SewerData;
import com.project.aau.sw3.p3.repository.SewerDataRepo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SewerService {

    private final SewerDataRepo sewerDataRepo;

    public SewerService(SewerDataRepo sewerDataRepo) {
        this.sewerDataRepo = sewerDataRepo;
    }

    // Currently hardcoded to a bbox around Aarhus municipality using EPSG:25832 CRS
    private static final String URL = "https://geoserver.plandata.dk/geoserver/wfs"
            + "?service=WFS&version=2.0.0&request=GetFeature"
            + "&typeNames=pdk:theme_pdk_kloakopland_vedtaget_v"
            + "&outputFormat=application/json"
            + "&bbox=566487.6742,6218236.0206,578426.1126,6228805.4170,EPSG:25832";

    public String saveAndGetSewageLand() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(URL)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String json = response.body();

        //parse json
        //ObjectMapper can read JSON-strings and turn them into objects
        ObjectMapper mapper = new ObjectMapper();

        //readTree(json) turns json string into tree of JsonNode objects,
        //where each node is a json-element (object or value),
        //necessary because json-elements are nested
        //root represents entire json-document
        JsonNode root = mapper.readTree(json);

        //access the "features" array from the root JSON object
        JsonNode features = root.get("features");

        if (features.isArray()) {

            //loop through features in array. a feature is an object with geometry and properties
            for (JsonNode feature : features) {
                //get coordinates as json-string
                String coordinates = feature.get("geometry").get("coordinates").toString();

                //get vaerd1201a
                String sewerType = feature.get("properties").get("vaerd1201a").asText();

                //create new object with two values
                SewerData data = new SewerData(coordinates, sewerType);
                //save data in db. jpa automatically inserts
                sewerDataRepo.save(data);
            }
        }

        return json;
    }
}