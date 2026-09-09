package com.peermentoring.controller;

import com.peermentoring.dto.FeedbackDTO;
import com.peermentoring.dto.FeedbackRequest;
import com.peermentoring.service.FeedbackService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedback")
@CrossOrigin(origins = "*")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    /**
     * POST /api/feedback
     */
    @PostMapping
    public ResponseEntity<FeedbackDTO> submitFeedback(@RequestBody FeedbackRequest request) {
        FeedbackDTO feedback = feedbackService.submitFeedback(request);
        return new ResponseEntity<>(feedback, HttpStatus.CREATED);
    }

    /**
     * GET /api/feedback/tutor/{tutorId}
     */
    @GetMapping("/tutor/{tutorId}")
    public ResponseEntity<List<FeedbackDTO>> getTutorFeedback(@PathVariable Long tutorId) {
        return ResponseEntity.ok(feedbackService.getTutorFeedback(tutorId));
    }
}
