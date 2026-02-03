package com.medicare.controller;

import com.medicare.dto.ApiResponse;
import com.medicare.dto.AppointmentDTO.*;
import com.medicare.dto.PageResponse;
import com.medicare.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Appointments", description = "Appointment management endpoints")
public class AppointmentController {
    
    private final AppointmentService appointmentService;
    
    @PostMapping
    @PreAuthorize("hasAuthority('PATIENT')")
    @Operation(summary = "Book new appointment")
    public ResponseEntity<ApiResponse<Response>> bookAppointment(
            @Valid @RequestBody CreateRequest request,
            Authentication authentication) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Appointment booked successfully", 
                    appointmentService.bookAppointment(request, authentication.getName())));
    }
    
    @GetMapping
    @Operation(summary = "Get all appointments (paginated)")
    public ResponseEntity<ApiResponse<PageResponse<Response>>> getAllAppointments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.success(
                appointmentService.getAppointments(page, size, status, authentication)));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get appointment by ID")
    public ResponseEntity<ApiResponse<Response>> getAppointmentById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(appointmentService.getAppointmentById(id)));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update appointment")
    public ResponseEntity<ApiResponse<Response>> updateAppointment(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRequest request,
            Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.success("Appointment updated", 
                appointmentService.updateAppointment(id, request, authentication)));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Cancel appointment")
    public ResponseEntity<ApiResponse<Void>> cancelAppointment(
            @PathVariable Long id,
            Authentication authentication) {
        appointmentService.cancelAppointment(id, authentication);
        return ResponseEntity.ok(ApiResponse.success("Appointment cancelled", null));
    }
    
    @GetMapping("/my-appointments")
    @Operation(summary = "Get current user's appointments")
    public ResponseEntity<ApiResponse<PageResponse<Response>>> getMyAppointments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {
        return ResponseEntity.ok(ApiResponse.success(
                appointmentService.getUserAppointments(page, size, authentication.getName())));
    }
}
