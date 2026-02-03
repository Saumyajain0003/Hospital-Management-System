package com.medicare.repository;

import com.medicare.model.Doctor;
import com.medicare.model.enums.Specialization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    
    Optional<Doctor> findByUserId(Long userId);
    
    Page<Doctor> findByIsActive(Boolean isActive, Pageable pageable);
    
    Page<Doctor> findBySpecialization(Specialization specialization, Pageable pageable);
    
    Page<Doctor> findBySpecializationAndIsAvailable(Specialization specialization, 
                                                     Boolean isAvailable, 
                                                     Pageable pageable);
    
    @Query("SELECT d FROM Doctor d WHERE d.isActive = true AND d.isAvailable = true AND " +
           "(LOWER(d.user.fullName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(d.specialization) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Doctor> searchAvailableDoctors(@Param("search") String search, Pageable pageable);
    
    @Query("SELECT d FROM Doctor d WHERE d.consultationFee BETWEEN :minFee AND :maxFee")
    Page<Doctor> findByConsultationFeeBetween(@Param("minFee") BigDecimal minFee, 
                                                @Param("maxFee") BigDecimal maxFee, 
                                                Pageable pageable);
    
    @Query("SELECT d FROM Doctor d WHERE d.rating >= :minRating ORDER BY d.rating DESC")
    List<Doctor> findTopRatedDoctors(@Param("minRating") Double minRating, Pageable pageable);
    
    long countBySpecialization(Specialization specialization);
    
    long countByIsAvailable(Boolean isAvailable);
    
    @Query("SELECT AVG(d.consultationFee) FROM Doctor d")
    BigDecimal getAverageConsultationFee();
}
