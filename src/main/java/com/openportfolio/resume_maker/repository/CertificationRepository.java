package com.openportfolio.resume_maker.repository;

import com.openportfolio.resume_maker.model.Certification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CertificationRepository extends JpaRepository<Certification, Long> {
    List<Certification> findByUserId(Long userId);

    java.util.Optional<Certification> findByIdAndUserId(Long id, Long userId);
}
