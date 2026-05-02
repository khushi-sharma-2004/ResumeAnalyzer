package com.example.smartresume.service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.smartresume.dto.AnalyticsDTO;
import com.example.smartresume.model.FeedbackData;
import com.example.smartresume.repository.FeedbackRepository;
import com.example.smartresume.repository.ResumeRepository;

@Service
public class AnalyticsService {

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    public AnalyticsDTO getAnalytics() {
        List<FeedbackData> allFeedback = feedbackRepository.findAll();
        int totalResumes = allFeedback.size();
        double totalScore = 0.0;

        // Frequency maps
        Map<String, Integer> matchedFreq = new HashMap<>();
        Map<String, Integer> missingFreq = new HashMap<>();

        for (FeedbackData feedback : allFeedback) {
            totalScore += feedback.getScore();

            // ✅ Parse matched skills
            if (feedback.getMatchedSkills() != null) {
                for (String skill : feedback.getMatchedSkills()) {
                    String normalized = cleanSkill(skill);
                    if (!normalized.isEmpty()) {
                        matchedFreq.put(normalized, matchedFreq.getOrDefault(normalized, 0) + 1);
                    }
                }
            }

            // ✅ Parse missing skills
            if (feedback.getMissingSkills() != null) {
                for (String skill : feedback.getMissingSkills()) {
                    String normalized = cleanSkill(skill);
                    if (!normalized.isEmpty()) {
                        missingFreq.put(normalized, missingFreq.getOrDefault(normalized, 0) + 1);
                    }
                }
            }
        }

        double avgScore = totalResumes > 0 ? (totalScore / totalResumes) : 0.0;

        return new AnalyticsDTO(
                totalResumes,
                Math.round(avgScore * 100.0) / 100.0,
                sortAndLimit(matchedFreq, 5),
                sortAndLimit(missingFreq, 5)
        );
    }

    // ✅ Clean and normalize skill strings
    private String cleanSkill(String skill) {
        if (skill == null) return "";
        return skill.trim().toLowerCase();
    }

    // ✅ Sort by frequency, return top N
    private Map<String, Integer> sortAndLimit(Map<String, Integer> input, int limit) {
        return input.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder()))
                .limit(limit)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }
}