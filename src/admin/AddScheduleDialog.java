package admin;

import dao.FilmDAO;
import dao.ScreeningScheduleDAO;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import model.Film;
import model.ScreeningSchedule;

public class AddScheduleDialog extends javax.swing.JDialog {
    private FilmDAO filmDAO;
    private ScreeningScheduleDAO screeningScheduleDAO;

    public AddScheduleDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        filmDAO = new FilmDAO();
        screeningScheduleDAO = new ScreeningScheduleDAO();
        loadFilms();
    }

    private void loadFilms() {
        List<Film> films = filmDAO.getFilms(null);
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
        
        for (Film film : films) {
            model.addElement(film.getTitle() + " (" + film.getGenre() + ")");
        }
        
        filmComboBox.setModel(model);
    }

    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        filmComboBox = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        screenIdField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        dateField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        timeField = new javax.swing.JTextField();
        addButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Add New Screening Schedule");

        jLabel1.setText("Film:");

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
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(filmComboBox, 0, 250, Short.MAX_VALUE)
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
                    .addComponent(jLabel1)
                    .addComponent(filmComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
        ));

        pack();
        setLocationRelativeTo(getParent());
    }

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            List<Film> films = filmDAO.getFilms(null);
            int selectedIndex = filmComboBox.getSelectedIndex();
            if (selectedIndex < 0) {
                JOptionPane.showMessageDialog(this, "Please select a film", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int filmId = films.get(selectedIndex).getFilmId();
            int screenId = Integer.parseInt(screenIdField.getText());
            Date date = Date.valueOf(dateField.getText());
            Time time = Time.valueOf(timeField.getText());
            
            ScreeningSchedule schedule = new ScreeningSchedule(filmId, screenId, date, time);
            
            if (screeningScheduleDAO.addScreeningSchedule(schedule)) {
                JOptionPane.showMessageDialog(this, "Schedule added successfully");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add schedule", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid input: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    // Variables declaration
    private javax.swing.JButton addButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField dateField;
    private javax.swing.JComboBox<String> filmComboBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField screenIdField;
    private javax.swing.JTextField timeField;
}