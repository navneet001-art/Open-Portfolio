package com.openportfolio.resume_maker.controller;

import com.openportfolio.resume_maker.dto.ResumeData;
import com.openportfolio.resume_maker.service.ResumeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.openportfolio.resume_maker.model.Skill;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class ProfileController {

    private final ResumeService resumeService;

    public ProfileController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @GetMapping("/profile/{username}")
    public String publicProfile(@PathVariable String username, Model model) {
        try {
            ResumeData data = resumeService.getResumeData(username);

            // Group skills by type
            Map<String, List<Skill>> groupedSkills = Map.of();
            if (data.getSkills() != null) {
                groupedSkills = data.getSkills().stream()
                        .collect(Collectors.groupingBy(
                                skill -> skill.getType() != null && !skill.getType().isEmpty() ? skill.getType()
                                        : "Other"));
            }

            model.addAttribute("resume", data);
            model.addAttribute("groupedSkills", groupedSkills);
            return "profile";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "User '" + username + "' not found.");
            return "index";
        }
    }
}
