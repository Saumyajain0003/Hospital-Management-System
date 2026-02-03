package com.medicare.controller;

import com.medicare.dto.ApiResponse;
import com.medicare.dto.DoctorDTO.*;
import com.medicare.dto.PageResponse;
import com.medicare.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
@Tag(name = "Doctors", description = "Doctor management endpoints")
public class DoctorController {
    
    private final DoctorService doctorService;
    
    @GetMapping
    @Operation(summary = "Get all doctors (paginated, filterable)")
    public ResponseEntity<ApiResponse<PageResponse<Response>>> getAllDoctors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String specialization,
            @RequestParam(required = false) BigDecimal minFee,
            @RequestParam(required = false) BigDecimal maxFee,
            @RequestParam(required = false) String search) {
        return ResponseEntity.ok(ApiResponse.success(
                doctorService.getAllDoctors(page, size, specialization, minFee, maxFee, search)));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get doctor by ID")
    public ResponseEntity<ApiResponse<Response>> getDoctorById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(doctorService.getDoctorById(id)));
    }
    
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Create doctor profile")
    public ResponseEntity<ApiResponse<Response>> createDoctor(@Valid @RequestBody CreateRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Doctor profile created", doctorService.createDoctor(request)));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('DOCTOR', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Update doctor profile")
    public ResponseEntity<ApiResponse<Response>> updateDoctor(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Doctor profile updated", 
                doctorService.updateDoctor(id, request)));
    }
    
    @PostMapping("/{id}/schedule")
    @PreAuthorize("hasAnyAuthority('DOCTOR', 'ADMIN')")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Add doctor schedule")
    public ResponseEntity<ApiResponse<ScheduleResponse>> addSchedule(
            @PathVariable Long id,
            @Valid @RequestBody ScheduleRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Schedule added", doctorService.addSchedule(id, request)));
    }
    
    @GetMapping("/top-rated")
    @Operation(summary = "Get top-rated doctors")
    public ResponseEntity<ApiResponse<?>> getTopRatedDoctors() {
        return ResponseEntity.ok(ApiResponse.success(doctorService.getTopRatedDoctors()));
    }
}
