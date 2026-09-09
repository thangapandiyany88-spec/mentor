package com.peermentoring.repository;

import com.peermentoring.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByTutorId(Long tutorId);
    Optional<Feedback> findByBookingId(Long bookingId);
    Boolean existsByBookingId(Long bookingId);
}
