package com.peermentoring.service;

import com.peermentoring.dto.LoginRequest;
import com.peermentoring.dto.RegisterRequest;
import com.peermentoring.dto.UserDTO;
import com.peermentoring.entity.TutorProfile;
import com.peermentoring.entity.User;
import com.peermentoring.exception.BadRequestException;
import com.peermentoring.exception.ResourceNotFoundException;
import com.peermentoring.repository.TutorProfileRepository;
import com.peermentoring.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * AuthService handles User Registration and Authentication logic.
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final TutorProfileRepository tutorProfileRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UserRepository userRepository, TutorProfileRepository tutorProfileRepository) {
        this.userRepository = userRepository;
        this.tutorProfileRepository = tutorProfileRepository;
    }

    public UserDTO registerUser(RegisterRequest request) {
        // Validation
        if (request.getFullName() == null || request.getFullName().trim().isEmpty()) {
            throw new BadRequestException("Full name cannot be empty");
        }
        if (request.getEmail() == null || !request.getEmail().contains("@")) {
            throw new BadRequestException("Valid college email is required");
        }
        if (request.getPassword() == null || request.getPassword().length() < 6) {
            throw new BadRequestException("Password must be at least 6 characters long");
        }
        if (request.getDepartment() == null || request.getDepartment().trim().isEmpty()) {
            throw new BadRequestException("Department is required");
        }
        if (request.getYear() == null || request.getYear() < 1 || request.getYear() > 4) {
            throw new BadRequestException("Valid academic year (1-4) is required");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("User with this email already exists");
        }

        String role = (request.getRole() != null && request.getRole().equalsIgnoreCase("TUTOR")) ? "TUTOR" : "JUNIOR";

        User user = new User();
        user.setFullName(request.getFullName().trim());
        user.setEmail(request.getEmail().trim().toLowerCase());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setDepartment(request.getDepartment().trim());
        user.setYear(request.getYear());
        user.setRole(role);

        User savedUser = userRepository.save(user);

        UserDTO userDTO = new UserDTO(savedUser);

        // If registered as TUTOR, create an initial TutorProfile
        if ("TUTOR".equals(role)) {
            TutorProfile tutorProfile = new TutorProfile();
            tutorProfile.setUser(savedUser);
            tutorProfile.setBio("Senior student ready to mentor juniors in core subjects.");
            tutorProfile.setCgpa(8.0); // default initial CGPA
            tutorProfile.setIsAvailable(true);
            TutorProfile savedProfile = tutorProfileRepository.save(tutorProfile);
            userDTO.setTutorProfileId(savedProfile.getId());
        }

        return userDTO;
    }

    public UserDTO loginUser(LoginRequest request) {
        if (request.getEmail() == null || request.getPassword() == null) {
            throw new BadRequestException("Email and password are required");
        }

        User user = userRepository.findByEmail(request.getEmail().trim().toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid email or password"));

        // Match BCrypt password or fallback for raw text passwords in dev sample data
        boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword()) ||
                request.getPassword().equals(user.getPassword());

        if (!passwordMatches) {
            throw new BadRequestException("Invalid email or password");
        }

        UserDTO userDTO = new UserDTO(user);
        if ("TUTOR".equalsIgnoreCase(user.getRole())) {
            Optional<TutorProfile> profileOpt = tutorProfileRepository.findByUserId(user.getId());
            profileOpt.ifPresent(profile -> userDTO.setTutorProfileId(profile.getId()));
        }

        return userDTO;
    }
}
