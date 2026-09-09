package com.peermentoring.dto;

public class BookingRequest {
    private Long juniorId;
    private Long tutorId;
    private Long subjectId;
    private Long slotId;
    private String meetingType; // "LIBRARY" or "ONLINE"

    public BookingRequest() {
    }

    public BookingRequest(Long juniorId, Long tutorId, Long subjectId, Long slotId, String meetingType) {
        this.juniorId = juniorId;
        this.tutorId = tutorId;
        this.subjectId = subjectId;
        this.slotId = slotId;
        this.meetingType = meetingType;
    }

    public Long getJuniorId() {
        return juniorId;
    }

    public void setJuniorId(Long juniorId) {
        this.juniorId = juniorId;
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

    public Long getSlotId() {
        return slotId;
    }

    public void setSlotId(Long slotId) {
        this.slotId = slotId;
    }

    public String getMeetingType() {
        return meetingType;
    }

    public void setMeetingType(String meetingType) {
        this.meetingType = meetingType;
    }
}
