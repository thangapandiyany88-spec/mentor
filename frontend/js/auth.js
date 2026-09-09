/**
 * Authentication & Session Management Module
 */

function getCurrentUser() {
    const userStr = localStorage.getItem("mentor_hub_user");
    if (!userStr) return null;
    try {
        return JSON.parse(userStr);
    } catch (e) {
        return null;
    }
}

function setCurrentUser(user) {
    localStorage.setItem("mentor_hub_user", JSON.stringify(user));
}

function logoutUser() {
    localStorage.removeItem("mentor_hub_user");
    window.location.href = "login.html";
}

function checkAuth(requiredRole = null) {
    const user = getCurrentUser();
    if (!user) {
        window.location.href = "login.html";
        return null;
    }
    if (requiredRole && user.role !== requiredRole) {
        if (user.role === "JUNIOR") {
            window.location.href = "junior-dashboard.html";
        } else {
            window.location.href = "tutor-dashboard.html";
        }
        return null;
    }
    return user;
}

function updateNavigation() {
    const user = getCurrentUser();
    const navLinks = document.getElementById("navLinks");
    if (!navLinks) return;

    if (user) {
        let roleDashboard = user.role === "TUTOR" ? "tutor-dashboard.html" : "junior-dashboard.html";
        let linksHTML = `
            <li><a href="index.html">Home</a></li>
            <li><a href="${roleDashboard}">Dashboard</a></li>
            <li><a href="tutors.html">Find Tutors</a></li>
        `;
        if (user.role === "JUNIOR") {
            linksHTML += `<li><a href="my-sessions.html">My Sessions</a></li>`;
        } else if (user.role === "TUTOR") {
            linksHTML += `<li><a href="availability.html">Manage Availability</a></li>`;
            linksHTML += `<li><a href="my-sessions.html">Booked Sessions</a></li>`;
        }
        linksHTML += `
            <li><span class="user-badge">${user.fullName} (${user.role})</span></li>
            <li><a href="#" onclick="logoutUser()" class="btn btn-sm btn-danger">Logout</a></li>
        `;
        navLinks.innerHTML = linksHTML;
    } else {
        navLinks.innerHTML = `
            <li><a href="index.html">Home</a></li>
            <li><a href="tutors.html">Find Tutors</a></li>
            <li><a href="login.html" class="btn btn-sm btn-secondary">Login</a></li>
            <li><a href="register.html" class="btn btn-sm btn-primary">Register</a></li>
        `;
    }
}

document.addEventListener("DOMContentLoaded", updateNavigation);
