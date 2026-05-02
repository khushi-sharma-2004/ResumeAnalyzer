package com.example.smartresume.controller;

import java.io.IOException;

import org.apache.tika.exception.TikaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.smartresume.model.User;
import com.example.smartresume.repository.UserRepository;
import com.example.smartresume.service.ResumeParserService;

@Controller
public class ResumeUploadController {

    @Autowired
    private ResumeParserService parserService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/upload")
    public String uploadPage() {
        return "upload";
    }

    @PostMapping("/upload")
    public String handleUpload(@RequestParam("file") MultipartFile file, Model model) {
        if (file.isEmpty() || !file.getOriginalFilename().endsWith(".pdf")) {
            model.addAttribute("error", "Only PDF resumes are allowed.");
            return "upload";
        }

        try {
            // ✅ Get logged-in username
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();

            // ✅ Get user from DB
            User user = userRepository.findByUsername(username);

            // ✅ Parse and save with user
            parserService.parseAndSave(file, user);

            model.addAttribute("message", "Resume uploaded and analyzed successfully!");
        } catch (IOException | TikaException e) {
            model.addAttribute("error", "Failed to process the resume. Please try again.");
        }

        return "upload";
    }
}