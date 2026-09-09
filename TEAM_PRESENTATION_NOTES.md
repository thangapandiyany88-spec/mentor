# 🎓 Peer-to-Peer Academic Mentoring Hub - Team Presentation & Evaluation Guide

> **Guide for 4-Member Team Presentation to Tutor/Evaluator**  
> Project: **Peer-to-Peer Academic Mentoring & Doubt Clearing Hub**  
> Architecture: **Full-Stack (Spring Boot 3 + MySQL 8 + Vanilla Web UI & Node.js Mock)**

---

## 📋 Role Division & Script Summary

```
                      +------------------------------------------+
                      |           TEAM PRESENTATION              |
                      +--------------------+---------------------+
                                           |
     +------------------+------------------+------------------+------------------+
     |                  |                                     |                  |
     v                  v                                     v                  v
+----------+      +-----------+                         +-----------+      +-----------+
| Member 1 |      | Member 2  |                         | Member 3  |      | Member 4  |
| Frontend |      |  Backend  |                         | Database  |      | QA & Ops  |
|   Lead   |      |   Lead    |                         |   Lead    |      |   Lead    |
+----------+      +-----------+                         +-----------+      +-----------+
```

---

## 🎨 Member 1: Frontend Lead

### 🎯 Primary Responsibility
Explaining the User Interface architecture, UI design system, page navigation flows, and Vanilla JavaScript integration.

---

### 💬 Key Talking Points for Tutor
1. **No-Framework Philosophy (Vanilla HTML5 / CSS3 / ES6)**:
   > *"We deliberately built the frontend using raw HTML5, CSS3, and ES6 JavaScript without heavy frameworks like React or Angular. This ensures zero bundle overhead, instantaneous page load times, and maximum browser compatibility."*

2. **Design System & Aesthetics (`style.css` & `responsive.css`)**:
   > *"We developed a modern **Glassmorphism design system** featuring CSS custom variables, soft frosted glass backdrops, smooth gradient buttons, interactive star selectors, and tablet/mobile responsive breakpoints."*

3. **Page Structure & User Journeys**:
   - **`index.html`**: Landing page with hero banner and quick navigation features.
   - **`register.html` & `login.html`**: Role-based authentication screens (selecting `JUNIOR` student vs `SENIOR TUTOR`).
   - **`junior-dashboard.html`**: Junior student overview showing upcoming/past bookings and quick tutor search shortcuts.
   - **`tutor-dashboard.html`**: Senior tutor portal showing published slots, incoming session reservations, and session completion toggles.
   - **`tutors.html` & `tutor-profile.html`**: Dynamic tutor search catalog with live filtering by subject and department, displaying CGPA, bio, average star ratings, and an interactive slot picker modal.
   - **`availability.html`**: Slot publisher interface for tutors.
   - **`my-sessions.html` & `booking-confirmation.html`**: Receipt display for session details (Library Room vs Online video link).
   - **`feedback.html`**: 1-to-5 star rating selector and review comment submission.

4. **JavaScript Module Architecture (`frontend/js/`)**:
   - **`api.js`**: Centralized `apiRequest(endpoint, method, data)` wrapper built around the native browser `Fetch API` handling header injection and error parsing.
   - **`auth.js`**: Manages session state via `localStorage`, exposing helper functions like `getCurrentUser()`, `isTutor()`, `isJunior()`, `logout()`, and `requireAuth()`.
   - **`tutors.js`, `booking.js`, `availability.js`, `feedback.js`, `dashboard.js`**: Modular DOM handlers for user interactions.

---

### ❓ Possible Questions by Evaluator & Answers
- **Q: Why didn't you use React/Vue?**
  - *Answer*: Vanilla JS eliminates build tool setup (Webpack/Vite), keeps deployment ultra-lightweight, and demonstrates core DOM manipulation and Fetch API skills.
- **Q: How is user session maintained across pages?**
  - *Answer*: We store user profile details in browser `localStorage` under the key `currentUser`. `auth.js` acts as a route guard on every page.

---

## ⚙️ Member 2: Backend Lead

### 🎯 Primary Responsibility
Explaining the Spring Boot architecture, REST API design, controller-service-repository layered pattern, DTO pattern, and security.

---

### 💬 Key Talking Points for Tutor
1. **Technology Stack**:
   > *"The backend is powered by **Java 17** and **Spring Boot 3.2.3**, utilizing Spring Web, Spring Data JPA, and Spring Security Crypto."*

2. **Layered Software Architecture**:
   - **Controller Layer (`com.peermentoring.controller`)**: Exposes RESTful endpoints for Auth, Tutors, Subjects, Availability, Bookings, and Feedback.
   - **Service Layer (`com.peermentoring.service`)**: Encapsulates core business logic, validation rules, BCrypt hashing, and dynamic rating calculations.
   - **Repository Layer (`com.peermentoring.repository`)**: Extends Spring Data JPA `JpaRepository` for data access.
   - **DTO Layer (`com.peermentoring.dto`)**: Separates internal DB entities from client payload DTOs (`RegisterRequest`, `UserDTO`, `BookingResponse`, etc.), ensuring sensitive fields like passwords are never exposed.

3. **Core REST API Endpoints**:
   - `POST /api/auth/register` & `POST /api/auth/login` (Authentication)
   - `GET /api/tutors?subject=DSA` (Search & Filter Tutors)
   - `POST /api/availability` (Slot creation)
   - `POST /api/bookings` (Create reservation & transition slot to `BOOKED`)
   - `PUT /api/bookings/{id}/complete` (Mark session completed)
   - `POST /api/feedback` (Submit rating & review)

4. **Security & Exception Handling**:
   - **BCrypt Password Hashing**: Passwords stored in `users` table are hashed using `BCryptPasswordEncoder`.
   - **CORS Configuration (`CorsConfig.java`)**: Configures WebMvc CORS mapping to allow seamless frontend cross-origin requests.
   - **Unified Error Handling (`GlobalExceptionHandler.java`)**: Catches custom exceptions (`ResourceNotFoundException`, `BadRequestException`) and returns standardized JSON error responses with timestamps and HTTP status codes.

5. **Standalone Node.js Mock API Server (`server.js`)**:
   > *"In addition to Spring Boot, we built a zero-dependency **Node.js mock server** (`server.js`) using built-in `http` and `fs` modules. This allows immediate testing and demonstration on port 8080 without requiring Java or MySQL dependencies."*

---

### ❓ Possible Questions by Evaluator & Answers
- **Q: How do you prevent plain text passwords from being saved?**
  - *Answer*: In `AuthService.java`, passwords pass through `BCryptPasswordEncoder.encode()` before persisting, and `UserDTO` excludes the password field.
- **Q: What happens if a requested resource ID does not exist?**
  - *Answer*: The service throws `ResourceNotFoundException`, caught by `@ControllerAdvice` in `GlobalExceptionHandler.java`, which returns a `404 Not Found` JSON error response.

---

## 🗄️ Member 3: Database Lead

### 🎯 Primary Responsibility
Explaining the relational schema design, Entity-Relationship (ER) model, foreign key constraints, JPA entity mappings, and database seed scripts.

---

### 💬 Key Talking Points for Tutor
1. **Database Engine & ORM**:
   > *"We use **MySQL 8.0** as our relational database management system, integrated with Spring Boot via **Hibernate / Spring Data JPA**."*

2. **Schema & Tables Overview ([`schema.sql`](file:///c:/Users/tamil/mentor%20hub/src/main/resources/schema.sql))**:
   - **`users`**: Primary table for user accounts (`id`, `full_name`, `email`, `password`, `department`, `year`, `role`).
   - **`tutor_profiles`**: 1-to-1 extension table linked to `users` for tutors (`bio`, `cgpa`, `is_available`).
   - **`subjects`**: Academic course catalog (`subject_code`, `subject_name`, `description`).
   - **`tutor_subjects`**: Many-to-Many junction table mapping tutors to subjects they teach.
   - **`availability_slots`**: Slots created by tutors (`slot_date`, `start_time`, `end_time`, `status`: `AVAILABLE` / `BOOKED` / `CANCELLED`).
   - **`bookings`**: Reservations made by junior students (`meeting_type`: `LIBRARY` vs `ONLINE`, `venue`, `meeting_link`, `status`: `CONFIRMED` / `COMPLETED` / `CANCELLED`).
   - **`feedback`**: 1-to-1 review table linked to `bookings` storing `rating` (1–5 stars) and `comment`.

3. **Key Database Constraints & Business Integrity**:
   - `ON DELETE CASCADE` foreign keys maintain data cleanliness if a user or booking is deleted.
   - `rating INT CHECK (rating >= 1 AND rating <= 5)` enforces rating boundaries.
   - **Dynamic Rating Computation**: Rather than storing static ratings in `tutor_profiles`, tutor average ratings and review counts are dynamically aggregated from the `feedback` table.

4. **Database Initialization & Seed Scripts**:
   - [`schema.sql`](file:///c:/Users/tamil/mentor%20hub/src/main/resources/schema.sql): DDL creation script for tables.
   - [`data.sql`](file:///c:/Users/tamil/mentor%20hub/src/main/resources/data.sql): DML script inserting seed data for 7 core subjects, sample tutors (`Arun`, `Priya`, `Rahul`, `Siddharth`), slots, bookings, and feedback reviews.

---

### ❓ Possible Questions by Evaluator & Answers
- **Q: How are Tutors linked to Subjects?**
  - *Answer*: Via the `tutor_subjects` junction table implementing a Many-to-Many relationship between `tutor_profiles` and `subjects`.
- **Q: Why isn't `average_rating` stored as a column in `tutor_profiles`?**
  - *Answer*: Storing computed values leads to data redundancy and update anomalies. Aggregating ratings dynamically from `feedback` guarantees 100% data consistency.

---

## 🧪 Member 4: Integration, QA & Operations Lead

### 🎯 Primary Responsibility
Explaining end-to-end integration flows, business rule enforcement, Postman/cURL API testing, system execution modes, and troubleshooting.

---

### 💬 Key Talking Points for Tutor
1. **End-to-End System Workflow Integration**:
   > *"I verified the seamless integration across all 4 system layers: Frontend Browser -> Spring Boot Controllers -> Service Business Logic -> MySQL Database."*

2. **Core Business Rules Enforced**:
   - **Rule 1: Self-Booking Guard**: A student cannot book a session with themselves (`junior_id != tutor.user_id`).
   - **Rule 2: Slot Locking Integrity**: Booking a session automatically transitions slot status from `AVAILABLE` to `BOOKED` within a single database transaction to prevent double bookings.
   - **Rule 3: Venue Generation**: The system automatically assigns a Library Room number for `LIBRARY` sessions or generates a video meeting link for `ONLINE` sessions.

3. **API Validation & QA Testing**:
   - Verified all REST API endpoints using **Postman collections** and **cURL scripts**.
   - Verified HTTP status codes: `201 Created` for registrations/bookings, `200 OK` for successful queries, `400 Bad Request` for rule violations, `404 Not Found` for missing resources.

4. **Dual Execution & Operations ([`RUNBOOK.md`](file:///c:/Users/tamil/mentor%20hub/RUNBOOK.md))**:
   - **Fast Mock Mode**: `node server.js` for instant testing without database overhead.
   - **Full-Stack Mode**: `mvn spring-boot:run` with MySQL 8.0 database.
   - Comprehensive troubleshooting matrix for port `8080` conflicts, database authentication, and CORS.

---

### ❓ Possible Questions by Evaluator & Answers
- **Q: How did you test for double-booking issues?**
  - *Answer*: We simulated concurrent booking requests for the same `slot_id`. The backend checks slot status `AVAILABLE` inside the booking transaction; the first succeeds and updates status to `BOOKED`, while subsequent requests fail with `BadRequestException`.
- **Q: How do you run the project if MySQL is not available during demo?**
  - *Answer*: We execute `node server.js`, which launches our Node.js mock API server on port 8080 with in-memory seed data.

---

## 🚀 Presentation Flow Strategy (5-Minute Team Demo)

| Minute | Presenter | Topic Covered |
|---|---|---|
| **0:00 - 1:00** | **Member 1 (Frontend)** | Project overview, problem statement, live UI walkthrough on `index.html`, `tutors.html`, and `tutor-profile.html`. |
| **1:00 - 2:00** | **Member 2 (Backend)** | Spring Boot 3 architecture, Controller-Service-Repository pattern, REST API endpoints, DTO pattern, and BCrypt security. |
| **2:00 - 3:00** | **Member 3 (Database)** | ER Diagram, 7 MySQL tables, FK relationships, `schema.sql`/`data.sql`, and dynamic rating aggregation. |
| **3:00 - 4:00** | **Member 4 (QA & Ops)** | End-to-End demo (booking a slot, venue link generation, completing session, submitting 5-star feedback), business rules, and dual run modes (`node server.js`). |
| **4:00 - 5:00** | **Entire Team** | Q&A with Evaluator / Tutor. |

---

*This document is ready to share directly with your teammates for presentation preparation!*
