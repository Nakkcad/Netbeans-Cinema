package Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NewClass {
    private Connection connection;

    public NewClass() {
        this.connection = DatabaseConnection.connectDB(); // get connection from your DB class
    }

    public void insertUser() {
        String username = "admin";
        String email = "admin@gmail.com"; 
        String phoneNumber = "0123456789"; 
        String pass = "admin4321";

        // Hash the password before storing
        String hashed = PasswordUtils.hashPassword(pass);

        String sql = "INSERT INTO customer (username, email, phone_number, password) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, phoneNumber);
            stmt.setString(4, hashed);

            stmt.executeUpdate();

            System.out.println("User inserted successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        NewClass inserter = new NewClass();  // Correct object creation
        inserter.insertUser();               // Call insertUser()
    }
}
