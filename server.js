/**
 * Node.js Mock API Server for Peer-to-Peer Academic Mentoring Hub
 * Runs on http://localhost:8080 using built-in Node.js modules.
 */

const http = require('http');
const fs = require('fs');
const path = require('path');
const PORT = 8080;

const MIME_TYPES = {
    '.html': 'text/html',
    '.css': 'text/css',
    '.js': 'application/javascript',
    '.json': 'application/json',
    '.png': 'image/png',
    '.jpg': 'image/jpeg',
    '.svg': 'image/svg+xml'
};

// Seed Data
let users = [
    { id: 1, fullName: "Arun Kumar", email: "arun@college.edu", department: "CSE", year: 3, role: "TUTOR", tutorProfileId: 1 },
    { id: 2, fullName: "Priya Sharma", email: "priya@college.edu", department: "CSE", year: 4, role: "TUTOR", tutorProfileId: 2 },
    { id: 3, fullName: "Rahul Kumar", email: "rahul@college.edu", department: "IT", year: 3, role: "TUTOR", tutorProfileId: 3 },
    { id: 5, fullName: "Ananya Sen", email: "ananya@college.edu", department: "CSE", year: 2, role: "JUNIOR" }
];

let subjects = [
    { id: 1, subjectCode: "DSA101", subjectName: "Data Structures & Algorithms", description: "Trees, Graphs, Pointers, Sorting" },
    { id: 2, subjectCode: "DBMS201", subjectName: "Database Management Systems", description: "SQL, Normalization, Transactions" },
    { id: 3, subjectCode: "JAVA201", subjectName: "Java Programming", description: "OOP, Collections, Streams" },
    { id: 4, subjectCode: "PYTHON101", subjectName: "Python Programming", description: "Core Syntax, Scripting" },
    { id: 5, subjectCode: "MAT101", subjectName: "Mathematics", description: "Linear Algebra, Probability" },
    { id: 6, subjectCode: "OS301", subjectName: "Operating Systems", description: "Processes, Memory Management" }
];

let tutorProfiles = [
    {
        id: 1, userId: 1, fullName: "Arun Kumar", email: "arun@college.edu", department: "CSE", year: 3,
        bio: "Passionate 3rd-year CSE student specializing in DSA and Java competitive programming.",
        cgpa: 9.2, isAvailable: true, subjects: [subjects[0], subjects[2]], averageRating: 4.8, totalReviews: 5
    },
    {
        id: 2, userId: 2, fullName: "Priya Sharma", email: "priya@college.edu", department: "CSE", year: 4,
        bio: "Final year CSE student with strong expertise in DBMS and Operating Systems.",
        cgpa: 9.4, isAvailable: true, subjects: [subjects[1], subjects[5]], averageRating: 4.9, totalReviews: 8
    },
    {
        id: 3, userId: 3, fullName: "Rahul Kumar", email: "rahul@college.edu", department: "IT", year: 3,
        bio: "3rd-year IT student focused on Engineering Mathematics and Python development.",
        cgpa: 8.9, isAvailable: true, subjects: [subjects[3], subjects[4]], averageRating: 4.5, totalReviews: 3
    }
];

let slots = [
    { id: 1, tutorId: 1, subject: subjects[0], slotDate: "2026-09-20", startTime: "10:00:00", endTime: "10:30:00", status: "AVAILABLE" },
    { id: 2, tutorId: 1, subject: subjects[2], slotDate: "2026-09-21", startTime: "14:00:00", endTime: "14:30:00", status: "AVAILABLE" },
    { id: 3, tutorId: 2, subject: subjects[1], slotDate: "2026-09-20", startTime: "15:00:00", endTime: "15:30:00", status: "AVAILABLE" }
];

let bookings = [];
let feedbacks = [];

const server = http.createServer((req, res) => {
    // CORS Headers
    res.setHeader('Access-Control-Allow-Origin', '*');
    res.setHeader('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE, OPTIONS');
    res.setHeader('Access-Control-Allow-Headers', 'Content-Type, Authorization');

    if (req.method === 'OPTIONS') {
        res.writeHead(200);
        res.end();
        return;
    }

    const url = new URL(req.url, `http://${req.headers.host}`);
    const pathname = url.pathname;

    let body = '';
    req.on('data', chunk => { body += chunk; });
    req.on('end', () => {
        let parsed = {};
        if (body) {
            try { parsed = JSON.parse(body); } catch (e) {}
        }

        res.setHeader('Content-Type', 'application/json');

        // ROUTER
        if (pathname === '/api/tutors' && req.method === 'GET') {
            const subj = url.searchParams.get('subject');
            const search = url.searchParams.get('search');
            let result = tutorProfiles;
            if (subj) {
                result = result.filter(t => t.subjects.some(s => s.subjectName.toLowerCase().includes(subj.toLowerCase())));
            }
            if (search) {
                result = result.filter(t => t.fullName.toLowerCase().includes(search.toLowerCase()) || t.subjects.some(s => s.subjectName.toLowerCase().includes(search.toLowerCase())));
            }
            res.writeHead(200);
            res.end(JSON.stringify(result));
        }
        else if (pathname.startsWith('/api/tutors/') && req.method === 'GET') {
            const id = parseInt(pathname.split('/')[3]);
            const tutor = tutorProfiles.find(t => t.id === id);
            if (tutor) {
                res.writeHead(200);
                res.end(JSON.stringify(tutor));
            } else {
                res.writeHead(404);
                res.end(JSON.stringify({ message: "Tutor not found" }));
            }
        }
        else if (pathname === '/api/auth/register' && req.method === 'POST') {
            const newId = users.length + 1;
            const newUser = {
                id: newId,
                fullName: parsed.fullName,
                email: parsed.email,
                department: parsed.department,
                year: parsed.year,
                role: parsed.role || 'JUNIOR'
            };
            if (newUser.role === 'TUTOR') {
                const newTutorId = tutorProfiles.length + 1;
                newUser.tutorProfileId = newTutorId;
                tutorProfiles.push({
                    id: newTutorId, userId: newId, fullName: parsed.fullName, email: parsed.email,
                    department: parsed.department, year: parsed.year, bio: "Senior Tutor", cgpa: 8.5,
                    isAvailable: true, subjects: [subjects[0]], averageRating: 5.0, totalReviews: 0
                });
            }
            users.push(newUser);
            res.writeHead(201);
            res.end(JSON.stringify(newUser));
        }
        else if (pathname === '/api/auth/login' && req.method === 'POST') {
            const user = users.find(u => u.email.toLowerCase() === (parsed.email || '').toLowerCase());
            if (user) {
                res.writeHead(200);
                res.end(JSON.stringify(user));
            } else {
                res.writeHead(400);
                res.end(JSON.stringify({ message: "Invalid email or password" }));
            }
        }
        else if (pathname === '/api/subjects' && req.method === 'GET') {
            res.writeHead(200);
            res.end(JSON.stringify(subjects));
        }
        else if (pathname.startsWith('/api/availability/tutor/') && req.method === 'GET') {
            const parts = pathname.split('/');
            const tutorId = parseInt(parts[4]);
            const isAvailableOnly = parts.includes('available');
            let resSlots = slots.filter(s => s.tutorId === tutorId);
            if (isAvailableOnly) {
                resSlots = resSlots.filter(s => s.status === 'AVAILABLE');
            }
            res.writeHead(200);
            res.end(JSON.stringify(resSlots));
        }
        else if (pathname === '/api/availability' && req.method === 'POST') {
            const newSlot = {
                id: slots.length + 1,
                tutorId: parsed.tutorId,
                subject: subjects.find(s => s.id === parsed.subjectId) || subjects[0],
                slotDate: parsed.slotDate,
                startTime: parsed.startTime,
                endTime: parsed.endTime,
                status: "AVAILABLE"
            };
            slots.push(newSlot);
            res.writeHead(201);
            res.end(JSON.stringify(newSlot));
        }
        else if (pathname === '/api/bookings' && req.method === 'POST') {
            const slot = slots.find(s => s.id === parsed.slotId);
            const junior = users.find(u => u.id === parsed.juniorId);
            const tutor = tutorProfiles.find(t => t.id === parsed.tutorId);

            if (slot) slot.status = "BOOKED";

            const newBooking = {
                id: bookings.length + 1,
                bookingCode: "BK" + (10001 + bookings.length),
                juniorId: parsed.juniorId,
                juniorName: junior ? junior.fullName : "Junior Student",
                tutorId: parsed.tutorId,
                tutorName: tutor ? tutor.fullName : "Senior Tutor",
                subjectId: parsed.subjectId,
                subjectName: slot ? slot.subject.subjectName : "DSA",
                bookingDate: slot ? slot.slotDate : "2026-09-20",
                startTime: "10:00 AM",
                endTime: "10:30 AM",
                meetingType: parsed.meetingType || "LIBRARY",
                venue: parsed.meetingType === 'ONLINE' ? 'Online' : 'Library Room 4',
                meetingLink: parsed.meetingType === 'ONLINE' ? `https://meet.example.com/session/${bookings.length + 1}` : null,
                status: "CONFIRMED"
            };
            bookings.push(newBooking);
            res.writeHead(201);
            res.end(JSON.stringify(newBooking));
        }
        else if (pathname.startsWith('/api/bookings/junior/') && req.method === 'GET') {
            const juniorId = parseInt(pathname.split('/')[4]);
            res.writeHead(200);
            res.end(JSON.stringify(bookings.filter(b => b.juniorId === juniorId)));
        }
        else if (pathname.startsWith('/api/bookings/tutor/') && req.method === 'GET') {
            const tutorId = parseInt(pathname.split('/')[4]);
            res.writeHead(200);
            res.end(JSON.stringify(bookings.filter(b => b.tutorId === tutorId)));
        }
        else if (pathname.endsWith('/complete') && req.method === 'PUT') {
            const id = parseInt(pathname.split('/')[3]);
            const booking = bookings.find(b => b.id === id);
            if (booking) booking.status = 'COMPLETED';
            res.writeHead(200);
            res.end(JSON.stringify(booking));
        }
        else if (pathname === '/api/feedback' && req.method === 'POST') {
            feedbacks.push(parsed);
            res.writeHead(201);
            res.end(JSON.stringify(parsed));
        }
        else if (pathname.startsWith('/api/feedback/tutor/') && req.method === 'GET') {
            res.writeHead(200);
            res.end(JSON.stringify(feedbacks));
        }
        else if (pathname.startsWith('/api/')) {
            res.writeHead(404);
            res.end(JSON.stringify({ message: "Endpoint not found" }));
        }
        else {
            let filePath = path.join(__dirname, 'frontend', pathname === '/' ? 'index.html' : pathname);
            const ext = path.extname(filePath).toLowerCase();
            const contentType = MIME_TYPES[ext] || 'application/octet-stream';

            fs.readFile(filePath, (err, content) => {
                if (err) {
                    if (err.code === 'ENOENT') {
                        res.writeHead(404, { 'Content-Type': 'text/html' });
                        res.end('<h1>404 Not Found</h1>');
                    } else {
                        res.writeHead(500);
                        res.end(`Server Error: ${err.code}`);
                    }
                } else {
                    res.writeHead(200, { 'Content-Type': contentType });
                    res.end(content, 'utf-8');
                }
            });
        }
    });
});

server.listen(PORT, () => {
    console.log(`\n=======================================================`);
    console.log(`  Peer Mentoring Backend API Server Running on Port ${PORT}!`);
    console.log(`  URL: http://localhost:${PORT}`);
    console.log(`=======================================================\n`);
});
