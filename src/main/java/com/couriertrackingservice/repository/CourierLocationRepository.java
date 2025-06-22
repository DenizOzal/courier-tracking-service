package com.couriertrackingservice.repository;

import java.util.List;

import com.couriertrackingservice.model.CourierLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourierLocationRepository extends JpaRepository<CourierLocation, Long> {
    List<CourierLocation> findByCourierIdOrderByTimestampAsc(String courierId);
}
