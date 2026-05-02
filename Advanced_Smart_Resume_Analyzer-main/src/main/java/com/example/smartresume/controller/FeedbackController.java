package com.example.smartresume.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.smartresume.dto.FeedbackResult;
import com.example.smartresume.model.FeedbackData;
import com.example.smartresume.model.JobDescription;
import com.example.smartresume.model.ResumeData;
import com.example.smartresume.repository.FeedbackRepository;
import com.example.smartresume.repository.JobDescriptionRepository;
import com.example.smartresume.repository.ResumeRepository;
import com.example.smartresume.service.FeedbackService;
import com.lowagie.text.Document;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class FeedbackController {

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private JobDescriptionRepository jobDescriptionRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private FeedbackService feedbackService;

    // ✅ Generate feedback and save (without duplication)
    @GetMapping("/feedback/{resumeId}/{jobId}")
    public String generateFeedback(@PathVariable Long resumeId,
                                   @PathVariable Long jobId,
                                   Model model) {
        ResumeData resume = resumeRepository.findById(resumeId).orElse(null);
        JobDescription job = jobDescriptionRepository.findById(jobId).orElse(null);

        if (resume == null || job == null || resume.getSkills() == null || job.getRequiredSkills() == null) {
            model.addAttribute("error", "Resume or Job or Skills not found.");
            return "error";
        }

        FeedbackResult result = feedbackService.generateFeedback(
                resume.getSkills(),
                job.getRequiredSkills(),
                resumeId,
                jobId
        );

        model.addAttribute("result", result);
        model.addAttribute("resume", resume);
        model.addAttribute("job", job);

        return "feedbackResult";
    }

    // ✅ Show all feedback for a resume
    @GetMapping("/my-feedback/{resumeId}")
    public String viewFeedbackForResume(@PathVariable Long resumeId, Model model) {
        List<FeedbackData> feedbackList = feedbackService.getFeedbackByResumeId(resumeId);
        model.addAttribute("feedbackList", feedbackList);
        return "feedbackResult";
    }

    // ✅ PDF Download
    @GetMapping("/feedback/download/{feedbackId}")
    public void downloadFeedbackPdf(@PathVariable Long feedbackId, HttpServletResponse response) throws IOException {
        FeedbackData data = feedbackRepository.findById(feedbackId).orElse(null);
        if (data == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=feedback_" + feedbackId + ".pdf");

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        document.add(new Paragraph("Resume Feedback Report", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
        document.add(new Paragraph(" "));
        document.add(new Paragraph("Resume ID: " + data.getResumeId()));
        document.add(new Paragraph("Job ID: " + data.getJobId()));
        document.add(new Paragraph("Score: " + data.getScore() + "%"));
        document.add(new Paragraph("Feedback: " + data.getFeedback()));
        document.add(new Paragraph("Matched Skills: " + String.join(", ", data.getMatchedSkills())));
        document.add(new Paragraph("Missing Skills: " + String.join(", ", data.getMissingSkills())));

        document.close();
    }
}