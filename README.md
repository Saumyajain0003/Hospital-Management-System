# MediCare - Hospital Appointment Management System

## 🏥 Complete Backend Engineering Project

### Project Overview
Production-grade Spring Boot backend for hospital appointment management system with JWT authentication, role-based access control, and comprehensive REST APIs.

### ✨ Features Implemented
- ✅ User Management (Patient, Doctor, Admin roles)
- ✅ JWT Authentication & Authorization
- ✅ Appointment Booking & Management
- ✅ Doctor Scheduling System
- ✅ Medical Records with File Upload
- ✅ Email Notifications
- ✅ Payment Integration (Razorpay Mock)
- ✅ Analytics & Reporting APIs
- ✅ Rate Limiting
- ✅ Caching (Caffeine)
- ✅ Pagination & Filtering
- ✅ Global Exception Handling
- ✅ Input Validation
- ✅ Swagger/OpenAPI Documentation

### 🛠️ Technology Stack
- **Backend**: Spring Boot 3.2.0
- **Database**: PostgreSQL (H2 for development)
- **Security**: Spring Security + JWT
- **Documentation**: Swagger/OpenAPI
- **Caching**: Caffeine
- **Email**: Spring Mail
- **Build Tool**: Maven

### 📁 Project Structure
```
medicare-backend/
├── src/main/java/com/medicare/
│   ├── config/             # Configuration classes
│   ├── controller/         # REST Controllers
│   ├── dto/               # Data Transfer Objects
│   ├── exception/         # Exception handlers
│   ├── filter/            # Security filters
│   ├── model/             # JPA Entities
│   ├── repository/        # Data repositories
│   ├── security/          # Security components
│   ├── service/           # Business logic
│   └── util/              # Utility classes
├── src/main/resources/
│   └── application.properties
└── pom.xml
```

### 🚀 Getting Started

#### Prerequisites
- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+ (or use H2 for development)

#### Installation

1. **Clone the repository**
```bash
git clone <your-repo-url>
cd medicare-backend
```

2. **Configure Database**
Update `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/medicare
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. **Configure Email**
```properties
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

4. **Build the project**
```bash
mvn clean install
```

5. **Run the application**
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### 📚 API Documentation

Access Swagger UI at: `http://localhost:8080/swagger-ui.html`

### 🔑 Default API Endpoints

#### Authentication
- POST `/api/auth/register` - Register new user
- POST `/api/auth/login` - Login
- POST `/api/auth/refresh` - Refresh token

#### Appointments
- GET `/api/appointments` - List all appointments (paginated)
- POST `/api/appointments` - Book new appointment
- GET `/api/appointments/{id}` - Get appointment details
- PUT `/api/appointments/{id}` - Update appointment
- DELETE `/api/appointments/{id}` - Cancel appointment

#### Doctors
- GET `/api/doctors` - List all doctors (paginated, filterable)
- GET `/api/doctors/{id}` - Get doctor details
- POST `/api/doctors` - Create doctor profile
- PUT `/api/doctors/{id}` - Update doctor profile
- GET `/api/doctors/search` - Search doctors by specialization

#### Medical Records
- GET `/api/medical-records` - List medical records
- POST `/api/medical-records` - Create medical record
- GET `/api/medical-records/{id}` - Get record details
- POST `/api/medical-records/{id}/upload` - Upload file

#### Analytics (Admin only)
- GET `/api/analytics/dashboard` - System statistics
- GET `/api/analytics/appointments` - Appointment analytics
- GET `/api/analytics/doctors` - Doctor performance metrics

### 🔐 Security

The application uses JWT-based authentication. Include the token in requests:
```
Authorization: Bearer <your-jwt-token>
```

**Roles:**
- `PATIENT` - Can book appointments, view own records
- `DOCTOR` - Can manage schedule, update appointments, create medical records
- `ADMIN` - Full system access

### 🧪 Testing

Run tests:
```bash
mvn test
```

### 📊 Database Schema

Key entities:
- **User** - Base user information
- **Doctor** - Doctor profiles and specializations
- **Appointment** - Appointment bookings
- **DoctorSchedule** - Doctor availability
- **MedicalRecord** - Patient medical history
- **Review** - Doctor reviews and ratings

### 🎯 Advanced Features

1. **Rate Limiting** - 100 requests per hour per user
2. **Caching** - Frequently accessed data cached with Caffeine
3. **Async Email** - Non-blocking email notifications
4. **File Upload** - Support for medical documents (PDF, images)
5. **Complex Queries** - Advanced filtering and search
6. **Pagination** - All list endpoints support pagination
7. **Payment Integration** - Mock Razorpay integration

### 📝 Sample API Calls

#### Register a Patient
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "patient@example.com",
    "password": "password123",
    "fullName": "John Doe",
    "phone": "1234567890",
    "role": "PATIENT"
  }'
```

#### Book an Appointment
```bash
curl -X POST http://localhost:8080/api/appointments \
  -H "Authorization: Bearer <your-token>" \
  -H "Content-Type: application/json" \
  -d '{
    "doctorId": 1,
    "appointmentDateTime": "2024-12-25T10:00:00",
    "appointmentType": "IN_PERSON",
    "symptoms": "Fever and headache"
  }'
```

### 🎓 For Students

This project demonstrates:
- ✅ Clean architecture and layered design
- ✅ SOLID principles
- ✅ RESTful API best practices
- ✅ Security implementation
- ✅ Database design and JPA
- ✅ Exception handling
- ✅ Input validation
- ✅ Documentation
- ✅ File handling
- ✅ Email integration


