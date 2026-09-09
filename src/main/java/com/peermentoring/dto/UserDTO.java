package com.peermentoring.dto;

import com.peermentoring.entity.User;

public class UserDTO {
    private Long id;
    private String fullName;
    private String email;
    private String department;
    private Integer year;
    private String role;
    private Long tutorProfileId; // Present if user is a tutor

    public UserDTO() {
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        this.department = user.getDepartment();
        this.year = user.getYear();
        this.role = user.getRole();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getTutorProfileId() {
        return tutorProfileId;
    }

    public void setTutorProfileId(Long tutorProfileId) {
        this.tutorProfileId = tutorProfileId;
    }
}
