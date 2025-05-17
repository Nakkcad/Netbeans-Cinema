package model;

import java.sql.Timestamp;
import java.util.List;

public class Booking {
    private int bookingId;
    private int customerId;
    private List<Integer> seatIds;  // List of seat IDs for multiple seats
    private int scheduleId;
    private String paymentMethod;
    private double totalPrice;
    private String paymentStatus;
    private Timestamp bookingDate;
    private String qrCodeData;  // Stores the QR code information

    // Constructors
    public Booking() {
    }

    public Booking(int customerId, List<Integer> seatIds, int scheduleId, 
                  String paymentMethod, double totalPrice) {
        this.customerId = customerId;
        this.seatIds = seatIds;
        this.scheduleId = scheduleId;
        this.paymentMethod = paymentMethod;
        this.totalPrice = totalPrice;
        this.paymentStatus = "Pending";  // Default status
    }

    // Getters and Setters
    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public List<Integer> getSeatIds() {
        return seatIds;
    }

    public void setSeatIds(List<Integer> seatIds) {
        this.seatIds = seatIds;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Timestamp getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Timestamp bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getQrCodeData() {
        return qrCodeData;
    }

    public void setQrCodeData(String qrCodeData) {
        this.qrCodeData = qrCodeData;
    }

    // Helper method to format seat IDs for display
    public String getSeatNumbersFormatted() {
        // This would typically query the database to get actual seat numbers (A1, B2, etc.)
        // For now returning the IDs
        return seatIds.toString();
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId=" + bookingId +
                ", customerId=" + customerId +
                ", seatIds=" + seatIds +
                ", scheduleId=" + scheduleId +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", totalPrice=" + totalPrice +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", bookingDate=" + bookingDate +
                '}';
    }
}