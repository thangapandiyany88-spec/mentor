package com.peermentoring.service;

import com.peermentoring.dto.FeedbackDTO;
import com.peermentoring.dto.FeedbackRequest;
import com.peermentoring.entity.Booking;
import com.peermentoring.entity.Feedback;
import com.peermentoring.entity.User;
import com.peermentoring.exception.BadRequestException;
import com.peermentoring.exception.ResourceNotFoundException;
import com.peermentoring.repository.BookingRepository;
import com.peermentoring.repository.FeedbackRepository;
import com.peermentoring.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    public FeedbackService(FeedbackRepository feedbackRepository,
                           BookingRepository bookingRepository,
                           UserRepository userRepository) {
        this.feedbackRepository = feedbackRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }

    public FeedbackDTO submitFeedback(FeedbackRequest request) {
        // Validation 1: Rating must be between 1 and 5 (Rule 8)
        if (request.getRating() == null || request.getRating() < 1 || request.getRating() > 5) {
            throw new BadRequestException("Rating must be an integer between 1 and 5 stars");
        }

        // Validation 2: Booking exists
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + request.getBookingId()));

        // Validation 3: Junior exists
        User junior = userRepository.findById(request.getJuniorId())
                .orElseThrow(() -> new ResourceNotFoundException("Junior student user not found with ID: " + request.getJuniorId()));

        // Validation 4: Junior owns the booking
        if (!booking.getJunior().getId().equals(junior.getId())) {
            throw new BadRequestException("You can only submit feedback for your own bookings");
        }

        // Validation 5: Booking status must be COMPLETED (Rule 6)
        if (!"COMPLETED".equalsIgnoreCase(booking.getStatus())) {
            throw new BadRequestException("Feedback can only be submitted for completed sessions");
        }

        // Validation 6: Prevent duplicate feedback (Rule 7)
        if (feedbackRepository.existsByBookingId(booking.getId())) {
            throw new BadRequestException("Feedback has already been submitted for this session");
        }

        Feedback feedback = new Feedback();
        feedback.setBooking(booking);
        feedback.setJunior(junior);
        feedback.setTutor(booking.getTutor());
        feedback.setRating(request.getRating());
        feedback.setComment(request.getComment() != null ? request.getComment().trim() : "");

        Feedback saved = feedbackRepository.save(feedback);
        return mapToDTO(saved);
    }

    public List<FeedbackDTO> getTutorFeedback(Long tutorId) {
        return feedbackRepository.findByTutorId(tutorId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private FeedbackDTO mapToDTO(Feedback feedback) {
        FeedbackDTO dto = new FeedbackDTO();
        dto.setId(feedback.getId());
        dto.setBookingId(feedback.getBooking().getId());
        dto.setJuniorName(feedback.getJunior().getFullName());
        dto.setSubjectName(feedback.getBooking().getSubject().getSubjectName());
        dto.setRating(feedback.getRating());
        dto.setComment(feedback.getComment());
        dto.setCreatedAt(feedback.getCreatedAt());
        return dto;
    }
}
