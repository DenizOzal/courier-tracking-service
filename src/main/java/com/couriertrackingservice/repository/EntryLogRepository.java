package com.couriertrackingservice.repository;

import java.util.Optional;

import com.couriertrackingservice.model.EntryLog;
import com.couriertrackingservice.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntryLogRepository extends JpaRepository<EntryLog, Long> {
    Optional<EntryLog> findTopByCourierIdAndStoreIdOrderByTimestampDesc(String courierId, Long storeId);
}