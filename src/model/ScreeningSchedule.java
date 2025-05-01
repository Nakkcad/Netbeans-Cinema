package model;

import java.sql.Date;
import java.sql.Time;

public class ScreeningSchedule {
    private int scheduleId;
    private int filmId;
    private int screenId;
    private Date screeningDate;
    private Time screeningTime;

    // Constructors
    public ScreeningSchedule() {}

    public ScreeningSchedule(int filmId, int screenId, Date screeningDate, Time screeningTime) {
        this.filmId = filmId;
        this.screenId = screenId;
        this.screeningDate = screeningDate;
        this.screeningTime = screeningTime;
    }

    public ScreeningSchedule(int scheduleId, int filmId, int screenId, Date screeningDate, Time screeningTime) {
        this.scheduleId = scheduleId;
        this.filmId = filmId;
        this.screenId = screenId;
        this.screeningDate = screeningDate;
        this.screeningTime = screeningTime;
    }

    // Getters and setters
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

    public int getScreenId() {
        return screenId;
    }

    public void setScreenId(int screenId) {
        this.screenId = screenId;
    }

    public Date getScreeningDate() {
        return screeningDate;
    }

    public void setScreeningDate(Date screeningDate) {
        this.screeningDate = screeningDate;
    }

    public Time getScreeningTime() {
        return screeningTime;
    }

    public void setScreeningTime(Time screeningTime) {
        this.screeningTime = screeningTime;
    }

    @Override
    public String toString() {
        return "ScreeningSchedule{" +
                "scheduleId=" + scheduleId +
                ", filmId=" + filmId +
                ", screenId=" + screenId +
                ", screeningDate=" + screeningDate +
                ", screeningTime=" + screeningTime +
                '}';
    }
}