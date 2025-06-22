package com.couriertrackingservice.controller;

import java.util.List;

import com.couriertrackingservice.common.model.ApiResponse;
import com.couriertrackingservice.common.model.BaseController;
import com.couriertrackingservice.model.CourierLocation;
import com.couriertrackingservice.service.CourierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/courier")
public class CourierController extends BaseController {

    private final CourierService service;

    public CourierController(CourierService service) {
        this.service = service;
    }

    @PostMapping("/location")
    public ApiResponse<String> addLocation(@RequestBody CourierLocation location) {
        service.addLocation(location);
        return success("Location created");
    }

    @PostMapping("/location/batch")
    public ApiResponse<String> addLocationBatch(@RequestBody List<CourierLocation> locations) {
        for (CourierLocation location : locations) {
            service.addLocation(location);
        }
        return success("Batch location created");
    }

    @GetMapping("/distance/{courierId}")
    public ApiResponse<String> getDistance(@PathVariable String courierId) {
        String formattedDistance = service.getTotalTravelDistance(courierId);
        return success(formattedDistance);
    }
}
