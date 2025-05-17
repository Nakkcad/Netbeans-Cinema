package Utils;

public class UserSession {
    private static UserSession instance;

    private String username;
    private int userId; // <-- Add this line

    private UserSession(String username, int userId) {
        this.username = username;
        this.userId = userId; // <-- And this
    }

    public static void createSession(String username, int userId) {
        if (instance == null) {
            instance = new UserSession(username, userId);
        }
    }

    public static UserSession getInstance() {
        return instance;
    }

    public String getUsername() {
        return username;
    }

    public int getUserId() {
        return userId; // <-- Add this getter
    }

    public static void clearSession() {
        instance = null;
    }
}
