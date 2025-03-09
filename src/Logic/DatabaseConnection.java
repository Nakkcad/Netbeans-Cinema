package Logic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/bioskop_db"; // XAMPP MySQL Database URL
    private static final String USER = "root"; // Default MySQL username in XAMPP
    private static final String PASSWORD = ""; // Default MySQL password in XAMPP (empty by default)

    public static Connection connectDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Load MySQL JDBC Driver
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database Connected Successfully!");
            return conn;
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Database Connection Failed: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}
