package com.medicare.service;

import com.medicare.dto.DoctorDTO;
import com.medicare.dto.PageResponse;
import com.medicare.exception.*;
import com.medicare.model.*;
import com.medicare.model.enums.Specialization;
import com.medicare.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final DoctorScheduleRepository scheduleRepository;
    
    @Transactional
    public DoctorDTO.Response createDoctor(DoctorDTO.CreateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        if (doctorRepository.findByUserId(user.getId()).isPresent()) {
            throw new BadRequestException("Doctor profile already exists for this user");
        }
        
        Doctor doctor = Doctor.builder()
                .user(user)
                .specialization(request.getSpecialization())
                .qualification(request.getQualification())
                .experienceYears(request.getExperienceYears())
                .consultationFee(request.getConsultationFee())
                .bio(request.getBio())
                .build();
        
        doctor = doctorRepository.save(doctor);
        return mapToResponse(doctor);
    }
    
    @Cacheable(value = "doctors", key = "#id")
    public DoctorDTO.Response getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        return mapToResponse(doctor);
    }
    
    public PageResponse<DoctorDTO.Response> getAllDoctors(int page, int size, String specialization,
                                                           BigDecimal minFee, BigDecimal maxFee, String search) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("rating").descending());
        Page<Doctor> doctorPage;
        
        if (search != null && !search.isEmpty()) {
            doctorPage = doctorRepository.searchAvailableDoctors(search, pageable);
        } else if (specialization != null) {
            Specialization spec = Specialization.valueOf(specialization.toUpperCase());
            doctorPage = doctorRepository.findBySpecializationAndIsAvailable(spec, true, pageable);
        } else if (minFee != null && maxFee != null) {
            doctorPage = doctorRepository.findByConsultationFeeBetween(minFee, maxFee, pageable);
        } else {
            doctorPage = doctorRepository.findByIsActive(true, pageable);
        }
        
        return mapToPageResponse(doctorPage);
    }
    
    @Transactional
    public DoctorDTO.Response updateDoctor(Long id, DoctorDTO.UpdateRequest request) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        
        if (request.getSpecialization() != null) {
            doctor.setSpecialization(request.getSpecialization());
        }
        if (request.getQualification() != null) {
            doctor.setQualification(request.getQualification());
        }
        if (request.getExperienceYears() != null) {
            doctor.setExperienceYears(request.getExperienceYears());
        }
        if (request.getConsultationFee() != null) {
            doctor.setConsultationFee(request.getConsultationFee());
        }
        if (request.getBio() != null) {
            doctor.setBio(request.getBio());
        }
        if (request.getIsAvailable() != null) {
            doctor.setIsAvailable(request.getIsAvailable());
        }
        
        doctor = doctorRepository.save(doctor);
        return mapToResponse(doctor);
    }
    
    @Transactional
    public DoctorDTO.ScheduleResponse addSchedule(Long doctorId, DoctorDTO.ScheduleRequest request) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));
        
        // Check if schedule already exists for this day
        Optional<DoctorSchedule> existing = scheduleRepository
                .findByDoctorIdAndDayOfWeek(doctorId, request.getDayOfWeek());
        
        if (existing.isPresent()) {
            throw new BadRequestException("Schedule already exists for " + request.getDayOfWeek());
        }
        
        DoctorSchedule schedule = DoctorSchedule.builder()
                .doctor(doctor)
                .dayOfWeek(request.getDayOfWeek())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .slotDurationMinutes(request.getSlotDurationMinutes())
                .build();
        
        schedule = scheduleRepository.save(schedule);
        return mapToScheduleResponse(schedule);
    }
    
    public List<DoctorDTO.Response> getTopRatedDoctors() {
        Pageable limit = PageRequest.of(0, 10);
        return doctorRepository.findTopRatedDoctors(4.0, limit).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    private DoctorDTO.Response mapToResponse(Doctor doctor) {
        List<DoctorDTO.ScheduleResponse> schedules = scheduleRepository
                .findByDoctorId(doctor.getId()).stream()
                .map(this::mapToScheduleResponse)
                .collect(Collectors.toList());
        
        return DoctorDTO.Response.builder()
                .id(doctor.getId())
                .userId(doctor.getUser().getId())
                .fullName(doctor.getUser().getFullName())
                .email(doctor.getUser().getEmail())
                .phone(doctor.getUser().getPhone())
                .specialization(doctor.getSpecialization())
                .qualification(doctor.getQualification())
                .experienceYears(doctor.getExperienceYears())
                .consultationFee(doctor.getConsultationFee())
                .bio(doctor.getBio())
                .isAvailable(doctor.getIsAvailable())
                .rating(doctor.getRating())
                .totalRatings(doctor.getTotalRatings())
                .profilePicture(doctor.getUser().getProfilePicture())
                .schedules(schedules)
                .build();
    }
    
    private DoctorDTO.ScheduleResponse mapToScheduleResponse(DoctorSchedule schedule) {
        return DoctorDTO.ScheduleResponse.builder()
                .id(schedule.getId())
                .dayOfWeek(schedule.getDayOfWeek())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .slotDurationMinutes(schedule.getSlotDurationMinutes())
                .isAvailable(schedule.getIsAvailable())
                .build();
    }
    
    private PageResponse<DoctorDTO.Response> mapToPageResponse(Page<Doctor> page) {
        return PageResponse.<DoctorDTO.Response>builder()
                .content(page.getContent().stream().map(this::mapToResponse).collect(Collectors.toList()))
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .first(page.isFirst())
                .build();
    }
}
