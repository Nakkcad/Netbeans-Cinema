/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import Utils.Authenticate;
import Utils.DatabaseConnection;
import Utils.PasswordUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
/**
 * Data Access Object for managing user-related operations in the database.
 * This class provides methods to interact with the user data in the database,
 including user authentication, user creation, and username validation. It serves as an intermediary between the application and the database for all user-related
 operations, handling the database connections and query executions.
 * @author hp
 */
public class UserDAO {

   

    private Connection connection;

    public UserDAO() {
        this.connection = DatabaseConnection.connectDB();
    }

    
    
    // Method to authenticate user
    /**
     * Authenticates a user based on username and password.
     * This method verifies the provided username and password against the stored
     * credentials in the database. If authentication is successful, it returns
     * an Authenticate object containing the user's role and ID. If authentication
     * fails, it returns null.
     * @param username the username to authenticate
     * @param passwordInput the password to verify
     * @return an Authenticate object if authentication is successful, null otherwise
     * @throws SQLException if a database error occurs
     */
    public Authenticate authenticateUser(String username, String passwordInput) throws SQLException {
    String sql = "SELECT password, role, customer_id FROM customer WHERE username=?";
    PreparedStatement pst = connection.prepareStatement(sql);
    pst.setString(1, username);
    ResultSet rs = pst.executeQuery();

    if (rs.next()) {
        String storedHashedPassword = rs.getString("password");
        String role = rs.getString("role");
        int userid = rs.getInt("customer_id");
        String hashedInputPassword = PasswordUtils.hashPassword(passwordInput);

        if (storedHashedPassword.equals(hashedInputPassword)) {
            return new Authenticate(role, userid);
        }
    }

    return null; // Authentication failed
}


    // Method to check if username exists
    /**
     * Checks if a username already exists in the database.
     * This method queries the database to determine if the specified username
     * is already taken. It's typically used during user registration to ensure
     * username uniqueness.
     * @param username the username to check
     * @return true if the username exists, false otherwise
     */
    public boolean usernameExists(String username) {
        try {
            PreparedStatement checkStmt = connection.prepareStatement("SELECT * FROM customer WHERE username = ?");
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
            return false;
        }
    }

    // Method to create new user
   /**
    * Creates a new user in the database.
     * This method inserts a new user record into the database with the provided
     * information. The password is hashed before storage for security. This method
     * is typically used during user registration.
     * @param username the username for the new user
     * @param email the email address of the new user
     * @param phone the phone number of the new user
     * @param password the password for the new user (will be hashed)
     * @return true if the user was successfully created, false otherwise
    */
    public boolean createUser(String username, String email, String phone, String password) {
        try {
            String hashedPassword = PasswordUtils.hashPassword(password);
            PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO customer (username, email, phone_number, password) VALUES (?, ?, ?, ?)");
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, phone);
            stmt.setString(4, hashedPassword);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
            return false;
        }
    }

    // Close connection when done
    /**
     * Closes the database connection.
     * This method should be called when the DAO is no longer needed to release
     * database resources. It ensures that the connection to the database is properly closed to prevent resource leaks.
     */
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error closing connection: " + e.getMessage());
        }
    }
}
