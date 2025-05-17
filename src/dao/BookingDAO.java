package dao;

import Utils.DatabaseConnection;
import model.Booking;
import java.sql.*;
import java.util.List;

public class BookingDAO {
    public boolean createBooking(Booking booking) {
        String sql = "INSERT INTO booking (customer_id, seat_id, schedule_id, " +
                     "payment_method, total_price, payment_status, qr_code_data) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.connectDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            conn.setAutoCommit(false);
            
            for (Integer seatId : booking.getSeatIds()) {
                stmt.setInt(1, booking.getCustomerId());
                stmt.setInt(2, seatId);
                stmt.setInt(3, booking.getScheduleId());
                stmt.setString(4, booking.getPaymentMethod());
                stmt.setDouble(5, booking.getTotalPrice());
                stmt.setString(6, booking.getPaymentStatus());
                stmt.setString(7, booking.getQrCodeData());
                stmt.addBatch();
            }
            
            int[] results = stmt.executeBatch();
            conn.commit();
            
            for (int result : results) {
                if (result <= 0) return false;
            }
            return true;
        } catch (SQLException e) {
            System.err.println("Error creating booking: " + e.getMessage());
            return false;
        }
    }
}