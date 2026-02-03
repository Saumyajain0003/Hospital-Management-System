package com.medicare.repository;

import com.medicare.model.Appointment;
import com.medicare.model.enums.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    
    Page<Appointment> findByPatientId(Long patientId, Pageable pageable);
    
    Page<Appointment> findByDoctorId(Long doctorId, Pageable pageable);
    
    Page<Appointment> findByStatus(AppointmentStatus status, Pageable pageable);
    
    @Query("SELECT a FROM Appointment a WHERE a.patient.id = :patientId AND a.status = :status")
    Page<Appointment> findByPatientIdAndStatus(@Param("patientId") Long patientId, 
                                                @Param("status") AppointmentStatus status, 
                                                Pageable pageable);
    
    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.status = :status")
    Page<Appointment> findByDoctorIdAndStatus(@Param("doctorId") Long doctorId, 
                                               @Param("status") AppointmentStatus status, 
                                               Pageable pageable);
    
    @Query("SELECT a FROM Appointment a WHERE a.appointmentDateTime BETWEEN :start AND :end")
    List<Appointment> findAppointmentsBetweenDates(@Param("start") LocalDateTime start, 
                                                     @Param("end") LocalDateTime end);
    
    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND " +
           "a.appointmentDateTime BETWEEN :start AND :end AND " +
           "a.status NOT IN ('CANCELLED', 'NO_SHOW')")
    List<Appointment> findDoctorAppointmentsInDateRange(@Param("doctorId") Long doctorId,
                                                         @Param("start") LocalDateTime start,
                                                         @Param("end") LocalDateTime end);
    
    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.status = :status AND " +
           "a.appointmentDateTime BETWEEN :start AND :end")
    long countByStatusAndDateRange(@Param("status") AppointmentStatus status,
                                    @Param("start") LocalDateTime start,
                                    @Param("end") LocalDateTime end);
    
    @Query("SELECT a FROM Appointment a WHERE a.patient.id = :patientId AND " +
           "a.appointmentDateTime < CURRENT_TIMESTAMP ORDER BY a.appointmentDateTime DESC")
    List<Appointment> findPastAppointmentsByPatient(@Param("patientId") Long patientId, 
                                                     Pageable pageable);
    
    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND " +
           "a.appointmentDateTime > CURRENT_TIMESTAMP AND " +
           "a.status = 'SCHEDULED' ORDER BY a.appointmentDateTime")
    List<Appointment> findUpcomingAppointmentsByDoctor(@Param("doctorId") Long doctorId);
    
    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.doctor.id = :doctorId")
    long countAppointmentsByDoctor(@Param("doctorId") Long doctorId);
    
    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.patient.id = :patientId")
    long countAppointmentsByPatient(@Param("patientId") Long patientId);
    
    @Query("SELECT a FROM Appointment a WHERE a.isPaid = false AND a.status = 'SCHEDULED'")
    List<Appointment> findUnpaidAppointments();
    
    boolean existsByDoctorIdAndAppointmentDateTimeAndStatusNot(Long doctorId, 
                                                                LocalDateTime dateTime, 
                                                                AppointmentStatus status);
}
