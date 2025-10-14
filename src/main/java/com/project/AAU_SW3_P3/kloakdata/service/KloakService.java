package com.project.AAU_SW3_P3.kloakdata.service;

import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class KloakService {

    private static final String URL = "https://geoserver.plandata.dk/geoserver/wfs"
            + "?service=WFS&version=2.0.0&request=GetFeature"
            + "&typeNames=pdk:theme_pdk_kloakopland_vedtaget_v"
            + "&outputFormat=application/json"
            + "&bbox=565000,6228000,585000,6242000,EPSG:25832";

    public String hentKloakOpland() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(URL)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body(); // Returnér JSON som string
    }
}
