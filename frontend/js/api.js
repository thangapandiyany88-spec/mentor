/**
 * Centralized API Fetch Helper for Peer-to-Peer Academic Mentoring Hub.
 * Communicates with Spring Boot / Node REST Backend on http://localhost:8080/api
 * Falls back seamlessly to client-side mock data if backend server is offline (e.g. GitHub Pages).
 */
const API_BASE_URL = "http://localhost:8080/api";

// Fallback Mock Dataset for GitHub Pages & standalone static mode
const MOCK_DATA = {
    subjects: [
        { id: 1, subjectCode: "DSA101", subjectName: "Data Structures & Algorithms", description: "Trees, Graphs, Pointers, Sorting" },
        { id: 2, subjectCode: "DBMS201", subjectName: "Database Management Systems", description: "SQL, Normalization, Transactions" },
        { id: 3, subjectCode: "JAVA201", subjectName: "Java Programming", description: "OOP, Collections, Streams" },
        { id: 4, subjectCode: "PYTHON101", subjectName: "Python Programming", description: "Core Syntax, Scripting" },
        { id: 5, subjectCode: "MAT101", subjectName: "Mathematics", description: "Linear Algebra, Probability" },
        { id: 6, subjectCode: "OS301", subjectName: "Operating Systems", description: "Processes, Memory Management" }
    ],
    tutors: [
        {
            id: 1, userId: 1, fullName: "Arun Kumar", email: "arun@college.edu", department: "Computer Science", year: 3,
            bio: "Passionate 3rd-year CSE student specializing in Data Structures, Algorithms, and Java competitive programming.",
            cgpa: 9.2, isAvailable: true, subjects: [{ id: 1, subjectCode: "DSA101", subjectName: "Data Structures" }, { id: 3, subjectCode: "JAVA201", subjectName: "Java" }],
            averageRating: 4.8, totalReviews: 12
        },
        {
            id: 2, userId: 2, fullName: "Priya Sharma", email: "priya@college.edu", department: "Computer Science", year: 4,
            bio: "Final year CSE student with strong expertise in DBMS, SQL normalization, and Operating Systems.",
            cgpa: 9.4, isAvailable: true, subjects: [{ id: 2, subjectCode: "DBMS201", subjectName: "DBMS" }, { id: 6, subjectCode: "OS301", subjectName: "Operating Systems" }],
            averageRating: 4.9, totalReviews: 18
        },
        {
            id: 3, userId: 3, fullName: "Rahul Kumar", email: "rahul@college.edu", department: "Information Technology", year: 3,
            bio: "3rd-year IT student focused on Engineering Mathematics, Linear Algebra, and Python development.",
            cgpa: 8.9, isAvailable: true, subjects: [{ id: 4, subjectCode: "PYTHON101", subjectName: "Python" }, { id: 5, subjectCode: "MAT101", subjectName: "Mathematics" }],
            averageRating: 4.5, totalReviews: 8
        }
    ],
    slots: [
        { id: 1, tutorId: 1, subject: { id: 1, subjectName: "Data Structures" }, slotDate: "2026-09-20", startTime: "10:00:00", endTime: "10:30:00", status: "AVAILABLE" },
        { id: 2, tutorId: 1, subject: { id: 3, subjectName: "Java" }, slotDate: "2026-09-21", startTime: "14:00:00", endTime: "14:30:00", status: "AVAILABLE" },
        { id: 3, tutorId: 2, subject: { id: 2, subjectName: "DBMS" }, slotDate: "2026-09-20", startTime: "15:00:00", endTime: "15:30:00", status: "AVAILABLE" }
    ],
    bookings: [
        {
            id: 1, bookingCode: "BK10001", juniorId: 5, juniorName: "Ananya Sen", tutorId: 1, tutorName: "Arun Kumar",
            subjectId: 1, subjectName: "Data Structures", bookingDate: "2026-09-20", startTime: "10:00 AM", endTime: "10:30 AM",
            meetingType: "LIBRARY", venue: "Library Room 4", status: "CONFIRMED"
        }
    ]
};

async function apiRequest(endpoint, method = "GET", data = null) {
    const config = {
        method: method,
        headers: {
            "Content-Type": "application/json",
            "Accept": "application/json"
        }
    };

    if (data && (method === "POST" || method === "PUT" || method === "PATCH")) {
        config.body = JSON.stringify(data);
    }

    try {
        const response = await fetch(`${API_BASE_URL}${endpoint}`, config);
        
        let result;
        const contentType = response.headers.get("content-type");
        if (contentType && contentType.includes("application/json")) {
            result = await response.json();
        } else {
            result = await response.text();
        }

        if (!response.ok) {
            const errorMessage = (typeof result === 'object' && result.message) ? result.message : `Error ${response.status}: Request failed`;
            throw new Error(errorMessage);
        }

        return result;
    } catch (error) {
        console.warn(`[API Server Unreachable] Using fallback mock data for: ${method} ${endpoint}`);
        return handleMockFallback(endpoint, method, data);
    }
}

function handleMockFallback(endpoint, method, data) {
    const cleanUrl = endpoint.split('?')[0];

    if (cleanUrl.startsWith("/tutors/") && method === "GET") {
        const id = parseInt(cleanUrl.split("/")[2]);
        const tutor = MOCK_DATA.tutors.find(t => t.id === id);
        return tutor || MOCK_DATA.tutors[0];
    }
    if (cleanUrl.startsWith("/tutors") && method === "GET") {
        return MOCK_DATA.tutors;
    }
    if (cleanUrl.startsWith("/subjects") && method === "GET") {
        return MOCK_DATA.subjects;
    }
    if (cleanUrl.startsWith("/availability") && method === "GET") {
        return MOCK_DATA.slots;
    }
    if (cleanUrl.startsWith("/bookings") && method === "GET") {
        return MOCK_DATA.bookings;
    }
    if (cleanUrl.startsWith("/bookings") && method === "POST") {
        const newBooking = {
            id: MOCK_DATA.bookings.length + 1,
            bookingCode: "BK" + (10001 + MOCK_DATA.bookings.length),
            tutorName: "Peer Tutor",
            subjectName: "Mentoring Session",
            bookingDate: "2026-09-22",
            startTime: "10:00 AM",
            endTime: "10:30 AM",
            status: "CONFIRMED",
            ...data
        };
        MOCK_DATA.bookings.push(newBooking);
        return newBooking;
    }
    if (cleanUrl.startsWith("/auth/login")) {
        return { id: 1, fullName: "Demo User", email: data ? data.email : "user@college.edu", role: "JUNIOR" };
    }
    if (cleanUrl.startsWith("/auth/register")) {
        return { id: 99, fullName: data ? data.fullName : "New User", email: data ? data.email : "new@college.edu", role: data ? data.role : "JUNIOR" };
    }

    return [];
}

