package com.openportfolio.resume_maker.model;

import jakarta.persistence.*;

@Entity
@Table(name = "experiences")
public class Experience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 100)
    private String jobTitle;

    @Column(nullable = false, length = 100)
    private String company;

    @Column(length = 100)
    private String location;

    @Column(length = 20)
    private String startDate;

    @Column(length = 20)
    private String endDate;

    private boolean current;

    @Column(columnDefinition = "TEXT")
    private String description;

    public Experience() {
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

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Experience e = new Experience();

        public Builder user(User v) {
            e.user = v;
            return this;
        }

        public Builder jobTitle(String v) {
            e.jobTitle = v;
            return this;
        }

        public Builder company(String v) {
            e.company = v;
            return this;
        }

        public Builder location(String v) {
            e.location = v;
            return this;
        }

        public Builder startDate(String v) {
            e.startDate = v;
            return this;
        }

        public Builder endDate(String v) {
            e.endDate = v;
            return this;
        }

        public Builder current(boolean v) {
            e.current = v;
            return this;
        }

        public Builder description(String v) {
            e.description = v;
            return this;
        }

        public Experience build() {
            return e;
        }
    }
}
