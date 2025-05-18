package Utils;

import com.google.gson.*;
import dao.FilmDAO;
import java.io.*;
import java.net.*;
import java.util.*;
import model.Film;
import javax.swing.*;
import java.awt.*;
// In your imports section, keep:
import java.util.List;
import java.util.ArrayList;

// Then in your code, use List normally:
/**
 * Utility class for fetching movie data from The Movie Database (TMDB) API.
 * This class provides functionality to retrieve movie information from the TMDB API,
 * including now playing, top rated, and popular movies. It can fetch multiple movies
 * at once or retrieve detailed information about a specific movie by its ID.
 * The fetched data is converted into Film objects that can be stored in the database.
 * @author hp
 */
public class TMDBFetcher {
    
List<Film> films = new ArrayList<>();
    /**
     * The API key used for authenticating requests to the TMDB API.
     */
    private static final String API_KEY = "76104b3bc3dd38c735f7a2347034a853";
    /**
     * The API key used for authenticating requests to the TMDB API.
     */
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    /**
     * The URL for fetching detailed information about a specific movie.
     */
    private static final String DETAIL_URL = "https://api.themoviedb.org/3/movie/";
    /**
     * Enumeration of different movie categories that can be fetched from TMDB.
     * This enum defines the different types of movie lists available from the TMDB API,
     * including now playing (currently in theaters), top rated (highest rated movies),
     * and popular (most popular movies).
     */
    public enum FetchType {
        NOW_PLAYING("now_playing"),
        TOP_RATED("top_rated"),
        POPULAR("popular");
        
        private final String path;
        /**
         * Constructs a FetchType with the specified API path.
         * @param path the API path segment for this fetch type
         */
        FetchType(String path) {
            this.path = path;
        }
        /**
         * Gets the API path segment for this fetch type.
         * @return the path segment used in API requests
         */
        public String getPath() {
            return path;
        }
    }
    /**
     * Main method to launch the TMDB Fetcher GUI.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }
    /**
     * Launches the TMDB Fetcher GUI.
     * This method provides a convenient way to start the GUI from other classes.
     */
    public static void gui() {
        createAndShowGUI();
    }
    
    /**
     * Creates and displays the TMDB Fetcher GUI.
     * This method sets up a graphical user interface that allows users to specify
     * the number and types of movies to fetch from TMDB. It includes options for
     * fetching now playing, top rated, and popular movies, with customizable counts
     * for each category.
     */
    private static void createAndShowGUI() {
        JFrame frame = new JFrame("TMDB Movie Fetcher");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout(10, 10));

        // Create panel for input fields
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Movie count field
        JLabel countLabel = new JLabel("Number of Movies:");
        JTextField countField = new JTextField("10");
        inputPanel.add(countLabel);
        inputPanel.add(countField);

        // Now Playing checkbox
        JCheckBox nowPlayingCheck = new JCheckBox("Now Playing");
        nowPlayingCheck.setSelected(true);
        inputPanel.add(nowPlayingCheck);

        JTextField nowPlayingField = new JTextField("10");
        nowPlayingField.setEnabled(false);
        inputPanel.add(nowPlayingField);

        // Top Rated checkbox
        JCheckBox topRatedCheck = new JCheckBox("Top Rated");
        inputPanel.add(topRatedCheck);

        JTextField topRatedField = new JTextField("10");
        topRatedField.setEnabled(false);
        inputPanel.add(topRatedField);

        // Popular checkbox
        JCheckBox popularCheck = new JCheckBox("Popular");
        inputPanel.add(popularCheck);

        JTextField popularField = new JTextField("10");
        popularField.setEnabled(false);
        inputPanel.add(popularField);

        // Add listeners to enable/disable fields based on checkbox
        nowPlayingCheck.addActionListener(e -> {
            nowPlayingField.setEnabled(nowPlayingCheck.isSelected());
            if (!nowPlayingCheck.isSelected()) nowPlayingField.setText("0");
        });

        topRatedCheck.addActionListener(e -> {
            topRatedField.setEnabled(topRatedCheck.isSelected());
            if (!topRatedCheck.isSelected()) topRatedField.setText("0");
        });

        popularCheck.addActionListener(e -> {
            popularField.setEnabled(popularCheck.isSelected());
            if (!popularCheck.isSelected()) popularField.setText("0");
        });

        // Create fetch button
        JButton fetchButton = new JButton("Fetch Movies");
        fetchButton.addActionListener(e -> {
            try {
                int totalCount = Integer.parseInt(countField.getText());
                int nowPlayingCount = nowPlayingCheck.isSelected() ? Integer.parseInt(nowPlayingField.getText()) : 0;
                int topRatedCount = topRatedCheck.isSelected() ? Integer.parseInt(topRatedField.getText()) : 0;
                int popularCount = popularCheck.isSelected() ? Integer.parseInt(popularField.getText()) : 0;

                if (nowPlayingCount + topRatedCount + popularCount > totalCount) {
                    JOptionPane.showMessageDialog(frame, 
                            "Sum of individual counts cannot exceed total count!", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                TMDBFetcher fetcher = new TMDBFetcher();
                FilmDAO dao = new FilmDAO();
                List<Film> allFilms = new ArrayList<>();

                if (nowPlayingCount > 0) {
                    List<Film> films = fetcher.fetchMovies(FetchType.NOW_PLAYING, nowPlayingCount);
                    allFilms.addAll(films);
                }

                if (topRatedCount > 0) {
                    List<Film> films = fetcher.fetchMovies(FetchType.TOP_RATED, topRatedCount);
                    allFilms.addAll(films);
                }

                if (popularCount > 0) {
                    List<Film> films = fetcher.fetchMovies(FetchType.POPULAR, popularCount);
                    allFilms.addAll(films);
                }

                dao.insertFilms(allFilms);
                JOptionPane.showMessageDialog(frame, 
                        "Successfully fetched and stored " + allFilms.size() + " movies!", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, 
                        "Please enter valid numbers in all fields!", 
                        "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, 
                        "Error fetching movies: " + ex.getMessage(), 
                        "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        // Add components to frame
        frame.add(inputPanel, BorderLayout.CENTER);
        frame.add(fetchButton, BorderLayout.SOUTH);

        // Center the frame
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Fetches a specified number of movies of a given type from TMDB.
     * This method retrieves movies from the TMDB API based on the specified category(now playing, top rated, or popular) and count. It filters out adult content
     * and movies that already exist in the database to avoid duplicates. For each movie, it fetches additional details such as genre and duration.
     * @param type the category of movies to fetch (NOW_PLAYING, TOP_RATED, or POPULAR)
     * @param totalMovies the number of movies to fetch
     * @return a list of Film objects containing the fetched movie data
     * @throws IOException if an error occurs during the API request
     */
    public List<Film> fetchMovies(FetchType type, int totalMovies) throws IOException {
        List<Film> films = new ArrayList<>();
        FilmDAO dao = new FilmDAO();

        System.out.println("[DEBUG] Loading existing films from DB...");
        Set<String> existingTitles = new HashSet<>();
        for (Film f : dao.getFilms(null)) {
            existingTitles.add(f.getTitle().toLowerCase());
        }
        System.out.println("[DEBUG] Loaded " + existingTitles.size() + " existing titles.");

        int moviesFetched = 0;
        int page = 1;
        Gson gson = new Gson();

        while (moviesFetched < totalMovies) {
            String url = BASE_URL + type.getPath() + "?api_key=" + API_KEY + "&page=" + page;
            System.out.println("[DEBUG] Fetching movies from URL: " + url);
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            JsonObject json = gson.fromJson(reader, JsonObject.class);
            reader.close();

            JsonArray results = json.getAsJsonArray("results");
            System.out.println("[DEBUG] Fetched " + results.size() + " movies from page " + page);

            for (JsonElement e : results) {
                JsonObject obj = e.getAsJsonObject();

                if (obj.get("adult").getAsBoolean()) {
                    continue;
                }

                String title = obj.get("title").getAsString();

                if (existingTitles.contains(title.toLowerCase())) {
                    System.out.println("[DEBUG] Skipped existing movie: " + title);
                    continue;
                }

                int movieId = obj.get("id").getAsInt();
                String overview = obj.get("overview").getAsString();
                String posterPath = obj.get("poster_path").isJsonNull() ? "" : obj.get("poster_path").getAsString();
                String releaseDate = obj.get("release_date").isJsonNull() ? "2000-01-01" : obj.get("release_date").getAsString();
                double rating = obj.get("vote_average").isJsonNull() ? 0.0 : obj.get("vote_average").getAsDouble();

                MovieDetail detail = fetchMovieDetail(movieId);

                Film film = new Film(
                        title,
                        detail.genre,
                        detail.duration,
                        overview,
                        "https://image.tmdb.org/t/p/w500" + posterPath,
                        releaseDate,
                        rating
                );

                films.add(film);
                existingTitles.add(title.toLowerCase());
                moviesFetched++;

                System.out.println("[DEBUG] Added movie: " + title + " (" + detail.genre + ", " + detail.duration + " min, Rating: " + rating + ")");

                if (moviesFetched >= totalMovies) {
                    break;
                }
            }

            page++;
        }

        System.out.println("[DEBUG] Total movies fetched: " + moviesFetched);
        return films;
    }
    /**
     * Fetches detailed information about a specific movie from TMDB.
     * This private method retrieves additional details about a movie, such as its duration and genre, which are not available in the basic movie list API.
     * @param movieId the TMDB ID of the movie to fetch details for
     * @return a MovieDetail object containing the movie's duration and genre
     * @throws IOException if an error occurs during the API request
     */
    private MovieDetail fetchMovieDetail(int movieId) throws IOException {
        String url = DETAIL_URL + movieId + "?api_key=" + API_KEY;
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        JsonObject json = new Gson().fromJson(reader, JsonObject.class);
        reader.close();

        int duration = json.get("runtime").isJsonNull() ? 120 : json.get("runtime").getAsInt();

        String genre = "Unknown";
        JsonArray genresArray = json.getAsJsonArray("genres");
        if (genresArray != null && genresArray.size() > 0) {
            genre = genresArray.get(0).getAsJsonObject().get("name").getAsString();
        }

        return new MovieDetail(duration, genre);
    }
    /**
     * Fetches a single movie by its TMDB ID.
     * This method retrieves detailed information about a specific movie from the TMDB API
     * using its unique ID. It includes all available details such as title, overview,
     * poster path, release date, duration, rating, and genre.
     * @param movieId the TMDB ID of the movie to fetch
     * @return a Film object containing the movie data, or null if the movie is adult content
     * @throws IOException if an error occurs during the API request
     */
    public Film fetchMovieById(int movieId) throws IOException {
        String url = DETAIL_URL + movieId + "?api_key=" + API_KEY;
        System.out.println("[DEBUG] Fetching single movie by ID: " + movieId);
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        JsonObject json = new Gson().fromJson(reader, JsonObject.class);
        reader.close();

        if (json.get("adult").getAsBoolean()) {
            System.out.println("[DEBUG] Movie is adult content, skipping.");
            return null;
        }

        String title = json.get("title").getAsString();
        String overview = json.get("overview").getAsString();
        String posterPath = json.get("poster_path").isJsonNull() ? "" : json.get("poster_path").getAsString();
        String releaseDate = json.get("release_date").isJsonNull() ? "2000-01-01" : json.get("release_date").getAsString();
        int duration = json.get("runtime").isJsonNull() ? 120 : json.get("runtime").getAsInt();
        double rating = json.get("vote_average").isJsonNull() ? 0.0 : json.get("vote_average").getAsDouble();

        String genre = "Unknown";
        JsonArray genresArray = json.getAsJsonArray("genres");
        if (genresArray != null && genresArray.size() > 0) {
            genre = genresArray.get(0).getAsJsonObject().get("name").getAsString();
        }

        System.out.println("[DEBUG] Movie fetched: " + title);
        return new Film(title, genre, duration, overview,
                "https://image.tmdb.org/t/p/w500" + posterPath,
                releaseDate,
                rating);
    }
    /**
     * Inner class to hold detailed movie information about genre and duration.
     */
    private class MovieDetail {
        int duration;
        String genre;

        MovieDetail(int duration, String genre) {
            this.duration = duration;
            this.genre = genre;
        }
    }
}