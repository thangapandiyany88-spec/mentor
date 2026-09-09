package com.peermentoring.service;

import com.peermentoring.dto.AvailabilityRequest;
import com.peermentoring.entity.AvailabilitySlot;
import com.peermentoring.entity.Subject;
import com.peermentoring.entity.TutorProfile;
import com.peermentoring.exception.BadRequestException;
import com.peermentoring.exception.ResourceNotFoundException;
import com.peermentoring.repository.AvailabilitySlotRepository;
import com.peermentoring.repository.SubjectRepository;
import com.peermentoring.repository.TutorProfileRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AvailabilityService {

    private final AvailabilitySlotRepository availabilitySlotRepository;
    private final TutorProfileRepository tutorProfileRepository;
    private final SubjectRepository subjectRepository;

    public AvailabilityService(AvailabilitySlotRepository availabilitySlotRepository,
                               TutorProfileRepository tutorProfileRepository,
                               SubjectRepository subjectRepository) {
        this.availabilitySlotRepository = availabilitySlotRepository;
        this.tutorProfileRepository = tutorProfileRepository;
        this.subjectRepository = subjectRepository;
    }

    public AvailabilitySlot addSlot(AvailabilityRequest request) {
        if (request.getTutorId() == null || request.getSubjectId() == null ||
            request.getSlotDate() == null || request.getStartTime() == null || request.getEndTime() == null) {
            throw new BadRequestException("All slot fields (tutorId, subjectId, slotDate, startTime, endTime) are required");
        }

        TutorProfile tutor = tutorProfileRepository.findById(request.getTutorId())
                .orElseThrow(() -> new ResourceNotFoundException("Tutor not found with ID: " + request.getTutorId()));

        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with ID: " + request.getSubjectId()));

        LocalDate date = LocalDate.parse(request.getSlotDate());
        LocalTime start = parseTime(request.getStartTime());
        LocalTime end = parseTime(request.getEndTime());

        if (end.isBefore(start) || end.equals(start)) {
            throw new BadRequestException("End time must be after start time");
        }

        AvailabilitySlot slot = new AvailabilitySlot();
        slot.setTutor(tutor);
        slot.setSubject(subject);
        slot.setSlotDate(date);
        slot.setStartTime(start);
        slot.setEndTime(end);
        slot.setStatus("AVAILABLE");

        return availabilitySlotRepository.save(slot);
    }

    public List<AvailabilitySlot> getSlotsByTutor(Long tutorId) {
        return availabilitySlotRepository.findByTutorId(tutorId);
    }

    public List<AvailabilitySlot> getAvailableSlotsByTutor(Long tutorId) {
        return availabilitySlotRepository.findByTutorIdAndStatus(tutorId, "AVAILABLE");
    }

    public void deleteSlot(Long slotId) {
        AvailabilitySlot slot = availabilitySlotRepository.findById(slotId)
                .orElseThrow(() -> new ResourceNotFoundException("Slot not found with ID: " + slotId));

        if ("BOOKED".equalsIgnoreCase(slot.getStatus())) {
            throw new BadRequestException("Cannot delete a slot that is already booked");
        }

        availabilitySlotRepository.delete(slot);
    }

    private LocalTime parseTime(String timeStr) {
        if (timeStr.length() == 5) { // "10:00"
            return LocalTime.parse(timeStr, DateTimeFormatter.ofPattern("HH:mm"));
        }
        return LocalTime.parse(timeStr);
    }
}
