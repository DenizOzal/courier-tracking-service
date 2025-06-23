package com.couriertrackingservice.service;

import static com.couriertrackingservice.common.model.ApiError.INTERNAL_ERROR;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import com.couriertrackingservice.common.exception.SystemException;
import com.couriertrackingservice.common.model.ApiError;
import com.couriertrackingservice.model.CourierLocation;
import com.couriertrackingservice.model.EntryLog;
import com.couriertrackingservice.model.Store;
import com.couriertrackingservice.repository.CourierLocationRepository;
import com.couriertrackingservice.repository.EntryLogRepository;
import com.couriertrackingservice.repository.StoreRepository;
import com.couriertrackingservice.common.util.DistanceStrategy;
import com.couriertrackingservice.common.util.HaversineDistanceStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourierService {

    private final CourierLocationRepository courierRepo;
    private final StoreRepository storeRepo;
    private final EntryLogRepository entryLogRepo;
    private DistanceStrategy distanceStrategy = new HaversineDistanceStrategy();

    @Transactional
    public void addLocation(CourierLocation location) {
       try {
           courierRepo.save(location);
           List<Store> stores = storeRepo.findAll();
           for (Store store : stores) {
               double distance = distanceStrategy.calculate(
                   location.getLat(), location.getLng(),
                   store.getLat(), store.getLng());
               if (distance <= 100) {
                   Optional<EntryLog> lastEntry = entryLogRepo
                       .findTopByCourierIdAndStoreIdOrderByTimestampDesc(location.getCourierId(), store.getId());
                   if (lastEntry.isEmpty() ||
                       Duration.between(lastEntry.get().getTimestamp(), location.getTimestamp()).toMinutes() > 1) {
                       entryLogRepo.save(new EntryLog(null, location.getCourierId(), store.getId(), location.getTimestamp()));
                       log.info("Logged new entry for courierId={} at store='{}' timestamp={}",
                           location.getCourierId(), store.getName(), location.getTimestamp());
                   }
               }
           }
       } catch (Exception e) {
           log.error("Failed to add location for courierId={} at timestamp={} - error: {}",
               location.getCourierId(), location.getTimestamp(), e.getMessage(), e);
           throw new SystemException(INTERNAL_ERROR);
       }
    }

    public String getTotalTravelDistance(String courierId) {
        try {
            List<CourierLocation> points = courierRepo.findByCourierIdOrderByTimestampAsc(courierId);
            double total = 0;
            for (int i = 1; i < points.size(); i++) {
                total += distanceStrategy.calculate(
                    points.get(i - 1).getLat(), points.get(i - 1).getLng(),
                    points.get(i).getLat(), points.get(i).getLng());
            }
            return String.format("%.2f meters", total);
        } catch (Exception e) {
            log.error("Failed to calculate total travel distance for courierId={} - error: {}", courierId, e.getMessage(), e);
            throw new SystemException(INTERNAL_ERROR);
        }
    }
}