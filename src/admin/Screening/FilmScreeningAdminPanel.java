/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package admin.Screening;

/**
 * The FilmScreeningAdminPanel provides an interface for managing screenings of a specific film.
 * Allows administrators to view, add, edit, and delete screening schedules for a selected film.
 * @author ACER
 */
import dao.FilmDAO;
import dao.ScreeningScheduleDAO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Film;
import model.ScreeningSchedule;

public class FilmScreeningAdminPanel extends javax.swing.JFrame {

    private ScreeningScheduleDAO screeningScheduleDAO;
    private final FilmDAO filmDAO;
    private final Film film;
    private final JFrame parent;
    /**
    * Constructs a new FilmScreeningAdminPanel for managing screenings of a specific film.
    * @param parent The parent frame of this panel
    * @param film The film whose screenings will be managed
    */
    public FilmScreeningAdminPanel(JFrame parent, Film film) {
        super("Screening Admin - " + film.getTitle());
        this.parent = parent;
        this.film = film;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initComponents();
        screeningScheduleDAO = new ScreeningScheduleDAO();
        filmDAO = new FilmDAO();
        refreshScheduleTable();
        setTitle("Screening Admin - " + film.getTitle());
        setLocationRelativeTo(null); // or use parent if passed
        toFront();

    }
    /**
     * Refreshes the schedule table with current screening data from the database.
     * Loads all screenings for the current film and displays them in the table.
     * Clears any existing data in the table before populating it with the latest information.
     * Each row in the table represents a screening schedule with its ID, screen number, date, and time.
     */
    private void refreshScheduleTable() {
        List<ScreeningSchedule> schedules = screeningScheduleDAO.getScreeningSchedulesByFilmId(film.getFilmId());
        DefaultTableModel model = (DefaultTableModel) scheduleTable.getModel();
        model.setRowCount(0); // Clear existing data

        for (ScreeningSchedule schedule : schedules) {
            model.addRow(new Object[]{
                schedule.getScheduleId(),
                "Screen " + schedule.getScreenId(),
                schedule.getScreeningDate(),
                schedule.getScreeningTime()
            });
        }
    }
    /**
     * Initializes all Swing components and their layout.
     * Sets up the table, buttons, and their action listeners.
     * Configures the table model with appropriate column types and editability settings.
     * Sets up action listeners for all buttons to handle user interactions.
     * Arranges components using GroupLayout for a responsive interface.
     */
    private void initComponents() {
        // GUI components initialization
        jScrollPane1 = new javax.swing.JScrollPane();
        scheduleTable = new javax.swing.JTable();
        addButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        refreshButton = new javax.swing.JButton();
        backButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        scheduleTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "ID", "Screen", "Date", "Time"
                }
        ) {
            Class[] types = new Class[]{
                java.lang.Integer.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean[]{
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jScrollPane1.setViewportView(scheduleTable);

        addButton.setText("Add New Screening");
        addButton.addActionListener(new ActionListener() {
            /**
             * Opens the AddScreeningDialog to add a new screening schedule.
             * Refreshes the schedule table after the dialog is closed.
             * @param evt the action event
             */
            public void actionPerformed(ActionEvent evt) {
                AddScreeningDialog dialog = new AddScreeningDialog(FilmScreeningAdminPanel.this, true, film);
                dialog.setVisible(true);
                refreshScheduleTable();
            }
        });

        editButton.setText("Edit Selected");
        editButton.addActionListener(new ActionListener() {
            /**
             * Opens the EditScreeningDialog to edit the selected screening schedule.
             * Shows a warning message if no screening is selected.
             * Refreshes the schedule table after the dialog is closed.
             * @param evt the action event
             */
            public void actionPerformed(ActionEvent evt) {
                int selectedRow = scheduleTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int scheduleId = (Integer) scheduleTable.getValueAt(selectedRow, 0);
                    EditScreeningDialog dialog = new EditScreeningDialog(FilmScreeningAdminPanel.this, true, scheduleId, film);
                    dialog.setVisible(true);
                    refreshScheduleTable();
                } else {
                    JOptionPane.showMessageDialog(FilmScreeningAdminPanel.this,
                            "Please select a screening to edit",
                            "No Selection",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        deleteButton.setText("Delete Selected");
        deleteButton.addActionListener(new ActionListener() {
            /**
             * Deletes the selected screening schedule after confirmation.
             * Shows a warning message if no screening is selected.
             * Refreshes the schedule table after successful deletion.
             * @param evt the action event
             */
            public void actionPerformed(ActionEvent evt) {
                int selectedRow = scheduleTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int scheduleId = (Integer) scheduleTable.getValueAt(selectedRow, 0);
                    int confirm = JOptionPane.showConfirmDialog(
                            FilmScreeningAdminPanel.this,
                            "Are you sure you want to delete this screening?",
                            "Confirm Delete",
                            JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        if (screeningScheduleDAO.deleteScreeningSchedule(scheduleId)) {
                            JOptionPane.showMessageDialog(FilmScreeningAdminPanel.this,
                                    "Screening deleted successfully");
                            refreshScheduleTable();
                        } else {
                            JOptionPane.showMessageDialog(FilmScreeningAdminPanel.this,
                                    "Failed to delete screening",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(FilmScreeningAdminPanel.this,
                            "Please select a screening to delete",
                            "No Selection",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        refreshButton.setText("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            /**
             * Refreshes the schedule table with the latest data from the database.
             * @param evt the action event
             */
            public void actionPerformed(ActionEvent evt) {
                refreshScheduleTable();
            }
        });

        backButton.setText("Back");
        backButton.addActionListener(new ActionListener() {
            /**
             * Navigates back to the ScreeningUI panel and disposes this panel.
             * @param evt the action event
             */
            public void actionPerformed(ActionEvent evt) {
                new UI.ScreeningUI(parent, film).setVisible(true); // reopen screening
                dispose();
            }
        });

        // Layout setup
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(backButton)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(addButton)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(editButton)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(deleteButton)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(refreshButton)))
                                .addContainerGap())
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(addButton)
                                        .addComponent(editButton)
                                        .addComponent(deleteButton)
                                        .addComponent(refreshButton)
                                        .addComponent(backButton))
                                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);

    }
    // Variables declaration
    private javax.swing.JButton addButton;
    private javax.swing.JButton backButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton editButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton refreshButton;
    private javax.swing.JTable scheduleTable;

}
