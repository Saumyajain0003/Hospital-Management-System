package com.medicare.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "medical_records")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalRecord extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;
    
    @OneToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;
    
    @Column(nullable = false)
    private String diagnosis;
    
    @Column(length = 2000)
    private String treatment;
    
    @Column(length = 2000)
    private String medications;
    
    @Column(length = 1000)
    private String testReports;
    
    @ElementCollection
    @CollectionTable(name = "medical_record_files", joinColumns = @JoinColumn(name = "record_id"))
    @Column(name = "file_path")
    private List<String> attachments = new ArrayList<>();
    
    @Column(length = 500)
    private String doctorNotes;
    
    private String bloodPressure;
    
    private String temperature;
    
    private Integer heartRate;
    
    private Double weight;
    
    private Double height;
}
