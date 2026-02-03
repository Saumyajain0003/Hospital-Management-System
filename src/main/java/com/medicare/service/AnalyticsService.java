package com.medicare.service;

import com.medicare.model.enums.AppointmentStatus;
import com.medicare.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AnalyticsService {
    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final MedicalRecordRepository medicalRecordRepository;
    
    public Map<String, Object> getDashboardStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalPatients", userRepository.countByRole(com.medicare.model.enums.UserRole.PATIENT));
        stats.put("totalDoctors", doctorRepository.count());
        stats.put("totalAppointments", appointmentRepository.count());
        stats.put("activeDoctors", doctorRepository.countByIsAvailable(true));
        stats.put("completedAppointments", appointmentRepository.findByStatus(AppointmentStatus.COMPLETED, null).getTotalElements());
        stats.put("pendingAppointments", appointmentRepository.findByStatus(AppointmentStatus.SCHEDULED, null).getTotalElements());
        stats.put("averageConsultationFee", doctorRepository.getAverageConsultationFee());
        
        return stats;
    }
    
    public Map<String, Object> getAppointmentAnalytics(String startDate, String endDate) {
        Map<String, Object> analytics = new HashMap<>();
        
        LocalDateTime start = startDate != null ? LocalDateTime.parse(startDate) : LocalDateTime.now().minusMonths(1);
        LocalDateTime end = endDate != null ? LocalDateTime.parse(endDate) : LocalDateTime.now();
        
        List<com.medicare.model.Appointment> appointments = appointmentRepository.findAppointmentsBetweenDates(start, end);
        
        analytics.put("totalAppointments", appointments.size());
        analytics.put("completedAppointments", appointments.stream()
                .filter(a -> a.getStatus() == AppointmentStatus.COMPLETED).count());
        analytics.put("cancelledAppointments", appointments.stream()
                .filter(a -> a.getStatus() == AppointmentStatus.CANCELLED).count());
        analytics.put("totalRevenue", appointments.stream()
                .filter(com.medicare.model.Appointment::getIsPaid)
                .map(com.medicare.model.Appointment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        
        Map<String, Long> byType = new HashMap<>();
        for (com.medicare.model.Appointment apt : appointments) {
            String type = apt.getAppointmentType().name();
            byType.put(type, byType.getOrDefault(type, 0L) + 1);
        }
        analytics.put("appointmentsByType", byType);
        
        return analytics;
    }
    
    public Map<String, Object> getDoctorPerformanceMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        
        List<com.medicare.model.Doctor> doctors = doctorRepository.findAll();
        
        Map<String, Object> topDoctors = new HashMap<>();
        doctors.stream()
                .sorted((d1, d2) -> Double.compare(d2.getRating(), d1.getRating()))
                .limit(5)
                .forEach(d -> topDoctors.put(d.getUser().getFullName(), d.getRating()));
        
        metrics.put("topRatedDoctors", topDoctors);
        metrics.put("averageRating", doctors.stream()
                .mapToDouble(com.medicare.model.Doctor::getRating)
                .average()
                .orElse(0.0));
        
        Map<String, Long> bySpecialization = new HashMap<>();
        for (com.medicare.model.Doctor doctor : doctors) {
            String spec = doctor.getSpecialization().name();
            bySpecialization.put(spec, bySpecialization.getOrDefault(spec, 0L) + 1);
        }
        metrics.put("doctorsBySpecialization", bySpecialization);
        
        return metrics;
    }
    
    public Map<String, Object> getRevenueStatistics() {
        Map<String, Object> revenue = new HashMap<>();
        
        List<com.medicare.model.Appointment> paidAppointments = appointmentRepository.findAll().stream()
                .filter(com.medicare.model.Appointment::getIsPaid)
                .toList();
        
        BigDecimal totalRevenue = paidAppointments.stream()
                .map(com.medicare.model.Appointment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        revenue.put("totalRevenue", totalRevenue);
        revenue.put("totalPaidAppointments", paidAppointments.size());
        revenue.put("averageAppointmentValue", paidAppointments.isEmpty() ? BigDecimal.ZERO :
                totalRevenue.divide(BigDecimal.valueOf(paidAppointments.size()), 2, java.math.RoundingMode.HALF_UP));
        
        return revenue;
    }
}
