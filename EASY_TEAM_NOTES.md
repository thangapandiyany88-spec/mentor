# 💡 Simple & Easy Presentation Notes for Teammates

> **Peer-to-Peer Academic Mentoring & Doubt Clearing Hub**  
> Share these simple, short notes with your team so everyone can easily understand and explain their part to the tutor!

---

# 🎨 1. FRONTEND NOTES (For Frontend Lead)

### 📌 What is the Frontend?
The Frontend is everything the user sees, clicks, and interacts with in the web browser.

### 🛠️ Technology Used
* **HTML5**: Page structure and forms.
* **CSS3**: Glassmorphism design system (`style.css`), buttons, cards, and mobile responsiveness (`responsive.css`).
* **Vanilla JavaScript (ES6)**: Pure JS for dynamic UI changes and API calls (No heavy frameworks like React or Angular used, making it super fast and lightweight!).

### 📁 Main Files You Own
* `frontend/index.html` ➔ Landing page with hero banner and project features.
* `frontend/register.html` & `login.html` ➔ Registration & login forms (Select **Junior Student** or **Senior Tutor**).
* `frontend/junior-dashboard.html` ➔ Junior dashboard showing upcoming bookings and search shortcuts.
* `frontend/tutor-dashboard.html` ➔ Tutor dashboard to manage time slots and student session requests.
* `frontend/tutors.html` & `tutor-profile.html` ➔ Browse tutors by subject/department, view tutor CGPA/reviews, and pick a time slot.
* `frontend/feedback.html` ➔ Interactive 1 to 5 star rating & review submission form.
* `frontend/js/api.js` ➔ Sends HTTP requests to the backend using native browser `fetch()`.
* `frontend/js/auth.js` ➔ Saves logged-in user info in browser memory (`localStorage`).

### 🗣️ How to Explain Your Part to Tutor in 30 Seconds:
> *"I built the user interface using HTML5, CSS3, and Vanilla JavaScript. We chose pure JS without heavy frameworks so the app loads instantly. Juniors can browse tutors, filter by subject, pick an available slot, and submit star feedback. Tutors get a dedicated dashboard to publish their available time slots. All pages communicate with the backend using the native JavaScript Fetch API."*

---

# ⚙️ 2. BACKEND NOTES (For Backend Lead)

### 📌 What is the Backend?
The Backend is the server-side brain of the project. It listens to user requests, runs business rules, encrypts passwords, and fetches data from the database.

### 🛠️ Technology Used
* **Java 17 & Spring Boot 3.2.3**: Industry standard enterprise framework.
* **Spring Web**: Exposes REST API endpoints.
* **Spring Security Crypto**: Hashes user passwords using **BCrypt**.
* **Node.js (`server.js`)**: An additional standalone mock server for fast testing without needing Java/MySQL.

### 📁 Main Folders & Files You Own
* `src/main/java/com/peermentoring/`
  * `controller/` ➔ REST API endpoints (`AuthController`, `TutorController`, `BookingController`, `FeedbackController`).
  * `service/` ➔ Core logic (`AuthService`, `BookingService`, `TutorService` for password hashing, booking validation, and dynamic rating calculations).
  * `dto/` ➔ Data objects sent to/from frontend (`RegisterRequest`, `BookingRequest`, `UserDTO` — excludes sensitive passwords).
  * `exception/` ➔ Handles errors gracefully (`GlobalExceptionHandler.java` returns clean JSON error messages).
  * `config/CorsConfig.java` ➔ Allows the browser frontend to talk to the backend without CORS blocks.
* `server.js` ➔ Standalone zero-dependency Node.js mock API server running on port `8080`.

### 🗣️ How to Explain Your Part to Tutor in 30 Seconds:
> *"I developed the backend REST APIs using Java and Spring Boot. It processes requests, hashes passwords securely using BCrypt, and enforces core business rules—like stopping students from booking themselves or preventing double-booking of time slots. We also implemented a clean Controller-Service-Repository architecture and built a backup Node.js mock server (`server.js`) for quick offline testing."*

---

# 🗄️ 3. DATABASE NOTES (For Database Lead)

### 📌 What is the Database?
The Database is the persistent storage layer that safely holds all registered users, academic subjects, tutor availability slots, bookings, and feedback reviews.

### 🛠️ Technology Used
* **MySQL 8.0**: Relational Database Management System.
* **Spring Data JPA / Hibernate ORM**: Automatically maps Java objects to MySQL database tables.

### 📁 Main Tables & Files You Own
* `src/main/resources/schema.sql` ➔ SQL DDL script creating all 7 database tables.
* `src/main/resources/data.sql` ➔ SQL DML script inserting sample seed data (subjects, tutors, slots, reviews).
* `src/main/resources/application.properties` ➔ Database connection settings (URL, root credentials).

#### The 7 Database Tables:
1. `users`: Stores user account info (`id`, `full_name`, `email`, `password`, `department`, `year`, `role`).
2. `tutor_profiles`: Stores tutor details (`bio`, `cgpa`, `is_available`) — 1-to-1 link with `users`.
3. `subjects`: Catalog of courses (`DSA101`, `DBMS201`, `JAVA201`, `PYTHON101`, `MAT101`, `OS301`).
4. `tutor_subjects`: Junction table linking Tutors to the subjects they teach (Many-to-Many).
5. `availability_slots`: Slots published by tutors (`slot_date`, `start_time`, `end_time`, `status`: `AVAILABLE` / `BOOKED`).
6. `bookings`: Session reservations (`meeting_type`: `LIBRARY` vs `ONLINE`, `venue`, `meeting_link`, `status`).
7. `feedback`: Stores 1 to 5 star ratings and written reviews.

### 🗣️ How to Explain Your Part to Tutor in 30 Seconds:
> *"I designed the relational database schema in MySQL with 7 structured tables. We enforced foreign key integrity with cascading deletes and added check constraints for ratings (1 to 5 stars). Rather than storing hardcoded rating values, tutor average ratings are dynamically calculated from the feedback table for 100% data accuracy. We also automated database setup using `schema.sql` and `data.sql`."*

---

# 🔄 4. INTEGRATION & FLOW SUMMARY (Quick Sheet for All 3)

```
[ FRONTEND ] ──(Fetch API / JSON)──> [ BACKEND ] ──(Spring Data JPA)──> [ DATABASE ]
HTML / CSS / JS                      Spring Boot 3                       MySQL 8.0
(Port 8080)                          (Port 8080)                         (Port 3306)
```

1. **Junior logs in** on Frontend (`login.html`) ➔ Backend validates password with BCrypt ➔ Database checks `users` table.
2. **Junior picks a slot** on Frontend (`tutor-profile.html`) ➔ Backend checks rules & updates slot status to `BOOKED` ➔ Database updates `availability_slots` & inserts into `bookings`.
3. **Junior submits 5-star review** on Frontend (`feedback.html`) ➔ Backend saves review ➔ Database inserts into `feedback` table and automatically recalculates tutor's average rating!

---

*Copy and share this guide with your teammates!*
