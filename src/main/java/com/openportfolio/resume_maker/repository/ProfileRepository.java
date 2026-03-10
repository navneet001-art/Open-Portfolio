package com.openportfolio.resume_maker.repository;

import com.openportfolio.resume_maker.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
        Optional<Profile> findByUserId(Long userId);

        @Modifying
        @Query("UPDATE Profile p SET p.headline = :headline, p.bio = :bio, p.phone = :phone, " +
                        "p.location = :location, p.linkedin = :linkedin, p.github = :github, p.website = :website, " +
                        "p.leetcode = :leetcode, p.codeforces = :codeforces, p.hackerrank = :hackerrank, " +
                        "p.codechef = :codechef, p.geeksforgeeks = :geeksforgeeks " +
                        "WHERE p.user.id = :userId")
        int updateFieldsByUserId(
                        @Param("userId") Long userId,
                        @Param("headline") String headline,
                        @Param("bio") String bio,
                        @Param("phone") String phone,
                        @Param("location") String location,
                        @Param("linkedin") String linkedin,
                        @Param("github") String github,
                        @Param("website") String website,
                        @Param("leetcode") String leetcode,
                        @Param("codeforces") String codeforces,
                        @Param("hackerrank") String hackerrank,
                        @Param("codechef") String codechef,
                        @Param("geeksforgeeks") String geeksforgeeks);

        @Modifying
        @Query("UPDATE Profile p SET p.profilePicUrl = :url WHERE p.user.id = :userId")
        int updateProfilePicByUserId(@Param("userId") Long userId, @Param("url") String url);
}
