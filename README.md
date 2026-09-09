# Peer-to-Peer Academic Mentoring & Doubt Clearing Hub

A full-stack web application developed with **Spring Boot 3, Spring Data JPA, MySQL 8, HTML5, CSS3 Grid/Flexbox, and Vanilla JavaScript (Fetch API)**. Designed specifically for college academic mentoring and doubt clearing.

---

## 📁 Project Directory Structure

```
c:\Users\tamil\mentor hub\
├── pom.xml                                   # Maven dependencies (Spring Boot 3, Web, JPA, MySQL, Security Crypto)
├── README.md                                 # Complete documentation, setup guide, Postman & Draw.io specs
├── src/
│   └── main/
│       ├── java/
│       │   └── com/peermentoring/
│       │       ├── PeerMentoringApplication.java    # Application Main Class
│       │       ├── config/
│       │       │   └── CorsConfig.java               # Global CORS setup for FE integration
│       │       ├── controller/
│       │       │   ├── AuthController.java           # Authentication endpoints (/api/auth)
│       │       │   ├── TutorController.java          # Tutor catalog & profile endpoints (/api/tutors)
│       │       │   ├── SubjectController.java        # Subject management endpoints (/api/subjects)
│       │       │   ├── AvailabilityController.java   # Tutor slot management endpoints (/api/availability)
│       │       │   ├── BookingController.java        # Session booking endpoints (/api/bookings)
│       │       │   └── FeedbackController.java       # Rating & review endpoints (/api/feedback)
│       │       ├── dto/
│       │       │   ├── RegisterRequest.java
│       │       │   ├── LoginRequest.java
│       │       │   ├── UserDTO.java
│       │       │   ├── TutorProfileRequest.java
│       │       │   ├── TutorResponse.java
│       │       │   ├── AvailabilityRequest.java
│       │       │   ├── BookingRequest.java
│       │       │   ├── BookingResponse.java
│       │       │   ├── FeedbackRequest.java
│       │       │   └── FeedbackDTO.java
│       │       ├── entity/
│       │       │   ├── User.java                     # Users table entity
│       │       │   ├── TutorProfile.java             # Tutor profiles table entity
│       │       │   ├── Subject.java                  # Subjects table entity
│       │       │   ├── AvailabilitySlot.java         # Availability slots table entity
│       │       │   ├── Booking.java                  # Session bookings table entity
│       │       │   └── Feedback.java                 # Ratings & reviews table entity
│       │       ├── exception/
│       │       │   ├── ResourceNotFoundException.java
│       │       │   ├── BadRequestException.java
│       │       │   └── GlobalExceptionHandler.java   # Unified REST error handling
│       │       ├── repository/
│       │       │   ├── UserRepository.java
│       │       │   ├── TutorProfileRepository.java
│       │       │   ├── SubjectRepository.java
│       │       │   ├── AvailabilitySlotRepository.java
│       │       │   ├── BookingRepository.java
│       │       │   └── FeedbackRepository.java
│       │       └── service/
│       │           ├── AuthService.java              # User registration & password hashing
│       │           ├── TutorService.java             # Search & dynamic rating calculation
│       │           ├── SubjectService.java           # Course management
│       │           ├── AvailabilityService.java      # Slot creation & deletion
│       │           ├── BookingService.java           # Booking rules, venue assignment
│       │           └── FeedbackService.java          # Review validation & rating calculation
│       └── resources/
│           ├── application.properties            # MySQL database config & JPA settings
│           ├── schema.sql                        # Database table DDL creation script
│           └── data.sql                          # Seed sample data (tutors, subjects, slots, feedback)
└── frontend/                                     # Web Frontend Assets
    ├── index.html                                # Landing page
    ├── register.html                             # User Registration page
    ├── login.html                                # User Login page
    ├── junior-dashboard.html                     # Junior Student Dashboard
    ├── tutor-dashboard.html                      # Senior Tutor Dashboard
    ├── tutors.html                               # Browse & Search Tutors
    ├── tutor-profile.html                        # Detailed Tutor View & Slot Picker
    ├── availability.html                         # Tutor Slot Management
    ├── my-sessions.html                          # Session History & Upcoming Meetings
    ├── booking-confirmation.html                 # Booking Confirmed Receipt
    ├── feedback.html                             # 1-5 Star Rating & Review Form
    ├── css/
    │   ├── style.css                             # Glassmorphism design system & component styles
    │   └── responsive.css                        # Mobile & Tablet responsive breakpoints
    └── js/
        ├── api.js                                # Centralized Fetch API wrapper & error handler
        ├── auth.js                               # LocalStorage session state & route guard
        ├── tutors.js                             # Tutor search & card renderer
        ├── booking.js                            # Slot booking modal & reservation logic
        ├── availability.js                       # Slot creation & deletion UI
        ├── feedback.js                           # Interactive star rating selector
        └── dashboard.js                          # Dashboard stats & table renderer
```

---

## 💻 Technology Stack

* **Frontend**: HTML5, CSS3, CSS Grid, Flexbox, JavaScript (ES6+), Fetch API (No React/Angular framework used).
* **Backend**: Java 17+, Spring Boot 3.2.3, Spring Data JPA, Spring Web REST APIs, BCrypt Security Hashing, Maven.
* **Database**: MySQL 8.0, JPA/Hibernate ORM.

---

## 🛠️ Step-by-Step Installation & Running Guide

### Step 1: Requirements Check
- Install JDK 17 or higher.
- Install MySQL Server 8.0 and MySQL Workbench.
- VS Code / IntelliJ IDEA / Eclipse.

### Step 2: MySQL Database Setup
Open MySQL Workbench or MySQL CLI and run:
```sql
CREATE DATABASE IF NOT EXISTS peer_mentoring_db;
```

### Step 3: Configure Database Credentials
Open `src/main/resources/application.properties` and update your MySQL username and password:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/peer_mentoring_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

### Step 4: Run Spring Boot Application
From VS Code or Terminal, execute:
```powershell
mvn spring-boot:run
```
*(The backend server starts on `http://localhost:8080` and automatically initializes tables and seed data).*

### Step 5: Run Frontend
Open `frontend/index.html` using VS Code **Live Server** extension (or open `index.html` directly in Google Chrome).

---

## 🧪 Postman API Testing Collection Guide

### 1. Register User (Junior / Senior Tutor)
* **Method**: `POST`
* **URL**: `http://localhost:8080/api/auth/register`
* **Body (JSON)**:
```json
{
  "fullName": "Arun Kumar",
  "email": "arun@college.edu",
  "password": "123456",
  "department": "CSE",
  "year": 3,
  "role": "TUTOR"
}
```
* **Expected Status**: `201 Created`

### 2. Login User
* **Method**: `POST`
* **URL**: `http://localhost:8080/api/auth/login`
* **Body (JSON)**:
```json
{
  "email": "arun@college.edu",
  "password": "123456"
}
```
* **Expected Status**: `200 OK`

### 3. Get All Tutors / Search Tutors by Subject
* **Method**: `GET`
* **URL**: `http://localhost:8080/api/tutors?subject=DSA`
* **Expected Status**: `200 OK`

### 4. Create Availability Slot (Tutor Only)
* **Method**: `POST`
* **URL**: `http://localhost:8080/api/availability`
* **Body (JSON)**:
```json
{
  "tutorId": 1,
  "subjectId": 1,
  "slotDate": "2026-09-20",
  "startTime": "10:00",
  "endTime": "10:30"
}
```
* **Expected Status**: `201 Created`

### 5. Create Study Session Booking (Junior Only)
* **Method**: `POST`
* **URL**: `http://localhost:8080/api/bookings`
* **Body (JSON)**:
```json
{
  "juniorId": 5,
  "tutorId": 1,
  "subjectId": 1,
  "slotId": 1,
  "meetingType": "LIBRARY"
}
```
* **Expected Status**: `201 Created`

### 6. Mark Session as Completed
* **Method**: `PUT`
* **URL**: `http://localhost:8080/api/bookings/1/complete`
* **Expected Status**: `200 OK`

### 7. Submit Feedback & Star Rating
* **Method**: `POST`
* **URL**: `http://localhost:8080/api/feedback`
* **Body (JSON)**:
```json
{
  "bookingId": 1,
  "juniorId": 5,
  "rating": 5,
  "comment": "Excellently explained Graph Traversals BFS & DFS!"
}
```
* **Expected Status**: `201 Created`

---

## 🎨 Draw.io Diagram Specifications

### 1. Entity-Relationship (ER) Diagram
* **Entities**:
  - `User` (id, full_name, email, password, department, year, role)
  - `TutorProfile` (id, user_id, bio, cgpa, is_available)
  - `Subject` (id, subject_code, subject_name, description)
  - `AvailabilitySlot` (id, tutor_id, subject_id, slot_date, start_time, end_time, status)
  - `Booking` (id, junior_id, tutor_id, subject_id, slot_id, booking_date, start_time, end_time, meeting_type, venue, meeting_link, status)
  - `Feedback` (id, booking_id, junior_id, tutor_id, rating, comment)
* **Relationships**:
  - User `1 --- 1` TutorProfile
  - TutorProfile `M --- N` Subject (via `tutor_subjects`)
  - TutorProfile `1 --- M` AvailabilitySlot
  - User (Junior) `1 --- M` Booking
  - Booking `1 --- 1` Feedback

### 2. System Architecture Diagram
`Browser Frontend (HTML/CSS/JS)` ──[Fetch API JSON]──> `Spring Boot REST Controllers` ──> `Service Layer` ──> `Spring Data JPA Repositories` ──[JDBC Driver]──> `MySQL Database (peer_mentoring_db)`

---

## ⚠️ Common Errors and Solutions

| Error Message / Issue | Root Cause | Solution |
| :--- | :--- | :--- |
| `Access denied for user 'root'@'localhost'` | Incorrect MySQL password in `application.properties` | Update `spring.datasource.password` to match your local MySQL root password. |
| `Unknown database 'peer_mentoring_db'` | Database not created in MySQL | Run `CREATE DATABASE peer_mentoring_db;` in MySQL Workbench. |
| `CORS Policy Error in browser console` | Missing or restricted CORS headers | `CorsConfig.java` and `@CrossOrigin(origins = "*")` allow cross-origin requests. Ensure backend is running. |
| `Cannot book a session with yourself` | Junior ID equals Tutor User ID | System enforces Rule 1 & 2 preventing self-booking. Log in with a different junior account. |
