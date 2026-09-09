-- Seed Initial Core Subjects
INSERT IGNORE INTO subjects (id, subject_code, subject_name, description) VALUES
(1, 'DSA101', 'Data Structures & Algorithms', 'Pointers, Trees, Graphs, Sorting, and Dynamic Programming concepts.'),
(2, 'DBMS201', 'Database Management Systems', 'Relational Model, SQL queries, Normalization, and Transaction Management.'),
(3, 'JAVA201', 'Java Programming', 'Object Oriented Concepts, Collections Framework, Multithreading, and Streams.'),
(4, 'PYTHON101', 'Python Programming', 'Core Python Syntax, Data Structures, OOP, and Scripting basics.'),
(5, 'MAT101', 'Mathematics', 'Linear Algebra, Discrete Mathematics, Probability, and Statistics.'),
(6, 'OS301', 'Operating Systems', 'Processes, Threads, CPU Scheduling, Memory Management, and Deadlocks.'),
(7, 'CN302', 'Computer Networks', 'OSI Model, TCP/IP, Routing Algorithms, and Network Security.');

-- Seed Initial Users (Password for all sample accounts is '123456')
-- BCrypt Hashed Password for '123456' is '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVym502LNnnX5180vY2b5a2y'
INSERT IGNORE INTO users (id, full_name, email, password, department, year, role, created_at) VALUES
(1, 'Arun Kumar', 'arun@college.edu', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVym502LNnnX5180vY2b5a2y', 'CSE', 3, 'TUTOR', NOW()),
(2, 'Priya Sharma', 'priya@college.edu', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVym502LNnnX5180vY2b5a2y', 'CSE', 4, 'TUTOR', NOW()),
(3, 'Rahul Kumar', 'rahul@college.edu', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVym502LNnnX5180vY2b5a2y', 'IT', 3, 'TUTOR', NOW()),
(4, 'Siddharth Roy', 'siddharth@college.edu', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVym502LNnnX5180vY2b5a2y', 'ECE', 4, 'TUTOR', NOW()),
(5, 'Ananya Sen', 'ananya@college.edu', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVym502LNnnX5180vY2b5a2y', 'CSE', 2, 'JUNIOR', NOW()),
(6, 'Rohan Verma', 'rohan@college.edu', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVym502LNnnX5180vY2b5a2y', 'IT', 2, 'JUNIOR', NOW());

-- Seed Initial Tutor Profiles
INSERT IGNORE INTO tutor_profiles (id, user_id, bio, cgpa, is_available) VALUES
(1, 1, 'Passionate 3rd-year CSE student specializing in DSA and Java competitive programming. Love helping juniors solve complex algorithmic doubts.', 9.2, TRUE),
(2, 2, 'Final year CSE student with strong expertise in DBMS and Operating Systems. Published researcher and DBMS teaching assistant.', 9.4, TRUE),
(3, 3, '3rd-year IT student focused on Engineering Mathematics and Python development. Patient tutor for foundational subjects.', 8.9, TRUE),
(4, 4, '4th-year ECE student with deep knowledge of Computer Networks and Discrete Math.', 9.0, TRUE);

-- Map Tutors to Subjects (Many-to-Many)
-- Arun Kumar: DSA, Java, DBMS
INSERT IGNORE INTO tutor_subjects (tutor_id, subject_id) VALUES (1, 1), (1, 3), (1, 2);
-- Priya Sharma: DBMS, OS, Java
INSERT IGNORE INTO tutor_subjects (tutor_id, subject_id) VALUES (2, 2), (2, 6), (2, 3);
-- Rahul Kumar: Mathematics, Python
INSERT IGNORE INTO tutor_subjects (tutor_id, subject_id) VALUES (3, 5), (3, 4);
-- Siddharth Roy: Computer Networks, Mathematics
INSERT IGNORE INTO tutor_subjects (tutor_id, subject_id) VALUES (4, 7), (4, 5);

-- Seed Initial Availability Slots
INSERT IGNORE INTO availability_slots (id, tutor_id, subject_id, slot_date, start_time, end_time, status) VALUES
(1, 1, 1, '2026-09-20', '10:00:00', '10:30:00', 'AVAILABLE'),
(2, 1, 1, '2026-09-20', '11:00:00', '11:30:00', 'BOOKED'),
(3, 1, 3, '2026-09-21', '14:00:00', '14:30:00', 'AVAILABLE'),
(4, 2, 2, '2026-09-20', '15:00:00', '15:30:00', 'BOOKED'),
(5, 2, 6, '2026-09-22', '16:00:00', '16:30:00', 'AVAILABLE'),
(6, 3, 5, '2026-09-21', '10:00:00', '10:30:00', 'AVAILABLE'),
(7, 3, 4, '2026-09-21', '11:00:00', '11:30:00', 'AVAILABLE');

-- Seed Sample Bookings
INSERT IGNORE INTO bookings (id, junior_id, tutor_id, subject_id, slot_id, booking_date, start_time, end_time, meeting_type, venue, meeting_link, status, created_at) VALUES
(1, 5, 1, 1, 2, '2026-09-20', '11:00:00', '11:30:00', 'LIBRARY', 'Library Room 4', 'https://meet.example.com/session/1', 'COMPLETED', NOW()),
(2, 6, 2, 2, 4, '2026-09-20', '15:00:00', '15:30:00', 'ONLINE', 'Online', 'https://meet.example.com/session/2', 'CONFIRMED', NOW());

-- Seed Sample Feedback & Ratings
INSERT IGNORE INTO feedback (id, booking_id, junior_id, tutor_id, rating, comment, created_at) VALUES
(1, 1, 5, 1, 5, 'Arun explained Graph Traversals BFS and DFS incredibly well! Cleared all my doubts in 30 mins.', NOW());
