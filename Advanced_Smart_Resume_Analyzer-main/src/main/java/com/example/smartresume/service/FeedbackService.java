package com.example.smartresume.service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.smartresume.dto.FeedbackResult;
import com.example.smartresume.dto.SkillMatchResult;
import com.example.smartresume.model.ResumeData;
import com.example.smartresume.model.JobDescription;
import com.example.smartresume.model.FeedbackData;
import com.example.smartresume.repository.ResumeRepository;
import com.example.smartresume.repository.JobDescriptionRepository;
import com.example.smartresume.repository.FeedbackRepository;


@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    private static final Map<String, Integer> skillWeights = Map.ofEntries(
            Map.entry("java", 4),
            Map.entry("spring boot", 3),
            Map.entry("sql", 3),
            Map.entry("docker", 2),
            Map.entry("aws", 3),
            Map.entry("python", 2),
            Map.entry("html", 1),
            Map.entry("css", 1)
    );

    public FeedbackResult generateFeedback(List<String> resumeSkills,
                                           List<String> requiredSkills,
                                           Long resumeId,
                                           Long jobId) {
        FeedbackResult result = computeMatchResult(resumeSkills, requiredSkills);

        FeedbackData feedbackData = feedbackRepository
                .findByResumeIdAndJobId(resumeId, jobId)
                .orElse(new FeedbackData());

        feedbackData.setResumeId(resumeId);
        feedbackData.setJobId(jobId);
        feedbackData.setScore(result.getScore());
        feedbackData.setFeedback(result.getFeedback());
        feedbackData.setMatchedSkills(result.getMatchedSkills());
        feedbackData.setMissingSkills(result.getMissingSkills());

        feedbackRepository.save(feedbackData);

        return result;
    }

    public FeedbackResult computeMatchResult(List<String> resumeSkills, List<String> requiredSkills) {
        if (resumeSkills == null) resumeSkills = new ArrayList<>();
        if (requiredSkills == null) requiredSkills = new ArrayList<>();

        Set<String> resumeSet = resumeSkills.stream()
                .map(s -> s.trim().toLowerCase())
                .collect(Collectors.toSet());

        Set<String> requiredSet = requiredSkills.stream()
                .map(s -> s.trim().toLowerCase())
                .collect(Collectors.toSet());

        Set<String> matched = new HashSet<>(resumeSet);
        matched.retainAll(requiredSet);

        Set<String> missing = new HashSet<>(requiredSet);
        missing.removeAll(resumeSet);

        int totalWeight = requiredSet.stream()
                .mapToInt(skill -> skillWeights.getOrDefault(skill, 1))
                .sum();

        int matchedWeight = matched.stream()
                .mapToInt(skill -> skillWeights.getOrDefault(skill, 1))
                .sum();

        int score = (totalWeight > 0) ? (int) (((double) matchedWeight / totalWeight) * 100) : 0;

        String feedback;
        if (score >= 80) {
            feedback = "Excellent match!";
        } else if (score >= 60) {
            feedback = "Good match. Consider learning: " + String.join(", ", missing);
        } else if (score >= 40) {
            feedback = "Fair match. Improve these: " + String.join(", ", missing);
        } else {
            feedback = "Low match. Learn key skills: " + String.join(", ", missing);
        }

        FeedbackResult result = new FeedbackResult();
        result.setResumeSkills(new ArrayList<>(resumeSet));
        result.setJobRequiredSkills(new ArrayList<>(requiredSet));
        result.setMatchedSkills(new ArrayList<>(matched));
        result.setMissingSkills(new ArrayList<>(missing));
        result.setScore(score);
        result.setFeedback(feedback);

        return result;
    }

    public List<FeedbackData> getFeedbackByResumeId(Long resumeId) {
        return feedbackRepository.findByResumeId(resumeId);
    }
}