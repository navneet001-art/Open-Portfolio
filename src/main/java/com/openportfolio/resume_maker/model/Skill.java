package com.openportfolio.resume_maker.model;

import jakarta.persistence.*;

@Entity
@Table(name = "skills")
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 80)
    private String name;

    @Column(length = 20)
    private String level;

    @Column(length = 50)
    private String type;

    public Skill() {
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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Skill s = new Skill();

        public Builder user(User v) {
            s.user = v;
            return this;
        }

        public Builder name(String v) {
            s.name = v;
            return this;
        }

        public Builder level(String v) {
            s.level = v;
            return this;
        }

        public Builder type(String v) {
            s.type = v;
            return this;
        }

        public Skill build() {
            return s;
        }
    }
}
