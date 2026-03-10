package com.openportfolio.resume_maker.model;

import jakarta.persistence.*;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 200)
    private String techStack;

    @Column(length = 300)
    private String projectUrl;

    @Column(length = 300)
    private String githubUrl;

    public Project() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTechStack() {
        return techStack;
    }

    public void setTechStack(String techStack) {
        this.techStack = techStack;
    }

    public String getProjectUrl() {
        return projectUrl;
    }

    public void setProjectUrl(String projectUrl) {
        this.projectUrl = projectUrl;
    }

    public String getGithubUrl() {
        return githubUrl;
    }

    public void setGithubUrl(String githubUrl) {
        this.githubUrl = githubUrl;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Project p = new Project();

        public Builder user(User v) {
            p.user = v;
            return this;
        }

        public Builder name(String v) {
            p.name = v;
            return this;
        }

        public Builder description(String v) {
            p.description = v;
            return this;
        }

        public Builder techStack(String v) {
            p.techStack = v;
            return this;
        }

        public Builder projectUrl(String v) {
            p.projectUrl = v;
            return this;
        }

        public Builder githubUrl(String v) {
            p.githubUrl = v;
            return this;
        }

        public Project build() {
            return p;
        }
    }
}
