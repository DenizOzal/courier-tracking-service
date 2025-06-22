package com.couriertrackingservice.service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import com.couriertrackingservice.model.CourierLocation;
import com.couriertrackingservice.model.EntryLog;
import com.couriertrackingservice.model.Store;
import com.couriertrackingservice.repository.CourierLocationRepository;
import com.couriertrackingservice.repository.EntryLogRepository;
import com.couriertrackingservice.repository.StoreRepository;
import com.couriertrackingservice.util.DistanceStrategy;
import com.couriertrackingservice.util.HaversineDistanceStrategy;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourierService {

    private final CourierLocationRepository courierRepo;
    private final StoreRepository storeRepo;
    private final EntryLogRepository entryLogRepo;
    private DistanceStrategy distanceStrategy = new HaversineDistanceStrategy();

    public void addLocation(CourierLocation location) {
        courierRepo.save(location);
        final var temp = location.getCourierId();
        List<Store> stores = storeRepo.findAll();
        for (Store store : stores) {
            double distance = distanceStrategy.calculate(
                location.getLat(), location.getLng(),
                store.getLat(), store.getLng());
            if (distance <= 100) {
                Optional<EntryLog> lastEntry = entryLogRepo
                    .findTopByCourierIdAndStoreOrderByTimestampDesc(location.getCourierId(), store);
                if (lastEntry.isEmpty() ||
                    Duration.between(lastEntry.get().getTimestamp(), location.getTimestamp()).toMinutes() > 1) {
                    entryLogRepo.save(new EntryLog(null, location.getCourierId(), store, location.getTimestamp()));
                }
            }
        }
    }

    public double getTotalTravelDistance(String courierId) {
        List<CourierLocation> points = courierRepo.findByCourierIdOrderByTimestampAsc(courierId);
        double total = 0;
        for (int i = 1; i < points.size(); i++) {
            total += distanceStrategy.calculate(
                points.get(i - 1).getLat(), points.get(i - 1).getLng(),
                points.get(i).getLat(), points.get(i).getLng());
        }
        return total;
    }
}