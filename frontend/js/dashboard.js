/**
 * Dashboard & Session Management Module
 */

async function loadJuniorDashboard() {
    const user = checkAuth("JUNIOR");
    if (!user) return;

    document.getElementById("welcomeMsg").innerText = `Welcome back, ${user.fullName}! 👋`;

    try {
        const bookings = await apiRequest(`/bookings/junior/${user.id}`);
        renderSessionsTables(bookings, true);
    } catch (error) {
        console.error("Failed to load junior dashboard sessions", error);
    }
}

async function loadTutorDashboard() {
    const user = checkAuth("TUTOR");
    if (!user || !user.tutorProfileId) return;

    document.getElementById("welcomeMsg").innerText = `Welcome back, ${user.fullName}! 👋`;

    try {
        const profile = await apiRequest(`/tutors/${user.tutorProfileId}`);
        document.getElementById("tutorRatingDisplay").innerText = profile.averageRating > 0 ? `${profile.averageRating.toFixed(1)} ★` : "New";
        document.getElementById("tutorReviewsCount").innerText = `${profile.totalReviews} Reviews`;

        const bookings = await apiRequest(`/bookings/tutor/${user.tutorProfileId}`);
        
        const upcomingCount = bookings.filter(b => b.status === "CONFIRMED").length;
        const totalCount = bookings.length;

        document.getElementById("upcomingCount").innerText = upcomingCount;
        document.getElementById("totalSessionsCount").innerText = totalCount;

        renderSessionsTables(bookings, false);
    } catch (error) {
        console.error("Failed to load tutor dashboard metrics", error);
    }
}

async function loadMySessionsPage() {
    const user = checkAuth();
    if (!user) return;

    try {
        let endpoint = user.role === "JUNIOR" ? `/bookings/junior/${user.id}` : `/bookings/tutor/${user.tutorProfileId}`;
        const bookings = await apiRequest(endpoint);
        renderSessionsTables(bookings, user.role === "JUNIOR");
    } catch (error) {
        console.error("Failed to load sessions", error);
    }
}

function renderSessionsTables(bookings, isJunior) {
    const upcomingTable = document.getElementById("upcomingSessionsBody");
    const pastTable = document.getElementById("pastSessionsBody");

    if (!upcomingTable || !pastTable) return;

    const upcoming = bookings.filter(b => b.status === "CONFIRMED" || b.status === "PENDING");
    const past = bookings.filter(b => b.status === "COMPLETED" || b.status === "CANCELLED");

    // Render Upcoming
    if (upcoming.length === 0) {
        upcomingTable.innerHTML = `<tr><td colspan="7" style="text-align: center; color: var(--text-muted);">No upcoming mentoring sessions scheduled.</td></tr>`;
    } else {
        upcomingTable.innerHTML = upcoming.map(b => `
            <tr>
                <td><strong>${b.bookingCode}</strong></td>
                <td>${isJunior ? b.tutorName : b.juniorName}</td>
                <td><span class="subject-pill">${b.subjectName}</span></td>
                <td>${b.bookingDate}</td>
                <td>${b.startTime} – ${b.endTime}</td>
                <td>
                    ${b.meetingType === 'ONLINE' ? `<a href="${b.meetingLink || '#'}" target="_blank" class="badge badge-info">Online Link 🔗</a>` : `<span class="badge badge-success">${b.venue}</span>`}
                </td>
                <td>
                    <span class="badge badge-warning">${b.status}</span>
                    <button onclick="cancelSession(${b.id})" class="btn btn-sm btn-danger" style="margin-left: 0.5rem;">Cancel</button>
                    ${!isJunior ? `<button onclick="completeSession(${b.id})" class="btn btn-sm btn-primary" style="margin-left: 0.25rem;">Complete</button>` : ''}
                </td>
            </tr>
        `).join("");
    }

    // Render Past
    if (past.length === 0) {
        pastTable.innerHTML = `<tr><td colspan="7" style="text-align: center; color: var(--text-muted);">No previous session history.</td></tr>`;
    } else {
        pastTable.innerHTML = past.map(b => `
            <tr>
                <td><strong>${b.bookingCode}</strong></td>
                <td>${isJunior ? b.tutorName : b.juniorName}</td>
                <td><span class="subject-pill">${b.subjectName}</span></td>
                <td>${b.bookingDate}</td>
                <td>${b.startTime} – ${b.endTime}</td>
                <td><span class="badge ${b.status === 'COMPLETED' ? 'badge-success' : 'badge-danger'}">${b.status}</span></td>
                <td>
                    ${(isJunior && b.status === 'COMPLETED') ? `
                        <a href="feedback.html?bookingId=${b.id}" class="btn btn-sm btn-primary">Give Feedback ⭐</a>
                    ` : '<span style="font-size:0.85rem; color:var(--text-muted);">-</span>'}
                </td>
            </tr>
        `).join("");
    }
}

async function cancelSession(bookingId) {
    if (!confirm("Are you sure you want to cancel this booking?")) return;
    try {
        await apiRequest(`/bookings/${bookingId}/cancel`, "PUT");
        location.reload();
    } catch (error) {
        alert(error.message);
    }
}

async function completeSession(bookingId) {
    if (!confirm("Mark this study session as completed?")) return;
    try {
        await apiRequest(`/bookings/${bookingId}/complete`, "PUT");
        location.reload();
    } catch (error) {
        alert(error.message);
    }
}
