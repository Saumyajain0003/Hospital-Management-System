package com.medicare.controller;

import com.medicare.dto.ApiResponse;
import com.medicare.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@PreAuthorize("hasAuthority('ADMIN')")
@Tag(name = "Analytics", description = "Analytics and reporting endpoints (Admin only)")
public class AnalyticsController {
    
    private final AnalyticsService analyticsService;
    
    @GetMapping("/dashboard")
    @Operation(summary = "Get dashboard statistics")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStats() {
        return ResponseEntity.ok(ApiResponse.success(analyticsService.getDashboardStatistics()));
    }
    
    @GetMapping("/appointments")
    @Operation(summary = "Get appointment analytics")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAppointmentAnalytics(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        return ResponseEntity.ok(ApiResponse.success(
                analyticsService.getAppointmentAnalytics(startDate, endDate)));
    }
    
    @GetMapping("/doctors/performance")
    @Operation(summary = "Get doctor performance metrics")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDoctorPerformance() {
        return ResponseEntity.ok(ApiResponse.success(analyticsService.getDoctorPerformanceMetrics()));
    }
    
    @GetMapping("/revenue")
    @Operation(summary = "Get revenue statistics")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getRevenueStats() {
        return ResponseEntity.ok(ApiResponse.success(analyticsService.getRevenueStatistics()));
    }
}
