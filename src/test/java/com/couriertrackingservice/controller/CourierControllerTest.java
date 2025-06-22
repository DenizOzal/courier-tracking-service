package com.couriertrackingservice.controller;

import com.couriertrackingservice.model.CourierLocation;
import com.couriertrackingservice.service.CourierService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;

@WebMvcTest(CourierController.class)
class CourierControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CourierService courierService;

    @Test
    void addLocation_shouldReturnSuccess() throws Exception {
        Mockito.doNothing().when(courierService).addLocation(any(CourierLocation.class));
        String json = "{" +
            "\"courierId\":\"c1\"," +
            "\"lat\":40.0," +
            "\"lng\":29.0," +
            "\"timestamp\":\"2024-06-22T12:00:00Z\"}";
        mockMvc.perform(post("/api/courier/location")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void addLocationBatch_shouldReturnSuccess() throws Exception {
        Mockito.doNothing().when(courierService).addLocation(any(CourierLocation.class));
        String json = "[{" +
            "\"courierId\":\"c1\"," +
            "\"lat\":40.0," +
            "\"lng\":29.0," +
            "\"timestamp\":\"2024-06-22T12:00:00Z\"}]";
        mockMvc.perform(post("/api/courier/location/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void getDistance_shouldReturnSuccess() throws Exception {
        Mockito.when(courierService.getTotalTravelDistance("c1")).thenReturn("100.00 m");
        mockMvc.perform(get("/api/courier/distance/c1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data").value("100.00 m"));
    }
}