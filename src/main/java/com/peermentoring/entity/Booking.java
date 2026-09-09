package com.peermentoring.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Booking Entity representing mentoring session bookings.
 * Meeting Types: 'LIBRARY', 'ONLINE'
 * Status values: 'PENDING', 'CONFIRMED', 'COMPLETED', 'CANCELLED'
 */
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "junior_id", nullable = false)
    private User junior;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tutor_id", nullable = false)
    private TutorProfile tutor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "slot_id", nullable = false)
    private AvailabilitySlot slot;

    @Column(name = "booking_date", nullable = false)
    private LocalDate bookingDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "meeting_type", nullable = false, length = 20)
    private String meetingType; // LIBRARY, ONLINE

    @Column(nullable = false, length = 100)
    private String venue; // e.g. "Library Room 4" or "Online"

    @Column(name = "meeting_link", length = 255)
    private String meetingLink;

    @Column(nullable = false, length = 20)
    private String status = "CONFIRMED"; // PENDING, CONFIRMED, COMPLETED, CANCELLED

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Booking() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getJunior() {
        return junior;
    }

    public void setJunior(User junior) {
        this.junior = junior;
    }

    public TutorProfile getTutor() {
        return tutor;
    }

    public void setTutor(TutorProfile tutor) {
        this.tutor = tutor;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public AvailabilitySlot getSlot() {
        return slot;
    }

    public void setSlot(AvailabilitySlot slot) {
        this.slot = slot;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getMeetingType() {
        return meetingType;
    }

    public void setMeetingType(String meetingType) {
        this.meetingType = meetingType;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getMeetingLink() {
        return meetingLink;
    }

    public void setMeetingLink(String meetingLink) {
        this.meetingLink = meetingLink;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
