package com.medicare.service;

import com.medicare.dto.MedicalRecordDTO;
import com.medicare.dto.PageResponse;
import com.medicare.exception.ResourceNotFoundException;
import com.medicare.model.*;
import com.medicare.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicalRecordService {
    private final MedicalRecordRepository medicalRecordRepository;
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    private final FileStorageService fileStorageService;
    
    @Transactional
    public MedicalRecordDTO.Response createMedicalRecord(MedicalRecordDTO.CreateRequest request) {
        User patient = userRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
        
        MedicalRecord.MedicalRecordBuilder builder = MedicalRecord.builder()
                .patient(patient)
                .diagnosis(request.getDiagnosis())
                .treatment(request.getTreatment())
                .medications(request.getMedications())
                .testReports(request.getTestReports())
                .doctorNotes(request.getDoctorNotes())
                .bloodPressure(request.getBloodPressure())
                .temperature(request.getTemperature())
                .heartRate(request.getHeartRate())
                .weight(request.getWeight())
                .height(request.getHeight());
        
        if (request.getAppointmentId() != null) {
            Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
            builder.appointment(appointment);
        }
        
        MedicalRecord record = builder.build();
        record = medicalRecordRepository.save(record);
        return mapToResponse(record);
    }
    
    public MedicalRecordDTO.Response getMedicalRecordById(Long id) {
        MedicalRecord record = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medical record not found"));
        return mapToResponse(record);
    }
    
    public PageResponse<MedicalRecordDTO.Response> getPatientMedicalRecords(Long patientId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<MedicalRecord> recordPage = medicalRecordRepository.findByPatientId(patientId, pageable);
        
        return PageResponse.<MedicalRecordDTO.Response>builder()
                .content(recordPage.getContent().stream().map(this::mapToResponse).collect(Collectors.toList()))
                .pageNumber(recordPage.getNumber())
                .pageSize(recordPage.getSize())
                .totalElements(recordPage.getTotalElements())
                .totalPages(recordPage.getTotalPages())
                .last(recordPage.isLast())
                .first(recordPage.isFirst())
                .build();
    }
    
    @Transactional
    public String uploadFile(Long recordId, MultipartFile file) {
        MedicalRecord record = medicalRecordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException("Medical record not found"));
        
        String fileName = fileStorageService.storeFile(file);
        record.getAttachments().add(fileName);
        medicalRecordRepository.save(record);
        
        return fileName;
    }
    
    @Transactional
    public MedicalRecordDTO.Response updateMedicalRecord(Long id, MedicalRecordDTO.UpdateRequest request) {
        MedicalRecord record = medicalRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medical record not found"));
        
        if (request.getDiagnosis() != null) record.setDiagnosis(request.getDiagnosis());
        if (request.getTreatment() != null) record.setTreatment(request.getTreatment());
        if (request.getMedications() != null) record.setMedications(request.getMedications());
        if (request.getTestReports() != null) record.setTestReports(request.getTestReports());
        if (request.getDoctorNotes() != null) record.setDoctorNotes(request.getDoctorNotes());
        if (request.getBloodPressure() != null) record.setBloodPressure(request.getBloodPressure());
        if (request.getTemperature() != null) record.setTemperature(request.getTemperature());
        if (request.getHeartRate() != null) record.setHeartRate(request.getHeartRate());
        if (request.getWeight() != null) record.setWeight(request.getWeight());
        if (request.getHeight() != null) record.setHeight(request.getHeight());
        
        record = medicalRecordRepository.save(record);
        return mapToResponse(record);
    }
    
    private MedicalRecordDTO.Response mapToResponse(MedicalRecord record) {
        return MedicalRecordDTO.Response.builder()
                .id(record.getId())
                .patientId(record.getPatient().getId())
                .patientName(record.getPatient().getFullName())
                .appointmentId(record.getAppointment() != null ? record.getAppointment().getId() : null)
                .diagnosis(record.getDiagnosis())
                .treatment(record.getTreatment())
                .medications(record.getMedications())
                .testReports(record.getTestReports())
                .attachments(record.getAttachments())
                .doctorNotes(record.getDoctorNotes())
                .bloodPressure(record.getBloodPressure())
                .temperature(record.getTemperature())
                .heartRate(record.getHeartRate())
                .weight(record.getWeight())
                .height(record.getHeight())
                .createdAt(record.getCreatedAt())
                .updatedAt(record.getUpdatedAt())
                .build();
    }
}
