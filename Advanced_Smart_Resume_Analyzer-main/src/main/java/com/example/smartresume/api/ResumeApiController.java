package com.example.smartresume.api;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.tika.exception.TikaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.smartresume.dto.FeedbackResult;
import com.example.smartresume.model.FeedbackData;
import com.example.smartresume.model.JobDescription;
import com.example.smartresume.model.ResumeData;
import com.example.smartresume.model.User;
import com.example.smartresume.repository.JobDescriptionRepository;
import com.example.smartresume.repository.ResumeRepository;
import com.example.smartresume.repository.UserRepository;
import com.example.smartresume.service.FeedbackService;
import com.example.smartresume.service.ResumeParserService;

@RestController
@RequestMapping("/api")
public class ResumeApiController {

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private ResumeParserService parserService;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private JobDescriptionRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    // ✅ Upload Resume
    @PostMapping("/upload")
    public ResponseEntity<String> uploadResume(@RequestParam("file") MultipartFile file) throws IOException, TikaException {
        if (file.isEmpty() || !file.getOriginalFilename().endsWith(".pdf")) {
            return ResponseEntity.badRequest().body("Only PDF files allowed");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(auth.getName());

        parserService.parseAndSave(file, user);
        return ResponseEntity.ok("Resume uploaded successfully");
    }

    // ✅ Get all resumes (admin only)
    @GetMapping("/resumes")
    public List<ResumeData> getAllResumes() {
        return resumeRepository.findAll();
    }

    // ✅ Generate feedback (resumeId + jobId)
    @PostMapping("/feedback")
    public ResponseEntity<FeedbackResult> getFeedback(@RequestParam Long resumeId, @RequestParam Long jobId) {
        ResumeData resume = resumeRepository.findById(resumeId).orElse(null);
        JobDescription job = jobRepository.findById(jobId).orElse(null);

        if (resume == null || job == null) {
            return ResponseEntity.notFound().build();
        }

        FeedbackResult result = feedbackService.generateFeedback(
            resume.getSkills(),
            job.getRequiredSkills(),
            resumeId,
            jobId
        );
        return ResponseEntity.ok(result);
    }

    // ✅ Get feedback by resume ID
    @GetMapping("/feedback/{resumeId}")
    public ResponseEntity<List<FeedbackResult>> getFeedbackByResume(@PathVariable Long resumeId) {
        List<FeedbackData> feedbackDataList = feedbackService.getFeedbackByResumeId(resumeId);

        if (feedbackDataList == null || feedbackDataList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<FeedbackResult> feedbackList = feedbackDataList.stream().map(data -> {
            FeedbackResult result = new FeedbackResult();
            result.setMatchedSkills(data.getMatchedSkills());
            result.setMissingSkills(data.getMissingSkills());
            result.setScore((int) Math.round(data.getScore())); // ✅ safe cast
            result.setFeedback(data.getFeedback());
            result.setResumeSkills(null);
            result.setJobRequiredSkills(null); // ✅ spelling is correct here
            return result;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(feedbackList);
    }
}