/**
 * Tutor Catalog, Search, Filter & Detailed Profile Module
 */

async function loadTutors(subjectFilter = "", departmentFilter = "", searchKeyword = "") {
    const container = document.getElementById("tutorsContainer");
    if (!container) return;

    container.innerHTML = `<div style="grid-column: 1/-1; text-align: center; padding: 3rem; color: var(--text-muted);">Loading peer tutors...</div>`;

    try {
        let endpoint = "/tutors?";
        if (subjectFilter) endpoint += `subject=${encodeURIComponent(subjectFilter)}&`;
        if (departmentFilter) endpoint += `department=${encodeURIComponent(departmentFilter)}&`;
        if (searchKeyword) endpoint += `search=${encodeURIComponent(searchKeyword)}&`;

        const tutors = await apiRequest(endpoint);

        if (!tutors || tutors.length === 0) {
            container.innerHTML = `
                <div style="grid-column: 1/-1; text-align: center; padding: 4rem; background: var(--bg-card); border-radius: var(--radius-lg); border: var(--glass-border);">
                    <h3 style="margin-bottom: 0.5rem;">No peer tutors found</h3>
                    <p style="color: var(--text-muted);">Try adjusting your search query or subject filters.</p>
                </div>`;
            return;
        }

        container.innerHTML = tutors.map(tutor => renderTutorCard(tutor)).join("");
    } catch (error) {
        container.innerHTML = `
            <div style="grid-column: 1/-1;" class="alert alert-danger">
                Failed to load tutors: ${error.message}. Please make sure the Spring Boot backend server is running.
            </div>`;
    }
}

function renderTutorCard(tutor) {
    const stars = renderStarRating(tutor.averageRating);
    const subjectsHTML = (tutor.subjects || []).map(s => `<span class="subject-pill">${s.subjectName}</span>`).join(" ");
    
    return `
        <div class="card" style="display: flex; flex-direction: column; justify-content: space-between;">
            <div>
                <div style="display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 1rem;">
                    <div>
                        <h3 style="font-size: 1.25rem; color: var(--text-primary);">${tutor.fullName}</h3>
                        <p style="font-size: 0.85rem; color: var(--text-secondary);">${tutor.department} — ${tutor.year}${getOrdinal(tutor.year)} Year</p>
                    </div>
                    <span class="badge badge-info">${tutor.cgpa ? tutor.cgpa.toFixed(1) : '8.0'} CGPA</span>
                </div>

                <div style="margin-bottom: 1rem; display: flex; flex-wrap: wrap; gap: 0.4rem;">
                    ${subjectsHTML || '<span class="subject-pill">General Mentoring</span>'}
                </div>

                <p style="font-size: 0.9rem; color: var(--text-muted); margin-bottom: 1.25rem; display: -webkit-box; -webkit-line-clamp: 3; -webkit-box-orient: vertical; overflow: hidden;">
                    ${tutor.bio || 'Experienced senior tutor ready to assist with academic concepts and project doubt clearing.'}
                </p>
            </div>

            <div>
                <div style="display: flex; align-items: center; justify-content: space-between; margin-bottom: 1.25rem; padding-top: 0.75rem; border-top: var(--border-color) 1px solid;">
                    <div class="rating-stars">
                        ${stars} <span class="rating-num">${tutor.averageRating > 0 ? tutor.averageRating.toFixed(1) : 'New'}</span>
                    </div>
                    <span style="font-size: 0.8rem; color: var(--text-muted);">${tutor.totalReviews} ${tutor.totalReviews === 1 ? 'Review' : 'Reviews'}</span>
                </div>

                <div style="display: flex; gap: 0.5rem;">
                    <a href="tutor-profile.html?id=${tutor.id}" class="btn btn-secondary btn-sm" style="flex: 1;">View Profile</a>
                    <button onclick="openBookingModal(${tutor.id}, '${tutor.fullName.replace(/'/g, "\\'")}')" class="btn btn-primary btn-sm" style="flex: 1;">Book Slot</button>
                </div>
            </div>
        </div>
    `;
}

function renderStarRating(rating) {
    let stars = "";
    const fullStars = Math.floor(rating);
    const hasHalf = rating % 1 >= 0.5;
    for (let i = 1; i <= 5; i++) {
        if (i <= fullStars) {
            stars += "★";
        } else if (i === fullStars + 1 && hasHalf) {
            stars += "★";
        } else {
            stars += "☆";
        }
    }
    return stars;
}

function getOrdinal(n) {
    const s = ["th", "st", "nd", "rd"];
    const v = n % 100;
    return s[(v - 20) % 10] || s[v] || s[0];
}

// Subject dropdown dynamic populator
async function loadSubjectDropdown(selectId = "subjectFilter") {
    const select = document.getElementById(selectId);
    if (!select) return;
    try {
        const subjects = await apiRequest("/subjects");
        subjects.forEach(subj => {
            const opt = document.createElement("option");
            opt.value = subj.subjectName;
            opt.textContent = `${subj.subjectName} (${subj.subjectCode})`;
            select.appendChild(opt);
        });
    } catch (e) {
        console.warn("Could not load subjects for dropdown", e);
    }
}
