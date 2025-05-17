package Utils;

import com.google.gson.*;
import dao.FilmDAO;
import java.io.*;
import java.net.*;
import java.util.*;
import model.Film;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// In your imports section, keep:
import java.util.List;
import java.util.ArrayList;

// Then in your code, use List normally:
public class TMDBFetcher {
    
List<Film> films = new ArrayList<>();

    private static final String API_KEY = "76104b3bc3dd38c735f7a2347034a853";
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String DETAIL_URL = "https://api.themoviedb.org/3/movie/";

    public enum FetchType {
        NOW_PLAYING("now_playing"),
        TOP_RATED("top_rated"),
        POPULAR("popular");
        
        private final String path;
        
        FetchType(String path) {
            this.path = path;
        }
        
        public String getPath() {
            return path;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("TMDB Movie Fetcher");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

    private class MovieDetail {
        int duration;
        String genre;

        MovieDetail(int duration, String genre) {
            this.duration = duration;
            this.genre = genre;
        }
    }
}