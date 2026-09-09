package com.peermentoring.controller;

import com.peermentoring.dto.BookingRequest;
import com.peermentoring.dto.BookingResponse;
import com.peermentoring.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * POST /api/bookings
     */
    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@RequestBody BookingRequest request) {
        BookingResponse booking = bookingService.createBooking(request);
        return new ResponseEntity<>(booking, HttpStatus.CREATED);
    }

    /**
     * GET /api/bookings/junior/{juniorId}
     */
    @GetMapping("/junior/{juniorId}")
    public ResponseEntity<List<BookingResponse>> getJuniorBookings(@PathVariable Long juniorId) {
        return ResponseEntity.ok(bookingService.getJuniorBookings(juniorId));
    }

    /**
     * GET /api/bookings/tutor/{tutorId}
     */
    @GetMapping("/tutor/{tutorId}")
    public ResponseEntity<List<BookingResponse>> getTutorBookings(@PathVariable Long tutorId) {
        return ResponseEntity.ok(bookingService.getTutorBookings(tutorId));
    }

    /**
     * PUT /api/bookings/{id}/cancel
     */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<BookingResponse> cancelBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.cancelBooking(id));
    }

    /**
     * PUT /api/bookings/{id}/complete
     */
    @PutMapping("/{id}/complete")
    public ResponseEntity<BookingResponse> completeBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.completeBooking(id));
    }
}
