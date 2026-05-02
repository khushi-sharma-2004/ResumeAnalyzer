package com.example.smartresume.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.smartresume.dto.AnalyticsDTO;
import com.example.smartresume.model.FeedbackData;
import com.example.smartresume.repository.FeedbackRepository;

@Controller
public class AnalyticsController {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @GetMapping("/admin/analytics")
    public String showAnalytics(Model model) {
        List<FeedbackData> allFeedback = feedbackRepository.findAll();

        AnalyticsDTO analytics = new AnalyticsDTO();
        analytics.setTotalResumes(allFeedback.size());

        double avgScore = allFeedback.stream()
                .mapToDouble(FeedbackData::getScore)
                .average()
                .orElse(0.0);
        analytics.setAverageScore(Math.round(avgScore * 100.0) / 100.0);

        Map<String, Integer> matchedSkillFreq = new HashMap<>();
        Map<String, Integer> missingSkillFreq = new HashMap<>();

        for (FeedbackData feedback : allFeedback) {
            if (feedback.getMatchedSkills() != null) {
                for (String skill : feedback.getMatchedSkills()) {
                    String clean = skill.trim().toLowerCase();
                    if (!clean.isEmpty()) {
                        matchedSkillFreq.put(clean, matchedSkillFreq.getOrDefault(clean, 0) + 1);
                    }
                }
            }

            if (feedback.getMissingSkills() != null) {
                for (String skill : feedback.getMissingSkills()) {
                    String clean = skill.trim().toLowerCase();
                    if (!clean.isEmpty()) {
                        missingSkillFreq.put(clean, missingSkillFreq.getOrDefault(clean, 0) + 1);
                    }
                }
            }
        }

        analytics.setTopMatchedSkills(getTopEntries(matchedSkillFreq, 5));
        analytics.setTopMissingSkills(getTopEntries(missingSkillFreq, 5));

        model.addAttribute("analytics", analytics);
        return "admin_analytics";
    }

    private Map<String, Integer> getTopEntries(Map<String, Integer> map, int limit) {
        return map.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(limit)
                .collect(
                        LinkedHashMap::new,
                        (m, e) -> m.put(e.getKey(), e.getValue()),
                        Map::putAll
                );
    }
}