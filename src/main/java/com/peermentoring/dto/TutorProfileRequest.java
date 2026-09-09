package com.peermentoring.dto;

import java.util.List;

public class TutorProfileRequest {
    private Long userId;
    private String bio;
    private Double cgpa;
    private List<Long> subjectIds;

    public TutorProfileRequest() {
    }

    public TutorProfileRequest(Long userId, String bio, Double cgpa, List<Long> subjectIds) {
        this.userId = userId;
        this.bio = bio;
        this.cgpa = cgpa;
        this.subjectIds = subjectIds;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Double getCgpa() {
        return cgpa;
    }

    public void setCgpa(Double cgpa) {
        this.cgpa = cgpa;
    }

    public List<Long> getSubjectIds() {
        return subjectIds;
    }

    public void setSubjectIds(List<Long> subjectIds) {
        this.subjectIds = subjectIds;
    }
}
