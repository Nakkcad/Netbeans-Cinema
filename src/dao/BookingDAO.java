package dao;

import Utils.DatabaseConnection;
import model.Booking;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    // Create a new booking with transaction
    public boolean createBooking(Booking booking) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.connectDB();
            conn.setAutoCommit(false);

            // 1. Insert booking record
            int bookingId = insertBooking(booking, conn);
            if (bookingId <= 0) {
                conn.rollback();
                return false;
            }

            // 2. Insert booking_seat records
            if (!insertBookingSeats(bookingId, booking.getScreeningSeatIds(), conn)) {
                conn.rollback();
                return false;
            }

            // 3. Update screening_seat statuses
            if (!updateScreeningSeatStatuses(booking.getScreeningSeatIds(), "booked", conn)) {
                conn.rollback();
                return false;
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            e.printStackTrace();
            return false;
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException e) {}
        }
    }

    // Get all bookings with seat information
    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.*, f.title AS film_title, ss.screening_date, ss.screening_time, sc.screen_name, "
                   + "GROUP_CONCAT(CONCAT(s.row_letter, s.seat_number)) AS seat_numbers "
                   + "FROM booking b "
                   + "JOIN screening_schedule ss ON b.schedule_id = ss.schedule_id "
                   + "JOIN film f ON ss.film_id = f.film_id "
                   + "JOIN screen sc ON ss.screen_id = sc.screen_id "
                   + "JOIN booking_seat bs ON b.booking_id = bs.booking_id "
                   + "JOIN screening_seat ss2 ON bs.screening_seat_id = ss2.screening_seat_id "
                   + "JOIN seat s ON ss2.seat_id = s.seat_id "
                   + "GROUP BY b.booking_id "
                   + "ORDER BY b.booking_date DESC";

        try (Connection conn = DatabaseConnection.connectDB();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Booking booking = mapResultSetToBooking(rs);
                booking.setFilmTitle(rs.getString("film_title"));
                booking.setScreeningDate(rs.getDate("screening_date"));
                booking.setScreeningTime(rs.getTime("screening_time"));
                booking.setScreenName(rs.getString("screen_name"));
                booking.setSeatNumbers(rs.getString("seat_numbers"));
                bookings.add(booking);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all bookings: " + e.getMessage());
        }
        return bookings;
    }

    // Get bookings by customer ID
    public List<Booking> getBookingsByCustomer(int customerId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.*, f.title AS film_title, ss.screening_date, ss.screening_time, sc.screen_name, "
                   + "GROUP_CONCAT(CONCAT(s.row_letter, s.seat_number)) AS seat_numbers "
                   + "FROM booking b "
                   + "JOIN screening_schedule ss ON b.schedule_id = ss.schedule_id "
                   + "JOIN film f ON ss.film_id = f.film_id "
                   + "JOIN screen sc ON ss.screen_id = sc.screen_id "
                   + "JOIN booking_seat bs ON b.booking_id = bs.booking_id "
                   + "JOIN screening_seat ss2 ON bs.screening_seat_id = ss2.screening_seat_id "
                   + "JOIN seat s ON ss2.seat_id = s.seat_id "
                   + "WHERE b.customer_id = ? "
                   + "GROUP BY b.booking_id "
                   + "ORDER BY b.booking_date DESC";

        try (Connection conn = DatabaseConnection.connectDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Booking booking = mapResultSetToBooking(rs);
                    booking.setFilmTitle(rs.getString("film_title"));
                    booking.setScreeningDate(rs.getDate("screening_date"));
                    booking.setScreeningTime(rs.getTime("screening_time"));
                    booking.setScreenName(rs.getString("screen_name"));
                    booking.setSeatNumbers(rs.getString("seat_numbers"));
                    bookings.add(booking);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting customer bookings: " + e.getMessage());
        }
        return bookings;
    }

    // Helper methods
    private int insertBooking(Booking booking, Connection conn) throws SQLException {
        String sql = "INSERT INTO booking (customer_id, schedule_id, payment_method, "
                   + "total_price, payment_status, qr_code_data) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, booking.getCustomerId());
            stmt.setInt(2, booking.getScheduleId());
            stmt.setString(3, booking.getPaymentMethod());
            stmt.setDouble(4, booking.getTotalPrice());
            stmt.setString(5, booking.getPaymentStatus());
            stmt.setString(6, booking.getQrCodeData());

            if (stmt.executeUpdate() > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) return rs.getInt(1);
                }
            }
            return 0;
        }
    }

    private boolean insertBookingSeats(int bookingId, List<Integer> screeningSeatIds, Connection conn) throws SQLException {
        String sql = "INSERT INTO booking_seat (booking_id, screening_seat_id) VALUES (?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int screeningSeatId : screeningSeatIds) {
                stmt.setInt(1, bookingId);
                stmt.setInt(2, screeningSeatId);
                stmt.addBatch();
            }
            int[] results = stmt.executeBatch();
            for (int result : results) {
                if (result == Statement.EXECUTE_FAILED) return false;
            }
            return true;
        }
    }

    private boolean updateScreeningSeatStatuses(List<Integer> screeningSeatIds, String status, Connection conn) throws SQLException {
        String sql = "UPDATE screening_seat SET status = ? WHERE screening_seat_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int screeningSeatId : screeningSeatIds) {
                stmt.setString(1, status);
                stmt.setInt(2, screeningSeatId);
                stmt.addBatch();
            }
            int[] results = stmt.executeBatch();
            for (int result : results) {
                if (result == Statement.EXECUTE_FAILED) return false;
            }
            return true;
        }
    }

    private Booking mapResultSetToBooking(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setBookingId(rs.getInt("booking_id"));
        booking.setCustomerId(rs.getInt("customer_id"));
        booking.setScheduleId(rs.getInt("schedule_id"));
        booking.setPaymentMethod(rs.getString("payment_method"));
        booking.setTotalPrice(rs.getDouble("total_price"));
        booking.setPaymentStatus(rs.getString("payment_status"));
        booking.setQrCodeData(rs.getString("qr_code_data"));
        booking.setBookingDate(rs.getTimestamp("booking_date"));
        return booking;
    }
}