package UI;

import dao.FilmDAO;
import dao.ScreeningDAO;
import model.Film;
import model.Screening;
import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.UnsupportedLookAndFeelException;

public class ScreeningAdminPanel extends javax.swing.JFrame {

    private Film selectedFilm;
    private Screening selectedScreening;
    private boolean isEditMode = false;

    public ScreeningAdminPanel() {
        initComponents();
        loadFilms();
        refreshScreeningsTable();
        setLocationRelativeTo(null);
    }

    private void loadFilms() {
        FilmDAO filmDAO = new FilmDAO();
        List<Film> films = filmDAO.getFilms(null);
        
        filmComboBox.removeAllItems();
        for (Film film : films) {
            filmComboBox.addItem(film.getTitle());
        }
        
        if (filmComboBox.getItemCount() > 0) {
            filmComboBox.setSelectedIndex(0);
            updateSelectedFilm();
        }
    }

    private void updateSelectedFilm() {
        String selectedTitle = (String) filmComboBox.getSelectedItem();
        if (selectedTitle != null) {
            FilmDAO filmDAO = new FilmDAO();
            selectedFilm = filmDAO.getFilmByTitle(selectedTitle);
            refreshScreeningsTable();
        }
    }

    private void refreshScreeningsTable() {
        if (selectedFilm == null) return;
        
        ScreeningDAO screeningDAO = new ScreeningDAO();
        List<Screening> screenings = screeningDAO.getScreeningsByFilm(selectedFilm.getFilmId());
        
        DefaultTableModel model = (DefaultTableModel) screeningsTable.getModel();
        model.setRowCount(0);
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        
        for (Screening screening : screenings) {
            try {
                Date date = dateFormat.parse(screening.getDate());
                Date time = timeFormat.parse(screening.getTime());
                
                model.addRow(new Object[]{
                    screening.getScheduleId(),
                    dateFormat.format(date),
                    timeFormat.format(time)
                });
            } catch (ParseException e) {
                System.err.println("Error parsing date/time: " + e.getMessage());
            }
        }
    }

    private void clearForm() {
        dateField.setText("");
        timeField.setText("");
        isEditMode = false;
        selectedScreening = null;
        addButton.setText("Add");
        statusLabel.setText("Ready");
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("CinemaApp - Screening Management");
        setMinimumSize(new java.awt.Dimension(800, 600));
        setPreferredSize(new java.awt.Dimension(900, 700));

        // Main panel with border layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // North panel - Film selection
        JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        northPanel.setBorder(BorderFactory.createTitledBorder("Film Selection"));
        northPanel.add(new JLabel("Select Film:"));
        
        filmComboBox = new JComboBox<>();
        filmComboBox.addActionListener(e -> updateSelectedFilm());
        northPanel.add(filmComboBox);
        
        mainPanel.add(northPanel, BorderLayout.NORTH);

        // Center panel - Screening details
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBorder(BorderFactory.createTitledBorder("Screening Details"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Date field
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
        
        gbc.gridx = 1;
        dateField = new JTextField(15);
        centerPanel.add(dateField, gbc);
        
        // Time field
        gbc.gridx = 0;
        gbc.gridy = 1;
        centerPanel.add(new JLabel("Time (HH:MM):"), gbc);
        
        gbc.gridx = 1;
        timeField = new JTextField(15);
        centerPanel.add(timeField, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        addButton = new JButton("Add");
        addButton.addActionListener(e -> addOrUpdateScreening());
        buttonPanel.add(addButton);
        
        editButton = new JButton("Edit");
        editButton.addActionListener(e -> enterEditMode());
        buttonPanel.add(editButton);
        
        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> deleteScreening());
        buttonPanel.add(deleteButton);
        
        clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> clearForm());
        buttonPanel.add(clearButton);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        centerPanel.add(buttonPanel, gbc);
        
        // Status label
        statusLabel = new JLabel("Ready");
        gbc.gridy = 3;
        centerPanel.add(statusLabel, gbc);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // South panel - Screenings table
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBorder(BorderFactory.createTitledBorder("Screenings"));
        
        screeningsTable = new JTable();
        screeningsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "ID", "Date", "Time"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(screeningsTable);
        southPanel.add(scrollPane, BorderLayout.CENTER);
        
        mainPanel.add(southPanel, BorderLayout.SOUTH);

        getContentPane().add(mainPanel);
        pack();
    }

    private void addOrUpdateScreening() {
        if (selectedFilm == null) {
            statusLabel.setText("Please select a film first");
            return;
        }
        
        String dateStr = dateField.getText().trim();
        String timeStr = timeField.getText().trim();
        
        if (dateStr.isEmpty() || timeStr.isEmpty()) {
            statusLabel.setText("Please enter both date and time");
            return;
        }
        
        try {
            // Validate date format
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            Date date = dateFormat.parse(dateStr);
            
            // Validate time format
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            timeFormat.setLenient(false);
            Date time = timeFormat.parse(timeStr);
            
            ScreeningDAO screeningDAO = new ScreeningDAO();
            
            if (isEditMode && selectedScreening != null) {
                // Update existing screening
                selectedScreening.setDate(dateFormat.format(date));
                selectedScreening.setTime(timeFormat.format(time));
                
                if (screeningDAO.updateScreening(selectedScreening)) {
                    statusLabel.setText("Screening updated successfully");
                    refreshScreeningsTable();
                    clearForm();
                } else {
                    statusLabel.setText("Failed to update screening");
                }
            } else {
                // Add new screening
                Screening newScreening = new Screening(
                    selectedFilm.getFilmId(),
                    dateFormat.format(date),
                    timeFormat.format(time)
                );
                
                if (screeningDAO.addScreening(newScreening)) {
                    statusLabel.setText("Screening added successfully");
                    refreshScreeningsTable();
                    clearForm();
                } else {
                    statusLabel.setText("Failed to add screening");
                }
            }
        } catch (ParseException e) {
            statusLabel.setText("Invalid date/time format. Use YYYY-MM-DD and HH:MM");
        } catch (Exception e) {
            statusLabel.setText("Error: " + e.getMessage());
        }
    }

    private void enterEditMode() {
        int selectedRow = screeningsTable.getSelectedRow();
        if (selectedRow == -1) {
            statusLabel.setText("Please select a screening to edit");
            return;
        }
        
        int scheduleId = (Integer) screeningsTable.getValueAt(selectedRow, 0);
        String date = (String) screeningsTable.getValueAt(selectedRow, 1);
        String time = (String) screeningsTable.getValueAt(selectedRow, 2);
        
        ScreeningDAO screeningDAO = new ScreeningDAO();
        selectedScreening = screeningDAO.getScreeningById(scheduleId);
        
        if (selectedScreening != null) {
            dateField.setText(date);
            timeField.setText(time);
            isEditMode = true;
            addButton.setText("Update");
            statusLabel.setText("Editing screening ID: " + scheduleId);
        }
    }

    private void deleteScreening() {
        int selectedRow = screeningsTable.getSelectedRow();
        if (selectedRow == -1) {
            statusLabel.setText("Please select a screening to delete");
            return;
        }
        
        int scheduleId = (Integer) screeningsTable.getValueAt(selectedRow, 0);
        String date = (String) screeningsTable.getValueAt(selectedRow, 1);
        String time = (String) screeningsTable.getValueAt(selectedRow, 2);
        
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Delete screening on " + date + " at " + time + "?",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            ScreeningDAO screeningDAO = new ScreeningDAO();
            if (screeningDAO.deleteScreening(scheduleId)) {
                statusLabel.setText("Screening deleted successfully");
                refreshScreeningsTable();
                clearForm();
            } else {
                statusLabel.setText("Failed to delete screening");
            }
        }
    }

    public static void main(String args[]) {
        try {
            javax.swing.UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            System.err.println("Failed to set FlatDarkLaf look and feel");
        }

        java.awt.EventQueue.invokeLater(() -> {
            new ScreeningAdminPanel().setVisible(true);
        });
    }

    // Variables declaration
    private JComboBox<String> filmComboBox;
    private JTextField dateField;
    private JTextField timeField;
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton clearButton;
    private JLabel statusLabel;
    private JTable screeningsTable;
}