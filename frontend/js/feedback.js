/**
 * Feedback & Star Rating Module
 */

let selectedRating = 0;

function initStarRating() {
    const container = document.getElementById("starRatingContainer");
    if (!container) return;

    const stars = container.querySelectorAll("span");
    stars.forEach((star, index) => {
        star.addEventListener("click", () => {
            selectedRating = index + 1;
            updateStarDisplay(stars, selectedRating);
            document.getElementById("selectedRatingVal").innerText = `${selectedRating} of 5 Stars`;
        });
        star.addEventListener("mouseover", () => {
            updateStarDisplay(stars, index + 1);
        });
        star.addEventListener("mouseout", () => {
            updateStarDisplay(stars, selectedRating);
        });
    });
}

function updateStarDisplay(stars, rating) {
    stars.forEach((star, idx) => {
        if (idx < rating) {
            star.classList.add("selected");
            star.innerText = "★";
        } else {
            star.classList.remove("selected");
            star.innerText = "☆";
        }
    });
}

async function loadFeedbackPageData() {
    const user = checkAuth("JUNIOR");
    if (!user) return;

    const urlParams = new URLSearchParams(window.location.search);
    const bookingId = urlParams.get("bookingId");
    const alertBox = document.getElementById("feedbackAlert");

    if (!bookingId) {
        showAlert(alertBox, "Missing booking reference parameter.", "danger");
        return;
    }

    try {
        const bookings = await apiRequest(`/bookings/junior/${user.id}`);
        const booking = bookings.find(b => b.id == bookingId);

        if (!booking) {
            showAlert(alertBox, "Booking record not found.", "danger");
            return;
        }

        if (booking.status !== "COMPLETED") {
            showAlert(alertBox, "Feedback can only be submitted after the mentoring session is marked COMPLETED.", "danger");
            document.getElementById("feedbackSubmitBtn").disabled = true;
        }

        document.getElementById("tutorName").innerText = booking.tutorName;
        document.getElementById("subjectName").innerText = booking.subjectName;
        document.getElementById("sessionDate").innerText = `${booking.bookingDate} (${booking.startTime} - ${booking.endTime})`;
    } catch (error) {
        showAlert(alertBox, error.message, "danger");
    }
}

async function handleFeedbackSubmit(event) {
    event.preventDefault();
    const user = getCurrentUser();
    const urlParams = new URLSearchParams(window.location.search);
    const bookingId = urlParams.get("bookingId");
    const comment = document.getElementById("feedbackComment").value;
    const alertBox = document.getElementById("feedbackAlert");

    if (selectedRating < 1 || selectedRating > 5) {
        showAlert(alertBox, "Please select a star rating between 1 and 5.", "danger");
        return;
    }

    const payload = {
        bookingId: parseInt(bookingId),
        juniorId: user.id,
        rating: selectedRating,
        comment: comment
    };

    try {
        await apiRequest("/feedback", "POST", payload);
        showAlert(alertBox, "Thank you! Your feedback and rating have been submitted successfully.", "success");
        setTimeout(() => {
            window.location.href = "my-sessions.html";
        }, 1500);
    } catch (error) {
        showAlert(alertBox, error.message, "danger");
    }
}
