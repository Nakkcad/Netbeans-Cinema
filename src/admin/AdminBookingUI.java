package admin;

import dao.BookingDAO;
import model.Booking;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
/**
 * AdminBookingUI class provides an administrative interface for managing all bookings.
 * It displays a table of all bookings in the system with detailed information and
 * allows administrators to view and manage booking data.
 * @author hp
 */
public class AdminBookingUI extends JDialog {
    private static final Color BACKGROUND_COLOR = new Color(30, 32, 34);
    private static final Color TEXT_COLOR = new Color(220, 220, 220);
    private static final Color SECONDARY_COLOR = new Color(60, 63, 65);
    private static final Color ACCENT_COLOR = new Color(255, 204, 0);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private static final Font TABLE_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 16);
    /**
     * Constructs an AdminBookingUI dialog with the specified parent frame.
     * @param parent The parent frame for this dialog
     */
    public AdminBookingUI(JFrame parent) {
        super(parent, "All Bookings Management", true);
        initializeUI();
    }
    /**
     * Initializes the user interface components.
     * Sets up the layout, creates the booking table, and adds control buttons.
     */
    private void initializeUI() {
        setSize(1200, 700);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(BACKGROUND_COLOR);
        JLabel titleLabel = new JLabel("Booking Management", JLabel.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Get all bookings
        BookingDAO bookingDAO = new BookingDAO();
        List<Booking> bookings = bookingDAO.getAllBookings();

        if (bookings.isEmpty()) {
            JLabel noBookingsLabel = new JLabel("No bookings found", JLabel.CENTER);
            noBookingsLabel.setFont(TABLE_FONT);
            noBookingsLabel.setForeground(TEXT_COLOR);
            mainPanel.add(noBookingsLabel, BorderLayout.CENTER);
        } else {
            // Create table model with enhanced columns
            String[] columnNames = {"ID", "Customer", "Movie", "Date", "Time", "Screen", "Seats", "Total", "Method", "Status", "Booked On"};
            Object[][] data = new Object[bookings.size()][columnNames.length];

            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM dd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            SimpleDateFormat bookingDateFormat = new SimpleDateFormat("MMM dd, HH:mm");

            for (int i = 0; i < bookings.size(); i++) {
                Booking b = bookings.get(i);
                data[i][0] = b.getBookingId();
                data[i][1] = b.getCustomerName() != null ? b.getCustomerName() : "N/A";
                data[i][2] = b.getFilmTitle() != null ? b.getFilmTitle() : "N/A";
                data[i][3] = b.getScreeningDate() != null ? dateFormat.format(b.getScreeningDate()) : "N/A";
                data[i][4] = b.getScreeningTime() != null ? timeFormat.format(b.getScreeningTime()) : "N/A";
                data[i][5] = b.getScreenId() > 0 ? "Screen " + b.getScreenId() : "N/A";
                data[i][6] = b.getSeatNumbers() != null ? b.getSeatNumbers() : "N/A";
                data[i][7] = String.format("Rp%,.0f", b.getTotalPrice());
                data[i][8] = b.getPaymentMethod() != null ? b.getPaymentMethod() : "N/A";
                data[i][9] = b.getPaymentStatus() != null ? 
                             getStatusLabel(b.getPaymentStatus()) : "N/A";
                data[i][10] = b.getBookingDate() != null ? bookingDateFormat.format(b.getBookingDate()) : "N/A";
            }

            JTable bookingTable = new JTable(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Make table non-editable
                }
            };

            // Customize table appearance
            customizeTable(bookingTable);
            
            JScrollPane scrollPane = new JScrollPane(bookingTable);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            scrollPane.getViewport().setBackground(SECONDARY_COLOR);
            
            mainPanel.add(scrollPane, BorderLayout.CENTER);
        }

        // Add control buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        
        JButton refreshButton = createButton("Refresh", ACCENT_COLOR, e -> refreshBookings());
        JButton closeButton = createButton("Close", SECONDARY_COLOR, e -> dispose());
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(closeButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }
    /**
     * Customizes the appearance of the booking table.
     * Sets fonts, colors, alignment, and other visual properties.
     * @param table The JTable to customize
     */
    private void customizeTable(JTable table) {
        table.setFont(TABLE_FONT);
        table.setForeground(TEXT_COLOR);
        table.setBackground(SECONDARY_COLOR);
        table.setSelectionBackground(ACCENT_COLOR);
        table.setSelectionForeground(Color.BLACK);
        table.setRowHeight(30);
        table.setAutoCreateRowSorter(true);
        table.setGridColor(BACKGROUND_COLOR.darker());
        
        // Customize header
        JTableHeader header = table.getTableHeader();
        header.setBackground(SECONDARY_COLOR.darker());
        header.setForeground(TEXT_COLOR);
        header.setFont(HEADER_FONT);
        
        // Center-align numeric and date columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // ID
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // Date
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); // Time
        table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer); // Screen
        table.getColumnModel().getColumn(7).setCellRenderer(centerRenderer); // Total
        table.getColumnModel().getColumn(10).setCellRenderer(centerRenderer); // Booked On
    }
    /**
     * Creates a styled button with consistent appearance.
     * @param text The text to display on the button
     * @param bgColor The background color for the button
     * @param action The action listener to attach to the button
     * @return A styled JButton
     */
    private JButton createButton(String text, Color bgColor, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(TABLE_FONT);
        button.setBackground(bgColor);
        button.setForeground(bgColor.equals(ACCENT_COLOR) ? Color.BLACK : TEXT_COLOR);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 1),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        button.setFocusPainted(false);
        button.addActionListener(action);
        return button;
    }
    /**
     * Formats the payment status with an appropriate icon prefix.
     * @param status The payment status string
     * @return A formatted status string with an icon prefix
     */
    private String getStatusLabel(String status) {
        switch(status.toLowerCase()) {
            case "completed": return "✓ " + status;
            case "pending": return "⌛ " + status;
            case "cancelled": return "✗ " + status;
            default: return status;
        }
    }
    /**
     * Refreshes the booking data by re initializing the UI.
     * This reloads all booking data from the database.
     */
    private void refreshBookings() {
        // Reinitialize the UI to refresh data
        initializeUI();
        revalidate();
        repaint();
    }
}