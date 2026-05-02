package com.example.smartresume.dto;

import java.util.List;

public class FeedbackResult {

    private List<String> resumeSkills;
    private List<String> jobRequiredSkills;
    private List<String> matchedSkills;
    private List<String> missingSkills;
    private int score;
    private String feedback;

    // Getters and Setters
    public List<String> getResumeSkills() {
        return resumeSkills;
    }

    public void setResumeSkills(List<String> resumeSkills) {
        this.resumeSkills = resumeSkills;
    }

    public List<String> getJobRequiredSkills() {
        return jobRequiredSkills;
    }

    public void setJobRequiredSkills(List<String> jobRequiredSkills) {
        this.jobRequiredSkills = jobRequiredSkills;
    }

    public List<String> getMatchedSkills() {
        return matchedSkills;
    }

    public void setMatchedSkills(List<String> matchedSkills) {
        this.matchedSkills = matchedSkills;
    }

    public List<String> getMissingSkills() {
        return missingSkills;
    }

    public void setMissingSkills(List<String> missingSkills) {
        this.missingSkills = missingSkills;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}