package com.couriertrackingservice.common.util;

import org.springframework.stereotype.Component;

@Component("euclidean")
public class EuclideanDistanceStrategy implements DistanceStrategy {
    public double calculate(double lat1, double lon1, double lat2, double lon2) {
        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;
        return Math.sqrt(dLat * dLat + dLon * dLon) * 111139;
    }
}