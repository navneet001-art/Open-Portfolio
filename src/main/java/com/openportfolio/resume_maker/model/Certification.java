package com.openportfolio.resume_maker.model;

import jakarta.persistence.*;

@Entity
@Table(name = "certifications")
public class Certification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 150)
    private String issuer;

    @Column(length = 20)
    private String issueDate;

    @Column(length = 20)
    private String expiryDate;

    @Column(length = 100)
    private String credentialId;

    @Column(length = 500)
    private String credentialUrl;

    @Column(length = 500)
    private String certificateFileUrl;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] certificateFileData;

    @Column(length = 255)
    private String certificateFileName;

    @Column(length = 100)
    private String certificateFileType;

    public Certification() {
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

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCredentialId() {
        return credentialId;
    }

    public void setCredentialId(String credentialId) {
        this.credentialId = credentialId;
    }

    public String getCredentialUrl() {
        return credentialUrl;
    }

    public void setCredentialUrl(String credentialUrl) {
        this.credentialUrl = credentialUrl;
    }

    public String getCertificateFileUrl() {
        return certificateFileUrl;
    }

    public void setCertificateFileUrl(String certificateFileUrl) {
        this.certificateFileUrl = certificateFileUrl;
    }

    public byte[] getCertificateFileData() {
        return certificateFileData;
    }

    public void setCertificateFileData(byte[] certificateFileData) {
        this.certificateFileData = certificateFileData;
    }

    public String getCertificateFileName() {
        return certificateFileName;
    }

    public void setCertificateFileName(String certificateFileName) {
        this.certificateFileName = certificateFileName;
    }

    public String getCertificateFileType() {
        return certificateFileType;
    }

    public void setCertificateFileType(String certificateFileType) {
        this.certificateFileType = certificateFileType;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Certification c = new Certification();

        public Builder user(User v) {
            c.user = v;
            return this;
        }

        public Builder name(String v) {
            c.name = v;
            return this;
        }

        public Builder issuer(String v) {
            c.issuer = v;
            return this;
        }

        public Builder issueDate(String v) {
            c.issueDate = v;
            return this;
        }

        public Builder expiryDate(String v) {
            c.expiryDate = v;
            return this;
        }

        public Builder credentialId(String v) {
            c.credentialId = v;
            return this;
        }

        public Builder credentialUrl(String v) {
            c.credentialUrl = v;
            return this;
        }

        public Builder certificateFileUrl(String v) {
            c.certificateFileUrl = v;
            return this;
        }

        public Certification build() {
            return c;
        }
    }
}
