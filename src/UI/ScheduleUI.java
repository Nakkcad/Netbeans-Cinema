
package UI;


import dao.ScreeningDAO;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author ACER
 */

public class ScheduleUI extends javax.swing.JFrame {

    private int filmId;
    private String filmName;
    private String selectedTime;
    private ScreeningDAO screeningDAO = new ScreeningDAO();
    
    // Color constants
    private final Color BACKGROUND_COLOR = new Color(30, 32, 34);
    private final Color TEXT_COLOR = new Color(220, 220, 220);
    private final Color SECONDARY_COLOR = new Color(60, 63, 65);
    private final Color ACCENT_COLOR = new Color(255, 204, 0);

    public ScheduleUI(int filmId, String filmName) {
        this.filmId = filmId;
        this.filmName = filmName;
        requestFocus();
        initComponents();
        customizeUI();
        loadDates();
        setLocationRelativeTo(null);
    }

    private void customizeUI() {
        // Set frame properties
        setTitle("Schedule - " + filmName);
        getContentPane().setBackground(BACKGROUND_COLOR);
        
        // North Panel customization
        northPanel.setBackground(SECONDARY_COLOR);
        northPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        jLabel1.setFont(new Font("Segoe UI", Font.BOLD, 20));
        jLabel1.setForeground(ACCENT_COLOR);
        
        moviename_label.setFont(new Font("Segoe UI", Font.BOLD, 20));
        moviename_label.setForeground(TEXT_COLOR);
        
        // Center Panel customization
        centerPanel.setBackground(BACKGROUND_COLOR);
        centerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Date selection components
        dateLabel.setForeground(TEXT_COLOR);
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        dateComboBox.setBackground(SECONDARY_COLOR);
        dateComboBox.setForeground(TEXT_COLOR);
        dateComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateComboBox.setPreferredSize(new Dimension(200, 30));
        
        // Time slots panel
        timeSlotsPanel.setBackground(BACKGROUND_COLOR);
        timeSlotsPanel.setLayout(new GridLayout(0, 3, 15, 15));
        timeSlotsPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        timeScrollPane.setBorder(BorderFactory.createEmptyBorder());
        timeScrollPane.getViewport().setBackground(BACKGROUND_COLOR);
        
        // Button panel
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        styleButton(backButton);
        styleButton(confirmButton);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(ACCENT_COLOR);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR.darker(), 1),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        
    }

    private void loadDates() {
        dateComboBox.removeAllItems();
        List<String> dates = screeningDAO.getAvailableDates(filmId);

        for (String date : dates) {
            dateComboBox.addItem(date);
        }

        if (dateComboBox.getItemCount() > 0) {
            loadTimeSlots((String) dateComboBox.getSelectedItem());
        }
    }

    private void loadTimeSlots(String date) {
        timeSlotsPanel.removeAll();
        List<String> times = screeningDAO.getTimeSlots(filmId, date);

        for (String time : times) {
            JButton timeButton = new JButton(time);
            styleTimeButton(timeButton);
            timeButton.addActionListener(e -> {
                resetTimeButtonColors();
                timeButton.setBackground(ACCENT_COLOR);
                timeButton.setForeground(Color.BLACK);
                selectedTime = time;
            });
            timeSlotsPanel.add(timeButton);
        }

        timeSlotsPanel.revalidate();
        timeSlotsPanel.repaint();
    }

    private void styleTimeButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(SECONDARY_COLOR);
        button.setForeground(TEXT_COLOR);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SECONDARY_COLOR.brighter(), 1),
            BorderFactory.createEmptyBorder(10, 0, 10, 0)
        ));
        button.setPreferredSize(new Dimension(120, 50));
    }

    private void resetTimeButtonColors() {
        for (Component comp : timeSlotsPanel.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                button.setBackground(SECONDARY_COLOR);
                button.setForeground(TEXT_COLOR);
            }
        }
    }

    private void handleBookingConfirmation(String date, String time) {
        JOptionPane.showMessageDialog(this,
            "Booking confirmed for:\n"
            + "Movie: " + filmName + "\n"
            + "Date: " + date + "\n"
            + "Time: " + time,
            "Booking Confirmed",
            JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        northPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        moviename_label = new javax.swing.JLabel();
        centerPanel = new javax.swing.JPanel();
        dateLabel = new javax.swing.JLabel();
        dateComboBox = new javax.swing.JComboBox<>();
        timeScrollPane = new javax.swing.JScrollPane();
        timeSlotsPanel = new javax.swing.JPanel();
        buttonPanel = new javax.swing.JPanel();
        backButton = new javax.swing.JButton();
        confirmButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setText("SHOWTIME");

        moviename_label.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        moviename_label.setText("<MOVIE NAME>");

        javax.swing.GroupLayout northPanelLayout = new javax.swing.GroupLayout(northPanel);
        northPanel.setLayout(northPanelLayout);
        northPanelLayout.setHorizontalGroup(
            northPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(northPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(moviename_label, javax.swing.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE)
                .addContainerGap())
        );
        northPanelLayout.setVerticalGroup(
            northPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(northPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(northPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(moviename_label))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        getContentPane().add(northPanel, java.awt.BorderLayout.NORTH);

        centerPanel.setLayout(new java.awt.GridBagLayout());

        dateLabel.setText("Select Date");
        centerPanel.add(dateLabel, new java.awt.GridBagConstraints());

        dateComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        dateComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dateComboBoxActionPerformed(evt);
            }
        });
        centerPanel.add(dateComboBox, new java.awt.GridBagConstraints());

        timeSlotsPanel.setLayout(new java.awt.GridLayout(0, 3));
        timeScrollPane.setViewportView(timeSlotsPanel);

        centerPanel.add(timeScrollPane, new java.awt.GridBagConstraints());

        buttonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        backButton.setText("Back");
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(backButton);

        confirmButton.setText("Confirm");
        confirmButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(confirmButton);

        centerPanel.add(buttonPanel, new java.awt.GridBagConstraints());

        getContentPane().add(centerPanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void dateComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dateComboBoxActionPerformed
        // TODO add your handling code here:
        if (dateComboBox.getSelectedItem() != null) {
            loadTimeSlots((String) dateComboBox.getSelectedItem());
        }
    }//GEN-LAST:event_dateComboBoxActionPerformed

    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backButtonActionPerformed
        // TODO add your handling code here:
        this.dispose();

    }//GEN-LAST:event_backButtonActionPerformed

    private void confirmButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmButtonActionPerformed
        // TODO add your handling code here:
        if (selectedTime == null) {
            JOptionPane.showMessageDialog(this, "Please select a time slot", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String selectedDate = (String) dateComboBox.getSelectedItem();
        handleBookingConfirmation(selectedDate, selectedTime);
    }//GEN-LAST:event_confirmButtonActionPerformed




    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backButton;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JPanel centerPanel;
    private javax.swing.JButton confirmButton;
    private javax.swing.JComboBox<String> dateComboBox;
    private javax.swing.JLabel dateLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel moviename_label;
    private javax.swing.JPanel northPanel;
    private javax.swing.JScrollPane timeScrollPane;
    private javax.swing.JPanel timeSlotsPanel;
    // End of variables declaration//GEN-END:variables
}
