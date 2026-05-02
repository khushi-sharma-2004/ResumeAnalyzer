package com.example.smartresume.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.smartresume.model.ResumeData;
import com.example.smartresume.model.User;
import com.example.smartresume.repository.ResumeRepository;

@Service
public class ResumeParserService {

    @Autowired
    private ResumeRepository resumeRepository;

    public void parseAndSave(MultipartFile file, User user) throws IOException, TikaException {
        Tika tika = new Tika();
        String text = tika.parseToString(file.getInputStream());

        String name = extractName(text);
        String email = extractEmail(text);
        String phone = extractPhone(text);
        List<String> skills = extractSkills(text);

        ResumeData data = new ResumeData();
        data.setName(name);
        data.setEmail(email);
        data.setPhone(phone);
        data.setSkills(skills);
        data.setSkillsText(String.join(", ", skills));
        data.setResumeText(text);

        // ✅ NEW: Associate resume with logged-in user
        data.setUser(user);

        resumeRepository.save(data);
    }

    private String extractName(String text) {
        String[] lines = text.split("\\r?\\n");
        for (String line : lines) {
            if (!line.trim().isEmpty() && line.trim().split(" ").length <= 5) {
                return line.trim();
            }
        }
        return "Unknown";
    }

    private String extractEmail(String text) {
        Matcher matcher = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-z]{2,6}").matcher(text);
        return matcher.find() ? matcher.group() : "unknown@example.com";
    }

    private String extractPhone(String text) {
        Matcher matcher = Pattern.compile("\\d{10}").matcher(text);
        return matcher.find() ? matcher.group() : "0000000000";
    }

    private List<String> extractSkills(String text) {
        String[] possibleSkills = {"Java", "Spring Boot", "SQL", "Python", "C++", "MySQL", "HTML", "CSS"};
        String lowerText = text.toLowerCase();

        return Arrays.stream(possibleSkills)
                .filter(skill -> lowerText.contains(skill.toLowerCase()))
                .toList();
    }
}