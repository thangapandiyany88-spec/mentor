# 🎓 Peer-to-Peer Academic Mentoring & Doubt Clearing Hub
## 📌 AI System Context & Technical Blueprint

> **Notice for AI Assistant / LLM**: This document provides a complete, self-contained overview of the codebase, architecture, data schemas, API contracts, frontend workflows, and development guidelines for the **Peer-to-Peer Academic Mentoring & Doubt Clearing Hub**. Use this file as your primary context when implementing features, fixing bugs, or refactoring code in this workspace.

---

## 📁 1. Project Overview & Repository Structure

The system is a full-stack academic mentoring platform designed for college campuses. It allows **Junior Students** to find **Senior Tutors**, book doubt-clearing sessions based on tutor availability, conduct sessions (Online or In-Person Library rooms), and leave 1–5 star feedback.

### Directory Structure
```
c:\Users\tamil\mentor hub\
├── pom.xml                                   # Maven configuration (Spring Boot 3.2.3, JPA, MySQL, Security Crypto)
├── README.md                                 # Complete documentation, setup guide, Postman & Draw.io specs
├── server.js                                 # Standalone zero-dependency Node.js Mock API Server (port 8080)
├── src/
│   └── main/
│       ├── java/
│       │   └── com/peermentoring/
│       │       ├── PeerMentoringApplication.java    # Spring Boot Entry Point
│       │       ├── config/
│       │       │   └── CorsConfig.java               # Global WebMvc CORS mapping (* allowed for FE)
│       │       ├── controller/                       # REST API Layer
│       │       │   ├── AuthController.java           # /api/auth (Login & Registration)
│       │       │   ├── TutorController.java          # /api/tutors (Tutor search, profiles)
│       │       │   ├── SubjectController.java        # /api/subjects (Course catalog)
│       │       │   ├── AvailabilityController.java   # /api/availability (Slot management)
│       │       │   ├── BookingController.java        # /api/bookings (Session reservations & status)
│       │       │   └── FeedbackController.java       # /api/feedback (Ratings & reviews)
│       │       ├── dto/                              # Request & Response Data Transfer Objects
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
│       │       ├── entity/                           # Spring Data JPA Entities
│       │       │   ├── User.java                     # Table: users
│       │       │   ├── TutorProfile.java             # Table: tutor_profiles
│       │       │   ├── Subject.java                  # Table: subjects
│       │       │   ├── AvailabilitySlot.java         # Table: availability_slots
│       │       │   ├── Booking.java                  # Table: bookings
│       │       │   └── Feedback.java                 # Table: feedback
│       │       ├── exception/                        # Custom Exceptions & Global Exception Handler
│       │       │   ├── ResourceNotFoundException.java
│       │       │   ├── BadRequestException.java
│       │       │   └── GlobalExceptionHandler.java   # Returns structured JSON error responses
│       │       ├── repository/                       # JPA Repositories
│       │       │   ├── UserRepository.java
│       │       │   ├── TutorProfileRepository.java
│       │       │   ├── SubjectRepository.java
│       │       │   ├── AvailabilitySlotRepository.java
│       │       │   ├── BookingRepository.java
│       │       │   └── FeedbackRepository.java
│       │       └── service/                          # Core Business Logic Layer
│       │           ├── AuthService.java              # BCrypt password hashing & validation
│       │           ├── TutorService.java             # Dynamic average rating & review count calculation
│       │           ├── SubjectService.java           # Course management
│       │           ├── AvailabilityService.java      # Slot creation & status toggling
│       │           ├── BookingService.java           # Booking rules & venue/meeting link generation
│       │           └── FeedbackService.java          # Rating validation & review recording
│       └── resources/
│           ├── application.properties            # Datasource config (MySQL localhost:3306/peer_mentoring_db)
│           ├── schema.sql                        # SQL DDL script for database tables
│           └── data.sql                          # Initial seed data for users, subjects, slots, feedback
└── frontend/                                     # Web Frontend UI
    ├── index.html                                # Landing page with hero banner & features
    ├── register.html                             # Registration form (Junior / Senior Tutor role switch)
    ├── login.html                                # Login form with LocalStorage session initialization
    ├── junior-dashboard.html                     # Dashboard for Juniors (upcoming/past bookings, quick actions)
    ├── tutor-dashboard.html                      # Dashboard for Tutors (slot manager, incoming sessions, stats)
    ├── tutors.html                               # Browse & filter tutors by subject, department, search
    ├── tutor-profile.html                        # Tutor details, CGPA, subjects taught, reviews & slot picker
    ├── availability.html                         # Tutor slot creation interface
    ├── my-sessions.html                          # Detailed session history and meeting status
    ├── booking-confirmation.html                 # Booking receipt display
    ├── feedback.html                             # 1-5 Star interactive rating & review form
    ├── css/
    │   ├── style.css                             # Custom CSS Design System (Glassmorphism, CSS Variables)
    │   └── responsive.css                        # Mobile & Tablet responsive breakpoints
    └── js/
        ├── api.js                                # Central fetch utility (`apiRequest(endpoint, method, data)`)
        ├── auth.js                               # LocalStorage state management (`getCurrentUser()`, `logout()`)
        ├── tutors.js                             # Tutor search filtering & card rendering
        ├── booking.js                            # Slot booking flow & venue selection
        ├── availability.js                       # Slot creation & deletion DOM handlers
        ├── feedback.js                           # Interactive star selector & feedback submission
        └── dashboard.js                          # Stats calculation & table populators
```

---

## 🛠️ 2. Technology Stack & Architecture

| Layer | Technologies / Frameworks | Details |
|---|---|---|
| **Frontend** | Vanilla HTML5, CSS3, ES6+ JavaScript | Glassmorphism design system (`style.css`), no heavy framework (No React/Angular/Vue). Uses native `Fetch API` and `localStorage`. |
| **Backend** | Java 17+, Spring Boot 3.2.3 | Spring Web, Spring Data JPA, Spring Security Crypto (`BCryptPasswordEncoder`). |
| **Mock Backend** | Node.js `http` module | `server.js` serves mock JSON responses on port 8080 without needing Maven or MySQL. |
| **Database** | MySQL 8.0 | Database `peer_mentoring_db`. ORM managed via Hibernate / Spring Data JPA. |
| **API Format** | RESTful JSON APIs | Base URL: `http://localhost:8080/api` |

---

## 🗄️ 3. Database Schema & Data Models

### 1. `users` Table
Stores user account details for both Juniors and Senior Tutors.
- `id` (BIGINT, PK, AUTO_INCREMENT)
- `full_name` (VARCHAR 100)
- `email` (VARCHAR 120, UNIQUE)
- `password` (VARCHAR 255, BCrypt hashed)
- `department` (VARCHAR 50) — e.g. `CSE`, `ECE`, `IT`, `MECH`
- `year` (INT) — e.g., 1, 2, 3, 4
- `role` (VARCHAR 20) — `JUNIOR` or `TUTOR`
- `created_at` (TIMESTAMP)

### 2. `tutor_profiles` Table
Extended profile for users with role = `TUTOR` (1:1 relationship with `users`).
- `id` (BIGINT, PK, AUTO_INCREMENT)
- `user_id` (BIGINT, FK -> `users.id`, UNIQUE)
- `bio` (TEXT)
- `cgpa` (DOUBLE)
- `is_available` (BOOLEAN, default TRUE)

### 3. `subjects` Table
Academic subjects available for mentoring.
- `id` (BIGINT, PK, AUTO_INCREMENT)
- `subject_code` (VARCHAR 20, UNIQUE) — e.g., `DSA101`, `DBMS201`
- `subject_name` (VARCHAR 100) — e.g., `Data Structures & Algorithms`
- `description` (TEXT)

### 4. `tutor_subjects` Table (Junction Table)
Many-to-Many relationship mapping tutors to the subjects they can teach.
- `tutor_id` (BIGINT, FK -> `tutor_profiles.id`)
- `subject_id` (BIGINT, FK -> `subjects.id`)
- Composite Primary Key: (`tutor_id`, `subject_id`)

### 5. `availability_slots` Table
Slots published by senior tutors for mentoring sessions.
- `id` (BIGINT, PK, AUTO_INCREMENT)
- `tutor_id` (BIGINT, FK -> `tutor_profiles.id`)
- `subject_id` (BIGINT, FK -> `subjects.id`)
- `slot_date` (DATE) — YYYY-MM-DD
- `start_time` (TIME) — HH:MM:SS
- `end_time` (TIME) — HH:MM:SS
- `status` (VARCHAR 20) — `AVAILABLE`, `BOOKED`, `CANCELLED`

### 6. `bookings` Table
Reservations made by junior students for specific availability slots.
- `id` (BIGINT, PK, AUTO_INCREMENT)
- `junior_id` (BIGINT, FK -> `users.id`)
- `tutor_id` (BIGINT, FK -> `tutor_profiles.id`)
- `subject_id` (BIGINT, FK -> `subjects.id`)
- `slot_id` (BIGINT, FK -> `availability_slots.id`)
- `booking_date` (DATE)
- `start_time` (TIME)
- `end_time` (TIME)
- `meeting_type` (VARCHAR 20) — `LIBRARY` or `ONLINE`
- `venue` (VARCHAR 100) — e.g., `Library Room 4` or `Online`
- `meeting_link` (VARCHAR 255) — Optional link for online sessions
- `status` (VARCHAR 20) — `PENDING`, `CONFIRMED`, `COMPLETED`, `CANCELLED`
- `created_at` (TIMESTAMP)

### 7. `feedback` Table
Reviews and ratings given by juniors after session completion.
- `id` (BIGINT, PK, AUTO_INCREMENT)
- `booking_id` (BIGINT, FK -> `bookings.id`, UNIQUE)
- `junior_id` (BIGINT, FK -> `users.id`)
- `tutor_id` (BIGINT, FK -> `tutor_profiles.id`)
- `rating` (INT, 1 to 5)
- `comment` (TEXT)
- `created_at` (TIMESTAMP)

---

## 🌐 4. Core REST API Endpoints Specification

Base Path: `http://localhost:8080/api`

### Authentication (`/api/auth`)
- **`POST /api/auth/register`**: Registers a new user (`JUNIOR` or `TUTOR`). If `TUTOR`, automatically creates a `TutorProfile`.
- **`POST /api/auth/login`**: Validates credentials using BCrypt and returns user DTO.

### Tutors (`/api/tutors`)
- **`GET /api/tutors`**: List all tutors. Accepts query params: `?subject=DSA`, `?department=CSE`, `?search=Arun`. Returns average rating and total reviews computed dynamically.
- **`GET /api/tutors/{id}`**: Fetch detailed tutor profile by ID.
- **`POST /api/tutors/profile`**: Create/update tutor bio & CGPA.
- **`PUT /api/tutors/profile/{id}`**: Update profile details.

### Subjects (`/api/subjects`)
- **`GET /api/subjects`**: Fetch list of all academic subjects.
- **`POST /api/subjects`**: Add a new subject course.

### Availability (`/api/availability`)
- **`GET /api/availability/tutor/{tutorId}`**: Get all slots created by a tutor.
- **`GET /api/availability/tutor/{tutorId}/available`**: Get only unbooked (`AVAILABLE`) slots for a tutor.
- **`POST /api/availability`**: Create a new availability slot for a tutor and subject.
- **`DELETE /api/availability/{id}`**: Delete/cancel an availability slot.

### Bookings (`/api/bookings`)
- **`POST /api/bookings`**: Create session booking. Automatically updates slot status to `BOOKED` and generates venue / meeting link.
- **`GET /api/bookings/junior/{juniorId}`**: Fetch all bookings for a junior student.
- **`GET /api/bookings/tutor/{tutorId}`**: Fetch all bookings assigned to a tutor.
- **`PUT /api/bookings/{id}/cancel`**: Cancel booking and free up the slot (`AVAILABLE`).
- **`PUT /api/bookings/{id}/complete`**: Mark booking as `COMPLETED`.

### Feedback (`/api/feedback`)
- **`POST /api/feedback`**: Submit rating (1–5 stars) & review comment for a completed booking.
- **`GET /api/feedback/tutor/{tutorId}`**: Get all feedback reviews for a specific tutor.
- **`GET /api/feedback/booking/{bookingId}`**: Get feedback for a specific booking.

---

## 🎨 5. Frontend Architecture & User Flows

1. **Session Management (`auth.js`)**:
   - Stores current logged-in user object in `localStorage.setItem('currentUser', JSON.stringify(user))`.
   - Utility functions: `getCurrentUser()`, `isLoggedIn()`, `isTutor()`, `isJunior()`, `logout()`, `requireAuth()`.

2. **API Communication (`api.js`)**:
   - `apiRequest(endpoint, method, data)` centralizes request headers, JSON stringification, HTTP error parsing, and fallback exception handling.

3. **User Flow**:
   - **Visitor** -> `index.html` -> Register as `JUNIOR` or `TUTOR` (`register.html`) or Login (`login.html`).
   - **Junior User** -> Redirected to `junior-dashboard.html`. Browses tutors on `tutors.html`. Filters by subject. Clicks tutor to view profile on `tutor-profile.html`. Selects slot & venue (`LIBRARY` vs `ONLINE`). Confirms booking -> `booking-confirmation.html`. Attends session -> Leaves rating on `feedback.html`.
   - **Tutor User** -> Redirected to `tutor-dashboard.html`. Defines available time slots on `availability.html`. Views upcoming student bookings and marks sessions as `COMPLETED`.

---

## 🏃 6. How to Run the Application

### Option A: Node.js Mock Server (Fast Development Mode)
No database setup required.
```bash
node server.js
```
*Runs API server on `http://localhost:8080` with in-memory seed data.*

### Option B: Spring Boot + MySQL (Production / Full Stack Mode)
1. Create database:
   ```sql
   CREATE DATABASE IF NOT EXISTS peer_mentoring_db;
   ```
2. Configure credentials in `src/main/resources/application.properties`.
3. Run backend:
   ```powershell
   mvn spring-boot:run
   ```
4. Open `frontend/index.html` in browser or run via Live Server.

---

## 🚀 7. Key Business Logic & Implementation Rules for Future AI Development

1. **Rating Calculation**: Average rating and total review counts for tutors are dynamically aggregated from the `feedback` table (do not hardcode rating in `tutor_profiles`).
2. **Slot Status Integrity**: When a booking is created (`POST /api/bookings`), the slot status **MUST** transition from `AVAILABLE` to `BOOKED` within a transaction to prevent double booking.
3. **Password Security**: Passwords stored in `users` table are hashed using `BCryptPasswordEncoder`. Do not store or return plain text passwords in DTOs (`UserDTO` explicitly excludes password).
4. **CORS Handling**: `CorsConfig.java` enables global CORS mapping (`allowedOrigins("*")`). Any new REST controller must retain standard `@CrossOrigin` compatibility.
5. **No Heavy Frontend Framework**: Maintain the raw HTML/CSS/Vanilla JS architecture for high performance, portability, and instant loading without build tools.

---

*This document was auto-generated to serve as a complete knowledge transfer blueprint for AI coding agents.*
