package com.medicare.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    
    private final JavaMailSender mailSender;
    
    @Async
    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom("noreply@medicare.com");
            
            mailSender.send(message);
            log.info("Email sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send email to: {}", to, e);
        }
    }
    
    @Async
    public void sendWelcomeEmail(String email, String fullName) {
        String subject = "Welcome to MediCare!";
        String body = String.format(
                "Dear %s,\n\n" +
                "Welcome to MediCare! Your account has been successfully created.\n" +
                "You can now book appointments with our expert doctors.\n\n" +
                "Best regards,\nMediCare Team",
                fullName
        );
        sendEmail(email, subject, body);
    }
    
    @Async
    public void sendAppointmentConfirmation(String email, String patientName, 
                                           String doctorName, String dateTime) {
        String subject = "Appointment Confirmation - MediCare";
        String body = String.format(
                "Dear %s,\n\n" +
                "Your appointment has been confirmed!\n\n" +
                "Doctor: Dr. %s\n" +
                "Date & Time: %s\n\n" +
                "Please arrive 10 minutes before your appointment.\n\n" +
                "Best regards,\nMediCare Team",
                patientName, doctorName, dateTime
        );
        sendEmail(email, subject, body);
    }
    
    @Async
    public void sendAppointmentCancellation(String email, String patientName, String dateTime) {
        String subject = "Appointment Cancelled - MediCare";
        String body = String.format(
                "Dear %s,\n\n" +
                "Your appointment scheduled for %s has been cancelled.\n" +
                "If you did not request this cancellation, please contact us immediately.\n\n" +
                "Best regards,\nMediCare Team",
                patientName, dateTime
        );
        sendEmail(email, subject, body);
    }
}
