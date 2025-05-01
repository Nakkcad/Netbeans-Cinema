package model;

public class Screen {
    private int screenId;
    private String screenName;
    private int capacity;

    // Constructors
    public Screen() {}

    public Screen(String screenName, int capacity) {
        this.screenName = screenName;
        this.capacity = capacity;
    }

    public Screen(int screenId, String screenName, int capacity) {
        this.screenId = screenId;
        this.screenName = screenName;
        this.capacity = capacity;
    }

    // Getters and setters
    public int getScreenId() {
        return screenId;
    }

    public void setScreenId(int screenId) {
        this.screenId = screenId;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "Screen{" +
                "screenId=" + screenId +
                ", screenName='" + screenName + '\'' +
                ", capacity=" + capacity +
                '}';
    }
}