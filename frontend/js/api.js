/**
 * Centralized API Fetch Helper for Peer-to-Peer Academic Mentoring Hub.
 * Communicates with Spring Boot REST Backend on http://localhost:8080/api
 */
const API_BASE_URL = "http://localhost:8080/api";

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
        console.error(`[API Error] ${method} ${endpoint}:`, error);
        throw error;
    }
}
