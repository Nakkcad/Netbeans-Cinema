package dao;

import Utils.DatabaseConnection;
import model.Film;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
    /**
    * Data Access Object for managing film-related operations in the database.
    * This class provides methods to interact with the film data in the database,including adding, retrieving, updating, and deleting films. It serves as an
    intermediary between the application and the database for all film-related operations, handling database connections and query executions.
    * @author hp
    */
public class FilmDAO {

    // Add rating to the addFilm method
    /**
     * Adds a new film to the database.
     * This method inserts a new film record into the database with the provided
     * film information. If the insertion is successful, the generated film ID is
     * set in the film object.
     * @param film the Film object containing the film information to add
     * @return true if the film was successfully added, false otherwise
     */
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
    /**
     * Retrieves a list of films from the database with an optional limit.
     * This method fetches films from the database, ordered by title. If a limit
     * is provided, only that number of films will be returned
     * @param limit the maximum number of films to return, or null for all films
     * @return a list of Film objects
     */
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
    /**
     * Retrieves a list of top-rated films from the database.
     * This method fetches films from the database, ordered by rating in descending
     * order, limited to the specified number of results.
     * @param limit the maximum number of films to return
     * @return a list of Film objects, ordered by rating
     */
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
    /**
     * Retrieves a list of films of a specific genre from the database.
     * This method fetches films of the specified genre from the database,
     * ordered by rating in descending order, limited to the specified number of results.
     * @param genre the genre to filter by
     * @param limit the maximum number of films to return
     * @return a list of Film objects of the specified genre
     */
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
    /**
     * Retrieves a list of the newest films from the database.
     * This method fetches films from the database, ordered by release date in
     * descending order (newest first), limited to the specified number of results.
     * @param limit the maximum number of films to return
     * @return a list of Film objects, ordered by release date
     */
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
    /**
     * Retrieves a film by its title.
     * This method fetches a single film from the database based on its title.
     * If no film with the specified title is found, null is returned.
     * @param title the title of the film to retrieve
     * @return the Film object if found, null otherwise
     */
    public Film getFilmByTitle(String title) {
        String sql = "SELECT * FROM film WHERE title = ?";
        try (Connection conn = DatabaseConnection.connectDB(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToFilm(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting film by title: " + e.getMessage());
        }
        return null;
    }

    // Get all films that have at least one screening schedule
    /**
     * Retrieves all films that have at least one screening schedule.
     * This method fetches films from the database that are associated with at least
     * one screening schedule, ordered by title.
     * @return a list of Film objects with screening schedules
     */
    public List<Film> getAllFilmsWithSchedule() {
        List<Film> films = new ArrayList<>();
        String sql = "SELECT DISTINCT f.* FROM film f "
                + "INNER JOIN screening_schedule s ON f.film_id = s.film_id "
                + "ORDER BY f.title";

        try (Connection conn = DatabaseConnection.connectDB(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Film film = mapResultSetToFilm(rs);
                films.add(film);
            }
        } catch (SQLException e) {
            System.err.println("Error getting films with schedules: " + e.getMessage());
        }

        return films;
    }
    /**
     * Inserts multiple films into the database in a batch operation.
     * This method inserts a list of films into the database using batch processing
     * for improved performance when adding multiple films at once.
     * @param films the list of Film objects to insert
     */
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
    /**
     * Deletes a film from the database by its ID.
     * This method removes a film record from the database based on its film ID.
     * @param filmId the ID of the film to delete
     * @return true if the film was successfully deleted, false otherwise
     */
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
    /**
     * Retrieves a film by its ID.
     * This method fetches a single film from the database based on its film ID.
     * If no film with the specified ID is found, null is returned.
     * @param filmId the ID of the film to retrieve
     * @return the Film object if found, null otherwise
     */
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
    /**
     * Updates an existing film in the database.
     * This method updates a film record in the database with the provided
     * film information, identified by the film ID.
     * @param film the Film object containing the updated film information
     * @return true if the film was successfully updated, false otherwise
     */
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
    /**
     * Helper method to map a ResultSet to a Film object.
     * This private method converts a database result row into a Film object,
     * mapping each column to the corresponding property of the Film class.
     * @param rs the ResultSet containing film data
     * @return a populated Film object
     * @throws SQLException if a database error occurs
     */
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
