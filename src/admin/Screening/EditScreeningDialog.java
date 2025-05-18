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
 * A dialog for editing screening schedule information.
 * Provides fields to modify screen ID, date, and time for a film screening.
 * @author hp
 */
public class EditScreeningDialog extends javax.swing.JDialog {
    private ScreeningScheduleDAO screeningScheduleDAO;
    private int scheduleId;
    private Film film;
    /**
    * Creates new EditScreeningDialog.
    * @param parent The parent frame of this dialog
    * @param modal Whether the dialog should be modal
    * @param scheduleId The ID of the screening schedule to edit
    * @param film The film associated with this screening
    */
    public EditScreeningDialog(java.awt.Frame parent, boolean modal, int scheduleId, Film film) {
        super(parent, modal);
        this.scheduleId = scheduleId;
        this.film = film;
        initComponents();
        screeningScheduleDAO = new ScreeningScheduleDAO();
        loadData();
        setTitle("Edit Screening for " + film.getTitle());
    }
    /**
     * Loads screening data from database and populates form fields.
     * Displays error message and closes dialog if screening not found.
     */
    private void loadData() {
        ScreeningSchedule schedule = screeningScheduleDAO.getScreeningScheduleById(scheduleId);
        if (schedule == null) {
            JOptionPane.showMessageDialog(this, "Screening not found", "Error", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        screenIdField.setText(String.valueOf(schedule.getScreenId()));
        dateField.setText(schedule.getScreeningDate().toString());
        timeField.setText(schedule.getScreeningTime().toString());
    }
    /**
     * Initializes all Swing components and their layout.
     * Sets up text fields, labels, and buttons with their action listeners.
     */
    private void initComponents() {
        jLabel2 = new javax.swing.JLabel();
        screenIdField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        dateField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        timeField = new javax.swing.JTextField();
        updateButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel2.setText("Screen ID:");

        jLabel3.setText("Date (YYYY-MM-DD):");

        jLabel4.setText("Time (HH:MM:SS):");

        updateButton.setText("Update");
        updateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateButtonActionPerformed(evt);
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
                .addComponent(updateButton)
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
                    .addComponent(updateButton)
                    .addComponent(cancelButton))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(getParent());
    }
    /**
     * Handles Update button click event.
     * Validates input fields and updates screening schedule in database.
     * Shows success/error messages and closes dialog on successful update.
     * @param evt The ActionEvent object containing event details
     */
    private void updateButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            int screenId = Integer.parseInt(screenIdField.getText());
            Date date = Date.valueOf(dateField.getText());
            Time time = Time.valueOf(timeField.getText());

            ScreeningSchedule schedule = new ScreeningSchedule(
                    scheduleId,
                    film.getFilmId(),
                    screenId,
                    date,
                    time
            );

            if (screeningScheduleDAO.updateScreeningSchedule(schedule)) {
                JOptionPane.showMessageDialog(this, "Screening updated successfully");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update screening", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid input: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * Handles Cancel button click event.
     * Closes the dialog without saving changes.
     * @param evt The ActionEvent object containing event details
     */
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    // Variables declaration
    private javax.swing.JButton updateButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField dateField;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField screenIdField;
    private javax.swing.JTextField timeField;
}