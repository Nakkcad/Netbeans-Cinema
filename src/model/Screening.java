package model;

public class Screening {
    private int scheduleId;
    private int filmId;
    private String date;  // Format: YYYY-MM-DD
    private String time;  // Format: HH:MM:SS

    public Screening() {}

    public Screening(int filmId, String date, String time) {
        this.filmId = filmId;
        this.date = date;
        this.time = time;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public int getFilmId() {
        return filmId;
    }

    public void setFilmId(int filmId) {
        this.filmId = filmId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Screening{" +
                "scheduleId=" + scheduleId +
                ", filmId=" + filmId +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
