package Utils;

import com.google.gson.*;
import java.io.*;
import java.net.*;
import java.util.*;
import model.Film;

public class TMDBFetcher {

    private static final String API_KEY = "76104b3bc3dd38c735f7a2347034a853";
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/popular";
    private static final String DETAIL_URL = "https://api.themoviedb.org/3/movie/";

    public List<Film> fetchMovies(int totalMovies) throws IOException {
        List<Film> films = new ArrayList<>();
        int moviesFetched = 0;
        int page = 1;

        Gson gson = new Gson();

        while (moviesFetched < totalMovies) {
            String url = BASE_URL + "?api_key=" + API_KEY + "&page=" + page;
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            JsonObject json = gson.fromJson(reader, JsonObject.class);
            reader.close();

            JsonArray results = json.getAsJsonArray("results");

            for (JsonElement e : results) {
                JsonObject obj = e.getAsJsonObject();
                int movieId = obj.get("id").getAsInt();
                String title = obj.get("title").getAsString();
                String overview = obj.get("overview").getAsString();
                String posterPath = obj.get("poster_path").isJsonNull() ? "" : obj.get("poster_path").getAsString();
                String releaseDate = obj.get("release_date").isJsonNull() ? "2000-01-01" : obj.get("release_date").getAsString();

                // Fetch runtime and genre
                MovieDetail detail = fetchMovieDetail(movieId);

                films.add(new Film(
                        title,
                        detail.genre,
                        detail.duration,
                        overview,
                        "https://image.tmdb.org/t/p/w500" + posterPath,
                        releaseDate
                ));

                moviesFetched++;

                if (moviesFetched >= totalMovies) break;
            }

            page++;
        }

        return films;
    }

    private MovieDetail fetchMovieDetail(int movieId) throws IOException {
        String url = DETAIL_URL + movieId + "?api_key=" + API_KEY;
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        JsonObject json = new Gson().fromJson(reader, JsonObject.class);
        reader.close();

        // Get duration/runtime
        int duration = json.get("runtime").isJsonNull() ? 120 : json.get("runtime").getAsInt();

        // Get genre name (first genre only)
        String genre = "Unknown";
        JsonArray genresArray = json.getAsJsonArray("genres");
        if (genresArray != null && genresArray.size() > 0) {
            genre = genresArray.get(0).getAsJsonObject().get("name").getAsString();
        }

        return new MovieDetail(duration, genre);
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
