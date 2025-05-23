/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package admin.Screening;


import dao.ScreeningScheduleDAO;
import java.sql.Date;
import java.sql.Time;
import javax.swing.JOptionPane;
import model.Film;
import model.ScreeningSchedule;
    /**
    * A dialog for adding new film screening schedules.
    * This class provides a user interface for cinema administrators to add new screening
    * schedules for a specific film. It includes form fields for entering screen ID,
    * date, and time information, along with functionality to save this information to the database through the ScreeningScheduleDAO.
    * @author hp
    */
public class AddScreeningDialog extends javax.swing.JDialog {
    /**
     * Data Access Object for screening schedule operations.
     * Used to add new screening schedules to the database.
     */
    private ScreeningScheduleDAO screeningScheduleDAO;
    /**
     * The film for which the screening schedule is being added.
     */
    private Film film;
    /**
     * Creates a new AddScreeningDialog.
     * Initializes the dialog with UI components and sets up the ScreeningScheduleDAO.
     * The dialog title is set to include the film title.
     * @param parent the parent frame
     * @param modal whether the dialog should be modal
     * @param film the film for which to add a screening schedule
     */
    public AddScreeningDialog(java.awt.Frame parent, boolean modal, Film film) {
        super(parent, modal);
        this.film = film;
        initComponents();
        screeningScheduleDAO = new ScreeningScheduleDAO();
        setTitle("Add Screening for " + film.getTitle());
    }
    /**
     * Initializes the UI components of the dialog.
     * Sets up labels, text fields, buttons, and their layout. Also configures
     * action listeners for the buttons.
     */
    private void initComponents() {
        jLabel2 = new javax.swing.JLabel();
        screenIdField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        dateField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        timeField = new javax.swing.JTextField();
        addButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel2.setText("Screen ID:");

        jLabel3.setText("Date (YYYY-MM-DD):");

        jLabel4.setText("Time (HH:MM:SS):");

        addButton.setText("Add");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(screenIdField)
                    .addComponent(dateField)
                    .addComponent(timeField))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cancelButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addButton)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(screenIdField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(dateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(timeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addButton)
                    .addComponent(cancelButton))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(getParent());
    }
    /**
     * Handles the action when the Add button is clicked.
     * Validates the input data, creates a new ScreeningSchedule object, and attempts
     * to add it to the database. Shows appropriate success or error messages and
     * closes the dialog on success.
     * @param evt the action event
     */
    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            int screenId = Integer.parseInt(screenIdField.getText());
            Date date = Date.valueOf(dateField.getText());
            Time time = Time.valueOf(timeField.getText());
            
            ScreeningSchedule schedule = new ScreeningSchedule(film.getFilmId(), screenId, date, time);
            
            if (screeningScheduleDAO.addScreeningSchedule(schedule)) {
                JOptionPane.showMessageDialog(this, "Screening added successfully");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add screening", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid input: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * Handles the action when the Cancel button is clicked.
     * Closes the dialog without saving any changes.
     * @param evt the action event
     */
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    // Variables declaration
    private javax.swing.JButton addButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField dateField;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField screenIdField;
    private javax.swing.JTextField timeField;
}