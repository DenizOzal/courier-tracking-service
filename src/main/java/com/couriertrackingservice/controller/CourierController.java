package com.couriertrackingservice.controller;

import java.util.List;

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
public class CourierController {

    private final CourierService service;

    public CourierController(CourierService service) {
        this.service = service;
    }

    @PostMapping("/location")
    public ResponseEntity<String> postLocation(@RequestBody CourierLocation location) {
        service.addLocation(location);
        return ResponseEntity.ok("Location saved");
    }

    @PostMapping("/location/batch")
    public ResponseEntity<String> postLocationBatch(@RequestBody List<CourierLocation> locations) {
        for (CourierLocation location : locations) {
            service.addLocation(location);
        }
        return ResponseEntity.ok("Batch of locations saved");
    }

    @GetMapping("/distance/{courierId}")
    public ResponseEntity<Double> getDistance(@PathVariable String courierId) {
        return ResponseEntity.ok(service.getTotalTravelDistance(courierId));
    }
}
