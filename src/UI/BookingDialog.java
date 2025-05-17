package UI;

import Utils.QRGenerator;
import Utils.UserSession;
import dao.BookingDAO;
import io.nayuki.qrcodegen.QrCode;
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
    private JLabel qrLabel;
    
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
        setSize(650, 600);
        setLocationRelativeTo(getParent());
        setResizable(false);
        
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Booking info panel
        JPanel infoPanel = createInfoPanel();
        
        // QR code panel (initially with placeholder)
        JPanel qrPanel = createQRPanel();
        
        // Button panel
        JPanel buttonPanel = createButtonPanel(qrPanel);
        
        mainPanel.add(infoPanel, BorderLayout.NORTH);
        mainPanel.add(qrPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    private JPanel createInfoPanel() {
        JPanel infoPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Booking Details"));
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        
        infoPanel.add(createStyledLabel("Movie: " + film.getTitle()));
        infoPanel.add(createStyledLabel("Date: " + dateFormat.format(screening.getScreeningDate())));
        infoPanel.add(createStyledLabel("Time: " + timeFormat.format(screening.getScreeningTime())));
        infoPanel.add(createStyledLabel("Screen: " + screening.getScreenId()));
        
        String seatsText = "Seats: " + selectedSeats.stream()
                                .map(Seat::toString)
                                .collect(Collectors.joining(", "));
        infoPanel.add(createStyledLabel(seatsText));
        
        infoPanel.add(createStyledLabel("Total Price: Rp" + String.format("%,.0f", totalPrice)));
        
        return infoPanel;
    }
    
    private JPanel createQRPanel() {
        JPanel qrPanel = new JPanel(new BorderLayout());
        qrPanel.setBorder(BorderFactory.createTitledBorder("Booking QR Code"));
        qrPanel.setBackground(Color.WHITE);
        
        // Placeholder text before QR is generated
        qrLabel = new JLabel("QR Code will appear after confirmation", SwingConstants.CENTER);
        qrLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        qrLabel.setForeground(Color.GRAY);
        qrPanel.add(qrLabel, BorderLayout.CENTER);
        
        return qrPanel;
    }
    
    private JPanel createButtonPanel(JPanel qrPanel) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        JButton confirmButton = new JButton("Confirm Booking");
        styleConfirmButton(confirmButton);
        
        confirmButton.addActionListener(e -> {
            String bookingData = generateBookingData();
            confirmBooking(bookingData, qrPanel);
        });
        
        JButton cancelButton = new JButton("Cancel");
        styleCancelButton(cancelButton);
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        
        return buttonPanel;
    }
    
    private String generateBookingData() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HHmm");
        
        String seats = selectedSeats.stream()
                          .map(Seat::toString)
                          .collect(Collectors.joining(","));
        
        return String.format(
            "MOVIE:%s|DATE:%s|TIME:%s|SCREEN:%d|SEATS:%s|TOTAL:%.0fIDR|REF:%d",
            film.getTitle(),
            dateFormat.format(screening.getScreeningDate()),
            timeFormat.format(screening.getScreeningTime()),
            screening.getScreenId(),
            seats,
            totalPrice,
            System.currentTimeMillis()
        );
    }
    
    private void confirmBooking(String qrCodeData, JPanel qrPanel) {
        Booking booking = new Booking(
            UserSession.getUserId(),
            selectedSeats.stream().map(Seat::getScreeningSeatId).collect(Collectors.toList()),
            screening.getScheduleId(),
            "Cash",
            totalPrice
        );
        booking.setQrCodeData(qrCodeData);
        booking.setPaymentStatus("Completed");
        
        BookingDAO bookingDAO = new BookingDAO();
        if (bookingDAO.createBooking(booking)) {
            showSuccessQR(qrCodeData, qrPanel);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Failed to create booking. Please try again.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showSuccessQR(String qrCodeData, JPanel qrPanel) {
        try {
            QRGenerator qrGenerator = new QRGenerator(6, 4, QrCode.Ecc.HIGH);
            BufferedImage qrImage = qrGenerator.generateQRImage(qrCodeData);
            
            qrLabel.setText("");
            qrLabel.setIcon(new ImageIcon(qrImage));
            
            JOptionPane.showMessageDialog(this, 
                "<html><div style='text-align: center;'>Booking confirmed!<br>Your tickets have been booked successfully.</div></html>", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Disable confirm button after successful booking
            ((JButton)((JPanel)getContentPane().getComponent(2)).getComponent(0)).setEnabled(false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Booking was successful but QR code couldn't be generated.", 
                "Warning", 
                JOptionPane.WARNING_MESSAGE);
        }
    }
    
    // Helper methods for UI styling
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        return label;
    }
    
    private void styleConfirmButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(150, 35));
    }
    
    private void styleCancelButton(JButton button) {
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setBackground(new Color(240, 240, 240));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(150, 35));
    }
}