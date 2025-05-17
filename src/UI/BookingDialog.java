package UI;

import Utils.QRGenerator;
import Utils.UserSession;
import dao.BookingDAO;
import model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

public class BookingDialog extends JDialog {
    private final Film film;
    private final ScreeningSchedule screening;
    private final List<Seat> selectedSeats;
    private final double totalPrice;
    
    public BookingDialog(JFrame parent, Film film, ScreeningSchedule screening, 
                        List<Seat> selectedSeats, double totalPrice) {
        super(parent, "Booking Confirmation", true);
        this.film = film;
        this.screening = screening;
        this.selectedSeats = selectedSeats;
        this.totalPrice = totalPrice;
        
        initializeUI();
    }
    
    private void initializeUI() {
        setSize(600, 500);
        setLocationRelativeTo(getParent());
        
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Booking info panel
        JPanel infoPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Booking Details"));
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        
        infoPanel.add(new JLabel("Movie: " + film.getTitle()));
        infoPanel.add(new JLabel("Date: " + dateFormat.format(screening.getScreeningDate())));
        infoPanel.add(new JLabel("Time: " + timeFormat.format(screening.getScreeningTime())));
        infoPanel.add(new JLabel("Screen: " + screening.getScreenId()));
        
        StringBuilder seatsText = new StringBuilder("Seats: ");
        for (Seat seat : selectedSeats) {
            seatsText.append(seat).append(", ");
        }
        seatsText.delete(seatsText.length()-2, seatsText.length());
        infoPanel.add(new JLabel(seatsText.toString()));
        
        infoPanel.add(new JLabel("Total Price: Rp" + String.format("%,.0f", totalPrice)));
        
        // QR code panel (initially empty)
        JPanel qrPanel = new JPanel(new BorderLayout());
        qrPanel.setBorder(BorderFactory.createTitledBorder("Booking QR Code"));
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton confirmButton = new JButton("Confirm Booking");
        confirmButton.addActionListener(e -> {
            String bookingData = generateBookingData();
            confirmBooking(bookingData, qrPanel);
        });
        buttonPanel.add(confirmButton);
        
        mainPanel.add(infoPanel, BorderLayout.NORTH);
        mainPanel.add(qrPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private String generateBookingData() {
        StringBuilder sb = new StringBuilder();
        sb.append("MOVIE:").append(film.getTitle()).append("|");
        sb.append("DATE:").append(new SimpleDateFormat("yyyyMMdd").format(screening.getScreeningDate())).append("|");
        sb.append("TIME:").append(new SimpleDateFormat("HHmm").format(screening.getScreeningTime())).append("|");
        sb.append("SCREEN:").append(screening.getScreenId()).append("|");
        sb.append("SEATS:");
        for (Seat seat : selectedSeats) {
            sb.append(seat).append(",");
        }
        sb.deleteCharAt(sb.length()-1).append("|");
        sb.append("TOTAL:").append(totalPrice).append("IDR|");
        sb.append("REF:").append(System.currentTimeMillis());
        return sb.toString();
    }
    
    private void confirmBooking(String qrCodeData, JPanel qrPanel) {
        BookingDAO bookingDAO = new BookingDAO();
        int customerId = UserSession.getUserId();
        
        // Create Booking object
        Booking booking = new Booking(
            customerId,
            selectedSeats.stream().map(Seat::getSeatId).collect(Collectors.toList()),
            screening.getScheduleId(),
            "Cash",  // Default payment method
            totalPrice
        );
        booking.setQrCodeData(qrCodeData);
        booking.setPaymentStatus("Completed");
        
        boolean success = bookingDAO.createBooking(booking);
        
        if (success) {
            // Generate and show QR code after confirmation
            QRGenerator qrGenerator = new QRGenerator();
            BufferedImage qrImage = qrGenerator.generateQRImage(qrCodeData);
            JLabel qrLabel = new JLabel(new ImageIcon(qrImage));
            
            qrPanel.removeAll();
            qrPanel.add(qrLabel, BorderLayout.CENTER);
            qrPanel.revalidate();
            qrPanel.repaint();
            
            JOptionPane.showMessageDialog(this, "Booking confirmed! Your QR code is now displayed.", 
                                        "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to create booking. Please try again.", 
                                        "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}