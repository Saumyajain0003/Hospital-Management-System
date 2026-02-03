package com.medicare.repository;

import com.medicare.model.MedicalRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    
    Page<MedicalRecord> findByPatientId(Long patientId, Pageable pageable);
    
    Optional<MedicalRecord> findByAppointmentId(Long appointmentId);
    
    @Query("SELECT mr FROM MedicalRecord mr WHERE mr.patient.id = :patientId AND " +
           "(LOWER(mr.diagnosis) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(mr.treatment) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<MedicalRecord> searchPatientRecords(@Param("patientId") Long patientId,
                                              @Param("search") String search,
                                              Pageable pageable);
    
    @Query("SELECT mr FROM MedicalRecord mr WHERE mr.createdAt BETWEEN :start AND :end")
    List<MedicalRecord> findRecordsBetweenDates(@Param("start") LocalDateTime start,
                                                 @Param("end") LocalDateTime end);
    
    long countByPatientId(Long patientId);
}
