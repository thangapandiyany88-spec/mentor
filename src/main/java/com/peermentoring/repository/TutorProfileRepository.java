package com.peermentoring.repository;

import com.peermentoring.entity.TutorProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TutorProfileRepository extends JpaRepository<TutorProfile, Long> {
    Optional<TutorProfile> findByUserId(Long userId);

    @Query("SELECT DISTINCT t FROM TutorProfile t JOIN t.subjects s WHERE " +
           "(:subject IS NULL OR LOWER(s.subjectName) LIKE LOWER(CONCAT('%', :subject, '%')) OR LOWER(s.subjectCode) LIKE LOWER(CONCAT('%', :subject, '%'))) AND " +
           "(:department IS NULL OR LOWER(t.user.department) LIKE LOWER(CONCAT('%', :department, '%'))) AND " +
           "(:search IS NULL OR LOWER(t.user.fullName) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(s.subjectName) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<TutorProfile> searchTutors(@Param("subject") String subject,
                                     @Param("department") String department,
                                     @Param("search") String search);
}
