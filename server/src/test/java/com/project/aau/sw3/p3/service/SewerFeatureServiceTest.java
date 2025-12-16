package com.project.aau.sw3.p3.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.project.aau.sw3.p3.model.SewerFeature;
import com.project.aau.sw3.p3.repository.SewerFeatureRepo;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class SewerFeatureServiceTest {

    @Test
    public void testCreateSewagelandFeatureAndTriggersRepoSave() throws Exception {

        //Mock the classes used in the createSewagelandFeatures() method
        SewerFeatureRepo sewerFeatureRepo = mock(SewerFeatureRepo.class); // No actual DB connection since it's mocked
        RestTemplate restTemplate = mock(RestTemplate.class); // No actual network traffic since it's mocked
        ObjectMapper objectMapper = Mockito.spy(new ObjectMapper()); // We use a spy since we are interested in some methods inside the ObjectMapper

        //Building the structure the service expects (this is the structure coming from the plandata GET request)
        Map<String, Object> root = new HashMap<>();
        root.put("features", List.of(
                Map.of(
                        "type", "Feature", // Key 1
                        "geometry", // Key 2
                        Map.of("coordinates",
                                List.of(
                                        List.of(
                                                List.of(10.20, 56.11)
                                        )
                                ),
                                "type", "MultiPolygon"
                        ),
                        "properties", Map.of(
                                "CRS", "EPSG:4326",
                                "vaerd1201a", "Fælleskloakeret (spildevand og overfladevand løber i samme ledning)"
                        )
                )
        ));

        String json = objectMapper.writeValueAsString(root);

        //Return a random body when we call restTemplate.getForEntity, we kind of mock the body below, so this body is ignored
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(ResponseEntity.ok(json));

        //Create an instance of SewerFeatureService to gain access to the createSewagelandFeatures method we're interested in testing
        SewerFeatureService sewerFeatureService = new SewerFeatureService(sewerFeatureRepo, restTemplate, objectMapper);

        sewerFeatureService.createSewagelandFeatures();

        //Verifying that the saveAll method is invoked on any List in the createSewagelandFeatures method.
        verify(sewerFeatureRepo).saveAll(anyList());
    }

    @Test
    public void testBuildSewerFeatureCollection() {

        //Mock the classes used in the createSewagelandFeatures() method
        SewerFeatureRepo sewerFeatureRepo = mock(SewerFeatureRepo.class); // No actual DB connection since it's mocked
        RestTemplate restTemplate = mock(RestTemplate.class); // No actual network traffic since it's mocked
        ObjectMapper objectMapper = Mockito.spy(new ObjectMapper()); // We use a spy since we are interested in some methods inside the ObjectMapper
        List<SewerFeature> sewerFeatures = new ArrayList<>();

        //Create an instance of SewerFeatureService to gain access to the buildSewerFeatureCollection method we're interested in testing
        SewerFeatureService sewerFeatureService = new SewerFeatureService(sewerFeatureRepo, restTemplate, objectMapper);

        ObjectNode featureCollection = sewerFeatureService.buildSewerFeatureCollection();

        //Verifying that the ObjectNode featureCollection is returned as a non-null value from the buildSewerFeatureCollection method
        assertNotNull(featureCollection);

        //Verifying that the findAll() method is invoked in the buildSewerFeatureCollection method
        verify(sewerFeatureRepo).findAll();
    }
}
