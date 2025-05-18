// BookingDAO.java
package dao;

import Utils.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Booking;
    /**
    * Data Access Object for managing booking-related operations in the database.
    * This class provides methods to interact with the booking data in the database, including creating bookings, checking seat availability, retrieving booking
    information, and managing the relationship between bookings and seats. It serves
    * as an intermediary between the application and the database for all booking-related operations.
    * @author hp
    */
public class BookingDAO {

    // Create a new booking with selected seats
    /**
     * Creates a new booking with selected seats in the database.
     * This method performs a transaction that includes creating a booking record
     * and associating it with the selected seats. The transaction ensures that
     * either all operations succeed or none do, maintaining database integrity.
     * @param customerId the ID of the customer making the booking
     * @param scheduleId the ID of the screening schedule being booked
     * @param paymentMethod the method of payment used
     * @param totalPrice the total price of the booking
     * @param seatIds a list of seat IDs selected for the booking
     * @return the generated booking ID if successful, -1 if the operation fails
     */
    public int createBooking(int customerId, int scheduleId, String paymentMethod,
            double totalPrice, List<Integer> seatIds) {
        Connection conn = null;
        PreparedStatement bookingStmt = null;
        PreparedStatement seatStmt = null;
        ResultSet generatedKeys = null;

        try {
            conn = DatabaseConnection.connectDB();
            conn.setAutoCommit(false); // Start transaction

            // 1. Insert the booking record
            String bookingSql = "INSERT INTO booking (customer_id, schedule_id, payment_method, total_price) "
                    + "VALUES (?, ?, ?, ?)";
            bookingStmt = conn.prepareStatement(bookingSql, Statement.RETURN_GENERATED_KEYS);
            bookingStmt.setInt(1, customerId);
            bookingStmt.setInt(2, scheduleId);
            bookingStmt.setString(3, paymentMethod);
            bookingStmt.setDouble(4, totalPrice);

            int affectedRows = bookingStmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating booking failed, no rows affected.");
            }

            // Get the generated booking ID
            generatedKeys = bookingStmt.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new SQLException("Creating booking failed, no ID obtained.");
            }
            int bookingId = generatedKeys.getInt(1);

            // 2. Insert all booked seats
            String seatSql = "INSERT INTO booking_seat (booking_id, screening_seat_id) VALUES (?, ?)";
            seatStmt = conn.prepareStatement(seatSql);

            for (int seatId : seatIds) {
                seatStmt.setInt(1, bookingId);
                seatStmt.setInt(2, seatId);
                seatStmt.addBatch();
            }

            seatStmt.executeBatch();

            // Commit transaction
            conn.commit();

            return bookingId;

        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback(); // Rollback on error
                }
            } catch (SQLException ex) {
                System.err.println("Error rolling back transaction: " + ex.getMessage());
            }
            System.err.println("Error creating booking: " + e.getMessage());
            return -1;
        } finally {
            try {
                if (generatedKeys != null) {
                    generatedKeys.close();
                }
                if (bookingStmt != null) {
                    bookingStmt.close();
                }
                if (seatStmt != null) {
                    seatStmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    // Check if a seat is already booked for a screening
    /**
     * Checks if a specific seat is already booked for a screening.
     * This method verifies whether a seat has already been booked for a particular
     * screening schedule, helping to prevent double bookings and ensure seat availability.
     * @param screeningSeatId the ID of the screening seat to check
     * @param scheduleId the ID of the screening schedule
     * @return true if the seat is already booked, false otherwise
     */
    public boolean isSeatBooked(int screeningSeatId, int scheduleId) {
        String sql = "SELECT COUNT(*) > 0 AS is_booked "
                + "FROM booking_seat bs "
                + "JOIN booking b ON bs.booking_id = b.booking_id "
                + "WHERE bs.screening_seat_id = ? AND b.schedule_id = ?";

        try (Connection conn = DatabaseConnection.connectDB(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, screeningSeatId);
            stmt.setInt(2, scheduleId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("is_booked");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking seat booking status: " + e.getMessage());
        }
        return false;
    }

    // Get all booked seat IDs for a screening
    /**
     * Retrieves all booked seat IDs for a specific screening.
     * This method fetches the IDs of all seats that have been booked for a particular
     * screening schedule, which is useful for displaying seat availability to users.
     * @param scheduleId the ID of the screening schedule
     * @return a list of booked seat IDs
     */
    public List<Integer> getBookedSeatIds(int scheduleId) {
        List<Integer> seatIds = new ArrayList<>();
        String sql = "SELECT bs.screening_seat_id "
                + "FROM booking_seat bs "
                + "JOIN booking b ON bs.booking_id = b.booking_id "
                + "WHERE b.schedule_id = ?";

        try (Connection conn = DatabaseConnection.connectDB(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, scheduleId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    seatIds.add(rs.getInt("screening_seat_id"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting booked seat IDs: " + e.getMessage());
        }
        return seatIds;
    }
    /**
     * Retrieves all bookings made by a specific customer.
     * This method fetches detailed booking information for a particular customer,
     * including film details, screening information, and seat numbers. The results
     * are ordered by booking date in descending order (newest first).
     * @param customerId the ID of the customer
     * @return a list of Booking objects containing the customer's booking history
     */
    public List<Booking> getBookingsByCustomer(int customerId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.booking_id, b.customer_id, b.schedule_id, b.payment_method, "
                + "b.total_price, b.payment_status, b.booking_date, "
                + "f.title AS film_title, ss.screening_date, ss.screening_time, ss.screen_id, "
                + "GROUP_CONCAT(CONCAT(ss2.row_letter, ss2.seat_number) ORDER BY ss2.row_letter, ss2.seat_number SEPARATOR ', ') AS seat_numbers "
                + "FROM booking b "
                + "JOIN screening_schedule ss ON b.schedule_id = ss.schedule_id "
                + "JOIN film f ON ss.film_id = f.film_id "
                + "JOIN booking_seat bs ON b.booking_id = bs.booking_id "
                + "JOIN screening_seat ss2 ON bs.screening_seat_id = ss2.screening_seat_id "
                + "WHERE b.customer_id = ? "
                + "GROUP BY b.booking_id "
                + "ORDER BY b.booking_date DESC";

        try (Connection conn = DatabaseConnection.connectDB(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Booking booking = new Booking();
                    booking.setBookingId(rs.getInt("booking_id"));
                    booking.setCustomerId(rs.getInt("customer_id"));
                    booking.setScheduleId(rs.getInt("schedule_id"));
                    booking.setPaymentMethod(rs.getString("payment_method"));
                    booking.setTotalPrice(rs.getDouble("total_price"));
                    booking.setPaymentStatus(rs.getString("payment_status"));
                    booking.setBookingDate(rs.getTimestamp("booking_date"));
                    booking.setFilmTitle(rs.getString("film_title"));
                    booking.setScreeningDate(rs.getDate("screening_date"));
                    booking.setScreeningTime(rs.getTime("screening_time"));
                    booking.setScreenId(rs.getInt("screen_id"));
                    booking.setSeatNumbers(rs.getString("seat_numbers"));

                    bookings.add(booking);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting bookings by customer: " + e.getMessage());
        }
        return bookings;
    }
    /**
     * Retrieves all bookings in the system.
     * This method fetches detailed information for all bookings in the database,
     * including customer details, film information, screening details, and seat numbers.
     * The results are ordered by booking date in descending order (newest first).
     * This method is typically used by administrators to view all booking activity.
     * @return a list of all Booking objects in the system
     */
    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.booking_id, b.customer_id, b.schedule_id, b.payment_method, "
                + "b.total_price, b.payment_status, b.booking_date, "
                + "f.title AS film_title, ss.screening_date, ss.screening_time, ss.screen_id, "
                + "c.username AS customer_name, "
                + "GROUP_CONCAT(CONCAT(ss2.row_letter, ss2.seat_number) ORDER BY ss2.row_letter, ss2.seat_number SEPARATOR ', ') AS seat_numbers "
                + "FROM booking b "
                + "JOIN screening_schedule ss ON b.schedule_id = ss.schedule_id "
                + "JOIN film f ON ss.film_id = f.film_id "
                + "LEFT JOIN customer c ON b.customer_id = c.customer_id "
                + "JOIN booking_seat bs ON b.booking_id = bs.booking_id "
                + "JOIN screening_seat ss2 ON bs.screening_seat_id = ss2.screening_seat_id "
                + "GROUP BY b.booking_id "
                + "ORDER BY b.booking_date DESC";

        try (Connection conn = DatabaseConnection.connectDB(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Booking booking = new Booking();
                    booking.setBookingId(rs.getInt("booking_id"));
                    booking.setCustomerId(rs.getInt("customer_id"));
                    booking.setScheduleId(rs.getInt("schedule_id"));
                    booking.setPaymentMethod(rs.getString("payment_method"));
                    booking.setTotalPrice(rs.getDouble("total_price"));
                    booking.setPaymentStatus(rs.getString("payment_status"));
                    booking.setBookingDate(rs.getTimestamp("booking_date"));
                    booking.setFilmTitle(rs.getString("film_title"));
                    booking.setScreeningDate(rs.getDate("screening_date"));
                    booking.setScreeningTime(rs.getTime("screening_time"));
                    booking.setScreenId(rs.getInt("screen_id"));
                    booking.setCustomerName(rs.getString("customer_name"));
                    booking.setSeatNumbers(rs.getString("seat_numbers"));

                    bookings.add(booking);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting all bookings: " + e.getMessage());
        }
        return bookings;
    }

}
