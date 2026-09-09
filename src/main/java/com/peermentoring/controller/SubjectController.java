package com.peermentoring.controller;

import com.peermentoring.entity.Subject;
import com.peermentoring.service.SubjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
@CrossOrigin(origins = "*")
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    /**
     * GET /api/subjects
     */
    @GetMapping
    public ResponseEntity<List<Subject>> getSubjects() {
        return ResponseEntity.ok(subjectService.getAllSubjects());
    }

    /**
     * POST /api/subjects
     */
    @PostMapping
    public ResponseEntity<Subject> addSubject(@RequestBody Subject subject) {
        Subject created = subjectService.createSubject(subject);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }
}
