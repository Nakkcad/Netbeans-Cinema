package model;

import java.sql.Timestamp;
import java.util.List;

public class Booking {
    private int bookingId;
    private int customerId;
    private List<Integer> screeningSeatIds;
    private int scheduleId;
    private String paymentMethod;
    private double totalPrice;
    private String paymentStatus;
    private Timestamp bookingDate;
    private String qrCodeData;
    private int screenId;  // Added proper screenId field
    
    // Display fields
    private String filmTitle;
    private java.sql.Date screeningDate;
    private java.sql.Time screeningTime;
    private String screenName;
    private String customerName;
    private String seatNumbers;

    // Constructors
    public Booking() {
        this.paymentStatus = "Pending";
    }

    public Booking(int customerId, List<Integer> screeningSeatIds, int scheduleId,
                  String paymentMethod, double totalPrice) {
        this();
        this.customerId = customerId;
        this.screeningSeatIds = screeningSeatIds;
        this.scheduleId = scheduleId;
        this.paymentMethod = paymentMethod;
        this.totalPrice = totalPrice;
    }

    // Getters and Setters
    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }
    
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    
    public List<Integer> getScreeningSeatIds() { return screeningSeatIds; }
    public void setScreeningSeatIds(List<Integer> screeningSeatIds) { 
        this.screeningSeatIds = screeningSeatIds; 
    }
    
    public int getScheduleId() { return scheduleId; }
    public void setScheduleId(int scheduleId) { this.scheduleId = scheduleId; }
    
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    
    public Timestamp getBookingDate() { return bookingDate; }
    public void setBookingDate(Timestamp bookingDate) { this.bookingDate = bookingDate; }
    
    public String getQrCodeData() { return qrCodeData; }
    public void setQrCodeData(String qrCodeData) { this.qrCodeData = qrCodeData; }
    
    public int getScreenId() { return screenId; }
    public void setScreenId(int screenId) { this.screenId = screenId; }
    
    public String getFilmTitle() { return filmTitle; }
    public void setFilmTitle(String filmTitle) { this.filmTitle = filmTitle; }
    
    public java.sql.Date getScreeningDate() { return screeningDate; }
    public void setScreeningDate(java.sql.Date screeningDate) { this.screeningDate = screeningDate; }
    
    public java.sql.Time getScreeningTime() { return screeningTime; }
    public void setScreeningTime(java.sql.Time screeningTime) { this.screeningTime = screeningTime; }
    
    public String getScreenName() { return screenName; }
    public void setScreenName(String screenName) { this.screenName = screenName; }
    
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    
    public String getSeatNumbers() { return seatNumbers; }
    public void setSeatNumbers(String seatNumbers) { this.seatNumbers = seatNumbers; }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId=" + bookingId +
                ", customerId=" + customerId +
                ", scheduleId=" + scheduleId +
                ", screenId=" + screenId +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", totalPrice=" + totalPrice +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", bookingDate=" + bookingDate +
                ", filmTitle='" + filmTitle + '\'' +
                ", screeningDate=" + screeningDate +
                ", screeningTime=" + screeningTime +
                ", seatNumbers='" + seatNumbers + '\'' +
                '}';
    }

    // Helper method to format seat numbers for display
    public String formatSeatNumbers() {
        if (screeningSeatIds == null || screeningSeatIds.isEmpty()) {
            return "N/A";
        }
        // This would be replaced with actual seat formatting logic
        // when you have access to seat data from DAO
        return seatNumbers != null ? seatNumbers : "N/A";
    }
}