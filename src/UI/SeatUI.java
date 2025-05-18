package UI;

import model.Film;
import model.ScreeningSchedule;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import model.ScreeningSeat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import Utils.UserSession;
import dao.BookingDAO;
import dao.SeatDAO;
import java.util.Objects;
/**
 * SeatUI class provides a user interface for selecting seats for a movie screening.
 * It displays a visual representation of the theater seating arrangement, allowing users
 * to select available seats and proceed with booking.
 * @author hp
 */
public class SeatUI extends JDialog {

    private final ScreeningSchedule screening;
    private final Color BACKGROUND_COLOR = new Color(30, 32, 34);
    private final Color TEXT_COLOR = new Color(220, 220, 220);
    private final Color SEAT_AVAILABLE = new Color(60, 63, 65);
    private final Color SEAT_SELECTED = new Color(255, 204, 0);
    private final Color SEAT_BOOKED = new Color(200, 50, 50);
    private final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private final Font SEAT_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    private final SeatDAO seatDao;
    private final List<ScreeningSeat> selectedSeats;
    private final BookingDAO bookingDao;
    /**
     * Constructs a SeatUI dialog for selecting seats for a movie screening.
     * @param parent The parent frame
     * @param screening The screening schedule for which seats are being selected
     * @param film The film being screened
     * @param seatDao The data access object for seat operations
     */
    public SeatUI(JFrame parent, ScreeningSchedule screening, Film film, SeatDAO seatDao) {
        super(parent, getWindowTitle(screening, film), true);
        this.seatDao = seatDao;
        this.selectedSeats = new ArrayList<>();
        this.screening = screening;
        this.bookingDao = new BookingDAO();

        setSize(1000, 600);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("Select Your Seats");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        titlePanel.add(titleLabel, BorderLayout.NORTH);

        // Screen visualization
        JLabel screenLabel = new JLabel("SCREEN", SwingConstants.CENTER);
        screenLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        screenLabel.setForeground(TEXT_COLOR);
        screenLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TEXT_COLOR, 2),
                BorderFactory.createEmptyBorder(10, 0, 10, 0)
        ));
        screenLabel.setOpaque(true);
        screenLabel.setBackground(new Color(50, 50, 50));
        titlePanel.add(screenLabel, BorderLayout.SOUTH);

        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Seat grid panel
        JPanel seatGridPanel = new JPanel(new GridBagLayout());
        seatGridPanel.setBackground(BACKGROUND_COLOR);
        seatGridPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Create column headers (1-15)
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;

        // Empty top-left corner
        gbc.gridx = 0;
        seatGridPanel.add(new JLabel(""), gbc);

        for (int col = 1; col <= 15; col++) {
            gbc.gridx = col;
            JLabel colLabel = new JLabel(String.valueOf(col));
            colLabel.setForeground(TEXT_COLOR);
            colLabel.setFont(SEAT_FONT);
            seatGridPanel.add(colLabel, gbc);
        }

        // Create seat buttons (A-J rows)
        char[] rows = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};

        for (int row = 0; row < rows.length; row++) {
            gbc.gridy = row + 1;

            // Row label
            gbc.gridx = 0;
            JLabel rowLabel = new JLabel(String.valueOf(rows[row]));
            rowLabel.setForeground(TEXT_COLOR);
            rowLabel.setFont(SEAT_FONT);
            seatGridPanel.add(rowLabel, gbc);

            // Seat buttons
            for (int col = 1; col <= 15; col++) {
                gbc.gridx = col;
                JButton seatButton = createSeatButton(rows[row], col);
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

        // Legend panel
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        legendPanel.setBackground(BACKGROUND_COLOR);

        legendPanel.add(createLegendItem("Available", SEAT_AVAILABLE));
        legendPanel.add(createLegendItem("Selected", SEAT_SELECTED));
        legendPanel.add(createLegendItem("Booked", SEAT_BOOKED));

        buttonPanel.add(legendPanel, BorderLayout.WEST);

        // Back button
        JButton backButton = new JButton("Back");
        styleButton(backButton, new Color(100, 100, 100));
        backButton.addActionListener(e -> {
            dispose();
            new ScreeningUI(parent, film).setVisible(true);
        });

        // Book button
        JButton bookButton = new JButton("Confirm Booking");
        styleButton(bookButton, SEAT_SELECTED);
        bookButton.addActionListener(e -> {
            if (selectedSeats.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Please select at least one seat",
                        "No Seats Selected",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (UserSession.getUserId() <= 0) {
                JOptionPane.showMessageDialog(this,
                        "You must be logged in to book seats",
                        "Not Logged In",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Calculate total price
            double totalPrice = selectedSeats.stream()
                    .filter(Objects::nonNull)
                    .mapToDouble(ScreeningSeat::getPrice)
                    .sum();

            // Show confirmation dialog
            BookingDialog confirmationDialog = new BookingDialog(
                    (JFrame) getParent(),
                    screening,
                    film,
                    selectedSeats,
                    totalPrice
            );

            confirmationDialog.addPropertyChangeListener("BOOKING_CONFIRMED", evt -> {
                String paymentMethod = (String) evt.getNewValue();

                // Create booking
                List<Integer> seatIds = selectedSeats.stream()
                        .map(ScreeningSeat::getScreeningSeatId)
                        .collect(Collectors.toList());

                int bookingId = bookingDao.createBooking(
                        UserSession.getUserId(),
                        screening.getScheduleId(),
                        paymentMethod,
                        totalPrice,
                        seatIds
                );

                if (bookingId > 0) {
                    JOptionPane.showMessageDialog(this,
                            "Booking confirmed! Your booking ID: " + bookingId,
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Failed to create booking. Please try again.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            });

            confirmationDialog.setVisible(true);
        });

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setBackground(BACKGROUND_COLOR);
        rightPanel.add(backButton);
        rightPanel.add(bookButton);
        buttonPanel.add(rightPanel, BorderLayout.EAST);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }
    /**
     * Creates a seat button with appropriate styling and behavior.
     * @param row The row identifier (A-J)
     * @param col The column number (1-15)
     * @return A configured JButton representing the seat
     */
    private JButton createSeatButton(char row, int col) {
        JButton seatButton = new JButton(row + "" + col);
        seatButton.setFont(SEAT_FONT);
        seatButton.setPreferredSize(new Dimension(40, 40));
        seatButton.setFocusPainted(false);
        // Get seat from database
        ScreeningSeat seat = seatDao.getSeatByPosition(screening.getScheduleId(), row, col);
        if (seat != null && "booked".equals(seat.getStatus())) {
            seatButton.setBackground(SEAT_BOOKED);
            seatButton.setEnabled(false);
        } else {
            seatButton.setBackground(SEAT_AVAILABLE);
            seatButton.setForeground(TEXT_COLOR);
            seatButton.addActionListener(e -> {
                Color current = seatButton.getBackground();
                if (current.equals(SEAT_AVAILABLE)) {
                    seatButton.setBackground(SEAT_SELECTED);
                    seatButton.setForeground(Color.BLACK);
                    selectedSeats.add(seat); // Add to selected seats list
                } else {
                    seatButton.setBackground(SEAT_AVAILABLE);
                    seatButton.setForeground(TEXT_COLOR);
                    selectedSeats.remove(seat); // Remove from selected seats list
                }
            });
        }

        seatButton.setBorder(BorderFactory.createLineBorder(BACKGROUND_COLOR, 1));
        return seatButton;
    }
    /**
     * Creates a legend item with color indicator and label.
     * @param text The text label for the legend item
     * @param color The color to display for the legend item
     * @return A panel containing the legend item
     */
    private JPanel createLegendItem(String text, Color color) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.setBackground(BACKGROUND_COLOR);

        JLabel colorLabel = new JLabel();
        colorLabel.setOpaque(true);
        colorLabel.setBackground(color);
        colorLabel.setPreferredSize(new Dimension(20, 20));
        colorLabel.setBorder(BorderFactory.createLineBorder(BACKGROUND_COLOR.brighter(), 1));

        JLabel textLabel = new JLabel(text);
        textLabel.setForeground(TEXT_COLOR);
        textLabel.setFont(SEAT_FONT);

        panel.add(colorLabel);
        panel.add(textLabel);

        return panel;
    }
    /**
     * Applies consistent styling to buttons in the UI.
     * @param button The button to style
     * @param bgColor The background color for the button
     */
    private void styleButton(JButton button, Color bgColor) {
        button.setFont(SEAT_FONT);
        button.setBackground(bgColor);
        button.setForeground(bgColor.equals(SEAT_SELECTED) ? Color.BLACK : TEXT_COLOR);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 1),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        button.setFocusPainted(false);
    }
    /**
     * Generates a window title based on screening information and film.
     * @param screening The screening schedule
     * @param film The film being screened
     * @return A formatted title string
     */
    private static String getWindowTitle(ScreeningSchedule screening, Film film) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        return String.format("%s - %s - %s",
                timeFormat.format(screening.getScreeningTime()),
                dateFormat.format(screening.getScreeningDate()),
                film.getTitle());
    }
}
