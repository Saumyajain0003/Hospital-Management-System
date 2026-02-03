package com.medicare.repository;

import com.medicare.model.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {
    
    List<DoctorSchedule> findByDoctorId(Long doctorId);
    
    List<DoctorSchedule> findByDoctorIdAndIsAvailable(Long doctorId, Boolean isAvailable);
    
    Optional<DoctorSchedule> findByDoctorIdAndDayOfWeek(Long doctorId, DayOfWeek dayOfWeek);
    
    @Query("SELECT ds FROM DoctorSchedule ds WHERE ds.doctor.id = :doctorId AND " +
           "ds.dayOfWeek = :dayOfWeek AND ds.isAvailable = true")
    Optional<DoctorSchedule> findAvailableSchedule(@Param("doctorId") Long doctorId,
                                                    @Param("dayOfWeek") DayOfWeek dayOfWeek);
}
