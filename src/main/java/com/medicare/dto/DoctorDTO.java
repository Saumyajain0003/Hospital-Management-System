package com.medicare.dto;

import com.medicare.model.enums.Specialization;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public class DoctorDTO {
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateRequest {
        @NotNull(message = "User ID is required")
        private Long userId;
        
        @NotNull(message = "Specialization is required")
        private Specialization specialization;
        
        @NotBlank(message = "Qualification is required")
        private String qualification;
        
        @NotNull(message = "Experience years is required")
        @Min(value = 0, message = "Experience cannot be negative")
        private Integer experienceYears;
        
        @NotNull(message = "Consultation fee is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Fee must be positive")
        private BigDecimal consultationFee;
        
        @Size(max = 1000, message = "Bio cannot exceed 1000 characters")
        private String bio;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateRequest {
        private Specialization specialization;
        private String qualification;
        private Integer experienceYears;
        private BigDecimal consultationFee;
        private String bio;
        private Boolean isAvailable;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private Long userId;
        private String fullName;
        private String email;
        private String phone;
        private Specialization specialization;
        private String qualification;
        private Integer experienceYears;
        private BigDecimal consultationFee;
        private String bio;
        private Boolean isAvailable;
        private Double rating;
        private Integer totalRatings;
        private String profilePicture;
        private List<ScheduleResponse> schedules;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ScheduleRequest {
        @NotNull(message = "Day of week is required")
        private DayOfWeek dayOfWeek;
        
        @NotNull(message = "Start time is required")
        private LocalTime startTime;
        
        @NotNull(message = "End time is required")
        private LocalTime endTime;
        
        @NotNull(message = "Slot duration is required")
        @Min(value = 15, message = "Slot duration must be at least 15 minutes")
        private Integer slotDurationMinutes;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ScheduleResponse {
        private Long id;
        private DayOfWeek dayOfWeek;
        private LocalTime startTime;
        private LocalTime endTime;
        private Integer slotDurationMinutes;
        private Boolean isAvailable;
    }
}
