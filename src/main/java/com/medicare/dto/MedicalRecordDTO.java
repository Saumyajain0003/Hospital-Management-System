package com.medicare.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class MedicalRecordDTO {
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateRequest {
        @NotNull(message = "Patient ID is required")
        private Long patientId;
        
        private Long appointmentId;
        
        @NotBlank(message = "Diagnosis is required")
        private String diagnosis;
        
        private String treatment;
        private String medications;
        private String testReports;
        private String doctorNotes;
        private String bloodPressure;
        private String temperature;
        private Integer heartRate;
        private Double weight;
        private Double height;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateRequest {
        private String diagnosis;
        private String treatment;
        private String medications;
        private String testReports;
        private String doctorNotes;
        private String bloodPressure;
        private String temperature;
        private Integer heartRate;
        private Double weight;
        private Double height;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private Long patientId;
        private String patientName;
        private Long appointmentId;
        private String diagnosis;
        private String treatment;
        private String medications;
        private String testReports;
        private List<String> attachments;
        private String doctorNotes;
        private String bloodPressure;
        private String temperature;
        private Integer heartRate;
        private Double weight;
        private Double height;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
