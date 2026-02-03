package com.medicare.repository;

import com.medicare.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    Page<Review> findByDoctorId(Long doctorId, Pageable pageable);
    
    Optional<Review> findByDoctorIdAndPatientId(Long doctorId, Long patientId);
    
    boolean existsByDoctorIdAndPatientId(Long doctorId, Long patientId);
    
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.doctor.id = :doctorId")
    Double getAverageRatingForDoctor(@Param("doctorId") Long doctorId);
    
    @Query("SELECT COUNT(r) FROM Review r WHERE r.doctor.id = :doctorId")
    long countReviewsForDoctor(@Param("doctorId") Long doctorId);
}
