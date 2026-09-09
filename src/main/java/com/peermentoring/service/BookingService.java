package com.peermentoring.service;

import com.peermentoring.dto.BookingRequest;
import com.peermentoring.dto.BookingResponse;
import com.peermentoring.entity.*;
import com.peermentoring.exception.BadRequestException;
import com.peermentoring.exception.ResourceNotFoundException;
import com.peermentoring.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final TutorProfileRepository tutorProfileRepository;
    private final SubjectRepository subjectRepository;
    private final AvailabilitySlotRepository availabilitySlotRepository;

    public BookingService(BookingRepository bookingRepository,
                          UserRepository userRepository,
                          TutorProfileRepository tutorProfileRepository,
                          SubjectRepository subjectRepository,
                          AvailabilitySlotRepository availabilitySlotRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.tutorProfileRepository = tutorProfileRepository;
        this.subjectRepository = subjectRepository;
        this.availabilitySlotRepository = availabilitySlotRepository;
    }

    @Transactional
    public BookingResponse createBooking(BookingRequest request) {
        // Validation 1: Junior User exists
        User junior = userRepository.findById(request.getJuniorId())
                .orElseThrow(() -> new ResourceNotFoundException("Junior student user not found with ID: " + request.getJuniorId()));

        // Validation 2: Tutor Profile exists
        TutorProfile tutor = tutorProfileRepository.findById(request.getTutorId())
                .orElseThrow(() -> new ResourceNotFoundException("Tutor profile not found with ID: " + request.getTutorId()));

        // Validation 3: Subject exists
        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with ID: " + request.getSubjectId()));

        // Validation 4: Slot exists
        AvailabilitySlot slot = availabilitySlotRepository.findById(request.getSlotId())
                .orElseThrow(() -> new ResourceNotFoundException("Availability slot not found with ID: " + request.getSlotId()));

        // Rule 1 & Rule 2: A user cannot book their own tutor profile
        if (junior.getId().equals(tutor.getUser().getId())) {
            throw new BadRequestException("You cannot book a mentoring session with yourself!");
        }

        // Rule 3 & Rule 4: Slot must be AVAILABLE
        if (!"AVAILABLE".equalsIgnoreCase(slot.getStatus())) {
            throw new BadRequestException("This slot is already booked or unavailable");
        }

        // Change slot status to BOOKED
        slot.setStatus("BOOKED");
        availabilitySlotRepository.save(slot);

        // Assign Meeting Type & Venue
        String meetingType = (request.getMeetingType() != null && request.getMeetingType().equalsIgnoreCase("ONLINE")) ? "ONLINE" : "LIBRARY";
        String venue = meetingType.equals("LIBRARY") ? "Library Room 4" : "Online";

        Booking booking = new Booking();
        booking.setJunior(junior);
        booking.setTutor(tutor);
        booking.setSubject(subject);
        booking.setSlot(slot);
        booking.setBookingDate(slot.getSlotDate());
        booking.setStartTime(slot.getStartTime());
        booking.setEndTime(slot.getEndTime());
        booking.setMeetingType(meetingType);
        booking.setVenue(venue);
        booking.setStatus("CONFIRMED");

        Booking savedBooking = bookingRepository.save(booking);

        // Set Meeting Link if Online
        if (meetingType.equals("ONLINE")) {
            savedBooking.setMeetingLink("https://meet.example.com/session/" + savedBooking.getId());
            savedBooking = bookingRepository.save(savedBooking);
        }

        return mapToBookingResponse(savedBooking);
    }

    public List<BookingResponse> getJuniorBookings(Long juniorId) {
        return bookingRepository.findByJuniorId(juniorId).stream()
                .map(this::mapToBookingResponse)
                .collect(Collectors.toList());
    }

    public List<BookingResponse> getTutorBookings(Long tutorId) {
        return bookingRepository.findByTutorId(tutorId).stream()
                .map(this::mapToBookingResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public BookingResponse cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + bookingId));

        if ("COMPLETED".equalsIgnoreCase(booking.getStatus())) {
            throw new BadRequestException("Cannot cancel a completed session");
        }

        booking.setStatus("CANCELLED");

        // Rule 5: Mark slot as AVAILABLE again
        AvailabilitySlot slot = booking.getSlot();
        if (slot != null) {
            slot.setStatus("AVAILABLE");
            availabilitySlotRepository.save(slot);
        }

        Booking updated = bookingRepository.save(booking);
        return mapToBookingResponse(updated);
    }

    @Transactional
    public BookingResponse completeBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + bookingId));

        booking.setStatus("COMPLETED");
        Booking updated = bookingRepository.save(booking);
        return mapToBookingResponse(updated);
    }

    public BookingResponse mapToBookingResponse(Booking booking) {
        BookingResponse resp = new BookingResponse();
        resp.setId(booking.getId());
        resp.setBookingCode("BK" + (10000 + booking.getId()));
        resp.setJuniorId(booking.getJunior().getId());
        resp.setJuniorName(booking.getJunior().getFullName());
        resp.setTutorId(booking.getTutor().getId());
        resp.setTutorName(booking.getTutor().getUser().getFullName());
        resp.setSubjectId(booking.getSubject().getId());
        resp.setSubjectName(booking.getSubject().getSubjectName());
        resp.setBookingDate(booking.getBookingDate().toString());
        resp.setStartTime(booking.getStartTime().format(DateTimeFormatter.ofPattern("hh:mm a")));
        resp.setEndTime(booking.getEndTime().format(DateTimeFormatter.ofPattern("hh:mm a")));
        resp.setMeetingType(booking.getMeetingType());
        resp.setVenue(booking.getVenue());
        resp.setMeetingLink(booking.getMeetingLink());
        resp.setStatus(booking.getStatus());
        return resp;
    }
}
