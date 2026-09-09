-- Database Creation Script
-- Run this in MySQL Workbench or MySQL Command Line Interface:
-- CREATE DATABASE IF NOT EXISTS peer_mentoring_db;
-- USE peer_mentoring_db;

-- 1. USERS Table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    department VARCHAR(50) NOT NULL,
    year INT NOT NULL,
    role VARCHAR(20) NOT NULL, -- 'JUNIOR' or 'TUTOR'
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. TUTOR_PROFILES Table
CREATE TABLE IF NOT EXISTS tutor_profiles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT UNIQUE NOT NULL,
    bio TEXT,
    cgpa DOUBLE NOT NULL,
    is_available BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 3. SUBJECTS Table
CREATE TABLE IF NOT EXISTS subjects (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    subject_code VARCHAR(20) NOT NULL UNIQUE,
    subject_name VARCHAR(100) NOT NULL,
    description TEXT
);

-- 4. TUTOR_SUBJECTS Junction Table (Many-to-Many)
CREATE TABLE IF NOT EXISTS tutor_subjects (
    tutor_id BIGINT NOT NULL,
    subject_id BIGINT NOT NULL,
    PRIMARY KEY (tutor_id, subject_id),
    FOREIGN KEY (tutor_id) REFERENCES tutor_profiles(id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE
);

-- 5. AVAILABILITY_SLOTS Table
CREATE TABLE IF NOT EXISTS availability_slots (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tutor_id BIGINT NOT NULL,
    subject_id BIGINT NOT NULL,
    slot_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE', -- 'AVAILABLE', 'BOOKED', 'CANCELLED'
    FOREIGN KEY (tutor_id) REFERENCES tutor_profiles(id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE
);

-- 6. BOOKINGS Table
CREATE TABLE IF NOT EXISTS bookings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    junior_id BIGINT NOT NULL,
    tutor_id BIGINT NOT NULL,
    subject_id BIGINT NOT NULL,
    slot_id BIGINT NOT NULL,
    booking_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    meeting_type VARCHAR(20) NOT NULL, -- 'LIBRARY', 'ONLINE'
    venue VARCHAR(100) NOT NULL,
    meeting_link VARCHAR(255),
    status VARCHAR(20) NOT NULL DEFAULT 'CONFIRMED', -- 'PENDING', 'CONFIRMED', 'COMPLETED', 'CANCELLED'
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (junior_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (tutor_id) REFERENCES tutor_profiles(id) ON DELETE CASCADE,
    FOREIGN KEY (subject_id) REFERENCES subjects(id) ON DELETE CASCADE,
    FOREIGN KEY (slot_id) REFERENCES availability_slots(id) ON DELETE CASCADE
);

-- 7. FEEDBACK Table
CREATE TABLE IF NOT EXISTS feedback (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_id BIGINT UNIQUE NOT NULL,
    junior_id BIGINT NOT NULL,
    tutor_id BIGINT NOT NULL,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE CASCADE,
    FOREIGN KEY (junior_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (tutor_id) REFERENCES tutor_profiles(id) ON DELETE CASCADE
);
