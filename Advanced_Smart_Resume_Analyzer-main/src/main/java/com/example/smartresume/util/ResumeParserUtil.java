package com.example.smartresume.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResumeParserUtil {

    public static String extractEmail(String text) {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        }
        return "Not found";
    }

    public static String extractPhone(String text) {
        Pattern pattern = Pattern.compile("\\b(\\+91[-\\s]?)?[0]?(91)?[789]\\d{9}\\b");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        }
        return "Not found";
    }

    public static String extractName(String text) {
        // Simple name assumption: first line of resume text
        String[] lines = text.split("\n");
        if (lines.length > 0) {
            return lines[0].trim();
        }
        return "Not found";
    }

    public static List<String> extractSkills(String text) {
        List<String> knownSkills = Arrays.asList("Java", "Python", "SQL", "HTML", "CSS", "JavaScript", "Spring", "React", "Node.js", "C++", "Machine Learning", "Data Analysis");
        List<String> foundSkills = new ArrayList<>();
        for (String skill : knownSkills) {
            if (text.toLowerCase().contains(skill.toLowerCase())) {
                foundSkills.add(skill);
            }
        }
        return foundSkills;
    }

    public static String extractEducation(String text) {
        Pattern pattern = Pattern.compile("(Bachelor|B\\.Tech|BSc|Master|M\\.Tech|MSc|PhD|Graduation)[^\\n]*", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group().trim();
        }
        return "Not found";
    }

    public static String extractExperience(String text) {
        Pattern pattern = Pattern.compile("([0-9]+\\s+years?\\s+of\\s+experience|experience\\s+of\\s+[0-9]+\\s+years)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group().trim();
        }
        return "Not found";
    }
}
