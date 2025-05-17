package UI;

import dao.SeatDAO;
import model.Seat;
import model.ScreeningSchedule;
import model.Film;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;

public class SeatUI extends JDialog {
    private final JFrame parent;
    private final ScreeningSchedule screening;
    private final Film film;
    private final SeatDAO seatDAO;
    private Seat selectedSeat = null;
    private JButton confirmButton;
    private JLabel selectionLabel;
    
    // Color scheme
    private final Color BACKGROUND_COLOR = new Color(30, 32, 34);
    private final Color TEXT_COLOR = new Color(220, 220, 220);
    private final Color SECONDARY_COLOR = new Color(60, 63, 65);
    private final Color ACCENT_COLOR = new Color(255, 204, 0);
    private final Color BOOKED_COLOR = new Color(150, 50, 50);
    private final Color SELECTED_COLOR = new Color(50, 150, 50);
    
    // Fonts
    private final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private final Font SEAT_FONT = new Font("Segoe UI", Font.BOLD, 12);
    private final Font INFO_FONT = new Font("Segoe UI", Font.PLAIN, 14);

    public SeatUI(JFrame parent, ScreeningSchedule screening, Film film) {
        super(parent, getWindowTitle(film, screening), true);
        this.parent = parent;
        this.screening = screening;
        this.film = film;
        this.seatDAO = new SeatDAO();
        
        initializeUI();
    }

    private static String getWindowTitle(Film film, ScreeningSchedule screening) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return String.format("%s - %s at %s", 
            film.getTitle(),
            dateFormat.format(screening.getScreeningDate()),
            timeFormat.format(screening.getScreeningTime()));
    }

    private void initializeUI() {
        setSize(1000, 1000);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("Select Your Seat");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        titlePanel.add(titleLabel, BorderLayout.NORTH);

        // Screen label
        JLabel screenLabel = new JLabel("SCREEN", JLabel.CENTER);
        screenLabel.setFont(TITLE_FONT);
        screenLabel.setForeground(ACCENT_COLOR);
        screenLabel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, ACCENT_COLOR));
        screenLabel.setOpaque(true);
        screenLabel.setBackground(SECONDARY_COLOR);
        titlePanel.add(screenLabel, BorderLayout.SOUTH);

        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Seat grid panel
        JPanel seatGridPanel = new JPanel(new GridBagLayout());
        seatGridPanel.setBackground(BACKGROUND_COLOR);
        seatGridPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        // Get available seats
        List<Seat> availableSeats = seatDAO.getAvailableSeatsForScreening(screening.getScheduleId());

        // Create seat buttons
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        for (int row = 0; row < 10; row++) {
            gbc.gridy = row;
            for (int col = 0; col < 15; col++) {
                gbc.gridx = col;
                
                char rowLetter = (char) ('A' + row);
                int seatNumber = col + 1;
                
                Seat currentSeat = findSeat(availableSeats, rowLetter, seatNumber);
                JButton seatButton = new JButton(String.format("%c%d", rowLetter, seatNumber));
                
                if (currentSeat != null) {
                    styleSeatButton(seatButton, true);
                    seatButton.addActionListener(new SeatSelectionListener(seatButton, currentSeat));
                } else {
                    styleSeatButton(seatButton, false);
                    seatButton.setEnabled(false);
                }
                
                seatGridPanel.add(seatButton, gbc);
            }
        }

        JScrollPane scrollPane = new JScrollPane(seatGridPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

// Back button
JButton backButton = new JButton("Back");
styleButton(backButton, SECONDARY_COLOR);
backButton.addActionListener(e -> {
    dispose(); // Close this dialog first
    new ScreeningUI(parent, film).setVisible(true); // Then show screening UI
});

        // Confirm button
        confirmButton = new JButton("Confirm Selection");
        styleButton(confirmButton, ACCENT_COLOR);
        confirmButton.setEnabled(false);
        confirmButton.addActionListener(e -> confirmBooking());

        // Selection info label
        selectionLabel = new JLabel("No seat selected", JLabel.CENTER);
        selectionLabel.setFont(INFO_FONT);
        selectionLabel.setForeground(TEXT_COLOR);

        // Add components to button panel
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setBackground(BACKGROUND_COLOR);
        leftPanel.add(backButton);

        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(BACKGROUND_COLOR);
        centerPanel.add(selectionLabel);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightPanel.setBackground(BACKGROUND_COLOR);
        rightPanel.add(confirmButton);

        buttonPanel.add(leftPanel, BorderLayout.WEST);
        buttonPanel.add(centerPanel, BorderLayout.CENTER);
        buttonPanel.add(rightPanel, BorderLayout.EAST);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private Seat findSeat(List<Seat> seats, char rowLetter, int seatNumber) {
        for (Seat seat : seats) {
            if (seat.getRowLetter() == rowLetter && seat.getSeatNumber() == seatNumber) {
                return seat;
            }
        }
        return null;
    }

    private void styleSeatButton(JButton button, boolean available) {
        button.setFont(SEAT_FONT);
        button.setPreferredSize(new Dimension(50, 50));
        button.setBackground(available ? SECONDARY_COLOR : BOOKED_COLOR);
        button.setForeground(TEXT_COLOR);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(TEXT_COLOR, 1));
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setFont(INFO_FONT);
        button.setBackground(bgColor);
        button.setForeground(bgColor.equals(ACCENT_COLOR) ? Color.BLACK : TEXT_COLOR);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 1),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        button.setFocusPainted(false);
    }

    private class SeatSelectionListener implements ActionListener {
        private final JButton button;
        private final Seat seat;
        private boolean isSelected = false;

        public SeatSelectionListener(JButton button, Seat seat) {
            this.button = button;
            this.seat = seat;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            isSelected = !isSelected;
            
            if (isSelected) {
                // Deselect any previously selected seat
                if (selectedSeat != null) {
                    // In a real implementation, you'd need to track the previously selected button
                    // This is simplified for demonstration
                }
                
                selectedSeat = seat;
                button.setBackground(SELECTED_COLOR);
                confirmButton.setEnabled(true);
                selectionLabel.setText("Selected: " + seat + " - $" + seat.getPrice());
            } else {
                selectedSeat = null;
                button.setBackground(SECONDARY_COLOR);
                confirmButton.setEnabled(false);
                selectionLabel.setText("No seat selected");
            }
        }
    }

    private void confirmBooking() {
        if (selectedSeat == null) return;
        
        // Here you would normally:
        // 1. Create a booking record
        // 2. Update seat status
        // 3. Process payment
        
        JOptionPane.showMessageDialog(this, 
            "Booking confirmed!\n" +
            "Movie: " + film.getTitle() + "\n" +
            "Date: " + new SimpleDateFormat("EEE, MMM d").format(screening.getScreeningDate()) + "\n" +
            "Time: " + new SimpleDateFormat("h:mm a").format(screening.getScreeningTime()) + "\n" +
            "Seat: " + selectedSeat + "\n" +
            "Price: $" + selectedSeat.getPrice(),
            "Booking Confirmed", 
            JOptionPane.INFORMATION_MESSAGE);
        
        dispose();
    }
}