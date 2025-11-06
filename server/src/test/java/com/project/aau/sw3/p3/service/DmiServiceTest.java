package com.project.aau.sw3.p3.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.aau.sw3.p3.model.DmiPoint;
import com.project.aau.sw3.p3.repository.DmiPointRepo;
import com.project.aau.sw3.p3.repository.GridRepo;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class DmiServiceTest {

    @Test
    public void saveDmiDiniPointTriggersRepoSave() throws Exception {

        // Mock the classes used in the saveDmiDiniPoint method
        // We mock out these classes so the method we're calling thinks these classes are real and then continues to the next line
        DmiPointRepo dmiPointRepo = mock(DmiPointRepo.class);
        GridRepo gridRepo = mock(GridRepo.class);
        RestTemplate restTemplate = mock(RestTemplate.class);
        ObjectMapper objectMapper = Mockito.spy(new ObjectMapper());

        // Building the structure the service expects (this is the structure coming from the DMI GET request
        Map<String, Object> root = Map.of(
                "ranges", // Key 1
                Map.of("total-precipitation",
                        Map.of("values", List.of(1, 2, 3))
                ),
                "domain", // Key 2
                Map.of("axes", Map.of(
                                "x", Map.of("values", List.of(10, 20, 30), "bounds", List.of(4, 5, 6)),
                                "y", Map.of("values", List.of(20, 30, 40), "bounds", List.of(7, 8, 9)),
                                "t", Map.of("values", List.of("2024-01-01T00:00:00Z", "2024-01-02T00:00:00Z", "2024-01-03T00:00:00Z"))
                        )
                )
        );
        String json = objectMapper.writeValueAsString(root);

        // Return a random body when we call restTemplate.getForEntity, we kind of mock the body below, so this body is ignored
        when(restTemplate.getForEntity(anyString(), eq(String.class))).thenReturn(ResponseEntity.ok(json));

        // Create an instance of DmiService to gain access to the saveDmiDiniPoint method we're interested in testing
        DmiService dmiService = new DmiService(dmiPointRepo, gridRepo, restTemplate, objectMapper);

        dmiService.saveDmiDiniPoint();

        // Verifying that the save method save any DmiPoint object in the saveDmiDiniPoint method.
        verify(dmiPointRepo).save(any(DmiPoint.class));
    }
}
