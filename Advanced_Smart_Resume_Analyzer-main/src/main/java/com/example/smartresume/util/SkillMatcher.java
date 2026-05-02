package com.example.smartresume.util;

import java.util.ArrayList;
import java.util.List;

public class SkillMatcher {

    public static List<String> matchSkills(String resumeSkills, List<String> jobSkills) {
        List<String> matched = new ArrayList<>();
        if (resumeSkills == null || resumeSkills.isEmpty()) return matched;

        String[] resumeSkillList = resumeSkills.toLowerCase().split(",\\s*");

        for (String skill : resumeSkillList) {
            if (jobSkills.contains(skill.trim().toLowerCase())) {
                matched.add(skill.trim());
            }
        }
        return matched;
    }

    public static int calculateScore(List<String> matchedSkills, List<String> jobSkills) {
        if (jobSkills.isEmpty()) return 0;
        return (int) (((double) matchedSkills.size() / jobSkills.size()) * 100);
    }
}
