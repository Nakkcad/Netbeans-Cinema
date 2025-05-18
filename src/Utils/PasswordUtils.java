package Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
/**
 * Utility class for password handling operations.
 * This class provides methods for password hashing and validation.
 * It implements secure password handling practices using SHA-256 hashing and enforces password strength requirements through validation.
 * @author hp
 */
public class PasswordUtils {

    // Method to hash a password using SHA-256
    /**
     * Hashes a plain text password using SHA-256 algorithm.
     * This method takes a plain text password and applies the SHA-256 hashing
     * algorithm to create a secure hash that can be stored in the database.
     * The resulting hash is represented as a hexadecimal string. The original
     * password cannot be recovered from the hash, providing security for
     * stored passwords.
     * @param password the plain text password to hash
     * @return the hashed password as a hexadecimal string
     * @throws RuntimeException if the SHA-256 algorithm is not available
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    // Method to validate password: At least 8 characters, 1 number, 1 uppercase, 1 lowercase
    /**
     * Validates a password against security requirements.
     * This method checks if a password meets the minimum security requirements:
     * -At least 8 characters in length
     * -Contains at least one digit (0-9)
     * -Contains at least one lowercase letter (a-z)
     * -Contains at least one uppercase letter (A-Z)
     * These requirements help ensure that passwords have sufficient complexity
     * to resist brute force and dictionary attacks.
     * @param password the password to validate
     * @return true if the password meets all requirements, false otherwise
     */
    public static boolean isValidPassword(String password) {
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
