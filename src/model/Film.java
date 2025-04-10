package model;

public class Film {
    private int filmId;
    private String title;
    private String genre;
    private int duration;
    private String synopsis;
    private String posterUrl;
    private String releaseDate; // New field

    // Constructors
    public Film() {}

    public Film(String title, String genre, int duration, String synopsis, String posterUrl, String releaseDate) {
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.synopsis = synopsis;
        this.posterUrl = posterUrl;
        this.releaseDate = releaseDate;
    }

    // Getters and setters
    public int getFilmId() { return filmId; }
    public void setFilmId(int filmId) { this.filmId = filmId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public String getSynopsis() { return synopsis; }
    public void setSynopsis(String synopsis) { this.synopsis = synopsis; }

    public String getPosterUrl() { return posterUrl; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }

    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }
}
