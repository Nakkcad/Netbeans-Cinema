/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
                data[i][0] = b.getCustomerName();
                data[i][1] = b.getFilmTitle();
                data[i][2] = dateFormat.format(b.getScreeningDate());
                data[i][3] = timeFormat.format(b.getScreeningTime());
                data[i][4] = b.getSeatInfo();
                data[i][5] = String.format("Rp%,.0f", b.getTotalPrice());
                data[i][6] = b.getPaymentStatus();
                data[i][7] = bookingDateFormat.format(b.getBookingDate());
            }

            JTable bookingTable = new JTable(data, columnNames);
            JScrollPane scrollPane = new JScrollPane(bookingTable);
            mainPanel.add(scrollPane, BorderLayout.CENTER);
        }

        add(mainPanel);
    }
}