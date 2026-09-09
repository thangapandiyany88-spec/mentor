package com.peermentoring.dto;

public class RegisterRequest {
    private String fullName;
    private String email;
    private String password;
    private String department;
    private Integer year;
    private String role; // "JUNIOR" or "TUTOR"

    public RegisterRequest() {
    }

    public RegisterRequest(String fullName, String email, String password, String department, Integer year, String role) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.department = department;
        this.year = year;
        this.role = role;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
