package com.openportfolio.resume_maker.service;

import com.openportfolio.resume_maker.model.*;
import com.openportfolio.resume_maker.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final ExperienceRepository experienceRepository;
    private final EducationRepository educationRepository;
    private final SkillRepository skillRepository;
    private final ProjectRepository projectRepository;
    private final CertificationRepository certificationRepository;
    private final com.openportfolio.resume_maker.repository.UserRepository userRepository;

    public ProfileService(ProfileRepository profileRepository,
            ExperienceRepository experienceRepository,
            EducationRepository educationRepository,
            SkillRepository skillRepository,
            ProjectRepository projectRepository,
            CertificationRepository certificationRepository,
            com.openportfolio.resume_maker.repository.UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.experienceRepository = experienceRepository;
        this.educationRepository = educationRepository;
        this.skillRepository = skillRepository;
        this.projectRepository = projectRepository;
        this.certificationRepository = certificationRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Profile saveProfile(Profile profile) {
        return profileRepository.save(profile);
    }

    @Transactional
    public Profile updateProfileFields(Long userId,
            String headline, String bio, String phone,
            String location, String linkedin, String github, String website,
            String leetcode, String codeforces, String hackerrank, String codechef, String geeksforgeeks) {
        int updated = profileRepository.updateFieldsByUserId(
                userId, headline, bio, phone, location, linkedin, github, website,
                leetcode, codeforces, hackerrank, codechef, geeksforgeeks);
        if (updated == 0) {
            // Profile doesn't exist yet — create it with the User entity
            com.openportfolio.resume_maker.model.User user = userRepository.findById(userId).orElseThrow();
            Profile profile = new Profile();
            profile.setUser(user);
            profile.setHeadline(headline);
            profile.setBio(bio);
            profile.setPhone(phone);
            profile.setLocation(location);
            profile.setLinkedin(linkedin);
            profile.setGithub(github);
            profile.setWebsite(website);
            profile.setLeetcode(leetcode);
            profile.setCodeforces(codeforces);
            profile.setHackerrank(hackerrank);
            profile.setCodechef(codechef);
            profile.setGeeksforgeeks(geeksforgeeks);
            return profileRepository.save(profile);
        }
        return profileRepository.findByUserId(userId).orElseThrow();
    }

    @Transactional
    public void deleteProfile(Long id) {
        profileRepository.deleteById(id);
    }

    /**
     * Saves the uploaded profile picture to the database.
     * Returns the public API URL path.
     */
    @Transactional
    public String saveProfilePic(Long userId, MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        byte[] bytes = file.getBytes();

        // Public URL that will be served by FileController
        String publicUrl = "/api/files/profile-pic/" + userId;

        // Ensure a profile row exists first, then update
        Profile profile = profileRepository.findByUserId(userId).orElseGet(() -> {
            com.openportfolio.resume_maker.model.User user = userRepository.findById(userId).orElseThrow();
            Profile p = new Profile();
            p.setUser(user);
            return p;
        });

        profile.setProfilePicUrl(publicUrl);
        profile.setProfilePicData(bytes);
        profile.setProfilePicType(contentType);
        profileRepository.save(profile);

        return publicUrl;
    }

    public Profile getProfileByUserId(Long userId) {
        return profileRepository.findByUserId(userId).orElse(null);
    }

    public List<Experience> getExperiences(Long userId) {
        return experienceRepository.findByUserId(userId);
    }

    @Transactional
    public void addExperience(Experience exp) {
        if (exp.isCurrent()) {
            List<Experience> userExps = experienceRepository.findByUserId(exp.getUser().getId());
            for (Experience userExp : userExps) {
                if (userExp.isCurrent()) {
                    userExp.setCurrent(false);
                    experienceRepository.save(userExp);
                }
            }
        }
        experienceRepository.save(exp);
    }

    @Transactional
    public void editExperience(Long id, Long userId, Experience updatedExp) {
        Experience existing = experienceRepository.findById(id).orElseThrow();
        if (!existing.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Not authorized to edit this experience");
        }

        if (updatedExp.isCurrent()) {
            List<Experience> userExps = experienceRepository.findByUserId(userId);
            for (Experience userExp : userExps) {
                if (userExp.isCurrent() && !userExp.getId().equals(id)) {
                    userExp.setCurrent(false);
                    experienceRepository.save(userExp);
                }
            }
        }

        existing.setJobTitle(updatedExp.getJobTitle());
        existing.setCompany(updatedExp.getCompany());
        existing.setLocation(updatedExp.getLocation());
        existing.setStartDate(updatedExp.getStartDate());
        existing.setEndDate(updatedExp.getEndDate());
        existing.setCurrent(updatedExp.isCurrent());
        existing.setDescription(updatedExp.getDescription());

        experienceRepository.save(existing);
    }

    @Transactional
    public void deleteExperience(Long id) {
        experienceRepository.deleteById(id);
    }

    public List<Education> getEducations(Long userId) {
        return educationRepository.findByUserId(userId);
    }

    @Transactional
    public void addEducation(Education edu) {
        educationRepository.save(edu);
    }

    @Transactional
    public void editEducation(Long id, Long userId, Education updatedEdu) {
        Education existing = educationRepository.findById(id).orElseThrow();
        if (!existing.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Not authorized to edit this education");
        }

        existing.setDegree(updatedEdu.getDegree());
        existing.setInstitution(updatedEdu.getInstitution());
        existing.setFieldOfStudy(updatedEdu.getFieldOfStudy());
        existing.setStartYear(updatedEdu.getStartYear());
        existing.setEndYear(updatedEdu.getEndYear());
        existing.setGrade(updatedEdu.getGrade());
        existing.setDescription(updatedEdu.getDescription());

        educationRepository.save(existing);
    }

    @Transactional
    public void deleteEducation(Long id) {
        educationRepository.deleteById(id);
    }

    public List<Skill> getSkills(Long userId) {
        return skillRepository.findByUserId(userId);
    }

    @Transactional
    public void addSkill(Skill skill) {
        skillRepository.save(skill);
    }

    @Transactional
    public void deleteSkill(Long id) {
        skillRepository.deleteById(id);
    }

    public List<Project> getProjects(Long userId) {
        return projectRepository.findByUserId(userId);
    }

    @Transactional
    public void addProject(Project project) {
        projectRepository.save(project);
    }

    @Transactional
    public void editProject(Long id, Long userId, Project updatedProject) {
        Project existing = projectRepository.findById(id).orElseThrow();
        if (!existing.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Not authorized to edit this project");
        }

        existing.setName(updatedProject.getName());
        existing.setTechStack(updatedProject.getTechStack());
        existing.setDescription(updatedProject.getDescription());
        existing.setProjectUrl(updatedProject.getProjectUrl());
        existing.setGithubUrl(updatedProject.getGithubUrl());

        projectRepository.save(existing);
    }

    @Transactional
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    public List<Certification> getCertifications(Long userId) {
        return certificationRepository.findByUserId(userId);
    }

    @Transactional
    public void addCertification(Certification cert) {
        certificationRepository.save(cert);
    }

    /**
     * Saves the uploaded certificate file to the database.
     * Returns the public API URL.
     */
    @Transactional
    public String saveCertificateFile(Long certId, Long userId, MultipartFile file) throws IOException {
        Certification cert = certificationRepository.findByIdAndUserId(certId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Certification not found"));

        String originalName = file.getOriginalFilename();
        String contentType = file.getContentType();
        byte[] bytes = file.getBytes();

        String publicUrl = "/api/files/certificate/" + certId;
        cert.setCertificateFileUrl(publicUrl);
        cert.setCertificateFileData(bytes);
        cert.setCertificateFileName(originalName);
        cert.setCertificateFileType(contentType);

        certificationRepository.save(cert);
        return publicUrl;
    }

    @Transactional
    public void deleteCertification(Long id) {
        certificationRepository.deleteById(id);
    }
}
