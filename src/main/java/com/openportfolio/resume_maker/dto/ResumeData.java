package com.openportfolio.resume_maker.dto;

import com.openportfolio.resume_maker.model.*;
import java.util.List;

public class ResumeData {
    private User user;
    private Profile profile;
    private List<Experience> experiences;
    private List<Education> educations;
    private List<Skill> skills;
    private List<Project> projects;
    private List<com.openportfolio.resume_maker.model.Certification> certifications;

    public ResumeData() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public List<Experience> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<Experience> experiences) {
        this.experiences = experiences;
    }

    public List<Education> getEducations() {
        return educations;
    }

    public void setEducations(List<Education> educations) {
        this.educations = educations;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public List<com.openportfolio.resume_maker.model.Certification> getCertifications() {
        return certifications;
    }

    public void setCertifications(List<com.openportfolio.resume_maker.model.Certification> certifications) {
        this.certifications = certifications;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final ResumeData r = new ResumeData();

        public Builder user(User v) {
            r.user = v;
            return this;
        }

        public Builder profile(Profile v) {
            r.profile = v;
            return this;
        }

        public Builder experiences(List<Experience> v) {
            r.experiences = v;
            return this;
        }

        public Builder educations(List<Education> v) {
            r.educations = v;
            return this;
        }

        public Builder skills(List<Skill> v) {
            r.skills = v;
            return this;
        }

        public Builder projects(List<Project> v) {
            r.projects = v;
            return this;
        }

        public Builder certifications(List<com.openportfolio.resume_maker.model.Certification> v) {
            r.certifications = v;
            return this;
        }

        public ResumeData build() {
            return r;
        }
    }
}
