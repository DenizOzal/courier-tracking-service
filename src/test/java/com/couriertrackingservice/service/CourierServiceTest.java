package com.couriertrackingservice.service;

import com.couriertrackingservice.common.util.DistanceStrategy;
import com.couriertrackingservice.model.CourierLocation;
import com.couriertrackingservice.model.EntryLog;
import com.couriertrackingservice.model.Store;
import com.couriertrackingservice.repository.CourierLocationRepository;
import com.couriertrackingservice.repository.EntryLogRepository;
import com.couriertrackingservice.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CourierServiceTest {

    @Mock
    private CourierLocationRepository courierRepo;

    @Mock
    private StoreRepository storeRepo;

    @Mock
    private EntryLogRepository entryLogRepo;

    @Mock
    private DistanceStrategy haversineStrategy;

    @Mock
    private DistanceStrategy euclideanStrategy;

    private CourierService courierService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Map<String, DistanceStrategy> strategyMap = new HashMap<>();
        strategyMap.put("haversine", haversineStrategy);
        strategyMap.put("euclidean", euclideanStrategy);

        courierService = new CourierService(courierRepo, storeRepo, entryLogRepo, "haversine", strategyMap);
    }

    @Test
    void addLocation_shouldSaveLocationAndLogEntry() {
        CourierLocation location = new CourierLocation();
        location.setCourierId("c1");
        location.setLat(40.0);
        location.setLng(29.0);
        location.setTimestamp(LocalDateTime.now());

        Store store = new Store(1L, "Test Store", 40.0, 29.0);

        when(storeRepo.findAll()).thenReturn(Collections.singletonList(store));
        when(entryLogRepo.findTopByCourierIdAndStoreIdOrderByTimestampDesc(anyString(), anyLong()))
            .thenReturn(Optional.empty());
        when(haversineStrategy.calculate(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
            .thenReturn(50.0);

        courierService.addLocation(location);

        verify(courierRepo).save(location);
        verify(entryLogRepo).save(any(EntryLog.class));
    }

    @Test
    void getTotalTravelDistance_shouldReturnFormattedDistance() {
        CourierLocation l1 = new CourierLocation();
        l1.setLat(40.0);
        l1.setLng(29.0);

        CourierLocation l2 = new CourierLocation();
        l2.setLat(40.001);
        l2.setLng(29.001);

        when(courierRepo.findByCourierIdOrderByTimestampAsc("c1"))
            .thenReturn(Arrays.asList(l1, l2));
        when(haversineStrategy.calculate(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
            .thenReturn(100.0);

        String result = courierService.getTotalTravelDistance("c1");

        assertTrue(result.endsWith("meters"));
        assertTrue(result.contains("100"));
    }

    @Test
    void addLocation_shouldNotLogEntryIfDistanceIsGreaterThan100() {
        CourierLocation location = new CourierLocation();
        location.setCourierId("c1");
        location.setLat(0.0);
        location.setLng(0.0);
        location.setTimestamp(LocalDateTime.now());

        Store store = new Store(1L, "Far Store", 50.0, 50.0);

        when(storeRepo.findAll()).thenReturn(Collections.singletonList(store));
        when(haversineStrategy.calculate(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
            .thenReturn(150.0);

        courierService.addLocation(location);

        verify(entryLogRepo, never()).save(any(EntryLog.class));
    }
}
