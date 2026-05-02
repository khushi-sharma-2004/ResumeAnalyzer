package com.example.smartresume.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.smartresume.model.ResumeData;
import com.example.smartresume.model.User;
import com.example.smartresume.repository.ResumeRepository;
import com.example.smartresume.util.SkillMatcher;

@Service
public class ResumeService {

    @Autowired
    private ResumeRepository resumeRepository;

    public ResumeData getResumeData(Long id) {
        return resumeRepository.findById(id).orElse(null);
    }

    public List<ResumeData> getAllResumes() {
        return resumeRepository.findAll();
    }

    public List<ResumeData> getResumesByUser(User user) {
        return resumeRepository.findByUser(user);
    }

    public int getMatchingScore(String resumeSkills, String jobSkillsInput) {
        List<String> jobSkills = Arrays.asList(jobSkillsInput.toLowerCase().split(",\\s*"));
        List<String> matched = SkillMatcher.matchSkills(resumeSkills, jobSkills);
        return SkillMatcher.calculateScore(matched, jobSkills);
    }
}