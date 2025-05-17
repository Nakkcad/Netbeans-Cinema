package admin;

import dao.FilmDAO;
import dao.ScreeningScheduleDAO;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Film;
import model.ScreeningSchedule;

public class ViewByFilmDialog extends javax.swing.JDialog {
    private FilmDAO filmDAO;
    private ScreeningScheduleDAO screeningScheduleDAO;

    public ViewByFilmDialog(java.awt.Frame parent, boolean modal) {
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

    private void loadSchedulesForFilm(int filmIndex) {
        List<Film> films = filmDAO.getFilms(null);
        if (filmIndex < 0 || filmIndex >= films.size()) {
            return;
        }
        
        int filmId = films.get(filmIndex).getFilmId();
        List<ScreeningSchedule> schedules = screeningScheduleDAO.getScreeningSchedulesByFilmId(filmId);
        
        DefaultTableModel model = (DefaultTableModel) scheduleTable.getModel();
        model.setRowCount(0);
        
        for (ScreeningSchedule schedule : schedules) {
            model.addRow(new Object[]{
                schedule.getScheduleId(),
                schedule.getScreenId(),
                schedule.getScreeningDate(),
                schedule.getScreeningTime()
            });
        }
    }

    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        filmComboBox = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        scheduleTable = new javax.swing.JTable();
        closeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("View Schedules by Film");

        jLabel1.setText("Select Film:");

        filmComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filmComboBoxActionPerformed(evt);
            }
        });

        scheduleTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "ID", "Screen", "Date", "Time"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(scheduleTable);

        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(filmComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(closeButton)))
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(closeButton)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(getParent());
    }

    private void filmComboBoxActionPerformed(java.awt.event.ActionEvent evt) {
        loadSchedulesForFilm(filmComboBox.getSelectedIndex());
    }

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    // Variables declaration
    private javax.swing.JButton closeButton;
    private javax.swing.JComboBox<String> filmComboBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable scheduleTable;
}