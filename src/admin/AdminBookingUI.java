package admin;

import dao.BookingDAO;
import model.Booking;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class AdminBookingUI extends JDialog {
    public AdminBookingUI(JFrame parent) {
        super(parent, "All Bookings", true);
        initializeUI();
    }

    private void initializeUI() {
        setSize(1000, 600);
        setLocationRelativeTo(getParent());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Get all bookings
        BookingDAO bookingDAO = new BookingDAO();
        List<Booking> bookings = bookingDAO.getAllBookings();

        if (bookings.isEmpty()) {
            mainPanel.add(new JLabel("No bookings found", JLabel.CENTER), BorderLayout.CENTER);
        } else {
            // Create table model
            String[] columnNames = {"Customer", "Movie", "Date", "Time", "Seats", "Total", "Status", "Booking Date"};
            Object[][] data = new Object[bookings.size()][8];

            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
            SimpleDateFormat bookingDateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm");

            for (int i = 0; i < bookings.size(); i++) {
                Booking b = bookings.get(i);
                data[i][0] = b.getCustomerName() != null ? b.getCustomerName() : "N/A";
                data[i][1] = b.getFilmTitle() != null ? b.getFilmTitle() : "N/A";
                data[i][2] = b.getScreeningDate() != null ? dateFormat.format(b.getScreeningDate()) : "N/A";
                data[i][3] = b.getScreeningTime() != null ? timeFormat.format(b.getScreeningTime()) : "N/A";
                data[i][4] = b.getSeatNumbers() != null ? b.getSeatNumbers() : "N/A";
                data[i][5] = String.format("Rp%,.0f", b.getTotalPrice());
                data[i][6] = b.getPaymentStatus() != null ? b.getPaymentStatus() : "N/A";
                data[i][7] = b.getBookingDate() != null ? bookingDateFormat.format(b.getBookingDate()) : "N/A";
            }

            JTable bookingTable = new JTable(data, columnNames);
            bookingTable.setAutoCreateRowSorter(true);
            JScrollPane scrollPane = new JScrollPane(bookingTable);
            mainPanel.add(scrollPane, BorderLayout.CENTER);
        }

        add(mainPanel);
    }
}