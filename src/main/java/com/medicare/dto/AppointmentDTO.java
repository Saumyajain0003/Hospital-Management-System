package com.medicare.dto;

import com.medicare.model.enums.AppointmentStatus;
import com.medicare.model.enums.AppointmentType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AppointmentDTO {
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateRequest {
        @NotNull(message = "Doctor ID is required")
        private Long doctorId;
        
        @NotNull(message = "Appointment date and time is required")
        @Future(message = "Appointment must be in the future")
        private LocalDateTime appointmentDateTime;
        
        @NotNull(message = "Appointment type is required")
        private AppointmentType appointmentType;
        
        @NotBlank(message = "Symptoms are required")
        private String symptoms;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateRequest {
        private LocalDateTime appointmentDateTime;
        private AppointmentType appointmentType;
        private AppointmentStatus status;
        private String symptoms;
        private String notes;
        private String prescription;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private Long patientId;
        private String patientName;
        private String patientEmail;
        private Long doctorId;
        private String doctorName;
        private String specialization;
        private LocalDateTime appointmentDateTime;
        private AppointmentType appointmentType;
        private AppointmentStatus status;
        private String symptoms;
        private String notes;
        private String prescription;
        private BigDecimal amount;
        private Boolean isPaid;
        private String paymentId;
        private LocalDateTime createdAt;
        private LocalDateTime completedAt;
    }
}
