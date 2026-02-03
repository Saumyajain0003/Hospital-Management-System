package com.medicare.controller;

import com.medicare.dto.ApiResponse;
import com.medicare.dto.MedicalRecordDTO;
import com.medicare.dto.PageResponse;
import com.medicare.service.MedicalRecordService;
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
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/medical-records")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Medical Records", description = "Medical record management endpoints")
public class MedicalRecordController {
    
    private final MedicalRecordService medicalRecordService;
    
    @PostMapping
    @PreAuthorize("hasAnyAuthority('DOCTOR', 'ADMIN')")
    @Operation(summary = "Create medical record")
    public ResponseEntity<ApiResponse<MedicalRecordDTO.Response>> createRecord(
            @Valid @RequestBody MedicalRecordDTO.CreateRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Medical record created", 
                    medicalRecordService.createMedicalRecord(request)));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get medical record by ID")
    public ResponseEntity<ApiResponse<MedicalRecordDTO.Response>> getRecordById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(medicalRecordService.getMedicalRecordById(id)));
    }
    
    @GetMapping("/patient/{patientId}")
    @Operation(summary = "Get patient's medical records")
    public ResponseEntity<ApiResponse<PageResponse<MedicalRecordDTO.Response>>> getPatientRecords(
            @PathVariable Long patientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.success(
                medicalRecordService.getPatientMedicalRecords(patientId, page, size)));
    }
    
    @PostMapping("/{id}/upload")
    @PreAuthorize("hasAnyAuthority('DOCTOR', 'ADMIN')")
    @Operation(summary = "Upload file to medical record")
    public ResponseEntity<ApiResponse<String>> uploadFile(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(ApiResponse.success("File uploaded", 
                medicalRecordService.uploadFile(id, file)));
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('DOCTOR', 'ADMIN')")
    @Operation(summary = "Update medical record")
    public ResponseEntity<ApiResponse<MedicalRecordDTO.Response>> updateRecord(
            @PathVariable Long id,
            @Valid @RequestBody MedicalRecordDTO.UpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Record updated", 
                medicalRecordService.updateMedicalRecord(id, request)));
    }
}
