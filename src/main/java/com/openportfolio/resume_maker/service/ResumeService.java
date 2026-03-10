package com.openportfolio.resume_maker.service;

import com.openportfolio.resume_maker.dto.ResumeData;
import com.openportfolio.resume_maker.model.User;
import org.springframework.stereotype.Service;

@Service
public class ResumeService {

    private final UserService userService;
    private final ProfileService profileService;

    public ResumeService(UserService userService, ProfileService profileService) {
        this.userService = userService;
        this.profileService = profileService;
    }

    public ResumeData getResumeData(String username) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
        return ResumeData.builder()
                .user(user)
                .profile(profileService.getProfileByUserId(user.getId()))
                .experiences(profileService.getExperiences(user.getId()))
                .educations(profileService.getEducations(user.getId()))
                .skills(profileService.getSkills(user.getId()))
                .projects(profileService.getProjects(user.getId()))
                .certifications(profileService.getCertifications(user.getId()))
                .build();
    }
}
