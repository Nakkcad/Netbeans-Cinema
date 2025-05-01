package dao;

import Utils.DatabaseConnection;
import model.Film;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FilmDAO {

    // Add rating to the addFilm method
    public boolean addFilm(Film film) {
        String sql = "INSERT INTO film (title, genre, duration, synopsis, poster_url, release_date, rating) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.connectDB(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, film.getTitle());
            stmt.setString(2, film.getGenre());
            stmt.setInt(3, film.getDuration());
            stmt.setString(4, film.getSynopsis());
            stmt.setString(5, film.getPosterUrl());
            stmt.setString(6, film.getReleaseDate());
            stmt.setDouble(7, film.getRating());

            int affectedRows = stmt.executeUpdate();

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

    // Get films with optional limit
    public List<Film> getFilms(Integer limit) {
        List<Film> films = new ArrayList<>();
        String sql = "SELECT * FROM film ORDER BY title";
        if (limit != null) {
            sql += " LIMIT " + limit;
        }

        try (Connection conn = DatabaseConnection.connectDB(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Film film = mapResultSetToFilm(rs);
                films.add(film);
            }
        } catch (SQLException e) {
            System.err.println("Error getting films: " + e.getMessage());
        }

        return films;
    }

    // Get top rated films
    public List<Film> getTopRatedFilms(int limit) {
        List<Film> films = new ArrayList<>();
        String sql = "SELECT * FROM film ORDER BY rating DESC LIMIT ?";

        try (Connection conn = DatabaseConnection.connectDB(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Film film = mapResultSetToFilm(rs);
                    films.add(film);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting top rated films: " + e.getMessage());
        }

        return films;
    }

    // Get films by genre
    public List<Film> getFilmsByGenre(String genre, int limit) {
        List<Film> films = new ArrayList<>();
        String sql = "SELECT * FROM film WHERE genre = ? ORDER BY rating DESC LIMIT ?";

        try (Connection conn = DatabaseConnection.connectDB(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, genre);
            stmt.setInt(2, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Film film = mapResultSetToFilm(rs);
                    films.add(film);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting films by genre: " + e.getMessage());
        }

        return films;
    }

    public List<Film> getFilmsByNewest(int limit) {
        List<Film> films = new ArrayList<>();
        String sql = "SELECT * FROM film ORDER BY release_date DESC LIMIT ?";

        try (Connection conn = DatabaseConnection.connectDB(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limit);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Film film = mapResultSetToFilm(rs);
                    films.add(film);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting top rated films: " + e.getMessage());
        }

        return films;
    }

    public void insertFilms(List<Film> films) {
        String sql = "INSERT INTO film (title, genre, duration, synopsis, poster_url, release_date, rating) VALUES (?, ?, ?, ?, ?, ?, ?)"; // Added 'rating'

        try (Connection conn = DatabaseConnection.connectDB(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (Film f : films) {
                stmt.setString(1, f.getTitle());
                stmt.setString(2, f.getGenre());
                stmt.setInt(3, f.getDuration());
                stmt.setString(4, f.getSynopsis());
                stmt.setString(5, f.getPosterUrl());
                stmt.setString(6, f.getReleaseDate());
                stmt.setDouble(7, f.getRating());  // Added this line to set the rating
                stmt.addBatch();
            }

            stmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete a film by ID
    public boolean deleteFilm(int filmId) {
        String sql = "DELETE FROM film WHERE film_id = ?";

        try (Connection conn = DatabaseConnection.connectDB(); PreparedStatement stmt = conn.prepareStatement(sql)) {

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

        try (Connection conn = DatabaseConnection.connectDB(); PreparedStatement stmt = conn.prepareStatement(sql)) {

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
        String sql = "UPDATE film SET title = ?, genre = ?, duration = ?, synopsis = ?, poster_url = ?, release_date = ? WHERE film_id = ?";

        try (Connection conn = DatabaseConnection.connectDB(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, film.getTitle());
            stmt.setString(2, film.getGenre());
            stmt.setInt(3, film.getDuration());
            stmt.setString(4, film.getSynopsis());
            stmt.setString(5, film.getPosterUrl());
            stmt.setString(6, film.getReleaseDate()); // ADD THIS
            stmt.setInt(7, film.getFilmId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating film ID " + film.getFilmId() + ": " + e.getMessage());
            return false;
        }
    }

    private Film mapResultSetToFilm(ResultSet rs) throws SQLException {
        Film film = new Film();
        film.setFilmId(rs.getInt("film_id"));
        film.setTitle(rs.getString("title"));
        film.setGenre(rs.getString("genre"));
        film.setDuration(rs.getInt("duration"));
        film.setSynopsis(rs.getString("synopsis"));
        film.setPosterUrl(rs.getString("poster_url"));
        film.setReleaseDate(rs.getString("release_date"));
        film.setRating(rs.getDouble("rating"));
        return film;
    }
}
