package model;

import dao.PosterDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Random;

public class MovieDetailsDialog extends JDialog {

    private final Film film;
    private final Color BACKGROUND_COLOR = new Color(30, 32, 34);
    private final Color TEXT_COLOR = new Color(220, 220, 220);
    private final Color SECONDARY_COLOR = new Color(60, 63, 65);
    private final Color ACCENT_COLOR = new Color(255, 204, 0);
    private final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 28);
    private final Font DETAIL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font SYNOPSIS_FONT = new Font("Segoe UI", Font.PLAIN, 15);

    public MovieDetailsDialog(JFrame parent, Film film) {
        super(parent, film.getTitle(), true);
        this.film = film;

        setSize(900, 600); // Larger size for better content display
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Poster container (Left Side)
        JPanel posterContainer = new JPanel(new BorderLayout());
        posterContainer.setPreferredSize(new Dimension(350, 500));
        posterContainer.setBorder(BorderFactory.createLineBorder(SECONDARY_COLOR, 2));
        posterContainer.setBackground(SECONDARY_COLOR);
        loadPosterImage(film.getPosterUrl(), posterContainer);

        // Movie Details (Right Side) - Using GridBagLayout
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBackground(SECONDARY_COLOR);
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();

// Title with star rating - Modified version
        JPanel titlePanel = new JPanel(new BorderLayout(10, 0)); // Added horizontal gap
        titlePanel.setBackground(SECONDARY_COLOR);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel titleLabel = new JLabel(film.getTitle());
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);

// Modified rating panel creation
        JPanel ratingPanel = createRatingPanel(film.getRating());
        ratingPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5)); // Add right padding

// Create a container panel for the title to prevent it from expanding too much
        JPanel titleContainer = new JPanel(new BorderLayout());
        titleContainer.setBackground(SECONDARY_COLOR);
        titleContainer.add(titleLabel, BorderLayout.WEST);

        titlePanel.add(titleContainer, BorderLayout.CENTER);
        titlePanel.add(ratingPanel, BorderLayout.EAST);

// Set constraints for the title panel in detailsPanel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 15, 0);
        detailsPanel.add(titlePanel, gbc);

// Metadata panel
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 15, 0);

        JPanel metaPanel = new JPanel(new GridLayout(3, 1, 0, 8));
        metaPanel.setBackground(SECONDARY_COLOR);
        metaPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        metaPanel.add(createDetailLabel("Genre: " + film.getGenre()));
        metaPanel.add(createDetailLabel("Duration: " + film.getDuration() + " minutes"));
        metaPanel.add(createDetailLabel("Release Date: " + film.getReleaseDate()));

        detailsPanel.add(metaPanel, gbc);

// Synopsis
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 15, 0);

        JLabel synopsisTitle = new JLabel("Synopsis:");
        synopsisTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        synopsisTitle.setForeground(TEXT_COLOR);
        synopsisTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        detailsPanel.add(synopsisTitle, gbc);

        gbc.gridy++;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        JTextArea synopsisArea = new JTextArea(film.getSynopsis());
        synopsisArea.setEditable(false);
        synopsisArea.setLineWrap(true);
        synopsisArea.setWrapStyleWord(true);
        synopsisArea.setBackground(SECONDARY_COLOR.darker());
        synopsisArea.setFont(SYNOPSIS_FONT);
        synopsisArea.setForeground(TEXT_COLOR);
        synopsisArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        synopsisArea.setHighlighter(null);
        synopsisArea.setFocusable(false);

        JScrollPane synopsisScroll = new JScrollPane(synopsisArea);
        synopsisScroll.setBorder(BorderFactory.createLineBorder(SECONDARY_COLOR.darker()));
        synopsisScroll.setBackground(SECONDARY_COLOR);

        detailsPanel.add(synopsisScroll, gbc);

// Book button
        gbc.gridy++;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(15, 0, 0, 0);

        JButton bookButton = new JButton("BOOK TICKETS");
        bookButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        bookButton.setBackground(ACCENT_COLOR);
        bookButton.setForeground(Color.BLACK);
        bookButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_COLOR.darker(), 1),
                BorderFactory.createEmptyBorder(12, 30, 12, 30)
        ));
        bookButton.setFocusPainted(false);
        bookButton.addActionListener(e -> {
            dispose();
            new UI.ScreeningUI(parent, film).setVisible(true);
        });

        detailsPanel.add(bookButton, gbc);

        mainPanel.add(posterContainer, BorderLayout.WEST);
        mainPanel.add(detailsPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JLabel createDetailLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(DETAIL_FONT);
        label.setForeground(TEXT_COLOR.brighter());
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JPanel createRatingPanel(double rating) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panel.setBackground(SECONDARY_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        panel.setMinimumSize(new Dimension(80, 30)); // Ensure minimum width for rating

        URL location = getClass().getResource("/icons/star.png");
        ImageIcon starIcon = null;

        if (location != null) {
            starIcon = new ImageIcon(location);
        } else {
            starIcon = new ImageIcon(createStarIcon());
//            System.err.println("[WARNING] Star icon not found, using fallback.");
        }

        JLabel ratingLabel = new JLabel(String.format("%.1f", rating));
        ratingLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        ratingLabel.setForeground(ACCENT_COLOR);

        panel.add(new JLabel(starIcon));
        panel.add(ratingLabel);

        return panel;
    }

    private Image createStarIcon() {
        BufferedImage image = new BufferedImage(20, 20, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(ACCENT_COLOR);
        g2.fillPolygon(new int[]{10, 12, 20, 14, 16, 10, 4, 6, 0, 8},
                new int[]{0, 8, 8, 13, 20, 15, 20, 13, 8, 8}, 10);
        g2.dispose();
        return image;
    }

    private void loadPosterImage(String imagePath, JPanel container) {
        new SwingWorker<JComponent, Void>() {
            @Override
            protected JComponent doInBackground() {
                try {
                    PosterDAO posterDAO = new PosterDAO();
                    BufferedImage scaledImage = posterDAO.loadAndScalePoster(imagePath, film.getTitle(), 320, 480);

                    if (scaledImage == null) {
                        return createTextPlaceholder(film.getTitle());
                    }

                    JPanel imagePanel = new JPanel(new GridBagLayout()) {
                        @Override
                        protected void paintComponent(Graphics g) {
                            super.paintComponent(g);
                            int x = (getWidth() - scaledImage.getWidth()) / 2;
                            int y = (getHeight() - scaledImage.getHeight()) / 2;
                            g.drawImage(scaledImage, x, y, this);
                        }
                    };
                    imagePanel.setOpaque(false);
                    return imagePanel;
                } catch (Exception e) {
                    System.err.println("Poster load error: " + e.getMessage());
                    return createErrorPlaceholder();
                }
            }

            @Override
            protected void done() {
                try {
                    JComponent result = get();
                    container.removeAll();
                    container.add(result, BorderLayout.CENTER);
                    container.revalidate();
                    container.repaint();
                } catch (Exception e) {
                    container.removeAll();
                    container.add(createErrorPlaceholder(), BorderLayout.CENTER);
                    container.revalidate();
                    container.repaint();
                }
            }
        }.execute();
    }

    private JPanel createTextPlaceholder(String title) {
        JPanel placeholder = new JPanel(new GridBagLayout());
        placeholder.setBackground(getRandomDarkColor());
        placeholder.setBorder(BorderFactory.createLineBorder(SECONDARY_COLOR, 2));

        String letter = title != null && !title.isEmpty()
                ? title.substring(0, 1).toUpperCase()
                : "?";

        JLabel letterLabel = new JLabel(letter);
        letterLabel.setFont(new Font("Segoe UI", Font.BOLD, 100));
        letterLabel.setForeground(Color.WHITE);
        placeholder.add(letterLabel);

        return placeholder;
    }

    private JPanel createErrorPlaceholder() {
        JPanel placeholder = new JPanel(new GridBagLayout());
        placeholder.setBackground(new Color(50, 50, 50));
        placeholder.setBorder(BorderFactory.createLineBorder(SECONDARY_COLOR, 2));

        JLabel errorLabel = new JLabel("!");
        errorLabel.setFont(new Font("Segoe UI", Font.BOLD, 100));
        errorLabel.setForeground(new Color(255, 100, 100));
        placeholder.add(errorLabel);

        return placeholder;
    }

    private Color getRandomDarkColor() {
        Color[] colors = {
            new Color(41, 128, 185), // Blue
            new Color(155, 89, 182), // Purple
            new Color(192, 57, 43), // Red
            new Color(39, 174, 96), // Green
            new Color(211, 84, 0), // Orange
            new Color(52, 73, 94) // Dark Blue
        };
        return colors[new Random().nextInt(colors.length)];
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Sample film for demonstration
            Film sampleFilm = new Film();
            sampleFilm.setFilmId(1);
            sampleFilm.setTitle("Inception");
            sampleFilm.setGenre("Science Fiction");
            sampleFilm.setDuration(148);
            sampleFilm.setReleaseDate("2010-07-16");
            sampleFilm.setRating(4.8);
            sampleFilm.setSynopsis("A thief who steals corporate secrets through use of dream-sharing technology is given the inverse task of planting an idea into the mind of a CEO.");
            sampleFilm.setPosterUrl("/path/to/sample/poster.jpg"); // Make sure this path is valid or test with a null

            JFrame dummyParent = new JFrame();
            dummyParent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            dummyParent.setVisible(true);

            MovieDetailsDialog dialog = new MovieDetailsDialog(dummyParent, sampleFilm);
            dialog.setVisible(true);
        });
    }

}
