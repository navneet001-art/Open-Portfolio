package com.openportfolio.resume_maker.controller;

import com.openportfolio.resume_maker.dto.ResumeData;
import com.openportfolio.resume_maker.service.ResumeService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import com.openportfolio.resume_maker.model.Skill;
import com.openportfolio.resume_maker.service.LatexService;

@Controller
public class ResumeController {

    private final ResumeService resumeService;
    private final TemplateEngine templateEngine;
    private final LatexService latexService;

    public ResumeController(ResumeService resumeService, TemplateEngine templateEngine, LatexService latexService) {
        this.resumeService = resumeService;
        this.templateEngine = templateEngine;
        this.latexService = latexService;
    }

    @GetMapping("/resume/{username}")
    public String templateSelector(@PathVariable String username, Model model) {
        try {
            ResumeData data = resumeService.getResumeData(username);
            model.addAttribute("resume", data);
            return "resume-select";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "User not found.");
            return "index";
        }
    }

    @GetMapping("/resume/{username}/download")
    public ResponseEntity<byte[]> downloadResume(
            @PathVariable String username,
            @RequestParam(defaultValue = "1") String template) {
        try {
            ResumeData data = resumeService.getResumeData(username);

            // Sanitize resume data for LaTeX
            sanitizeResumeDataForLatex(data);

            // Group skills by type
            Map<String, List<Skill>> groupedSkills = Map.of();
            if (data.getSkills() != null) {
                groupedSkills = data.getSkills().stream()
                        .collect(Collectors.groupingBy(
                                skill -> skill.getType() != null && !skill.getType().isEmpty() ? skill.getType()
                                        : "Other"));
            }

            Context ctx = new Context();
            ctx.setVariable("resume", data);
            ctx.setVariable("groupedSkills", groupedSkills);

            // Handle LaTeX-based template (Template folders: template-1, template-2,
            // template-3)
            String templateName = "template-" + template;
            if ("2".equals(template)) {
                templateName = "template-2"; // Keep consistency for current folder naming
            }

            // Extract profile picture for LaTeX
            Map<String, byte[]> additionalFiles = new HashMap<>();
            if (data.getProfile() != null && data.getProfile().getProfilePicData() != null) {
                String extension = "png";
                String picType = data.getProfile().getProfilePicType();
                if (picType != null && (picType.contains("jpeg") || picType.contains("jpg"))) {
                    extension = "jpg";
                }
                additionalFiles.put("profile_pic." + extension, data.getProfile().getProfilePicData());
                ctx.setVariable("profilePicExt", extension);
            }

            // Identify the source directory
            Path sourceDir = Paths.get("src/main/resources/templates/resume/" + templateName).toAbsolutePath();

            // Process the cv.tex file inside the corresponding template folder
            String tex = templateEngine.process("resume/" + templateName + "/cv", ctx);

            byte[] pdfBytes = latexService.compileDirectory(sourceDir, "cv.tex", tex, additionalFiles);

            String filename = username + "-resume-" + templateName + ".pdf";
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        } catch (Exception e) {
            e.printStackTrace(); // Log the error
            return ResponseEntity.internalServerError().build();
        }
    }

    private void sanitizeResumeDataForLatex(ResumeData data) {
        if (data.getUser() != null) {
            data.getUser().setFullName(latexService.escapeLatex(data.getUser().getFullName()));
            data.getUser().setEmail(latexService.escapeLatex(data.getUser().getEmail()));
        }
        if (data.getProfile() != null) {
            data.getProfile().setHeadline(latexService.escapeLatex(data.getProfile().getHeadline()));
            data.getProfile().setBio(latexService.escapeLatex(data.getProfile().getBio()));
            data.getProfile().setLocation(latexService.escapeLatex(data.getProfile().getLocation()));
            data.getProfile().setPhone(latexService.escapeLatex(data.getProfile().getPhone()));
            data.getProfile().setGithub(latexService.escapeLatex(data.getProfile().getGithub()));
            data.getProfile().setLinkedin(latexService.escapeLatex(data.getProfile().getLinkedin()));
            data.getProfile().setWebsite(latexService.escapeLatex(data.getProfile().getWebsite()));
            data.getProfile().setLeetcode(latexService.escapeLatex(data.getProfile().getLeetcode()));
            data.getProfile().setCodeforces(latexService.escapeLatex(data.getProfile().getCodeforces()));
            data.getProfile().setHackerrank(latexService.escapeLatex(data.getProfile().getHackerrank()));
            data.getProfile().setCodechef(latexService.escapeLatex(data.getProfile().getCodechef()));
            data.getProfile().setGeeksforgeeks(latexService.escapeLatex(data.getProfile().getGeeksforgeeks()));
        }
        if (data.getExperiences() != null) {
            data.getExperiences().forEach(exp -> {
                exp.setJobTitle(latexService.escapeLatex(exp.getJobTitle()));
                exp.setCompany(latexService.escapeLatex(exp.getCompany()));
                exp.setLocation(latexService.escapeLatex(exp.getLocation()));
                exp.setDescription(latexService.escapeLatex(exp.getDescription()));
                exp.setStartDate(latexService.escapeLatex(exp.getStartDate()));
                exp.setEndDate(latexService.escapeLatex(exp.getEndDate()));
            });
        }
        if (data.getProjects() != null) {
            data.getProjects().forEach(proj -> {
                proj.setName(latexService.escapeLatex(proj.getName()));
                proj.setTechStack(latexService.escapeLatex(proj.getTechStack()));
                proj.setDescription(latexService.escapeLatex(proj.getDescription()));
                proj.setProjectUrl(latexService.escapeLatex(proj.getProjectUrl()));
            });
        }
        if (data.getEducations() != null) {
            data.getEducations().forEach(edu -> {
                edu.setInstitution(latexService.escapeLatex(edu.getInstitution()));
                edu.setDegree(latexService.escapeLatex(edu.getDegree()));
                edu.setFieldOfStudy(latexService.escapeLatex(edu.getFieldOfStudy()));
                edu.setDescription(latexService.escapeLatex(edu.getDescription()));
                edu.setGrade(latexService.escapeLatex(edu.getGrade()));
            });
        }
        if (data.getSkills() != null) {
            data.getSkills().forEach(skill -> {
                skill.setName(latexService.escapeLatex(skill.getName()));
                skill.setLevel(latexService.escapeLatex(skill.getLevel()));
                skill.setType(latexService.escapeLatex(skill.getType()));
            });
        }
        if (data.getCertifications() != null) {
            data.getCertifications().forEach(cert -> {
                cert.setName(latexService.escapeLatex(cert.getName()));
                cert.setIssuer(latexService.escapeLatex(cert.getIssuer()));
                cert.setIssueDate(latexService.escapeLatex(cert.getIssueDate()));
                cert.setCredentialId(latexService.escapeLatex(cert.getCredentialId()));
            });
        }
    }
}
