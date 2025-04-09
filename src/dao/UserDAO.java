/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import Utils.DatabaseConnection;
import Utils.PasswordUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class UserDAO {

    private Connection connection;

    public UserDAO() {
        this.connection = DatabaseConnection.connectDB();
    }

    // Method to authenticate user
    public String authenticateUser(String username, String passwordInput) {
        if (connection == null) {
            JOptionPane.showMessageDialog(null, "Database connection error");
            return null;
        }

        try {
            String sql = "SELECT password, role FROM customer WHERE username=?";
            PreparedStatement pst = connection.prepareStatement(sql);
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String storedHashedPassword = rs.getString("password");
                String role = rs.getString("role");
                String hashedInputPassword = PasswordUtils.hashPassword(passwordInput);

                if (storedHashedPassword.equals(hashedInputPassword)) {
                    return role; // Return the user's role if authentication succeeds
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database error: " + e.getMessage());
        }
        return null; // Return null if authentication fails
    }

    // Method to check if username exists
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
