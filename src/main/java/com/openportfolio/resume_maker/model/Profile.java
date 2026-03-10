package com.openportfolio.resume_maker.model;

import jakarta.persistence.*;

@Entity
@Table(name = "profiles")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 150)
    private String headline;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(length = 20)
    private String phone;

    @Column(length = 100)
    private String location;

    @Column(length = 500)
    private String linkedin;

    @Column(length = 500)
    private String github;

    @Column(length = 500)
    private String website;

    @Column(length = 300)
    private String profilePicUrl;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] profilePicData;

    @Column(length = 50)
    private String profilePicType;

    // Competitive coding profile URLs
    @Column(length = 500)
    private String leetcode;

    @Column(length = 500)
    private String codeforces;

    @Column(length = 500)
    private String hackerrank;

    @Column(length = 500)
    private String codechef;

    @Column(length = 500)
    private String geeksforgeeks;

    public Profile() {
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

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getGithub() {
        return github;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public byte[] getProfilePicData() {
        return profilePicData;
    }

    public void setProfilePicData(byte[] profilePicData) {
        this.profilePicData = profilePicData;
    }

    public String getProfilePicType() {
        return profilePicType;
    }

    public void setProfilePicType(String profilePicType) {
        this.profilePicType = profilePicType;
    }

    public String getLeetcode() {
        return leetcode;
    }

    public void setLeetcode(String leetcode) {
        this.leetcode = leetcode;
    }

    public String getCodeforces() {
        return codeforces;
    }

    public void setCodeforces(String codeforces) {
        this.codeforces = codeforces;
    }

    public String getHackerrank() {
        return hackerrank;
    }

    public void setHackerrank(String hackerrank) {
        this.hackerrank = hackerrank;
    }

    public String getCodechef() {
        return codechef;
    }

    public void setCodechef(String codechef) {
        this.codechef = codechef;
    }

    public String getGeeksforgeeks() {
        return geeksforgeeks;
    }

    public void setGeeksforgeeks(String geeksforgeeks) {
        this.geeksforgeeks = geeksforgeeks;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Profile p = new Profile();

        public Builder user(User v) {
            p.user = v;
            return this;
        }

        public Builder headline(String v) {
            p.headline = v;
            return this;
        }

        public Builder bio(String v) {
            p.bio = v;
            return this;
        }

        public Builder phone(String v) {
            p.phone = v;
            return this;
        }

        public Builder location(String v) {
            p.location = v;
            return this;
        }

        public Builder linkedin(String v) {
            p.linkedin = v;
            return this;
        }

        public Builder github(String v) {
            p.github = v;
            return this;
        }

        public Builder website(String v) {
            p.website = v;
            return this;
        }

        public Builder leetcode(String v) {
            p.leetcode = v;
            return this;
        }

        public Builder codeforces(String v) {
            p.codeforces = v;
            return this;
        }

        public Builder hackerrank(String v) {
            p.hackerrank = v;
            return this;
        }

        public Builder codechef(String v) {
            p.codechef = v;
            return this;
        }

        public Builder geeksforgeeks(String v) {
            p.geeksforgeeks = v;
            return this;
        }

        public Profile build() {
            return p;
        }
    }
}
