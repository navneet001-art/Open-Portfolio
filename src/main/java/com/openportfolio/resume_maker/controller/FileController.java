package com.openportfolio.resume_maker.controller;

import com.openportfolio.resume_maker.model.Certification;
import com.openportfolio.resume_maker.model.Profile;
import com.openportfolio.resume_maker.repository.CertificationRepository;
import com.openportfolio.resume_maker.repository.ProfileRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final ProfileRepository profileRepository;
    private final CertificationRepository certificationRepository;

    public FileController(ProfileRepository profileRepository, CertificationRepository certificationRepository) {
        this.profileRepository = profileRepository;
        this.certificationRepository = certificationRepository;
    }

    @GetMapping("/profile-pic/{userId}")
    public ResponseEntity<byte[]> getProfilePic(@PathVariable Long userId) {
        Profile profile = profileRepository.findByUserId(userId).orElse(null);
        if (profile == null || profile.getProfilePicData() == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        String contentType = profile.getProfilePicType();
        if (contentType == null || contentType.isEmpty()) {
            contentType = MediaType.IMAGE_JPEG_VALUE;
        }
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setCacheControl("max-age=3600");

        return new ResponseEntity<>(profile.getProfilePicData(), headers, HttpStatus.OK);
    }

    @GetMapping("/certificate/{certId}")
    public ResponseEntity<byte[]> getCertificate(@PathVariable Long certId) {
        Certification cert = certificationRepository.findById(certId).orElse(null);
        if (cert == null || cert.getCertificateFileData() == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        String contentType = cert.getCertificateFileType();
        if (contentType == null || contentType.isEmpty()) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
        headers.setContentType(MediaType.parseMediaType(contentType));

        String fileName = cert.getCertificateFileName();
        if (fileName == null || fileName.isEmpty()) {
            fileName = "certificate";
        }
        headers.setContentDispositionFormData("attachment", fileName);

        return new ResponseEntity<>(cert.getCertificateFileData(), headers, HttpStatus.OK);
    }
}
