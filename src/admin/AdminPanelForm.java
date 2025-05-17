package admin;

import dao.FilmDAO;
import dao.ScreeningScheduleDAO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Film;
import model.ScreeningSchedule;

public class AdminPanelForm extends javax.swing.JFrame {
    private ScreeningScheduleDAO screeningScheduleDAO;
    private FilmDAO filmDAO;

    public AdminPanelForm() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initComponents();
        screeningScheduleDAO = new ScreeningScheduleDAO();
        filmDAO = new FilmDAO();
        refreshScheduleTable();
    }

    private void refreshScheduleTable() {
        List<ScreeningSchedule> schedules = screeningScheduleDAO.getAllScreeningSchedules();
        DefaultTableModel model = (DefaultTableModel) scheduleTable.getModel();
        model.setRowCount(0); // Clear existing data
        
        for (ScreeningSchedule schedule : schedules) {
            Film film = filmDAO.getFilmById(schedule.getFilmId());
            String filmTitle = (film != null) ? film.getTitle() : "Unknown Film";
            
            model.addRow(new Object[]{
                schedule.getScheduleId(),
                filmTitle,
                schedule.getScreenId(),
                schedule.getScreeningDate(),
                schedule.getScreeningTime()
            });
        }
    }

    private void initComponents() {
        // GUI components initialization
        jScrollPane1 = new javax.swing.JScrollPane();
        scheduleTable = new javax.swing.JTable();
        addButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        viewByFilmButton = new javax.swing.JButton();
        viewByDateButton = new javax.swing.JButton();
        refreshButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Screening Schedule Admin Panel");

        scheduleTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "ID", "Film", "Screen", "Date", "Time"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(scheduleTable);

        addButton.setText("Add New Schedule");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                AddScheduleDialog dialog = new AddScheduleDialog(AdminPanelForm.this, true);
                dialog.setVisible(true);
                refreshScheduleTable();
            }
        });

        editButton.setText("Edit Selected");
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                int selectedRow = scheduleTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int scheduleId = (Integer) scheduleTable.getValueAt(selectedRow, 0);
                    EditScheduleDialog dialog = new EditScheduleDialog(AdminPanelForm.this, true, scheduleId);
                    dialog.setVisible(true);
                    refreshScheduleTable();
                } else {
                    JOptionPane.showMessageDialog(AdminPanelForm.this, 
                        "Please select a schedule to edit", 
                        "No Selection", 
                        JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        deleteButton.setText("Delete Selected");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                int selectedRow = scheduleTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int scheduleId = (Integer) scheduleTable.getValueAt(selectedRow, 0);
                    int confirm = JOptionPane.showConfirmDialog(
                        AdminPanelForm.this, 
                        "Are you sure you want to delete this schedule?", 
                        "Confirm Delete", 
                        JOptionPane.YES_NO_OPTION);
                    
                    if (confirm == JOptionPane.YES_OPTION) {
                        if (screeningScheduleDAO.deleteScreeningSchedule(scheduleId)) {
                            JOptionPane.showMessageDialog(AdminPanelForm.this, 
                                "Schedule deleted successfully");
                            refreshScheduleTable();
                        } else {
                            JOptionPane.showMessageDialog(AdminPanelForm.this, 
                                "Failed to delete schedule", 
                                "Error", 
                                JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(AdminPanelForm.this, 
                        "Please select a schedule to delete", 
                        "No Selection", 
                        JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        viewByFilmButton.setText("View by Film");
        viewByFilmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                ViewByFilmDialog dialog = new ViewByFilmDialog(AdminPanelForm.this, true);
                dialog.setVisible(true);
            }
        });

        viewByDateButton.setText("View by Date");
        viewByDateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                ViewByDateDialog dialog = new ViewByDateDialog(AdminPanelForm.this, true);
                dialog.setVisible(true);
            }
        });

        refreshButton.setText("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                refreshScheduleTable();
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
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(viewByFilmButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(viewByDateButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(refreshButton)
                        .addGap(0, 0, Short.MAX_VALUE)))
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
                    .addComponent(viewByFilmButton)
                    .addComponent(viewByDateButton)
                    .addComponent(refreshButton))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }

    // Variables declaration
    private javax.swing.JButton addButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton editButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton refreshButton;
    private javax.swing.JTable scheduleTable;
    private javax.swing.JButton viewByDateButton;
    private javax.swing.JButton viewByFilmButton;

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AdminPanelForm().setVisible(true);
            }
        });
    }
}