package com.example.eventmanagementapp.Model;

public class BookingModel {

    private String eventName;
    private String bookingId;
    private String bookingDate;
    private int totalTickets;
    private String scannedStatus;
    private int amount;
    private String eventDate;
    private String userID;

    public BookingModel(){

    }


    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }


    public int getTotalTickets() {
        return totalTickets;
    }

    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public String getScannedStatus() {
        return scannedStatus;
    }

    public void setScannedStatus(String scannedStatus) {
        this.scannedStatus = scannedStatus;
    }


    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }


}
