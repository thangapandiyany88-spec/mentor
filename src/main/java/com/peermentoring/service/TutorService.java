package com.peermentoring.service;

import com.peermentoring.dto.TutorProfileRequest;
import com.peermentoring.dto.TutorResponse;
import com.peermentoring.entity.Feedback;
import com.peermentoring.entity.Subject;
import com.peermentoring.entity.TutorProfile;
import com.peermentoring.entity.User;
import com.peermentoring.exception.BadRequestException;
import com.peermentoring.exception.ResourceNotFoundException;
import com.peermentoring.repository.FeedbackRepository;
import com.peermentoring.repository.SubjectRepository;
import com.peermentoring.repository.TutorProfileRepository;
import com.peermentoring.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TutorService {

    private final TutorProfileRepository tutorProfileRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final FeedbackRepository feedbackRepository;

    public TutorService(TutorProfileRepository tutorProfileRepository,
                        UserRepository userRepository,
                        SubjectRepository subjectRepository,
                        FeedbackRepository feedbackRepository) {
        this.tutorProfileRepository = tutorProfileRepository;
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
        this.feedbackRepository = feedbackRepository;
    }

    public List<TutorResponse> getAllTutors(String subject, String department, String search) {
        List<TutorProfile> profiles;
        if ((subject != null && !subject.trim().isEmpty()) ||
            (department != null && !department.trim().isEmpty()) ||
            (search != null && !search.trim().isEmpty())) {
            profiles = tutorProfileRepository.searchTutors(subject, department, search);
        } else {
            profiles = tutorProfileRepository.findAll();
        }

        return profiles.stream().map(this::mapToTutorResponse).collect(Collectors.toList());
    }

    public TutorResponse getTutorById(Long id) {
        TutorProfile profile = tutorProfileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tutor profile not found with ID: " + id));
        return mapToTutorResponse(profile);
    }

    public TutorResponse createOrUpdateProfile(TutorProfileRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + request.getUserId()));

        if (!"TUTOR".equalsIgnoreCase(user.getRole())) {
            throw new BadRequestException("User role must be TUTOR to create a tutor profile");
        }

        TutorProfile profile = tutorProfileRepository.findByUserId(user.getId())
                .orElse(new TutorProfile());

        profile.setUser(user);
        profile.setBio(request.getBio());
        profile.setCgpa(request.getCgpa() != null ? request.getCgpa() : 8.0);
        profile.setIsAvailable(true);

        if (request.getSubjectIds() != null && !request.getSubjectIds().isEmpty()) {
            Set<Subject> subjects = new HashSet<>(subjectRepository.findAllById(request.getSubjectIds()));
            profile.setSubjects(subjects);
        }

        TutorProfile savedProfile = tutorProfileRepository.save(profile);
        return mapToTutorResponse(savedProfile);
    }

    public TutorResponse mapToTutorResponse(TutorProfile profile) {
        TutorResponse resp = new TutorResponse();
        resp.setId(profile.getId());
        resp.setUserId(profile.getUser().getId());
        resp.setFullName(profile.getUser().getFullName());
        resp.setEmail(profile.getUser().getEmail());
        resp.setDepartment(profile.getUser().getDepartment());
        resp.setYear(profile.getUser().getYear());
        resp.setBio(profile.getBio());
        resp.setCgpa(profile.getCgpa());
        resp.setIsAvailable(profile.getIsAvailable());
        resp.setSubjects(profile.getSubjects());

        // Dynamic Rating Calculation from Feedback (Rule 9)
        List<Feedback> feedbacks = feedbackRepository.findByTutorId(profile.getId());
        if (feedbacks != null && !feedbacks.isEmpty()) {
            double totalRating = feedbacks.stream().mapToInt(Feedback::getRating).sum();
            double avgRating = totalRating / feedbacks.size();
            resp.setAverageRating(Math.round(avgRating * 10.0) / 10.0);
            resp.setTotalReviews(feedbacks.size());
        } else {
            resp.setAverageRating(0.0);
            resp.setTotalReviews(0);
        }

        return resp;
    }
}
