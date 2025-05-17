package dao;

import Utils.DatabaseConnection;
import model.Booking;
import java.sql.*;
import java.util.ArrayList;
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
    // Get bookings for a specific customer
    public List<Booking> getBookingsByCustomer(int customerId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.*, f.title as film_title, ss.screening_date, ss.screening_time, " +
                     "s.row_letter, s.seat_number " +
                     "FROM booking b " +
                     "JOIN screening_schedule ss ON b.schedule_id = ss.schedule_id " +
                     "JOIN film f ON ss.film_id = f.film_id " +
                     "JOIN seat s ON b.seat_id = s.seat_id " +
                     "WHERE b.customer_id = ? " +
                     "ORDER BY b.booking_date DESC";

        try (Connection conn = DatabaseConnection.connectDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Booking booking = mapResultSetToBooking(rs);
                bookings.add(booking);
            }
        } catch (SQLException e) {
            System.err.println("Error getting customer bookings: " + e.getMessage());
        }
        return bookings;
    }

    // Get all bookings (for admin)
    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.*, f.title as film_title, ss.screening_date, ss.screening_time, " +
                     "s.row_letter, s.seat_number, c.username as customer_name " +
                     "FROM booking b " +
                     "JOIN screening_schedule ss ON b.schedule_id = ss.schedule_id " +
                     "JOIN film f ON ss.film_id = f.film_id " +
                     "JOIN seat s ON b.seat_id = s.seat_id " +
                     "JOIN customer c ON b.customer_id = c.customer_id " +
                     "ORDER BY b.booking_date DESC";

        try (Connection conn = DatabaseConnection.connectDB();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Booking booking = mapResultSetToBooking(rs);
                bookings.add(booking);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all bookings: " + e.getMessage());
        }
        return bookings;
    }

    // Helper method to map ResultSet to Booking object
    private Booking mapResultSetToBooking(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setBookingId(rs.getInt("booking_id"));
        booking.setCustomerId(rs.getInt("customer_id"));
        
        // For single seat (will be grouped later)
        List<Integer> seatIds = new ArrayList<>();
        seatIds.add(rs.getInt("seat_id"));
        booking.setSeatIds(seatIds);
        
        booking.setScheduleId(rs.getInt("schedule_id"));
        booking.setPaymentMethod(rs.getString("payment_method"));
        booking.setTotalPrice(rs.getDouble("total_price"));
        booking.setPaymentStatus(rs.getString("payment_status"));
        booking.setBookingDate(rs.getTimestamp("booking_date"));
        booking.setQrCodeData(rs.getString("qr_code_data"));
        
        // Additional joined fields
        booking.setFilmTitle(rs.getString("film_title"));
        booking.setScreeningDate(rs.getDate("screening_date"));
        booking.setScreeningTime(rs.getTime("screening_time"));
        booking.setSeatInfo(rs.getString("row_letter") + rs.getInt("seat_number"));
        
        if (rs.getMetaData().getColumnCount() > 12) { // Check if customer_name exists
            booking.setCustomerName(rs.getString("customer_name"));
        }
        
        return booking;
    }
}