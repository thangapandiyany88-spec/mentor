/**
 * Tutor Availability Slot Management Module
 */

async function loadTutorAvailability() {
    const user = checkAuth("TUTOR");
    if (!user || !user.tutorProfileId) return;

    await loadTutorSubjectOptions(user.tutorProfileId);
    await refreshSlotsList(user.tutorProfileId);
}

async function loadTutorSubjectOptions(tutorId) {
    const select = document.getElementById("slotSubjectSelect");
    if (!select) return;
    try {
        const tutor = await apiRequest(`/tutors/${tutorId}`);
        if (tutor.subjects && tutor.subjects.length > 0) {
            select.innerHTML = tutor.subjects.map(s => `
                <option value="${s.id}">${s.subjectName} (${s.subjectCode})</option>
            `).join("");
        } else {
            select.innerHTML = `<option value="">No subjects assigned to your profile</option>`;
        }
    } catch (e) {
        console.error("Error loading tutor subjects", e);
    }
}

async function handleAddSlot(event) {
    event.preventDefault();
    const user = getCurrentUser();
    const date = document.getElementById("slotDate").value;
    const startTime = document.getElementById("slotStartTime").value;
    const endTime = document.getElementById("slotEndTime").value;
    const subjectId = document.getElementById("slotSubjectSelect").value;
    const alertBox = document.getElementById("availabilityAlert");

    if (!subjectId) {
        showAlert(alertBox, "Please select a subject for this slot.", "danger");
        return;
    }

    const payload = {
        tutorId: user.tutorProfileId,
        subjectId: parseInt(subjectId),
        slotDate: date,
        startTime: startTime,
        endTime: endTime
    };

    try {
        await apiRequest("/availability", "POST", payload);
        showAlert(alertBox, "Availability slot added successfully!", "success");
        document.getElementById("availabilityForm").reset();
        await refreshSlotsList(user.tutorProfileId);
    } catch (error) {
        showAlert(alertBox, error.message, "danger");
    }
}

async function refreshSlotsList(tutorId) {
    const tableBody = document.getElementById("slotsTableBody");
    if (!tableBody) return;

    try {
        const slots = await apiRequest(`/availability/tutor/${tutorId}`);
        if (!slots || slots.length === 0) {
            tableBody.innerHTML = `<tr><td colspan="6" style="text-align: center; color: var(--text-muted);">No availability slots created yet.</td></tr>`;
            return;
        }

        tableBody.innerHTML = slots.map(slot => `
            <tr>
                <td>${slot.slotDate}</td>
                <td>${formatTime(slot.startTime)} – ${formatTime(slot.endTime)}</td>
                <td><span class="subject-pill">${slot.subject ? slot.subject.subjectName : 'General'}</span></td>
                <td>
                    <span class="badge ${slot.status === 'AVAILABLE' ? 'badge-success' : (slot.status === 'BOOKED' ? 'badge-warning' : 'badge-danger')}">
                        ${slot.status}
                    </span>
                </td>
                <td>
                    ${slot.status === 'AVAILABLE' ? `
                        <button onclick="deleteAvailabilitySlot(${slot.id})" class="btn btn-sm btn-danger">Delete</button>
                    ` : '<span style="font-size:0.8rem; color:var(--text-muted);">Booked</span>'}
                </td>
            </tr>
        `).join("");
    } catch (e) {
        tableBody.innerHTML = `<tr><td colspan="6" class="text-danger">Failed to load slots.</td></tr>`;
    }
}

async function deleteAvailabilitySlot(slotId) {
    if (!confirm("Are you sure you want to remove this availability slot?")) return;
    const user = getCurrentUser();
    try {
        await apiRequest(`/availability/${slotId}`, "DELETE");
        await refreshSlotsList(user.tutorProfileId);
    } catch (error) {
        alert(error.message);
    }
}

function showAlert(el, msg, type) {
    if (!el) return;
    el.className = `alert alert-${type}`;
    el.innerText = msg;
    el.style.display = "block";
    setTimeout(() => { el.style.display = "none"; }, 4000);
}
