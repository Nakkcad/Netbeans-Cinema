package model;

public class ScreeningSeat {
    private int screeningSeatId;
    private int scheduleId;
    private int screenId;
    private char rowLetter;
    private int seatNumber;
    private String status;
    private double price;
    
    
    public ScreeningSeat() {
        this.price = 50.00;  // Default price in code to match DB
    }
    
public int getScreeningSeatId() {
    return screeningSeatId;
}

public void setScreeningSeatId(int screeningSeatId) {
    this.screeningSeatId = screeningSeatId;
}

public int getScheduleId() {
    return scheduleId;
}

public void setScheduleId(int scheduleId) {
    this.scheduleId = scheduleId;
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
}