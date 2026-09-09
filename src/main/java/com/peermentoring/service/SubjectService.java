package com.peermentoring.service;

import com.peermentoring.entity.Subject;
import com.peermentoring.exception.BadRequestException;
import com.peermentoring.repository.SubjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public Subject createSubject(Subject subject) {
        if (subject.getSubjectCode() == null || subject.getSubjectCode().trim().isEmpty()) {
            throw new BadRequestException("Subject code is required");
        }
        if (subject.getSubjectName() == null || subject.getSubjectName().trim().isEmpty()) {
            throw new BadRequestException("Subject name is required");
        }
        if (subjectRepository.findBySubjectCode(subject.getSubjectCode()).isPresent()) {
            throw new BadRequestException("Subject code already exists");
        }
        return subjectRepository.save(subject);
    }
}
