package dao;

import Utils.DatabaseConnection;
import model.Screening;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScreeningDAO {

    // Get all screenings for a specific film
    public List<Screening> getScreeningsByFilm(int filmId) {
        List<Screening> list = new ArrayList<>();
        String sql = "SELECT * FROM screening_schedule WHERE film_id = ? ORDER BY screening_date, screening_time";

        try (Connection conn = DatabaseConnection.connectDB(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, filmId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToScreening(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching screenings: " + e.getMessage());
        }

        return list;
    }

    // Get available screening dates for a film (distinct dates)
    public List<String> getAvailableDates(int filmId) {
        List<String> dates = new ArrayList<>();
        String sql = "SELECT DISTINCT screening_date FROM screening_schedule WHERE film_id = ? ORDER BY screening_date";

        try (Connection conn = DatabaseConnection.connectDB(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, filmId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                dates.add(rs.getDate("screening_date").toString());
            }
        } catch (SQLException e) {
            System.err.println("Error fetching available dates: " + e.getMessage());
        }

        return dates;
    }

    // Get time slots for a film on a specific date
    public List<String> getTimeSlots(int filmId, String date) {
        List<String> timeSlots = new ArrayList<>();
        String sql = "SELECT screening_time FROM screening_schedule WHERE film_id = ? AND screening_date = ? ORDER BY screening_time";

        try (Connection conn = DatabaseConnection.connectDB(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, filmId);
            stmt.setDate(2, Date.valueOf(date));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                timeSlots.add(rs.getTime("screening_time").toString());
            }
        } catch (SQLException e) {
            System.err.println("Error fetching time slots: " + e.getMessage());
        }

        return timeSlots;
    }

    // Helper method to map ResultSet to Screening model
    private Screening mapResultSetToScreening(ResultSet rs) throws SQLException {
        Screening s = new Screening();
        s.setScheduleId(rs.getInt("schedule_id"));
        s.setFilmId(rs.getInt("film_id"));
        s.setDate(rs.getDate("screening_date").toString());
        s.setTime(rs.getTime("screening_time").toString());
        return s;
    }

    // Optional: Add screening entry
    public boolean addScreening(Screening screening) {
        String sql = "INSERT INTO screening_schedule (film_id, screening_date, screening_time) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.connectDB(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, screening.getFilmId());
            stmt.setDate(2, Date.valueOf(screening.getDate()));
            stmt.setTime(3, Time.valueOf(screening.getTime()));

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        screening.setScheduleId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error adding screening: " + e.getMessage());
        }
        return false;
    }

    public boolean updateScreening(Screening screening) {
        String sql = "UPDATE screening_schedule SET screening_date = ?, screening_time = ? WHERE schedule_id = ?";

        try (Connection conn = DatabaseConnection.connectDB(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(screening.getDate()));
            stmt.setTime(2, Time.valueOf(screening.getTime()));
            stmt.setInt(3, screening.getScheduleId());

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating screening: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteScreening(int scheduleId) {
        String sql = "DELETE FROM screening_schedule WHERE schedule_id = ?";

        try (Connection conn = DatabaseConnection.connectDB(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, scheduleId);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting screening: " + e.getMessage());
        }
        return false;
    }

    public Screening getScreeningById(int scheduleId) {
        String sql = "SELECT * FROM screening_schedule WHERE schedule_id = ?";

        try (Connection conn = DatabaseConnection.connectDB(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, scheduleId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToScreening(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching screening: " + e.getMessage());
        }
        return null;
    }

}
