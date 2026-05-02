package com.example.smartresume.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.smartresume.model.JobDescription;
import com.example.smartresume.repository.JobDescriptionRepository;

@Controller
public class JobDescriptionController {

    @Autowired
    private JobDescriptionRepository jobRepo;

    @GetMapping("/admin/job/add")
    public String showForm(Model model) {
        model.addAttribute("job", new JobDescription());
        return "add_job";
    }

    @PostMapping("/admin/job/save")
    public String saveJob(@ModelAttribute JobDescription job,
                          @RequestParam("skills") String skills) {
        job.setRequiredSkills(Arrays.asList(skills.split(",")));
        jobRepo.save(job);
        return "redirect:/admin/dashboard";
    }
}
