package com.openportfolio.resume_maker.controller;

import com.openportfolio.resume_maker.model.*;
import com.openportfolio.resume_maker.repository.UserRepository;
import com.openportfolio.resume_maker.service.ProfileService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final ProfileService profileService;
    private final UserRepository userRepository;

    public DashboardController(ProfileService profileService, UserRepository userRepository) {
        this.profileService = profileService;
        this.userRepository = userRepository;
    }

    private Long getSessionUserId(HttpSession session) {
        return (Long) session.getAttribute("userId");
    }

    private String redirectIfNotLoggedIn(HttpSession session) {
        if (getSessionUserId(session) == null)
            return "redirect:/login";
        return null;
    }

    @GetMapping
    public String dashboard(HttpSession session, Model model) {
        String redirect = redirectIfNotLoggedIn(session);
        if (redirect != null)
            return redirect;
        Long userId = getSessionUserId(session);
        User user = userRepository.findById(userId).orElseThrow();
        model.addAttribute("user", user);
        model.addAttribute("profile", profileService.getProfileByUserId(userId));
        model.addAttribute("experiences", profileService.getExperiences(userId));
        model.addAttribute("educations", profileService.getEducations(userId));
        model.addAttribute("skills", profileService.getSkills(userId));
        model.addAttribute("projects", profileService.getProjects(userId));
        model.addAttribute("certifications", profileService.getCertifications(userId));
        return "dashboard";
    }

    @PostMapping("/profile")
    public String updateProfile(HttpSession session,
            @RequestParam(required = false) String headline,
            @RequestParam(required = false) String bio,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String linkedin,
            @RequestParam(required = false) String github,
            @RequestParam(required = false) String website,
            @RequestParam(required = false) String leetcode,
            @RequestParam(required = false) String codeforces,
            @RequestParam(required = false) String hackerrank,
            @RequestParam(required = false) String codechef,
            @RequestParam(required = false) String geeksforgeeks,
            RedirectAttributes ra) {
        String redirect = redirectIfNotLoggedIn(session);
        if (redirect != null)
            return redirect;
        Long userId = getSessionUserId(session);
        profileService.updateProfileFields(userId, headline, bio, phone, location, linkedin, github, website,
                leetcode, codeforces, hackerrank, codechef, geeksforgeeks);
        ra.addFlashAttribute("successTab", "profile");
        return "redirect:/dashboard";
    }

    /** AJAX endpoint — returns JSON so the page doesn't reload */
    @PostMapping("/profile/save")
    @ResponseBody
    public java.util.Map<String, Object> saveProfileAjax(HttpSession session,
            @RequestParam(required = false) String headline,
            @RequestParam(required = false) String bio,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String linkedin,
            @RequestParam(required = false) String github,
            @RequestParam(required = false) String website,
            @RequestParam(required = false) String leetcode,
            @RequestParam(required = false) String codeforces,
            @RequestParam(required = false) String hackerrank,
            @RequestParam(required = false) String codechef,
            @RequestParam(required = false) String geeksforgeeks) {
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        if (getSessionUserId(session) == null) {
            result.put("success", false);
            result.put("error", "Not authenticated");
            return result;
        }
        Long userId = getSessionUserId(session);
        profileService.updateProfileFields(userId, headline, bio, phone, location, linkedin, github, website,
                leetcode, codeforces, hackerrank, codechef, geeksforgeeks);
        result.put("success", true);
        return result;
    }

    /** AJAX endpoint — uploads profile picture, returns { success, picUrl } */
    @PostMapping("/profile/upload-pic")
    @ResponseBody
    public java.util.Map<String, Object> uploadProfilePic(HttpSession session,
            @RequestParam("file") MultipartFile file) {
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        if (getSessionUserId(session) == null) {
            result.put("success", false);
            result.put("error", "Not authenticated");
            return result;
        }
        try {
            String url = profileService.saveProfilePic(getSessionUserId(session), file);
            result.put("success", true);
            result.put("picUrl", url);
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        return result;
    }

    @PostMapping("/experience/add")
    public String addExperience(HttpSession session,
            @RequestParam String jobTitle, @RequestParam String company,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false, defaultValue = "false") boolean current,
            @RequestParam(required = false) String description) {
        String redirect = redirectIfNotLoggedIn(session);
        if (redirect != null)
            return redirect;
        User user = userRepository.findById(getSessionUserId(session)).orElseThrow();
        Experience exp = Experience.builder().user(user).jobTitle(jobTitle).company(company)
                .location(location).startDate(startDate).endDate(endDate)
                .current(current).description(description).build();
        profileService.addExperience(exp);
        return "redirect:/dashboard#experience";
    }

    @PostMapping("/experience/edit/{id}")
    public String editExperience(@PathVariable Long id, HttpSession session,
            @RequestParam String jobTitle, @RequestParam String company,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false, defaultValue = "false") boolean current,
            @RequestParam(required = false) String description) {
        String redirect = redirectIfNotLoggedIn(session);
        if (redirect != null)
            return redirect;

        Long userId = getSessionUserId(session);
        Experience updatedExp = Experience.builder()
                .jobTitle(jobTitle).company(company)
                .location(location).startDate(startDate).endDate(endDate)
                .current(current).description(description).build();

        profileService.editExperience(id, userId, updatedExp);
        return "redirect:/dashboard#experience";
    }

    @PostMapping("/experience/delete/{id}")
    public String deleteExperience(@PathVariable Long id, HttpSession session) {
        if (redirectIfNotLoggedIn(session) != null)
            return "redirect:/login";
        profileService.deleteExperience(id);
        return "redirect:/dashboard#experience";
    }

    @PostMapping("/education/add")
    public String addEducation(HttpSession session,
            @RequestParam String degree, @RequestParam String institution,
            @RequestParam(required = false) String fieldOfStudy,
            @RequestParam(required = false) String startYear,
            @RequestParam(required = false) String endYear,
            @RequestParam(required = false) String grade,
            @RequestParam(required = false) String description) {
        String redirect = redirectIfNotLoggedIn(session);
        if (redirect != null)
            return redirect;
        User user = userRepository.findById(getSessionUserId(session)).orElseThrow();
        Education edu = Education.builder().user(user).degree(degree).institution(institution)
                .fieldOfStudy(fieldOfStudy).startYear(startYear).endYear(endYear)
                .grade(grade).description(description).build();
        profileService.addEducation(edu);
        return "redirect:/dashboard#education";
    }

    @PostMapping("/education/edit/{id}")
    public String editEducation(@PathVariable Long id, HttpSession session,
            @RequestParam String degree, @RequestParam String institution,
            @RequestParam(required = false) String fieldOfStudy,
            @RequestParam(required = false) String startYear,
            @RequestParam(required = false) String endYear,
            @RequestParam(required = false) String grade,
            @RequestParam(required = false) String description) {
        String redirect = redirectIfNotLoggedIn(session);
        if (redirect != null)
            return redirect;

        Long userId = getSessionUserId(session);
        Education updatedEdu = Education.builder()
                .degree(degree).institution(institution).fieldOfStudy(fieldOfStudy)
                .startYear(startYear).endYear(endYear).grade(grade).description(description).build();

        profileService.editEducation(id, userId, updatedEdu);
        return "redirect:/dashboard#education";
    }

    @PostMapping("/education/delete/{id}")
    public String deleteEducation(@PathVariable Long id, HttpSession session) {
        if (redirectIfNotLoggedIn(session) != null)
            return "redirect:/login";
        profileService.deleteEducation(id);
        return "redirect:/dashboard#education";
    }

    @PostMapping("/skill/add")
    public String addSkill(HttpSession session,
            @RequestParam String name,
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String type) {
        String redirect = redirectIfNotLoggedIn(session);
        if (redirect != null)
            return redirect;
        User user = userRepository.findById(getSessionUserId(session)).orElseThrow();
        profileService.addSkill(Skill.builder().user(user).name(name).level(level).type(type).build());
        return "redirect:/dashboard#skills";
    }

    @PostMapping("/skill/delete/{id}")
    public String deleteSkill(@PathVariable Long id, HttpSession session) {
        if (redirectIfNotLoggedIn(session) != null)
            return "redirect:/login";
        profileService.deleteSkill(id);
        return "redirect:/dashboard#skills";
    }

    @PostMapping("/project/add")
    public String addProject(HttpSession session,
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String techStack,
            @RequestParam(required = false) String projectUrl,
            @RequestParam(required = false) String githubUrl) {
        String redirect = redirectIfNotLoggedIn(session);
        if (redirect != null)
            return redirect;
        User user = userRepository.findById(getSessionUserId(session)).orElseThrow();
        Project project = Project.builder().user(user).name(name).description(description)
                .techStack(techStack).projectUrl(projectUrl).githubUrl(githubUrl).build();
        profileService.addProject(project);
        return "redirect:/dashboard#projects";
    }

    @PostMapping("/project/edit/{id}")
    public String editProject(@PathVariable Long id, HttpSession session,
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String techStack,
            @RequestParam(required = false) String projectUrl,
            @RequestParam(required = false) String githubUrl) {
        String redirect = redirectIfNotLoggedIn(session);
        if (redirect != null)
            return redirect;

        Long userId = getSessionUserId(session);
        Project updatedProject = Project.builder()
                .name(name)
                .description(description)
                .techStack(techStack)
                .projectUrl(projectUrl)
                .githubUrl(githubUrl)
                .build();

        profileService.editProject(id, userId, updatedProject);
        return "redirect:/dashboard#projects";
    }

    @PostMapping("/project/delete/{id}")
    public String deleteProject(@PathVariable Long id, HttpSession session) {
        if (redirectIfNotLoggedIn(session) != null)
            return "redirect:/login";
        profileService.deleteProject(id);
        return "redirect:/dashboard#projects";
    }

    @PostMapping("/certification/add")
    public String addCertification(HttpSession session,
            @RequestParam String name, @RequestParam String issuer,
            @RequestParam(required = false) String issueDate,
            @RequestParam(required = false) String expiryDate,
            @RequestParam(required = false) String credentialId,
            @RequestParam(required = false) String credentialUrl) {
        String redirect = redirectIfNotLoggedIn(session);
        if (redirect != null)
            return redirect;
        User user = userRepository.findById(getSessionUserId(session)).orElseThrow();
        com.openportfolio.resume_maker.model.Certification cert = com.openportfolio.resume_maker.model.Certification
                .builder()
                .user(user).name(name).issuer(issuer)
                .issueDate(issueDate).expiryDate(expiryDate)
                .credentialId(credentialId).credentialUrl(credentialUrl)
                .build();
        profileService.addCertification(cert);
        return "redirect:/dashboard#certifications";
    }

    /** AJAX — creates a certification and returns { success, certId } */
    @PostMapping("/certification/add-json")
    @ResponseBody
    public java.util.Map<String, Object> addCertificationAjax(HttpSession session,
            @RequestParam String name, @RequestParam String issuer,
            @RequestParam(required = false) String issueDate,
            @RequestParam(required = false) String expiryDate,
            @RequestParam(required = false) String credentialId,
            @RequestParam(required = false) String credentialUrl) {
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        if (getSessionUserId(session) == null) {
            result.put("success", false);
            result.put("error", "Not authenticated");
            return result;
        }
        User user = userRepository.findById(getSessionUserId(session)).orElseThrow();
        com.openportfolio.resume_maker.model.Certification cert = com.openportfolio.resume_maker.model.Certification
                .builder()
                .user(user).name(name).issuer(issuer)
                .issueDate(issueDate).expiryDate(expiryDate)
                .credentialId(credentialId).credentialUrl(credentialUrl)
                .build();
        profileService.addCertification(cert);
        result.put("success", true);
        result.put("certId", cert.getId());
        return result;
    }

    @PostMapping("/certification/delete/{id}")
    public String deleteCertification(@PathVariable Long id, HttpSession session) {
        if (redirectIfNotLoggedIn(session) != null)
            return "redirect:/login";
        profileService.deleteCertification(id);
        return "redirect:/dashboard#certifications";
    }

    /**
     * AJAX — uploads a file for an existing certification, returns { success,
     * fileUrl }
     */
    @PostMapping("/certification/upload-file")
    @ResponseBody
    public java.util.Map<String, Object> uploadCertificateFile(HttpSession session,
            @RequestParam("certId") Long certId,
            @RequestParam("file") MultipartFile file) {
        java.util.Map<String, Object> result = new java.util.HashMap<>();
        if (getSessionUserId(session) == null) {
            result.put("success", false);
            result.put("error", "Not authenticated");
            return result;
        }
        try {
            String url = profileService.saveCertificateFile(certId, getSessionUserId(session), file);
            result.put("success", true);
            result.put("fileUrl", url);
        } catch (Exception e) {
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        return result;
    }

    @PostMapping("/account/delete")
    public String deleteAccount(HttpSession session) {
        if (redirectIfNotLoggedIn(session) != null)
            return "redirect:/login";
        Long userId = getSessionUserId(session);
        // Delete child records first (FK constraint order)
        profileService.getExperiences(userId).forEach(e -> profileService.deleteExperience(e.getId()));
        profileService.getEducations(userId).forEach(e -> profileService.deleteEducation(e.getId()));
        profileService.getSkills(userId).forEach(s -> profileService.deleteSkill(s.getId()));
        profileService.getProjects(userId).forEach(p -> profileService.deleteProject(p.getId()));
        profileService.getCertifications(userId).forEach(c -> profileService.deleteCertification(c.getId()));
        com.openportfolio.resume_maker.model.Profile profile = profileService.getProfileByUserId(userId);
        if (profile != null)
            profileService.deleteProfile(profile.getId());
        userRepository.deleteById(userId);
        session.invalidate();
        return "redirect:/?deleted=true";
    }
}
