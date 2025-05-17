package UI;

import DAO.ScreeningScheduleDAO;
import model.ScreeningSchedule;
import Utils.UserSession;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import model.Film;
import admin.Screening.FilmScreeningAdminPanel;

public class ScreeningUI extends JDialog {

    private final JFrame parent;
    private final Film film;
    private final List<ScreeningSchedule> screenings; // Added class field
    private final Color BACKGROUND_COLOR = new Color(30, 32, 34);
    private final Color TEXT_COLOR = new Color(220, 220, 220);
    private final Color SECONDARY_COLOR = new Color(60, 63, 65);
    private final Color ACCENT_COLOR = new Color(255, 204, 0);
    private final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    private final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 16);
    private final Font DETAIL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy");

    private final JTable screeningsTable;
    private final JButton bookButton;

    public ScreeningUI(JFrame parent, Film film) {
        super(parent, "ScreeningSchedule - " + film.getTitle(), true);
        this.parent = parent;
        this.film = film;
        int filmid = film.getFilmId();

        // Initialize screenings list
        ScreeningScheduleDAO screeningDAO = new ScreeningScheduleDAO();
        this.screenings = screeningDAO.getScreeningSchedulesByFilmId(filmid);

        setSize(800, 500);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("Available Screenings");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        titlePanel.add(titleLabel, BorderLayout.NORTH);

        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Create table for screenings
        String[] columnNames = {"Date", "Time", "Screen"};
        Object[][] rowData = new Object[screenings.size()][3];

        for (int i = 0; i < screenings.size(); i++) {
            ScreeningSchedule s = screenings.get(i);
            rowData[i][0] = dateFormat.format(s.getScreeningDate());
            rowData[i][1] = s.getScreeningTime().toString().substring(0, 5);
            rowData[i][2] = "Screen " + s.getScreenId();
        }

        screeningsTable = new JTable(rowData, columnNames);
        customizeTable();

        JScrollPane scrollPane = new JScrollPane(screeningsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(SECONDARY_COLOR);

        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Back button on the left
        JButton backButton = new JButton("Back");
        styleButton(backButton, SECONDARY_COLOR);
        backButton.addActionListener(e -> {
            new model.MovieDetailsDialog(parent, film).setVisible(true);
            dispose();
        });

        // Book button on the right
        bookButton = new JButton("Select Seat");
        styleButton(bookButton, ACCENT_COLOR.darker());
        bookButton.setEnabled(false);
        bookButton.addActionListener(e -> bookSelectedScreening());

        // Add selection listener to table
        screeningsTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                bookButton.setEnabled(screeningsTable.getSelectedRow() != -1);
                styleButton(bookButton, bookButton.isEnabled() ? ACCENT_COLOR : ACCENT_COLOR.darker());
            }
        });

        // Admin button in the center if user is admin
        if (UserSession.getRole() != null && UserSession.getRole().equals("admin")) {
            JButton adminButton = new JButton("Edit/Add Schedule");
            styleButton(adminButton, new Color(100, 150, 255));
            adminButton.addActionListener(e -> {
                FilmScreeningAdminPanel adminPanel = new FilmScreeningAdminPanel(parent, film);
                adminPanel.setVisible(true);
                dispose();
            });

            JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            centerPanel.setBackground(BACKGROUND_COLOR);
            centerPanel.add(adminButton);
            buttonPanel.add(centerPanel, BorderLayout.CENTER);
        } else {
            JPanel centerPanel = new JPanel();
            centerPanel.setBackground(BACKGROUND_COLOR);
            buttonPanel.add(centerPanel, BorderLayout.CENTER);
        }

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setBackground(BACKGROUND_COLOR);
        leftPanel.add(backButton);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightPanel.setBackground(BACKGROUND_COLOR);
        rightPanel.add(bookButton);

        buttonPanel.add(leftPanel, BorderLayout.WEST);
        buttonPanel.add(rightPanel, BorderLayout.EAST);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void customizeTable() {
        screeningsTable.setBackground(SECONDARY_COLOR);
        screeningsTable.setForeground(TEXT_COLOR);
        screeningsTable.setFont(DETAIL_FONT);
        screeningsTable.setSelectionBackground(ACCENT_COLOR);
        screeningsTable.setSelectionForeground(Color.BLACK);
        screeningsTable.setRowHeight(40);
        screeningsTable.setShowGrid(false);
        screeningsTable.setIntercellSpacing(new Dimension(0, 0));
        screeningsTable.setFillsViewportHeight(true);

        JTableHeader header = screeningsTable.getTableHeader();
        header.setBackground(SECONDARY_COLOR.darker());
        header.setForeground(TEXT_COLOR);
        header.setFont(HEADER_FONT);
        header.setBorder(BorderFactory.createEmptyBorder());

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < screeningsTable.getColumnCount(); i++) {
            screeningsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        screeningsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setFont(DETAIL_FONT);
        button.setBackground(bgColor);
        button.setForeground(bgColor.equals(ACCENT_COLOR) ? Color.BLACK : TEXT_COLOR);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 1),
                BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        button.setFocusPainted(false);
    }

    private void bookSelectedScreening() {
        int selectedRow = screeningsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a screening first", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ScreeningSchedule selectedScreening = screenings.get(selectedRow);
        new SeatUI(parent, selectedScreening,film).setVisible(true);
        dispose();
    }

    public static void main(String[] args) {
        UserSession.setRole("admin");

        SwingUtilities.invokeLater(() -> {
            Film sampleFilm = new Film();
            sampleFilm.setFilmId(1);
            sampleFilm.setTitle("Inception");
            sampleFilm.setGenre("Science Fiction");
            sampleFilm.setDuration(148);
            sampleFilm.setReleaseDate("2010-07-16");
            sampleFilm.setRating(4.8);
            sampleFilm.setSynopsis("A thief who steals corporate secrets through use of dream-sharing technology is given the inverse task of planting an idea into the mind of a CEO.");
            sampleFilm.setPosterUrl("/path/to/sample/poster.jpg");
            JFrame dummyParent = new JFrame();
            ScreeningUI dialog = new ScreeningUI(dummyParent, sampleFilm);
            dialog.setVisible(true);
        });
    }
}