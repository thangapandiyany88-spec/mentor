package com.peermentoring.dto;

public class FeedbackRequest {
    private Long bookingId;
    private Long juniorId;
    private Integer rating; // 1 to 5
    private String comment;

    public FeedbackRequest() {
    }

    public FeedbackRequest(Long bookingId, Long juniorId, Integer rating, String comment) {
        this.bookingId = bookingId;
        this.juniorId = juniorId;
        this.rating = rating;
        this.comment = comment;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Long getJuniorId() {
        return juniorId;
    }

    public void setJuniorId(Long juniorId) {
        this.juniorId = juniorId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
