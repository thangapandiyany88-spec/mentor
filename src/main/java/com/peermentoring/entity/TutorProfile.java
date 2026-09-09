package com.peermentoring.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * TutorProfile Entity representing senior peer tutor profiles.
 * 
 * Educational Explanation for Viva:
 * @OneToOne maps 1 User to 1 TutorProfile.
 * @ManyToMany maps Tutors to multiple Subjects via the join table "tutor_subjects".
 */
@Entity
@Table(name = "tutor_profiles")
public class TutorProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(nullable = false)
    private Double cgpa;

    @Column(name = "is_available")
    private Boolean isAvailable = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "tutor_subjects",
        joinColumns = @JoinColumn(name = "tutor_id"),
        inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private Set<Subject> subjects = new HashSet<>();

    public TutorProfile() {
    }

    public TutorProfile(Long id, User user, String bio, Double cgpa, Boolean isAvailable) {
        this.id = id;
        this.user = user;
        this.bio = bio;
        this.cgpa = cgpa;
        this.isAvailable = isAvailable;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Set<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<Subject> subjects) {
        this.subjects = subjects;
    }
}
