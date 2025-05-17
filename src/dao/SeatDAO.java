package dao;

import Utils.DatabaseConnection;
import model.Seat;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeatDAO {
    // Get available seats for a specific screening
public List<Seat> getAvailableSeatsForScreening(int scheduleId) {
    List<Seat> seats = new ArrayList<>();
    String sql = "SELECT s.seat_id, s.screen_id, s.row_letter, s.seat_number, s.status, s.price " +
                 "FROM seat s " +
                 "LEFT JOIN booking b ON s.seat_id = b.seat_id AND b.schedule_id = ? " +
                 "WHERE s.screen_id = (SELECT screen_id FROM screening_schedule WHERE schedule_id = ?) " +
                 "AND b.booking_id IS NULL AND s.status = 'available' " +
                 "ORDER BY s.row_letter, s.seat_number";
    
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

    // Get all seats for a screen (including booked ones)
    public List<Seat> getAllSeatsForScreen(int screenId) {
        List<Seat> seats = new ArrayList<>();
        String sql = "SELECT * FROM seat WHERE screen_id = ? ORDER BY row_letter, seat_number";
        
        try (Connection conn = DatabaseConnection.connectDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, screenId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    seats.add(mapResultSetToSeat(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting seats by screen: " + e.getMessage());
        }
        return seats;
    }

    // Update seat status
    public boolean updateSeatStatus(int seatId, String status) {
        String sql = "UPDATE seat SET status = ? WHERE seat_id = ?";
        
        try (Connection conn = DatabaseConnection.connectDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, seatId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating seat status: " + e.getMessage());
            return false;
        }
    }

    // Get seat by ID
    public Seat getSeatById(int seatId) {
        String sql = "SELECT * FROM seat WHERE seat_id = ?";
        
        try (Connection conn = DatabaseConnection.connectDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, seatId);
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
        seat.setSeatId(rs.getInt("seat_id"));
        seat.setScreenId(rs.getInt("screen_id"));
        seat.setRowLetter(rs.getString("row_letter").charAt(0));
        seat.setSeatNumber(rs.getInt("seat_number"));
        seat.setStatus(rs.getString("status"));
        seat.setPrice(rs.getDouble("price"));
        return seat;
    }
}