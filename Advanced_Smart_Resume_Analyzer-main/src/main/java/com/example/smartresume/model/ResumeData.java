package com.example.smartresume.model;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class ResumeData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String phone;

    @ElementCollection
    private List<String> skills;

    @Column(columnDefinition = "TEXT")
    private String skillsText;

    private String education;
    private String experience;

    @Column(columnDefinition = "TEXT")
    private String resumeText;

    @Column(nullable = false)
    private boolean deleted = false;

    // ✅ NEW: Linked User
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;

    @CreationTimestamp
    private LocalDateTime uploadedAt;

    // Getters and Setters

    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public List<String> getSkills() { return skills; }
    public void setSkills(List<String> skills) {
        this.skills = skills;
        this.skillsText = String.join(", ", skills);
    }

    public String getSkillsText() { return skillsText; }
    public void setSkillsText(String skillsText) {
        this.skillsText = skillsText;
        this.skills = Arrays.stream(skillsText.split(","))
                            .map(String::trim)
                            .collect(Collectors.toList());
    }

    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }

    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }

    public String getResumeText() { return resumeText; }
    public void setResumeText(String resumeText) { this.resumeText = resumeText; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}