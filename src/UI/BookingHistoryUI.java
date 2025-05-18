package UI;

import dao.BookingDAO;
import model.Booking;
import Utils.UserSession;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.table.JTableHeader;
    /**
    * BookingHistoryUI class provides a user interface for viewing a customer's booking history.
    * It displays a table of all bookings made by the current user with detailed information about each booking.
    * @author hp
    */
public class BookingHistoryUI extends JDialog {
    private static final Color BACKGROUND_COLOR = new Color(30, 32, 34);
    private static final Color TEXT_COLOR = new Color(220, 220, 220);
    private static final Color SECONDARY_COLOR = new Color(60, 63, 65);
    private static final Color ACCENT_COLOR = new Color(255, 204, 0);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font TABLE_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 16);
    /**
     * Constructs a BookingHistoryUI dialog with the specified parent frame.
     * @param parent The parent frame for this dialog
     */
    public BookingHistoryUI(JFrame parent) {
        super(parent, "My Bookings", true);
        initializeUI();
    }
    /**
     * Initializes the user interface components.
     * Sets up the layout, creates the booking history table, and adds control buttons.
     */
    private void initializeUI() {
        setSize(1000, 700);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(BACKGROUND_COLOR);
        JLabel titleLabel = new JLabel("Booking History", JLabel.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Get bookings for current user
        BookingDAO bookingDAO = new BookingDAO();
            List<Booking> bookings = bookingDAO.getBookingsByCustomer(UserSession.getUserId());

        if (bookings.isEmpty()) {
            JLabel noBookingsLabel = new JLabel("No bookings found", JLabel.CENTER);
            noBookingsLabel.setFont(TABLE_FONT);
            noBookingsLabel.setForeground(TEXT_COLOR);
            mainPanel.add(noBookingsLabel, BorderLayout.CENTER);
        } else {
            // Create table model with more detailed columns
            String[] columnNames = {"Booking ID", "Movie", "Date", "Time", "Screen", "Seats", "Total", "Payment Method", "Status"};
            Object[][] data = new Object[bookings.size()][columnNames.length];

            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd, yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

            for (int i = 0; i < bookings.size(); i++) {
                Booking b = bookings.get(i);
                data[i][0] = b.getBookingId();
                data[i][1] = b.getFilmTitle() != null ? b.getFilmTitle() : "N/A";
                data[i][2] = b.getScreeningDate() != null ? dateFormat.format(b.getScreeningDate()) : "N/A";
                data[i][3] = b.getScreeningTime() != null ? timeFormat.format(b.getScreeningTime()) : "N/A";
                data[i][4] = b.getScreenId() > 0 ? "Screen " + b.getScreenId() : "N/A";
                data[i][5] = b.getSeatNumbers() != null ? formatSeatNumbers(b.getSeatNumbers()) : "N/A";
                data[i][6] = String.format("Rp%,.0f", b.getTotalPrice());
                data[i][7] = b.getPaymentMethod() != null ? b.getPaymentMethod() : "N/A";
                data[i][8] = b.getPaymentStatus() != null ? b.getPaymentStatus() : "N/A";
            }

            JTable bookingTable = new JTable(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Make table non-editable
                }
            };
            
            // Customize table appearance
            bookingTable.setFont(TABLE_FONT);
            bookingTable.setForeground(TEXT_COLOR);
            bookingTable.setBackground(SECONDARY_COLOR);
            bookingTable.setSelectionBackground(ACCENT_COLOR);
            bookingTable.setSelectionForeground(Color.BLACK);
            bookingTable.setRowHeight(30);
            bookingTable.setAutoCreateRowSorter(true);
            
            // Customize header
            JTableHeader header = bookingTable.getTableHeader();
            header.setBackground(SECONDARY_COLOR.darker());
            header.setForeground(TEXT_COLOR);
            header.setFont(HEADER_FONT);
            
            JScrollPane scrollPane = new JScrollPane(bookingTable);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.getViewport().setBackground(SECONDARY_COLOR);
            
            mainPanel.add(scrollPane, BorderLayout.CENTER);
        }

        // Add close button
        JButton closeButton = new JButton("Close");
        styleButton(closeButton, ACCENT_COLOR);
        closeButton.addActionListener(e -> dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(closeButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }
    /**
     * Formats seat numbers to be more readable.
     * Converts format like "A1A2B3" to "A1, A2, B3".
     * @param seatNumbers The raw seat numbers string
     * @return A formatted seat numbers string
     */
    private String formatSeatNumbers(String seatNumbers) {
        if (seatNumbers == null || seatNumbers.isEmpty()) {
            return "N/A";
        }
        // Format seat numbers to be more readable (e.g., "A1, A2, B3" instead of "A1A2B3")
        return seatNumbers.replaceAll("([A-Z])(\\d+)", "$1$2 ").trim().replace(" ", ", ");
    }
    /**
     * Applies consistent styling to buttons in the UI.
     * @param button The button to style
     * @param bgColor The background color for the button
     */
    private void styleButton(JButton button, Color bgColor) {
        button.setFont(TABLE_FONT);
        button.setBackground(bgColor);
        button.setForeground(bgColor.equals(ACCENT_COLOR) ? Color.BLACK : TEXT_COLOR);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 1),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        button.setFocusPainted(false);
    }
}