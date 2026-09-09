# 📘 Peer-to-Peer Academic Mentoring Hub - Operational Runbook

---

## 📌 Document Overview & Control

| Attribute | Details |
|---|---|
| **System Name** | Peer-to-Peer Academic Mentoring & Doubt Clearing Hub |
| **Document Version** | 1.0.0 |
| **Last Updated** | September 2026 |
| **Primary Maintainers** | Core Engineering & DevOps Team |
| **Base Repository** | [`c:\Users\tamil\mentor hub`](file:///c:/Users/tamil/mentor%20hub) |
| **Primary Backend URL** | `http://localhost:8080/api` |
| **Primary Frontend Path** | [`frontend/index.html`](file:///c:/Users/tamil/mentor%20hub/frontend/index.html) |

---

## 🏗️ 1. System Topology & Architecture

The **Peer-to-Peer Academic Mentoring & Doubt Clearing Hub** is a multi-tier web application allowing junior college students to schedule 1-on-1 mentoring and doubt-clearing sessions with senior tutors across academic subjects.

```
       +-------------------------------------------------------------+
       |               Browser Client (HTML5 / CSS3 / ES6)           |
       |  - Glassmorphism Design System (frontend/css/style.css)      |
       |  - Native Fetch API Client (frontend/js/api.js)             |
       +------------------------------+------------------------------+
                                      |
                     REST Calls (HTTP | Port 8080)
                                      v
       +-------------------------------------------------------------+
       |             Backend Layer (Dual Execution Modes)            |
       |                                                             |
       |  Mode A: Node.js Mock API       Mode B: Spring Boot 3.2.3   |
       |  (server.js - Zero Config)       (Java 17 / Spring Web)     |
       +------------------------------+------------------------------+
                                      |
                       JDBC Connection| Port 3306
                                      v
       +-------------------------------------------------------------+
       |                  MySQL 8.0 Relational DB                    |
       |               (Database: peer_mentoring_db)                 |
       +-------------------------------------------------------------+
```

### Component & Network Port Mapping

| Component | Technology | Default Port | Config File / Location |
|---|---|---|---|
| **Backend REST API (Spring Boot)** | Java 17 / Spring Boot 3.2.3 | `8080` | [`application.properties`](file:///c:/Users/tamil/mentor%20hub/src/main/resources/application.properties) |
| **Mock Server & Static Host** | Node.js Built-in HTTP Module | `8080` | [`server.js`](file:///c:/Users/tamil/mentor%20hub/server.js) |
| **Relational Database** | MySQL Server 8.0 | `3306` | Database: `peer_mentoring_db` |
| **Frontend Static Application** | HTML5, CSS3, ES6 JavaScript | Static File / HTTP Live Server | [`frontend/index.html`](file:///c:/Users/tamil/mentor%20hub/frontend/index.html) |

---

## ⚙️ 2. Environment & Prerequisites Checklist

Before executing operational tasks, ensure the operating system environment satisfies the software matrix below:

### Software Requirements Matrix

| Software Tool | Required Version | Verification Command | Expected Output Sample |
|---|---|---|---|
| **Java Development Kit (JDK)** | OpenJDK / Oracle JDK 17+ | `java -version` | `openjdk version "17.0.x"` or higher |
| **Apache Maven** | 3.8.0+ | `mvn -version` | `Apache Maven 3.8.x ...` |
| **Node.js** | 16.0.0+ | `node -v` | `v16.x.x` or `v18.x.x` or `v20.x.x` |
| **MySQL Server** | 8.0+ | `mysql --version` | `mysql  Ver 8.0.x for Win64 ...` |

> [!NOTE]
> Node.js is **optional** if running exclusively in Spring Boot + MySQL mode, but required if running in Fast Mock Server mode.

---

## ⚙️ 3. Application Configuration Reference

Backend configuration is specified in [`src/main/resources/application.properties`](file:///c:/Users/tamil/mentor%20hub/src/main/resources/application.properties):

```properties
# Server Configuration
server.port=8080

# MySQL Datasource Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/peer_mentoring_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA & Hibernate Settings
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect

# SQL Script Execution Configuration
spring.sql.init.mode=always
spring.jpa.defer-datasource-initialization=true

# Logging Level Configuration
logging.level.org.springframework.web=INFO
logging.level.com.peermentoring=DEBUG
```

### Environment Variable Overrides

Spring Boot allows overriding properties dynamically via environment variables without changing source code:

```powershell
# Windows PowerShell Example
$env:SPRING_DATASOURCE_USERNAME="my_user"
$env:SPRING_DATASOURCE_PASSWORD="my_secure_password"
$env:SERVER_PORT="8080"
mvn spring-boot:run
```

---

## 🏃 4. Execution & Operational Modes

The application supports three operational deployment modes depending on development, testing, or production needs.

---

### Mode A: Zero-Dependency Node.js Mock Mode (Fast Dev / Demo)

This mode runs a standalone mock REST API server in Node.js with built-in in-memory sample seed data. It requires **no MySQL installation** and **no Maven build**.

```powershell
# Navigate to project root directory
cd "c:\Users\tamil\mentor hub"

# Start Node.js Mock API Server
node server.js
```

- **API Base URL**: `http://localhost:8080/api`
- **Frontend Live Host**: Serves frontend static files directly from `http://localhost:8080`
- **Seed Users Provided**:
  - `arun@college.edu` (Tutor)
  - `priya@college.edu` (Tutor)
  - `rahul@college.edu` (Tutor)
  - `ananya@college.edu` (Junior)

---

### Mode B: Full-Stack Spring Boot + MySQL Mode (Standard Dev / QA)

This is the standard full-stack execution mode using Spring Boot 3.2.3 JPA backend connected to a local MySQL 8.0 database.

#### Step 1: Database Initialization
Open MySQL Workbench or MySQL CLI and run:
```sql
CREATE DATABASE IF NOT EXISTS peer_mentoring_db;
```

#### Step 2: Launch Backend
```powershell
# Open terminal in project root
cd "c:\Users\tamil\mentor hub"

# Execute Maven Spring Boot Plugin
mvn spring-boot:run
```

> [!IMPORTANT]
> On startup, Spring Boot automatically loads table structures from [`schema.sql`](file:///c:/Users/tamil/mentor%20hub/src/main/resources/schema.sql) and inserts seed data from [`data.sql`](file:///c:/Users/tamil/mentor%20hub/src/main/resources/data.sql).

#### Step 3: Launch Frontend UI
Open [`frontend/index.html`](file:///c:/Users/tamil/mentor%20hub/frontend/index.html) directly in a Web Browser (Chrome/Edge/Firefox) or launch via VS Code **Live Server** extension.

---

### Mode C: Production Build & Deployment (JAR Execution)

To package and run the application as an executable production Web Application Archive (JAR):

```powershell
# 1. Clean previous build artifacts & create production JAR
mvn clean package -DskipTests

# 2. Execute target JAR file
java -jar target/peer-mentoring-hub-0.0.1-SNAPSHOT.jar
```

---

## 📑 5. Standard Operating Procedures (SOPs)

### SOP-01: Cold System Startup Sequence

1. **Verify Database Service Status**:
   ```powershell
   # Ensure MySQL service is running on Windows
   Get-Service -Name "MySQL80" | Select-Status
   # If Stopped, start service:
   Start-Service -Name "MySQL80"
   ```
2. **Verify Database Creation**:
   ```sql
   SHOW DATABASES LIKE 'peer_mentoring_db';
   ```
3. **Launch Backend Service**:
   ```powershell
   cd "c:\Users\tamil\mentor hub"
   mvn spring-boot:run
   ```
4. **Confirm Application Readiness**:
   Observe console logs for: `Started PeerMentoringApplication in X.XXX seconds (JVM running for Y.YYY)`.
5. **Open Frontend Interface**:
   Navigate browser to [`frontend/index.html`](file:///c:/Users/tamil/mentor%20hub/frontend/index.html).

---

### SOP-02: Database Schema & Seed Data Reset

When database state needs to be completely reset to initial default state:

```powershell
# 1. Stop Spring Boot application (Ctrl + C in terminal)

# 2. Login to MySQL CLI and re-create database
mysql -u root -p -e "DROP DATABASE IF EXISTS peer_mentoring_db; CREATE DATABASE peer_mentoring_db;"

# 3. Restart Spring Boot application to automatically re-populate schema and seed data
mvn spring-boot:run
```

---

### SOP-03: Graceful Service Shutdown & Restart

#### Stopping the Service
- **Terminal Execution**: Press `Ctrl + C` in the running PowerShell console.
- **Process ID Termination**:
  ```powershell
  # Find PID bound to port 8080
  Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue | Select-Object OwningProcess

  # Stop target process
  Stop-Process -Id <PID> -Force
  ```

#### Restarting the Service
```powershell
mvn spring-boot:run
```

---

### SOP-04: System Health Verification & Sanity Check

Perform an API smoke test after starting the system:

```powershell
# 1. Query Course Catalog
Invoke-RestMethod -Uri "http://localhost:8080/api/subjects" -Method GET

# 2. Query Tutors Catalog
Invoke-RestMethod -Uri "http://localhost:8080/api/tutors" -Method GET
```

---

### SOP-05: User Session Reset & Authentication Cleanups

If front-end session state exhibits cached or invalid login states:

1. Open Browser Developer Tools (`F12`).
2. Go to **Application** -> **Local Storage** -> `http://127.0.0.1` (or local file domain).
3. Clear key `currentUser`.
4. Refresh browser page to reset to initial unauthenticated state.

---

## 🧪 6. REST API Operational Reference & cURL Cheatsheet

Base URL: `http://localhost:8080/api`

### 1. Authentication Endpoints

#### User Registration (`POST /api/auth/register`)
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "fullName": "Kavita Nair",
    "email": "kavita@college.edu",
    "password": "password123",
    "department": "CSE",
    "year": 3,
    "role": "TUTOR"
  }'
```

#### User Login (`POST /api/auth/login`)
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "arun@college.edu",
    "password": "123456"
  }'
```

---

### 2. Tutor Catalog & Profiles (`/api/tutors`)

#### List Tutors with Dynamic Filters
```bash
# Get all tutors
curl -X GET "http://localhost:8080/api/tutors"

# Filter by subject code/name
curl -X GET "http://localhost:8080/api/tutors?subject=DSA"

# Filter by department & keyword search
curl -X GET "http://localhost:8080/api/tutors?department=CSE&search=Arun"
```

#### Fetch Tutor Profile by ID
```bash
curl -X GET "http://localhost:8080/api/tutors/1"
```

---

### 3. Subject Catalog (`/api/subjects`)

#### Fetch All Academic Subjects
```bash
curl -X GET "http://localhost:8080/api/subjects"
```

---

### 4. Availability Slot Management (`/api/availability`)

#### Fetch Open Slots for Tutor
```bash
curl -X GET "http://localhost:8080/api/availability/tutor/1/available"
```

#### Publish New Availability Slot (Tutor Only)
```bash
curl -X POST http://localhost:8080/api/availability \
  -H "Content-Type: application/json" \
  -d '{
    "tutorId": 1,
    "subjectId": 1,
    "slotDate": "2026-09-25",
    "startTime": "10:00",
    "endTime": "10:30"
  }'
```

---

### 5. Session Booking Management (`/api/bookings`)

#### Create Session Booking (Junior Only)
```bash
curl -X POST http://localhost:8080/api/bookings \
  -H "Content-Type: application/json" \
  -d '{
    "juniorId": 5,
    "tutorId": 1,
    "subjectId": 1,
    "slotId": 1,
    "meetingType": "LIBRARY"
  }'
```

#### Mark Booking Completed (Tutor / System)
```bash
curl -X PUT http://localhost:8080/api/bookings/1/complete
```

#### Cancel Booking
```bash
curl -X PUT http://localhost:8080/api/bookings/1/cancel
```

---

### 6. Ratings & Feedback (`/api/feedback`)

#### Submit Review & 1-5 Star Rating
```bash
curl -X POST http://localhost:8080/api/feedback \
  -H "Content-Type: application/json" \
  -d '{
    "bookingId": 1,
    "juniorId": 5,
    "rating": 5,
    "comment": "Outstanding session! Clear explanation of Binary Search Trees."
  }'
```

#### View Reviews for Tutor
```bash
curl -X GET "http://localhost:8080/api/feedback/tutor/1"
```

---

## 🔍 7. Logging, Diagnostics & Monitoring

### Application Log Configuration

Spring Boot log levels are configured in [`application.properties`](file:///c:/Users/tamil/mentor%20hub/src/main/resources/application.properties):

- **Application Logs**: `logging.level.com.peermentoring=DEBUG`
- **Spring Framework Web**: `logging.level.org.springframework.web=INFO`
- **SQL Execution Queries**: `spring.jpa.show-sql=true`

### Key Log Signatures to Monitor

| Log Message Pattern | Meaning | Action / Interpretation |
|---|---|---|
| `HikariPool-1 - Added connection` | JDBC Connection Pool initialized | Normal operational behavior |
| `HHH000400: Configured SessionFactory` | Hibernate ORM initialization complete | Database tables synced successfully |
| `ResourceNotFoundException: ...` | API requested non-existent ID | Normal handling for bad request |
| `BadRequestException: ...` | Business validation violation | Check payload parameters |

### Useful SQL Inspection Queries

```sql
-- Check total registered users by role
SELECT role, COUNT(*) FROM users GROUP BY role;

-- Inspect pending or confirmed bookings
SELECT b.id, u.full_name AS junior, tp.id AS tutor_id, s.subject_name, b.status, b.meeting_type, b.venue
FROM bookings b
JOIN users u ON b.junior_id = u.id
JOIN tutor_profiles tp ON b.tutor_id = tp.id
JOIN subjects s ON b.subject_id = s.id;

-- Check dynamic average ratings per tutor
SELECT tutor_id, AVG(rating) as avg_rating, COUNT(*) as review_count
FROM feedback
GROUP BY tutor_id;
```

---

## ⚠️ 8. Incident Response & Troubleshooting Matrix

### Failure Matrix

| Issue Code / Error Message | Root Cause | Immediate Resolution Step |
|---|---|---|
| **ERR-01**: `Access denied for user 'root'@'localhost'` | MySQL password in [`application.properties`](file:///c:/Users/tamil/mentor%20hub/src/main/resources/application.properties) does not match local MySQL installation | Update `spring.datasource.password` to match your local root password. |
| **ERR-02**: `Unknown database 'peer_mentoring_db'` | Database has not been created in MySQL server | Run `CREATE DATABASE peer_mentoring_db;` in MySQL Workbench or CLI. |
| **ERR-03**: `Port 8080 is already in use` | Another process (or previous instance of Spring Boot / Node.js) is running on port 8080 | Execute `Stop-Process -Id (Get-NetTCPConnection -LocalPort 8080).OwningProcess -Force` |
| **ERR-04**: `CORS policy error` in browser console | Frontend origin blocked by API server | Verify CORS mapping in [`CorsConfig.java`](file:///c:/Users/tamil/mentor%20hub/src/main/java/com/peermentoring/config/CorsConfig.java). Ensure backend service is running. |
| **ERR-05**: `Slot is no longer available` | Slot was already booked by another user or cancelled | Slot status changed to `BOOKED`. Refresh tutor availability picker. |
| **ERR-06**: `Cannot book session with yourself` | User tried to book a session where `junior_id == tutor.user_id` | Business rule prevents self-booking. Log in with a separate Junior account. |
| **ERR-07**: `Invalid email or password` during login | Password mismatch or user does not exist | Verify user seed password (`123456` for default seed users). |

---

### Step-by-Step Recovery Procedures

#### Recovery Procedure 1: Resolving Port 8080 Conflicts on Windows

```powershell
# Step 1: Locate process using port 8080
$ProcessId = (Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue).OwningProcess

# Step 2: Terminate offending process
if ($ProcessId) {
    Stop-Process -Id $ProcessId -Force
    Write-Host "Process $ProcessId killed successfully."
} else {
    Write-Host "Port 8080 is free."
}
```

#### Recovery Procedure 2: Fixing MySQL Authentication Failures

1. Test connection via CLI:
   ```powershell
   mysql -u root -p
   ```
2. If password differs from `root`, update [`src/main/resources/application.properties`](file:///c:/Users/tamil/mentor%20hub/src/main/resources/application.properties):
   ```properties
   spring.datasource.password=YOUR_ACTUAL_MYSQL_PASSWORD
   ```
3. Re-run application:
   ```powershell
   mvn spring-boot:run
   ```

---

## 💾 9. Backup, Maintenance & Disaster Recovery

### Database Backup SOP (`mysqldump`)

To generate an full SQL backup dump of the production database:

```powershell
# Backup database to backup script file
mysqldump -u root -p --databases peer_mentoring_db > "c:\Users\tamil\mentor hub\backup_peer_mentoring_db.sql"
```

### Database Restore SOP

To restore database from a saved SQL dump file:

```powershell
# Restore database from backup script
mysql -u root -p peer_mentoring_db < "c:\Users\tamil\mentor hub\backup_peer_mentoring_db.sql"
```

### Automated Seed Recovery

If seed data is corrupted or lost:
1. Delete all database tables or drop `peer_mentoring_db`.
2. Restart Spring Boot (`mvn spring-boot:run`).
3. Spring Boot will execute [`schema.sql`](file:///c:/Users/tamil/mentor%20hub/src/main/resources/schema.sql) and [`data.sql`](file:///c:/Users/tamil/mentor%20hub/src/main/resources/data.sql) automatically on startup.

---

## 📞 10. Escalation & Contact Matrix

| Role | Responsibility | Contact / Channel |
|---|---|---|
| **Lead Developer** | Architecture, Spring Boot Backend & Data Models | `dev@peermentoring.edu` |
| **Frontend Engineer** | User Experience, Design System, HTML/CSS/JS | `fe@peermentoring.edu` |
| **Database Administrator** | MySQL Schema, Performance, Indexing, Backups | `dba@peermentoring.edu` |

---

*End of Operational Runbook.*
