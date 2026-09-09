package com.peermentoring.controller;

import com.peermentoring.dto.AvailabilityRequest;
import com.peermentoring.entity.AvailabilitySlot;
import com.peermentoring.service.AvailabilityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/availability")
@CrossOrigin(origins = "*")
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    public AvailabilityController(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    /**
     * POST /api/availability
     */
    @PostMapping
    public ResponseEntity<AvailabilitySlot> addSlot(@RequestBody AvailabilityRequest request) {
        AvailabilitySlot slot = availabilityService.addSlot(request);
        return new ResponseEntity<>(slot, HttpStatus.CREATED);
    }

    /**
     * GET /api/availability/tutor/{tutorId}
     */
    @GetMapping("/tutor/{tutorId}")
    public ResponseEntity<List<AvailabilitySlot>> getTutorSlots(@PathVariable Long tutorId) {
        return ResponseEntity.ok(availabilityService.getSlotsByTutor(tutorId));
    }

    /**
     * GET /api/availability/tutor/{tutorId}/available
     */
    @GetMapping("/tutor/{tutorId}/available")
    public ResponseEntity<List<AvailabilitySlot>> getAvailableTutorSlots(@PathVariable Long tutorId) {
        return ResponseEntity.ok(availabilityService.getAvailableSlotsByTutor(tutorId));
    }

    /**
     * DELETE /api/availability/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSlot(@PathVariable Long id) {
        availabilityService.deleteSlot(id);
        return ResponseEntity.noContent().build();
    }
}
