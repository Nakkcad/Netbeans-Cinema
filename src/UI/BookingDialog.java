package UI;

import model.ScreeningSeat;
import model.ScreeningSchedule;
import model.Film;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
    /**
    * BookingDialog class provides a confirmation dialog for finalizing movie bookings.
    * It displays booking details including movie information, selected seats, pricing, and payment options before confirming the booking.
    */
public class BookingDialog extends JDialog {
    private final Color BACKGROUND_COLOR = new Color(30, 32, 34);
    private final Color TEXT_COLOR = new Color(220, 220, 220);
    private final Color ACCENT_COLOR = new Color(255, 204, 0);
    private final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 20);
    private final Font TEXT_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font BOLD_FONT = new Font("Segoe UI", Font.BOLD, 14);
    /**
     * Constructs a BookingDialog with the specified parameters.
     * @param parent The parent frame
     * @param screening The screening schedule being booked
     * @param film The film being booked
     * @param selectedSeats List of seats selected by the user
     * @param totalPrice Total price for the booking
     */
    public BookingDialog(JFrame parent, ScreeningSchedule screening, Film film, 
                                   List<ScreeningSeat> selectedSeats, double totalPrice) {
        super(parent, "Confirm Booking", true);
        initializeUI(screening, film, selectedSeats, totalPrice);
    }
    /**
     * Initializes the user interface components.
     * Sets up the layout and populates the dialog with booking information.
     * @param screening The screening schedule being booked
     * @param film The film being booked
     * @param selectedSeats List of seats selected by the user
     * @param totalPrice Total price for the booking
     */
    private void initializeUI(ScreeningSchedule screening, Film film, 
                            List<ScreeningSeat> selectedSeats, double totalPrice) {
        setSize(500, 500);
        setLocationRelativeTo(getParent());
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(BACKGROUND_COLOR);
        JLabel titleLabel = new JLabel("Booking Confirmation", JLabel.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(BACKGROUND_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Movie info
        addInfoRow(contentPanel, "Movie:", film.getTitle());
        addInfoRow(contentPanel, "Date:", formatScreeningDate(screening));
        addInfoRow(contentPanel, "Time:", formatScreeningTime(screening));
        addInfoRow(contentPanel, "Screen:", "Screen " + screening.getScreenId());

        // Selected seats
        JPanel seatsPanel = new JPanel(new BorderLayout());
        seatsPanel.setBackground(BACKGROUND_COLOR);
        seatsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel seatsLabel = new JLabel("Selected Seats:");
        seatsLabel.setFont(BOLD_FONT);
        seatsLabel.setForeground(TEXT_COLOR);
        seatsPanel.add(seatsLabel, BorderLayout.NORTH);

        JTextArea seatsText = new JTextArea(selectedSeats.stream()
                .map(seat -> seat.getRowLetter() + "" + seat.getSeatNumber())
                .reduce((s1, s2) -> s1 + ", " + s2)
                .orElse(""));
        seatsText.setFont(TEXT_FONT);
        seatsText.setForeground(TEXT_COLOR);
        seatsText.setBackground(BACKGROUND_COLOR);
        seatsText.setEditable(false);
        seatsText.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 0));
        seatsPanel.add(seatsText, BorderLayout.CENTER);

        contentPanel.add(seatsPanel);

        // Price info
        addInfoRow(contentPanel, "Subtotal:", String.format("Rp%,.0f", totalPrice));
        addInfoRow(contentPanel, "Service Fee:", "Rp0");
        addInfoRow(contentPanel, "Total:", String.format("Rp%,.0f", totalPrice));

        // Payment method
        JPanel paymentPanel = new JPanel(new BorderLayout());
        paymentPanel.setBackground(BACKGROUND_COLOR);
        paymentPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel paymentLabel = new JLabel("Payment Method:");
        paymentLabel.setFont(BOLD_FONT);
        paymentLabel.setForeground(TEXT_COLOR);
        paymentPanel.add(paymentLabel, BorderLayout.NORTH);

        JComboBox<String> paymentCombo = new JComboBox<>(new String[]{"Cash", "Credit Card", "E-Wallet"});
        paymentCombo.setFont(TEXT_FONT);
        paymentCombo.setBackground(new Color(60, 63, 65));
        paymentCombo.setForeground(TEXT_COLOR);
        paymentCombo.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 0));
        paymentPanel.add(paymentCombo, BorderLayout.CENTER);

        contentPanel.add(paymentPanel);

        mainPanel.add(new JScrollPane(contentPanel), BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton cancelButton = createButton("Cancel", new Color(100, 100, 100), e -> dispose());
        JButton confirmButton = createButton("Confirm Booking", ACCENT_COLOR, e -> {
            String paymentMethod = (String) paymentCombo.getSelectedItem();
            firePropertyChange("BOOKING_CONFIRMED", null, paymentMethod);
            dispose();
        });

        buttonPanel.add(cancelButton);
        buttonPanel.add(confirmButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }
    /**
     * Adds an information row to the specified panel.
     * Creates a row with a label and value in a horizontal layout.
     * @param panel The panel to add the row to
     * @param label The label text
     * @param value The value text
     */
    private void addInfoRow(JPanel panel, String label, String value) {
        JPanel rowPanel = new JPanel(new BorderLayout());
        rowPanel.setBackground(BACKGROUND_COLOR);
        rowPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        JLabel labelComp = new JLabel(label);
        labelComp.setFont(BOLD_FONT);
        labelComp.setForeground(TEXT_COLOR);

        JLabel valueComp = new JLabel(value);
        valueComp.setFont(TEXT_FONT);
        valueComp.setForeground(TEXT_COLOR);
        valueComp.setHorizontalAlignment(SwingConstants.RIGHT);

        rowPanel.add(labelComp, BorderLayout.WEST);
        rowPanel.add(valueComp, BorderLayout.EAST);
        panel.add(rowPanel);
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
        button.setFont(TEXT_FONT);
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
     * Formats the screening date in a user-friendly format.
     * @param screening The screening schedule containing the date
     * @return A formatted date string
     */
    private String formatScreeningDate(ScreeningSchedule screening) {
        return new SimpleDateFormat("EEE, dd MMM yyyy").format(screening.getScreeningDate());
    }
    /**
     * Formats the screening time in a user-friendly format.
     * @param screening The screening schedule containing the time
     * @return A formatted time string
     */
    private String formatScreeningTime(ScreeningSchedule screening) {
        return new SimpleDateFormat("HH:mm").format(screening.getScreeningTime());
    }
}