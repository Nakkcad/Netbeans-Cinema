package UI;

import admin.AdminPanel;
import javax.swing.JOptionPane;
import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.event.ActionEvent;
import javax.swing.UnsupportedLookAndFeelException;
import Utils.UserSession;
import DAO.UserDAO;
import java.awt.*;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;

public class ModernLogin extends javax.swing.JFrame {

    private static final Color BACKGROUND_COLOR = new Color(30, 32, 34);
    private static final Color SECONDARY_COLOR = new Color(60, 63, 65);
    private static final Color ACCENT_COLOR = new Color(255, 204, 0);
    private static final Color TEXT_COLOR = new Color(220, 220, 220);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 28);
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.BOLD, 14);

    private JPanel mainPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signupButton;
    private JCheckBox showPasswordCheckbox;

    public ModernLogin() {
        initComponents();
        setLocationRelativeTo(null);
        setupUI();
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("CinemaApp - Login");
        setPreferredSize(new Dimension(800, 500));
        setMinimumSize(new Dimension(600, 400));

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Left panel with logo
        JPanel leftPanel = createLeftPanel();
        mainPanel.add(leftPanel, BorderLayout.WEST);

        // Right panel with login form
        JPanel rightPanel = createRightPanel();
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        getContentPane().add(mainPanel);
        pack();
    }

    private JPanel createLeftPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(153, 0, 0));
        panel.setPreferredSize(new Dimension(350, 500));

        // Logo or image
        JLabel logoLabel = new JLabel("CINEMA", SwingConstants.CENTER);
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(150, 0, 0, 0));

        panel.add(logoLabel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(SECONDARY_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Title
        JLabel titleLabel = new JLabel("LOGIN", SwingConstants.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(ACCENT_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(SECONDARY_COLOR);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Username field
        JPanel usernamePanel = new JPanel(new BorderLayout(10, 5));
        usernamePanel.setBackground(SECONDARY_COLOR);
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(LABEL_FONT);
        usernameLabel.setForeground(TEXT_COLOR);
        usernameField = createTextField();
        usernamePanel.add(usernameLabel, BorderLayout.NORTH);
        usernamePanel.add(usernameField, BorderLayout.CENTER);
        formPanel.add(usernamePanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Password field
        JPanel passwordPanel = new JPanel(new BorderLayout(10, 5));
        passwordPanel.setBackground(SECONDARY_COLOR);
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(LABEL_FONT);
        passwordLabel.setForeground(TEXT_COLOR);
        passwordField = createPasswordField();
        showPasswordCheckbox = new JCheckBox("Show password");
        showPasswordCheckbox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        showPasswordCheckbox.setForeground(TEXT_COLOR);
        showPasswordCheckbox.setBackground(SECONDARY_COLOR);
        showPasswordCheckbox.addActionListener(e -> {
            passwordField.setEchoChar(showPasswordCheckbox.isSelected() ? (char) 0 : '•');
        });

        JPanel passwordBottomPanel = new JPanel(new BorderLayout());
        passwordBottomPanel.setBackground(SECONDARY_COLOR);
        passwordBottomPanel.add(passwordField, BorderLayout.CENTER);
        passwordBottomPanel.add(showPasswordCheckbox, BorderLayout.SOUTH);

        passwordPanel.add(passwordLabel, BorderLayout.NORTH);
        passwordPanel.add(passwordBottomPanel, BorderLayout.CENTER);
        formPanel.add(passwordPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Login button
        loginButton = createButton("LOGIN");
        loginButton.addActionListener(this::loginButtonActionPerformed);
        formPanel.add(loginButton);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Signup prompt
        JPanel signupPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        signupPanel.setBackground(SECONDARY_COLOR);
        JLabel signupLabel = new JLabel("Don't have an account?");
        signupLabel.setForeground(TEXT_COLOR);
        signupButton = createButton("SIGN UP");
        signupButton.setPreferredSize(new Dimension(100, 30));
        signupButton.addActionListener(this::signupformbuttonActionPerformed);
        signupPanel.add(signupLabel);
        signupPanel.add(signupButton);

        formPanel.add(signupPanel);
        panel.add(formPanel, BorderLayout.CENTER);

        // Add enter key listener for login
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    loginButtonActionPerformed(null);
                }
            }
        });

        return panel;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(TEXT_COLOR);
        field.setBackground(SECONDARY_COLOR.darker());
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SECONDARY_COLOR.brighter(), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        field.setCaretColor(ACCENT_COLOR);
        field.setPreferredSize(new Dimension(250, 40));
        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(TEXT_COLOR);
        field.setBackground(SECONDARY_COLOR.darker());
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SECONDARY_COLOR.brighter(), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        field.setCaretColor(ACCENT_COLOR);
        field.setPreferredSize(new Dimension(250, 40));
        field.setEchoChar('•');
        return field;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.BLACK);
        button.setBackground(ACCENT_COLOR);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(ACCENT_COLOR.brighter());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(ACCENT_COLOR);
            }
        });
        
        return button;
    }

    private void setupUI() {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            System.err.println("Failed to set FlatDarkLaf look and feel");
        }
        
        SwingUtilities.updateComponentTreeUI(this);
    }

    private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {
        String username = usernameField.getText();
        String passwordInput = new String(passwordField.getPassword());

        UserDAO userDAO = new UserDAO();
        try {
            String userRole = userDAO.authenticateUser(username, passwordInput);

            if (userRole != null) {
                JOptionPane.showMessageDialog(null, "Login Successful!");
                UserSession.setUsername(username);
                UserSession.setRole(userRole);

                if ("admin".equalsIgnoreCase(userRole)) {
                    AdminPanel adminPanel = new AdminPanel();
                    adminPanel.setVisible(true);
                } else {
                    ModernHomepage main = new ModernHomepage();
                    main.setVisible(true);
                }
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Invalid Username or Password",
                        "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        } finally {
            userDAO.closeConnection();
        }
    }

    private void signupformbuttonActionPerformed(java.awt.event.ActionEvent evt) {
        Signup signupForm = new Signup();
        signupForm.setVisible(true);
        this.dispose();
    }

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            System.err.println("Failed to set FlatDarkLaf look and feel");
        }

        EventQueue.invokeLater(() -> {
            new ModernLogin().setVisible(true);
        });
    }
}