package com.peermentoring.repository;

import com.peermentoring.entity.AvailabilitySlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvailabilitySlotRepository extends JpaRepository<AvailabilitySlot, Long> {
    List<AvailabilitySlot> findByTutorId(Long tutorId);
    List<AvailabilitySlot> findByTutorIdAndStatus(Long tutorId, String status);
}
