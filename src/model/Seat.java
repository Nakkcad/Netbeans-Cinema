package model;

public class Seat {

    private int seatId;
    private int screenId;
    private char rowLetter;
    private int seatNumber;
    private String status;
    private double price;

    // Constructors
    public Seat() {
    }

    public Seat(int screenId, char rowLetter, int seatNumber, String status, double price) {
        this.screenId = screenId;
        this.rowLetter = rowLetter;
        this.seatNumber = seatNumber;
        this.status = status;
        this.price = price * 1000; // Convert to IDR (assuming price was in thousands)
    }

    // Getters and setters
    public int getSeatId() {
        return seatId;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

    public int getScreenId() {
        return screenId;
    }

    public void setScreenId(int screenId) {
        this.screenId = screenId;
    }

    public char getRowLetter() {
        return rowLetter;
    }

    public void setRowLetter(char rowLetter) {
        this.rowLetter = rowLetter;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return String.format("%c%d", rowLetter, seatNumber);
    }

    public String getFormattedPrice() {
        return String.format("Rp%,.0f", price);
    }
}
