package dao;

import Utils.DatabaseConnection;
import model.ScreeningSchedule;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScreeningScheduleDAO {

    // Add a new screening schedule
public boolean addScreeningSchedule(ScreeningSchedule schedule) {
    Connection conn = null;
    try {
        conn = DatabaseConnection.connectDB();
        conn.setAutoCommit(false); // Start transaction

        // 1. First insert the screening schedule
        String scheduleSql = "INSERT INTO screening_schedule (film_id, screen_id, screening_date, screening_time) "
                          + "VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement scheduleStmt = conn.prepareStatement(scheduleSql, Statement.RETURN_GENERATED_KEYS)) {
            scheduleStmt.setInt(1, schedule.getFilmId());
            scheduleStmt.setInt(2, schedule.getScreenId());
            scheduleStmt.setDate(3, schedule.getScreeningDate());
            scheduleStmt.setTime(4, schedule.getScreeningTime());

            int affectedRows = scheduleStmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating screening failed, no rows affected.");
            }

            // Get the generated schedule ID
            try (ResultSet generatedKeys = scheduleStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    schedule.setScheduleId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating screening failed, no ID obtained.");
                }
            }
        }

        // 2. Generate seats for this screening
        SeatDAO seatDao = new SeatDAO();
        boolean seatsCreated = seatDao.generateSeatsForScreening(
            conn, // Pass the same connection to maintain transaction
            schedule.getScheduleId(),
            schedule.getScreenId(),
            10,  // Number of rows (A-J)
            15   // Seats per row
        );

        if (!seatsCreated) {
            throw new SQLException("Failed to generate seats for screening");
        }

        conn.commit();
        return true;

    } catch (SQLException e) {
        try {
            if (conn != null) {
                conn.rollback();
            }
        } catch (SQLException ex) {
            System.err.println("Error rolling back transaction: " + ex.getMessage());
        }
        System.err.println("Error adding screening schedule: " + e.getMessage());
        return false;
    } finally {
        try {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}
    // Get all screening schedules
    public List<ScreeningSchedule> getAllScreeningSchedules() {
        List<ScreeningSchedule> schedules = new ArrayList<>();
        String sql = "SELECT * FROM screening_schedule ORDER BY screening_date, screening_time";

        try (Connection conn = DatabaseConnection.connectDB(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ScreeningSchedule schedule = mapResultSetToScreeningSchedule(rs);
                schedules.add(schedule);
            }
        } catch (SQLException e) {
            System.err.println("Error getting all screening schedules: " + e.getMessage());
        }

        return schedules;
    }

    // Get screening schedules by film ID
    public List<ScreeningSchedule> getScreeningSchedulesByFilmId(int filmId) {
        List<ScreeningSchedule> schedules = new ArrayList<>();
        String sql = "SELECT * FROM screening_schedule "
                + "WHERE film_id = ? "
                + "AND CONCAT(screening_date, ' ', screening_time) > NOW() "
                + "ORDER BY screening_date, screening_time";

        try (Connection conn = DatabaseConnection.connectDB(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, filmId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    schedules.add(mapResultSetToScreeningSchedule(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting screening schedules by film ID: " + e.getMessage());
        }
        return schedules;
    }

    // Get screening schedules by screen ID
    public List<ScreeningSchedule> getScreeningSchedulesByScreenId(int screenId) {
        List<ScreeningSchedule> schedules = new ArrayList<>();
        String sql = "SELECT * FROM screening_schedule WHERE screen_id = ? ORDER BY screening_date, screening_time";

        try (Connection conn = DatabaseConnection.connectDB(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, screenId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ScreeningSchedule schedule = mapResultSetToScreeningSchedule(rs);
                    schedules.add(schedule);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting screening schedules by screen ID: " + e.getMessage());
        }

        return schedules;
    }

    // Get screening schedules by date
    public List<ScreeningSchedule> getScreeningSchedulesByDate(Date date) {
        List<ScreeningSchedule> schedules = new ArrayList<>();
        String sql = "SELECT * FROM screening_schedule WHERE screening_date = ? ORDER BY screening_time";

        try (Connection conn = DatabaseConnection.connectDB(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, date);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ScreeningSchedule schedule = mapResultSetToScreeningSchedule(rs);
                    schedules.add(schedule);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting screening schedules by date: " + e.getMessage());
        }

        return schedules;
    }

    // Get a screening schedule by ID
    public ScreeningSchedule getScreeningScheduleById(int scheduleId) {
        String sql = "SELECT * FROM screening_schedule WHERE schedule_id = ?";

        try (Connection conn = DatabaseConnection.connectDB(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, scheduleId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToScreeningSchedule(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting screening schedule by ID: " + e.getMessage());
        }

        return null;
    }

    public List<ScreeningSchedule> getAllScreeningSchedules(boolean includePast) {
        List<ScreeningSchedule> schedules = new ArrayList<>();
        String sql = "SELECT * FROM screening_schedule";

        if (!includePast) {
            sql += " WHERE CONCAT(screening_date, ' ', screening_time) > NOW()";
        }

        sql += " ORDER BY screening_date, screening_time";

        try (Connection conn = DatabaseConnection.connectDB(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                schedules.add(mapResultSetToScreeningSchedule(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all screening schedules: " + e.getMessage());
        }
        return schedules;
    }

    public List<ScreeningSchedule> getScreeningsByDateRange(Date startDate, Date endDate, boolean includePast) {
        List<ScreeningSchedule> schedules = new ArrayList<>();
        String sql = "SELECT * FROM screening_schedule "
                + "WHERE screening_date BETWEEN ? AND ?";

        if (!includePast) {
            sql += " AND CONCAT(screening_date, ' ', screening_time) > NOW()";
        }

        sql += " ORDER BY screening_date, screening_time";

        try (Connection conn = DatabaseConnection.connectDB(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, startDate);
            stmt.setDate(2, endDate);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    schedules.add(mapResultSetToScreeningSchedule(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting screenings by date range: " + e.getMessage());
        }
        return schedules;
    }

    // Update a screening schedule
    public boolean updateScreeningSchedule(ScreeningSchedule schedule) {
        String sql = "UPDATE screening_schedule SET film_id = ?, screen_id = ?, screening_date = ?, screening_time = ? WHERE schedule_id = ?";

        try (Connection conn = DatabaseConnection.connectDB(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, schedule.getFilmId());
            stmt.setInt(2, schedule.getScreenId());
            stmt.setDate(3, schedule.getScreeningDate());
            stmt.setTime(4, schedule.getScreeningTime());
            stmt.setInt(5, schedule.getScheduleId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating screening schedule: " + e.getMessage());
            return false;
        }
    }

    // Delete a screening schedule
    public boolean deleteScreeningSchedule(int scheduleId) {
        String sql = "DELETE FROM screening_schedule WHERE schedule_id = ?";

        try (Connection conn = DatabaseConnection.connectDB(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, scheduleId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting screening schedule: " + e.getMessage());
            return false;
        }
    }

    // Helper method to map ResultSet to ScreeningSchedule object
    private ScreeningSchedule mapResultSetToScreeningSchedule(ResultSet rs) throws SQLException {
        ScreeningSchedule schedule = new ScreeningSchedule();
        schedule.setScheduleId(rs.getInt("schedule_id"));
        schedule.setFilmId(rs.getInt("film_id"));
        schedule.setScreenId(rs.getInt("screen_id"));
        schedule.setScreeningDate(rs.getDate("screening_date"));
        schedule.setScreeningTime(rs.getTime("screening_time"));
        return schedule;
    }
}
