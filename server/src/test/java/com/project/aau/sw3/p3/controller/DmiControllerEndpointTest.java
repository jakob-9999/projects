package com.project.aau.sw3.p3.controller;

import com.project.aau.sw3.p3.repository.DmiPointRepo;
import com.project.aau.sw3.p3.service.DmiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// This is a unit test
// Tests regarding endpoints in the DmiController class.
// Tests in here will test if the endpoints are reachable and verify if they are calling their expected method.
@WebMvcTest(DmiController.class)
public class DmiControllerEndpointTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DmiPointRepo dmiPointRepo;

    @MockitoBean
    private DmiService dmiService;

    // Testing if the endpoint is reachable and verifies that the saveDmiDiniPoint() method is invoked in the endpoint
    @Test
    public void postDmiDiniPointEndpointCallsService() throws Exception {

        mockMvc.perform(post("/api/dmi-dini/point")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(dmiService).saveDmiDiniPoint();
    }

        // Testing if the endpoint is reachable and verifies that the saveDmiDiniPoint() method is invoked in the endpoint
    @Test
    public void getDmiDiniPointEndpointCallsRepo() throws Exception {

        mockMvc.perform(get("/api/dmi-dini/point")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(dmiPointRepo).findById(any());
    }



}
