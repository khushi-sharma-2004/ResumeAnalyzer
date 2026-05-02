package com.example.smartresume.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.smartresume.model.FeedbackData;
import com.example.smartresume.model.ResumeData;
import com.example.smartresume.repository.FeedbackRepository;
import com.example.smartresume.repository.ResumeRepository;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AdminController {

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

    // ✅ Admin Dashboard – Show All Resumes
    @GetMapping("/admin/dashboard")
    public String showDashboard(Model model) {
        List<ResumeData> resumes = resumeRepository.findAll();
        model.addAttribute("resumes", resumes);
        return "admin_dashboard";
    }

    // ✅ Delete Resume + Associated Feedbacks
    @GetMapping("/admin/delete/{id}")
    public String deleteResume(@PathVariable Long id) {
        // Step 1: Delete all feedback entries linked to this resume
        List<FeedbackData> feedbacks = feedbackRepository.findByResumeId(id);
        if (feedbacks != null && !feedbacks.isEmpty()) {
            feedbackRepository.deleteAll(feedbacks);
        }

        // Step 2: Delete the resume
        resumeRepository.deleteById(id);
        return "redirect:/admin/dashboard";
    }

    // ✅ Export All Resumes to CSV
    @GetMapping("/admin/export/csv")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=resumes.csv");

        List<ResumeData> resumes = resumeRepository.findAll();
        CSVPrinter csvPrinter = new CSVPrinter(response.getWriter(),
                CSVFormat.DEFAULT.withHeader("ID", "Name", "Email", "Skills", "Uploaded At"));

        for (ResumeData resume : resumes) {
            csvPrinter.printRecord(
                    resume.getId(),
                    resume.getName(),
                    resume.getEmail(),
                    resume.getSkillsText(),
                    resume.getUploadedAt()
            );
        }
        csvPrinter.flush();
    }

    // ✅ Export All Resumes to PDF
    @GetMapping("/admin/export/pdf")
    public void exportToPDF(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=resumes.pdf");

        List<ResumeData> resumes = resumeRepository.findAll();
        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        for (ResumeData resume : resumes) {
            document.add(new Paragraph("ID: " + resume.getId()));
            document.add(new Paragraph("Name: " + resume.getName()));
            document.add(new Paragraph("Email: " + resume.getEmail()));
            document.add(new Paragraph("Skills: " + resume.getSkillsText()));
            document.add(new Paragraph("Uploaded At: " + resume.getUploadedAt()));
            document.add(new Paragraph("\n--------------------------------------------\n"));
        }

        document.close();
    }

    // ✅ Search Resumes by Name, Email, or Skills
    @GetMapping("/admin/search")
    public String searchResumes(@RequestParam("keyword") String keyword, Model model) {
        List<ResumeData> results = resumeRepository
                .findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrSkillsTextContainingIgnoreCase(
                        keyword, keyword, keyword);
        model.addAttribute("resumes", results);
        model.addAttribute("searchKeyword", keyword);
        return "admin_dashboard";
    }

    // ✅ Show Resume Update Form
    @GetMapping("/admin/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        Optional<ResumeData> resume = resumeRepository.findById(id);
        if (resume.isPresent()) {
            model.addAttribute("resume", resume.get());
            return "resume_update_form";
        } else {
            return "redirect:/admin/dashboard";
        }
    }

    // ✅ Submit Resume Update
    @PostMapping("/admin/update/{id}")
    public String updateResume(@PathVariable Long id, @ModelAttribute ResumeData updatedResume) {
        Optional<ResumeData> resumeOptional = resumeRepository.findById(id);
        if (resumeOptional.isPresent()) {
            ResumeData existing = resumeOptional.get();
            existing.setName(updatedResume.getName());
            existing.setEmail(updatedResume.getEmail());
            existing.setPhone(updatedResume.getPhone());
            existing.setSkillsText(updatedResume.getSkillsText());
            resumeRepository.save(existing);
        }
        return "redirect:/admin/dashboard";
    }
}