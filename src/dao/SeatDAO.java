// SeatDAO.java
package dao;

import Utils.DatabaseConnection;
import model.Seat;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeatDAO {

    // Get all seats for a screening, marking which ones are unavailable
    public List<Seat> getSeatsForScreening(int scheduleId) {
        List<Seat> seats = new ArrayList<>();
        String sql = "SELECT ss.screening_seat_id, ss.schedule_id, ss.screen_id, ss.row_letter, " +
                     "ss.seat_number, ss.status, ss.price, " +
                     "CASE WHEN bs.screening_seat_id IS NOT NULL THEN 'booked' ELSE ss.status END AS availability " +
                     "FROM screening_seat ss " +
                     "LEFT JOIN (" +
                     "   SELECT bs.screening_seat_id FROM booking_seat bs " +
                     "   JOIN booking b ON bs.booking_id = b.booking_id " +
                     "   WHERE b.schedule_id = ?" +
                     ") bs ON ss.screening_seat_id = bs.screening_seat_id " +
                     "WHERE ss.schedule_id = ? " +
                     "ORDER BY ss.row_letter, ss.seat_number";

        try (Connection conn = DatabaseConnection.connectDB(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, scheduleId);
            stmt.setInt(2, scheduleId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Seat seat = mapResultSetToSeat(rs);
                    seat.setStatus(rs.getString("availability"));
                    seats.add(seat);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting seats for screening: " + e.getMessage());
        }
        return seats;
    }

    public List<Seat> getAvailableSeatsForScreening(int scheduleId) {
        List<Seat> seats = new ArrayList<>();
        String sql = "SELECT ss.screening_seat_id, ss.schedule_id, ss.screen_id, ss.row_letter, " +
                     "ss.seat_number, ss.status, ss.price " +
                     "FROM screening_seat ss " +
                     "LEFT JOIN (" +
                     "   SELECT bs.screening_seat_id FROM booking_seat bs " +
                     "   JOIN booking b ON bs.booking_id = b.booking_id " +
                     "   WHERE b.schedule_id = ?" +
                     ") bs ON ss.screening_seat_id = bs.screening_seat_id " +
                     "WHERE ss.schedule_id = ? " +
                     "AND bs.screening_seat_id IS NULL AND ss.status = 'available' " +
                     "ORDER BY ss.row_letter, ss.seat_number";

        try (Connection conn = DatabaseConnection.connectDB(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {

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
    public List<Seat> getAllSeatsForScreening(int scheduleId) {
        List<Seat> seats = new ArrayList<>();
        String sql = "SELECT * FROM screening_seat WHERE schedule_id = ? ORDER BY row_letter, seat_number";

        try (Connection conn = DatabaseConnection.connectDB(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {

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
    public boolean updateSeatStatus(int screeningSeatId, String status) {
        String sql = "UPDATE screening_seat SET status = ? WHERE screening_seat_id = ?";

        try (Connection conn = DatabaseConnection.connectDB(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, screeningSeatId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating seat status: " + e.getMessage());
            return false;
        }
    }

    // Get seat by ID
    public Seat getSeatById(int screeningSeatId) {
        String sql = "SELECT * FROM screening_seat WHERE screening_seat_id = ?";

        try (Connection conn = DatabaseConnection.connectDB(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {

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

    // Helper method to map ResultSet to Seat object
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