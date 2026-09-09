package com.peermentoring.dto;

public class AvailabilityRequest {
    private Long tutorId;
    private Long subjectId;
    private String slotDate; // YYYY-MM-DD
    private String startTime; // HH:mm or HH:mm:ss
    private String endTime;   // HH:mm or HH:mm:ss

    public AvailabilityRequest() {
    }

    public AvailabilityRequest(Long tutorId, Long subjectId, String slotDate, String startTime, String endTime) {
        this.tutorId = tutorId;
        this.subjectId = subjectId;
        this.slotDate = slotDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getTutorId() {
        return tutorId;
    }

    public void setTutorId(Long tutorId) {
        this.tutorId = tutorId;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getSlotDate() {
        return slotDate;
    }

    public void setSlotDate(String slotDate) {
        this.slotDate = slotDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
