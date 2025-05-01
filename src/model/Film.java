package model;

public class Film {
    private int filmId;
    private String title;
    private String genre;
    private int duration;
    private String synopsis;
    private String posterUrl;
    private String releaseDate;
    private double rating; // New field for movie rating

    // Constructors
    public Film() {}

    public Film(String title, String genre, int duration, String synopsis, 
                String posterUrl, String releaseDate) {
        this(title, genre, duration, synopsis, posterUrl, releaseDate, 0.0);
    }

    public Film(String title, String genre, int duration, String synopsis, 
                String posterUrl, String releaseDate, double rating) {
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.synopsis = synopsis;
        this.posterUrl = posterUrl;
        this.releaseDate = releaseDate;
        this.rating = rating;
    }

    // Getters and setters
    public int getFilmId() { 
        return filmId; 
    }
    
    public void setFilmId(int filmId) { 
        this.filmId = filmId; 
    }

    public String getTitle() { 
        return title; 
    }
    
    public void setTitle(String title) { 
        this.title = title; 
    }

    public String getGenre() { 
        return genre; 
    }
    
    public void setGenre(String genre) { 
        this.genre = genre; 
    }

    public int getDuration() { 
        return duration; 
    }
    
    public void setDuration(int duration) { 
        this.duration = duration; 
    }

    public String getSynopsis() { 
        return synopsis; 
    }
    
    public void setSynopsis(String synopsis) { 
        this.synopsis = synopsis; 
    }

    public String getPosterUrl() { 
        return posterUrl; 
    }
    
    public void setPosterUrl(String posterUrl) { 
        this.posterUrl = posterUrl; 
    }

    public String getReleaseDate() { 
        return releaseDate; 
    }
    
    public void setReleaseDate(String releaseDate) { 
        this.releaseDate = releaseDate; 
    }

    // Rating getter and setter
    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        // Ensure rating is between 0.0 and 10.0
        this.rating = Math.max(0.0, Math.min(10.0, rating));
    }

    // Helper method to format duration as hours and minutes
    public String getFormattedDuration() {
        int hours = duration / 60;
        int minutes = duration % 60;
        return String.format("%dh %02dm", hours, minutes);
    }

    // Helper method to get year from release date
    public String getReleaseYear() {
        if (releaseDate == null || releaseDate.isEmpty()) {
            return "N/A";
        }
        try {
            return releaseDate.substring(0, 4);
        } catch (IndexOutOfBoundsException e) {
            return "N/A";
        }
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - %s", title, getReleaseYear(), genre);
    }
}