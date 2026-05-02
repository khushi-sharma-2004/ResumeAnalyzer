package com.example.smartresume.dto;

import java.util.Map;

public class AnalyticsDTO {
    private int totalResumes;
    private double averageScore;
    private Map<String, Integer> topMatchedSkills;
    private Map<String, Integer> topMissingSkills;

    public AnalyticsDTO() {} // No-arg constructor for Spring/Thymeleaf

    public AnalyticsDTO(int totalResumes, double averageScore,
                        Map<String, Integer> topMatchedSkills,
                        Map<String, Integer> topMissingSkills) {
        this.totalResumes = totalResumes;
        this.averageScore = averageScore;
        this.topMatchedSkills = topMatchedSkills;
        this.topMissingSkills = topMissingSkills;
    }

    public int getTotalResumes() {
        return totalResumes;
    }

    public void setTotalResumes(int totalResumes) {
        this.totalResumes = totalResumes;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }

    public Map<String, Integer> getTopMatchedSkills() {
        return topMatchedSkills;
    }

    public void setTopMatchedSkills(Map<String, Integer> topMatchedSkills) {
        this.topMatchedSkills = topMatchedSkills;
    }

    public Map<String, Integer> getTopMissingSkills() {
        return topMissingSkills;
    }

    public void setTopMissingSkills(Map<String, Integer> topMissingSkills) {
        this.topMissingSkills = topMissingSkills;
    }
}