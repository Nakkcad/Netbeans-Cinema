// SeatDAO.java
package dao;

import Utils.DatabaseConnection;
import model.Seat;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.ScreeningSeat;
/**
 * Data Access Object for managing seat-related operations in the database.
 * This class provides methods to interact with the seat data in the database,
 * including retrieving seats for screenings, checking seat availability,
 * updating seat status, and generating seats for new screenings. It serves as
 * an intermediary between the application and the database for all seat-related
 * operations.
 * @author hp
 */
public class SeatDAO {

    // Get all seats for a screening, marking which ones are unavailable
    /**
     * Retrieves all seats for a specific screening, marking which ones are unavailable.
     * This method fetches all seats associated with the given screening schedule ID
     * and marks their availability status based on existing bookings. It joins the
     * screening_seat table with booking information to determine which seats are already booked.
     * @param scheduleId the ID of the screening schedule
     * @return a list of Seat objects with their availability status
     */
    public List<Seat> getSeatsForScreening(int scheduleId) {
        System.out.println("Fetching seats for screening ID: " + scheduleId);

        List<Seat> seats = new ArrayList<>();
        String sql = "SELECT ss.screening_seat_id, ss.schedule_id, ss.screen_id, ss.row_letter, "
                + "ss.seat_number, ss.status, ss.price, "
                + "CASE WHEN bs.screening_seat_id IS NOT NULL THEN 'booked' ELSE ss.status END AS availability "
                + "FROM screening_seat ss "
                + "LEFT JOIN ("
                + "   SELECT bs.screening_seat_id FROM booking_seat bs "
                + "   JOIN booking b ON bs.booking_id = b.booking_id "
                + "   WHERE b.schedule_id = ?"
                + ") bs ON ss.screening_seat_id = bs.screening_seat_id "
                + "WHERE ss.schedule_id = ? "
                + "ORDER BY ss.row_letter, ss.seat_number";

        try (Connection conn = DatabaseConnection.connectDB(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, scheduleId);
            stmt.setInt(2, scheduleId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Seat seat = mapResultSetToSeat(rs);
                    seat.setStatus(rs.getString("availability"));
                    System.out.println("Loaded seat: " + seat.toString()
                            + " | Status: " + seat.getStatus()
                            + " | ScreeningSeatID: " + seat.getScreeningSeatId());
                    seats.add(seat);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting seats for screening: " + e.getMessage());
        }
        return seats;
    }
    /**
     * Retrieves a specific seat by its position in a screening.
     * This method fetches a seat based on the screening schedule ID, row letter,
     * and seat number. It also checks the booking status of the seat and returns
     * a ScreeningSeat object with all relevant information.
     * @param scheduleId the ID of the screening schedule
     * @param row the row letter of the seat
     * @param seatNumber the seat number
     * @return the ScreeningSeat object if found, null otherwise
     */
    public ScreeningSeat getSeatByPosition(int scheduleId, char row, int seatNumber) {
        String sql = "SELECT ss.screening_seat_id, ss.schedule_id, ss.screen_id, ss.row_letter, "
                + "ss.seat_number, ss.status, ss.price, "
                + "CASE WHEN bs.screening_seat_id IS NOT NULL THEN 'booked' ELSE ss.status END AS availability "
                + "FROM screening_seat ss "
                + "LEFT JOIN ("
                + "   SELECT bs.screening_seat_id FROM booking_seat bs "
                + "   JOIN booking b ON bs.booking_id = b.booking_id "
                + "   WHERE b.schedule_id = ?"
                + ") bs ON ss.screening_seat_id = bs.screening_seat_id "
                + "WHERE ss.schedule_id = ? AND ss.row_letter = ? AND ss.seat_number = ?";

        try (Connection conn = DatabaseConnection.connectDB(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, scheduleId);
            stmt.setInt(2, scheduleId);
            stmt.setString(3, String.valueOf(row));
            stmt.setInt(4, seatNumber);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ScreeningSeat seat = new ScreeningSeat();
                    seat.setScreeningSeatId(rs.getInt("screening_seat_id"));
                    seat.setScheduleId(rs.getInt("schedule_id"));
                    seat.setScreenId(rs.getInt("screen_id"));
                    seat.setRowLetter(rs.getString("row_letter").charAt(0));
                    seat.setSeatNumber(rs.getInt("seat_number"));
                    seat.setStatus(rs.getString("availability"));
                    seat.setPrice(rs.getDouble("price"));
                    return seat;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting seat by position: " + e.getMessage());
        }
        return null;
    }
    /**
     * Retrieves all available seats for a specific screening.
     * This method fetches only the seats that are marked as available and not
     * already booked for the given screening schedule ID. It filters out seats
     * that are either unavailable or already booked.
     * @param scheduleId the ID of the screening schedule
     * @return a list of available Seat objects
     */
    public List<Seat> getAvailableSeatsForScreening(int scheduleId) {
        List<Seat> seats = new ArrayList<>();
        String sql = "SELECT ss.screening_seat_id, ss.schedule_id, ss.screen_id, ss.row_letter, "
                + "ss.seat_number, ss.status, ss.price "
                + "FROM screening_seat ss "
                + "LEFT JOIN ("
                + "   SELECT bs.screening_seat_id FROM booking_seat bs "
                + "   JOIN booking b ON bs.booking_id = b.booking_id "
                + "   WHERE b.schedule_id = ?"
                + ") bs ON ss.screening_seat_id = bs.screening_seat_id "
                + "WHERE ss.schedule_id = ? "
                + "AND bs.screening_seat_id IS NULL AND ss.status = 'available' "
                + "ORDER BY ss.row_letter, ss.seat_number";

        try (Connection conn = DatabaseConnection.connectDB(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, scheduleId);
            stmt.setInt(2, scheduleId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    seats.add(mapResultSetToSeat(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting available seats: " + e.getMessage());
        }
        return seats;
    }

    // Get all seats for a screening (including booked ones)
    /**
     * Retrieves all seats for a specific screening, including booked ones.
     * This method fetches all seats associated with the given screening schedule ID
     * regardless of their availability status. It returns a complete list of all seats
     * in the screening, ordered by row and seat number.
     * @param scheduleId the ID of the screening schedule
     * @return a list of all Seat objects for the screening
     */
    public List<Seat> getAllSeatsForScreening(int scheduleId) {
        List<Seat> seats = new ArrayList<>();
        String sql = "SELECT * FROM screening_seat WHERE schedule_id = ? ORDER BY row_letter, seat_number";

        try (Connection conn = DatabaseConnection.connectDB(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, scheduleId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    seats.add(mapResultSetToSeat(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting seats by screening: " + e.getMessage());
        }
        return seats;
    }

    // Update seat status
    /**
     * Updates the status of a specific seat.
     * This method changes the status of a seat identified by its screening seat ID.
     * It can be used to mark seats as available, unavailable, or reserved.
     * @param screeningSeatId the ID of the screening seat
     * @param status the new status to set
     * @return true if the update was successful, false otherwise
     */
    public boolean updateSeatStatus(int screeningSeatId, String status) {
        System.out.println("Attempting to update seat ID " + screeningSeatId + " to status: " + status);
        // ... existing code ...

        String sql = "UPDATE screening_seat SET status = ? WHERE screening_seat_id = ?";

        try (Connection conn = DatabaseConnection.connectDB(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, screeningSeatId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating seat status: " + e.getMessage());
            return false;
        }
    }

    // Get seat by ID
    /**
     * Retrieves a seat by its ID.
     * This method fetches a single seat based on its screening seat ID.
     * It returns a Seat object with all the seat's information if found.
     * @param screeningSeatId the ID of the screening seat
     * @return the Seat object if found, null otherwise
     */
    public Seat getSeatById(int screeningSeatId) {
        String sql = "SELECT * FROM screening_seat WHERE screening_seat_id = ?";

        try (Connection conn = DatabaseConnection.connectDB(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, screeningSeatId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToSeat(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting seat by ID: " + e.getMessage());
        }
        return null;
    }
    /**
     * Generates seats for a new screening.
     * This method creates seat records in the database for a new screening based on
     * the provided parameters. It creates a grid of seats with the specified number
     * of rows and seats per row, assigning them to the given screening schedule and screen.
     * @param conn the database connection
     * @param scheduleId the ID of the screening schedule
     * @param screenId the ID of the screen
     * @param rows the number of rows to generate
     * @param seatsPerRow the number of seats per row
     * @return true if the seats were successfully generated, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean generateSeatsForScreening(Connection conn, int scheduleId, int screenId, int rows, int seatsPerRow) throws SQLException {
    String sql = "INSERT INTO screening_seat (schedule_id, screen_id, row_letter, seat_number, status, price) "
               + "VALUES (?, ?, ?, ?, 'available', ?)";
    
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        double seatPrice = 50.00; // Standard price for all seats
        
        for (int row = 0; row < rows; row++) {
            char rowLetter = (char) ('A' + row);
            for (int seatNum = 1; seatNum <= seatsPerRow; seatNum++) {
                stmt.setInt(1, scheduleId);
                stmt.setInt(2, screenId);
                stmt.setString(3, String.valueOf(rowLetter));
                stmt.setInt(4, seatNum);
                stmt.setDouble(5, seatPrice);
                stmt.addBatch();
            }
        }
        stmt.executeBatch();
        return true;
    }
}

    // Helper method to map ResultSet to Seat object
    /**
     * Helper method to map a ResultSet to a Seat object.
     * This private method converts a database result row into a Seat object,
     * mapping each column to the corresponding property of the Seat class.
     * @param rs the ResultSet containing seat data
     * @return a populated Seat object
     * @throws SQLException if a database error occurs
     */
    private Seat mapResultSetToSeat(ResultSet rs) throws SQLException {
        Seat seat = new Seat();
        seat.setScreeningSeatId(rs.getInt("screening_seat_id"));
        seat.setScheduleId(rs.getInt("schedule_id"));
        seat.setScreenId(rs.getInt("screen_id"));
        seat.setRowLetter(rs.getString("row_letter").charAt(0));
        seat.setSeatNumber(rs.getInt("seat_number"));
        seat.setStatus(rs.getString("status"));
        seat.setPrice(rs.getDouble("price"));
        return seat;
    }
}
