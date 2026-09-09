package com.peermentoring.controller;

import com.peermentoring.dto.TutorProfileRequest;
import com.peermentoring.dto.TutorResponse;
import com.peermentoring.service.TutorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TutorController handles endpoints for browsing tutors, search/filtering, profile retrieval and updates.
 */
@RestController
@RequestMapping("/api/tutors")
@CrossOrigin(origins = "*")
public class TutorController {

    private final TutorService tutorService;

    public TutorController(TutorService tutorService) {
        this.tutorService = tutorService;
    }

    /**
     * GET /api/tutors
     * GET /api/tutors?subject=DSA
     * GET /api/tutors?department=CSE
     * GET /api/tutors?search=Arun
     */
    @GetMapping
    public ResponseEntity<List<TutorResponse>> getAllTutors(
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String search) {
        List<TutorResponse> tutors = tutorService.getAllTutors(subject, department, search);
        return ResponseEntity.ok(tutors);
    }

    /**
     * GET /api/tutors/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<TutorResponse> getTutorById(@PathVariable Long id) {
        TutorResponse tutor = tutorService.getTutorById(id);
        return ResponseEntity.ok(tutor);
    }

    /**
     * POST /api/tutors/profile
     */
    @PostMapping("/profile")
    public ResponseEntity<TutorResponse> createProfile(@RequestBody TutorProfileRequest request) {
        TutorResponse response = tutorService.createOrUpdateProfile(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * PUT /api/tutors/profile/{id}
     */
    @PutMapping("/profile/{id}")
    public ResponseEntity<TutorResponse> updateProfile(@PathVariable Long id, @RequestBody TutorProfileRequest request) {
        TutorResponse response = tutorService.createOrUpdateProfile(request);
        return ResponseEntity.ok(response);
    }
}
