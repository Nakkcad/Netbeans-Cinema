package Utils;

public class UserSession {
    private static String username;
    private static String role;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        UserSession.username = username;
    }

    public static String getRole() {
        return role;
    }

    public static void setRole(String role) {
        UserSession.role = role;
    }

    public static void clearSession() {
        username = null;
        role = null;
    }
}