package dao;

import Utils.DatabaseConnection;
import model.Film;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FilmDAO {
    
    // Add a new film to the database
    public boolean addFilm(Film film) {
        String sql = "INSERT INTO film (title, genre, duration, synopsis, poster_url) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.connectDB();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, film.getTitle());
            stmt.setString(2, film.getGenre());
            stmt.setInt(3, film.getDuration());
            stmt.setString(4, film.getSynopsis());
            stmt.setString(5, film.getPosterUrl());
            
            int affectedRows = stmt.executeUpdate();
            
            // Set the generated film ID
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        film.setFilmId(rs.getInt(1));
                    }
                }
            }
            
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error adding film: " + e.getMessage());
            return false;
        }
    }

    // Get all films from the database
    public List<Film> getAllFilms() {
        List<Film> films = new ArrayList<>();
        String sql = "SELECT * FROM film ORDER BY title";
        
        try (Connection conn = DatabaseConnection.connectDB();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Film film = mapResultSetToFilm(rs);
                films.add(film);
            }
        } catch (SQLException e) {
            System.err.println("Error getting films: " + e.getMessage());
        }
        
        return films;
    }

    // Delete a film by ID
    public boolean deleteFilm(int filmId) {
        String sql = "DELETE FROM film WHERE film_id = ?";
        
        try (Connection conn = DatabaseConnection.connectDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, filmId);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting film ID " + filmId + ": " + e.getMessage());
            return false;
        }
    }

    // Get a single film by ID
    public Film getFilmById(int filmId) {
        String sql = "SELECT * FROM film WHERE film_id = ?";
        
        try (Connection conn = DatabaseConnection.connectDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, filmId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToFilm(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting film ID " + filmId + ": " + e.getMessage());
        }
        
        return null;
    }

    // Update an existing film
    public boolean updateFilm(Film film) {
        String sql = "UPDATE film SET title = ?, genre = ?, duration = ?, synopsis = ?, poster_url = ? WHERE film_id = ?";
        
        try (Connection conn = DatabaseConnection.connectDB();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, film.getTitle());
            stmt.setString(2, film.getGenre());
            stmt.setInt(3, film.getDuration());
            stmt.setString(4, film.getSynopsis());
            stmt.setString(5, film.getPosterUrl());
            stmt.setInt(6, film.getFilmId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating film ID " + film.getFilmId() + ": " + e.getMessage());
            return false;
        }
    }

    // Helper method to map ResultSet to Film object
    private Film mapResultSetToFilm(ResultSet rs) throws SQLException {
        Film film = new Film();
        film.setFilmId(rs.getInt("film_id"));
        film.setTitle(rs.getString("title"));
        film.setGenre(rs.getString("genre"));
        film.setDuration(rs.getInt("duration"));
        film.setSynopsis(rs.getString("synopsis"));
        film.setPosterUrl(rs.getString("poster_url"));
        return film;
    }
}