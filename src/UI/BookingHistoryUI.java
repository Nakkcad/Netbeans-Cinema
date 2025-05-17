package UI;

import dao.BookingDAO;
import model.Booking;
import Utils.UserSession;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class BookingHistoryUI extends JDialog {
    public BookingHistoryUI(JFrame parent) {
        super(parent, "My Bookings", true);
        initializeUI();
    }

    private void initializeUI() {
        setSize(800, 600);
        setLocationRelativeTo(getParent());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Get bookings for current user
        BookingDAO bookingDAO = new BookingDAO();
        List<Booking> bookings = bookingDAO.getBookingsByCustomer(UserSession.getUserId());

        if (bookings.isEmpty()) {
            mainPanel.add(new JLabel("No bookings found", JLabel.CENTER), BorderLayout.CENTER);
        } else {
            // Create table model
            String[] columnNames = {"Movie", "Date", "Time", "Seats", "Total", "Status"};
            Object[][] data = new Object[bookings.size()][6];

            SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");

            for (int i = 0; i < bookings.size(); i++) {
                Booking b = bookings.get(i);
                data[i][0] = b.getFilmTitle();
                data[i][1] = dateFormat.format(b.getScreeningDate());
                data[i][2] = timeFormat.format(b.getScreeningTime());
                data[i][3] = b.getSeatInfo();
                data[i][4] = String.format("Rp%,.0f", b.getTotalPrice());
                data[i][5] = b.getPaymentStatus();
            }

            JTable bookingTable = new JTable(data, columnNames);
            JScrollPane scrollPane = new JScrollPane(bookingTable);
            mainPanel.add(scrollPane, BorderLayout.CENTER);
        }

        add(mainPanel);
    }
}