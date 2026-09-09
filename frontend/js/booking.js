/**
 * Slot Booking Modal & Reservation Flow Module
 */

let currentTutorId = null;
let currentTutorSlots = [];

async function openBookingModal(tutorId, tutorName) {
    const user = getCurrentUser();
    if (!user) {
        alert("Please log in to book a mentoring session.");
        window.location.href = "login.html";
        return;
    }
    if (user.role === "TUTOR" && user.tutorProfileId == tutorId) {
        alert("Rule 1 Enforced: You cannot book a mentoring session with yourself!");
        return;
    }

    currentTutorId = tutorId;
    
    // Inject modal HTML if not already in document
    if (!document.getElementById("bookingModalOverlay")) {
        createBookingModalHTML();
    }

    document.getElementById("modalTutorName").innerText = tutorName || "Peer Tutor";
    document.getElementById("bookingError").style.display = "none";
    document.getElementById("bookingModalOverlay").classList.add("active");

    await fetchModalSlots(tutorId);
}

function closeBookingModal() {
    const modal = document.getElementById("bookingModalOverlay");
    if (modal) modal.classList.remove("active");
}

function createBookingModalHTML() {
    const modalHTML = `
        <div id="bookingModalOverlay" class="modal-overlay">
            <div class="modal-content">
                <div class="modal-header">
                    <h3>Book 30-min Study Slot</h3>
                    <button class="modal-close" onclick="closeBookingModal()">&times;</button>
                </div>
                
                <div id="bookingError" class="alert alert-danger" style="display: none;"></div>

                <form id="bookingForm" onsubmit="handleBookingSubmit(event)">
                    <div class="form-group">
                        <label class="form-label">Tutor</label>
                        <input type="text" id="modalTutorName" class="form-control" readonly style="background: rgba(255,255,255,0.04);">
                    </div>

                    <div class="form-group">
                        <label class="form-label">Select Available Slot</label>
                        <select id="modalSlotSelect" class="form-control" required onchange="handleSlotChange()">
                            <option value="">-- Loading available slots... --</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Subject</label>
                        <input type="text" id="modalSubjectName" class="form-control" readonly placeholder="Selected automatically based on slot">
                    </div>

                    <div class="form-group">
                        <label class="form-label">Meeting Type</label>
                        <div style="display: flex; gap: 1.5rem; margin-top: 0.5rem;">
                            <label style="display: flex; align-items: center; gap: 0.5rem; cursor: pointer;">
                                <input type="radio" name="meetingType" value="LIBRARY" checked onchange="toggleVenueDisplay()">
                                Library Room
                            </label>
                            <label style="display: flex; align-items: center; gap: 0.5rem; cursor: pointer;">
                                <input type="radio" name="meetingType" value="ONLINE" onchange="toggleVenueDisplay()">
                                Online Meeting
                            </label>
                        </div>
                    </div>

                    <div class="form-group" id="venueInfoGroup">
                        <label class="form-label">Assigned Venue</label>
                        <div id="venueDisplay" style="padding: 0.75rem; background: rgba(99, 102, 241, 0.1); border: 1px solid rgba(99, 102, 241, 0.3); border-radius: var(--radius-md); font-weight: 600; color: var(--accent);">
                            Library Room 4
                        </div>
                    </div>

                    <div style="display: flex; justify-content: flex-end; gap: 1rem; margin-top: 1.5rem;">
                        <button type="button" class="btn btn-secondary" onclick="closeBookingModal()">Cancel</button>
                        <button type="submit" class="btn btn-primary" id="confirmBookingBtn">Confirm Booking</button>
                    </div>
                </form>
            </div>
        </div>
    `;
    document.body.insertAdjacentHTML("beforeend", modalHTML);
}

async function fetchModalSlots(tutorId) {
    const slotSelect = document.getElementById("modalSlotSelect");
    slotSelect.innerHTML = `<option value="">Loading slots...</option>`;

    try {
        currentTutorSlots = await apiRequest(`/availability/tutor/${tutorId}/available`);
        
        if (!currentTutorSlots || currentTutorSlots.length === 0) {
            slotSelect.innerHTML = `<option value="">No available 30-min slots for this tutor</option>`;
            document.getElementById("confirmBookingBtn").disabled = true;
            return;
        }

        document.getElementById("confirmBookingBtn").disabled = false;
        slotSelect.innerHTML = `<option value="">-- Choose an Available Slot --</option>` +
            currentTutorSlots.map(s => `
                <option value="${s.id}" data-subject-id="${s.subject.id}" data-subject-name="${s.subject.subjectName}">
                    ${s.slotDate} | ${formatTime(s.startTime)} – ${formatTime(s.endTime)} (${s.subject.subjectName})
                </option>
            `).join("");
    } catch (e) {
        slotSelect.innerHTML = `<option value="">Failed to load slots</option>`;
    }
}

function handleSlotChange() {
    const select = document.getElementById("modalSlotSelect");
    const selectedOption = select.options[select.selectedIndex];
    const subjectName = selectedOption.getAttribute("data-subject-name");
    document.getElementById("modalSubjectName").value = subjectName || "";
}

function toggleVenueDisplay() {
    const meetingType = document.querySelector('input[name="meetingType"]:checked').value;
    const venueDisplay = document.getElementById("venueDisplay");
    if (meetingType === "LIBRARY") {
        venueDisplay.innerText = "Library Room 4";
    } else {
        venueDisplay.innerText = "Online Meeting (Link will be generated upon confirmation)";
    }
}

async function handleBookingSubmit(event) {
    event.preventDefault();
    const user = getCurrentUser();
    const slotId = document.getElementById("modalSlotSelect").value;
    if (!slotId) {
        showBookingError("Please select an available time slot.");
        return;
    }

    const select = document.getElementById("modalSlotSelect");
    const selectedOption = select.options[select.selectedIndex];
    const subjectId = selectedOption.getAttribute("data-subject-id");
    const meetingType = document.querySelector('input[name="meetingType"]:checked').value;

    const payload = {
        juniorId: user.id,
        tutorId: currentTutorId,
        subjectId: parseInt(subjectId),
        slotId: parseInt(slotId),
        meetingType: meetingType
    };

    try {
        const response = await apiRequest("/bookings", "POST", payload);
        closeBookingModal();
        // Redirect to booking confirmation page with booking ID parameter
        window.location.href = `booking-confirmation.html?id=${response.id}`;
    } catch (error) {
        showBookingError(error.message);
    }
}

function showBookingError(msg) {
    const errDiv = document.getElementById("bookingError");
    errDiv.innerText = msg;
    errDiv.style.display = "block";
}

function formatTime(timeStr) {
    if (!timeStr) return "";
    const parts = timeStr.split(":");
    let hours = parseInt(parts[0]);
    const minutes = parts[1];
    const ampm = hours >= 12 ? 'PM' : 'AM';
    hours = hours % 12;
    hours = hours ? hours : 12;
    return `${hours}:${minutes} ${ampm}`;
}
