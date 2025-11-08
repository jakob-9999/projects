package com.project.aau.sw3.p3.controller;

import com.project.aau.sw3.p3.repository.SewerFeatureRepo;
import com.project.aau.sw3.p3.service.SewerFeatureService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// This is a unit test
// Tests regarding endpoints in the SewerController class.
// Tests in here will test if the endpoints are reachable and verify if they are calling their expected method.
@WebMvcTest(SewerController.class)
public class SewerControllerEndpointTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SewerFeatureRepo sewerFeatureRepo;

    @MockitoBean
    private SewerFeatureService sewerFeatureService;

    // Testing if the endpoint is reachable and verifies that the createSewagelandFeatures() method is invoked in the endpoint
    @Test
    public void postSewagelandFeatureEndpointCallsService() throws Exception {

        // Testing if the endpoint returns status code 200 (OK)
        mockMvc.perform(post("/api/sewageland/features")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // testing if the createSewagelandFeatures() method was invoked in the endpoint method
        verify(sewerFeatureService).createSewagelandFeatures();
    }

    // Testing if the endpoint is reachable and verifies that the buildSewerFeatureCollection() method is invoked in the endpoint
    @Test
    public void getSewagelandFeatureEndpointCallsRepo() throws Exception {

        // Testing if the endpoint returns status code 200 (OK)
        mockMvc.perform(get("/api/sewageland/features")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // testing if the buildSewerFeatureCollection() method was invoked in the endpoint method
        verify(sewerFeatureService).buildSewerFeatureCollection();
    }



}
