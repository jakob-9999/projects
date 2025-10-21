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

    private static final String URL = "https://geoserver.plandata.dk/geoserver/wfs"
            + "?service=WFS&version=2.0.0&request=GetFeature"
            + "&typeNames=pdk:theme_pdk_kloakopland_vedtaget_v"
            + "&outputFormat=application/json"
            + "&bbox=565000,6228000,585000,6242000,EPSG:25832";

    public String getSewageLand() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(URL)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String json = response.body();

        //parse json
        ObjectMapper mapper = new ObjectMapper(); //can read JSON-strings and turn them into objects
        JsonNode root = mapper.readTree(json);    //readTree(json) turns json string into tree of JsonNode objects, where each node is a json-element, necessary because json is nested
        JsonNode features = root.get("features"); //root represents entire json-document

        if (features.isArray()) {
            for (JsonNode feature : features) {   //loop through features in array. a feature is an object with geometry and properties
                //get coordinates as json-string
                String coordinates = feature.get("geometry").get("coordinates").toString();

                //get vaerd1201a
                String sewerType = feature.get("properties").get("vaerd1201a").asText();

                SewerData data = new SewerData(coordinates, sewerType); //create new object with two values
                sewerDataRepo.save(data);                               //save data in db. jpa automatically inserts
            }
        }

        return json;
    }
}