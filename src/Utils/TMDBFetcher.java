package Utils;

import com.google.gson.*;
import dao.FilmDAO;
import java.io.*;
import java.net.*;
import java.util.*;
import model.Film;

public class TMDBFetcher {

    private static final String API_KEY = "76104b3bc3dd38c735f7a2347034a853";
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/now_playing";
    private static final String DETAIL_URL = "https://api.themoviedb.org/3/movie/";

    public List<Film> fetchMovies(int totalMovies) throws IOException {
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
            String url = BASE_URL + "?api_key=" + API_KEY + "&page=" + page;
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
//                    System.out.println("[DEBUG] Skipped adult content: " + obj.get("title").getAsString());
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
//        System.out.println("[DEBUG] Fetching movie detail for ID: " + movieId);
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

//        System.out.println("[DEBUG] Detail fetched - Genre: " + genre + ", Duration: " + duration + " mins");
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
