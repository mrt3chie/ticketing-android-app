package com.example.eventmanagementapp.Model;

public class Events {
    private int categoryId;
    private String eventName;
    private String eventLocation;
    private String eventPrice;
    private String eventDescription;
    private String eventimagePath;
    private String eventDate;

    public Events() {
    }

    @Override
    public String toString() {
        return eventName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventPrice() {
        return eventPrice;
    }

    public void setEventPrice(String eventPrice) {
        this.eventPrice = eventPrice;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventimagePath() {
        return eventimagePath;
    }

    public void setEventimagePath(String eventimagePath) {
        this.eventimagePath = eventimagePath;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }
}
