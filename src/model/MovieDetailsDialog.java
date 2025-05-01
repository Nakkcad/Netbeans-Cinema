package model;

import dao.PosterDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Random;

public class MovieDetailsDialog extends JDialog {

    private final Film film;

    public MovieDetailsDialog(JFrame parent, Film film) {
        super(parent, film.getTitle(), true);
        this.film = film;

        setSize(700, 550); // Increased size for better layout
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(70,73,75));

        // Poster container (Left Side)
        JPanel posterContainer = new JPanel(new BorderLayout());
        posterContainer.setPreferredSize(new Dimension(280, 400));
        posterContainer.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        loadPosterImage(film.getPosterUrl(), posterContainer);

        // Movie Details (Right Side)
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Title with star rating
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel(film.getTitle());
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(new Color(30, 30, 30));
        
        // Star rating
        JPanel ratingPanel = createRatingPanel(film.getRating());
        titlePanel.add(titleLabel, BorderLayout.WEST);
        titlePanel.add(ratingPanel, BorderLayout.EAST);
        
        // Metadata panel
        JPanel metaPanel = new JPanel();
        metaPanel.setLayout(new BoxLayout(metaPanel, BoxLayout.Y_AXIS));
        metaPanel.setBackground(Color.WHITE);
        metaPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel genreLabel = createDetailLabel("Genre: " + film.getGenre());
        JLabel durationLabel = createDetailLabel("Duration: " + film.getDuration() + " minutes");
        JLabel releaseLabel = createDetailLabel("Release Date: " + film.getReleaseDate());
        
        metaPanel.add(genreLabel);
        metaPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        metaPanel.add(durationLabel);
        metaPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        metaPanel.add(releaseLabel);
        
        // Synopsis
        JLabel synopsisTitle = new JLabel("Synopsis:");
        synopsisTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        synopsisTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextArea synopsisArea = new JTextArea(film.getSynopsis());
        synopsisArea.setEditable(false);
        synopsisArea.setLineWrap(true);
        synopsisArea.setWrapStyleWord(true);
        synopsisArea.setBackground(Color.WHITE);
        synopsisArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
        synopsisArea.setForeground(new Color(60, 60, 60));
        synopsisArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        synopsisArea.setHighlighter(null); // Make non-selectable
        synopsisArea.setFocusable(false); // Make non-focusable
        
        JScrollPane synopsisScroll = new JScrollPane(synopsisArea);
        synopsisScroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        synopsisScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Book button
        JButton bookButton = new JButton("Book Tickets");
        bookButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        bookButton.setBackground(new Color(255, 204, 0));
        bookButton.setForeground(Color.BLACK);
        bookButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        bookButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        bookButton.addActionListener(e -> {
            new UI.SchedulePage().setVisible(true);
            dispose();
        });

        // Add components to details panel
        detailsPanel.add(titlePanel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        detailsPanel.add(metaPanel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        detailsPanel.add(synopsisTitle);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        detailsPanel.add(synopsisScroll);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        detailsPanel.add(bookButton);

        mainPanel.add(posterContainer, BorderLayout.WEST);
        mainPanel.add(detailsPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JLabel createDetailLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.PLAIN, 13));
        label.setForeground(new Color(80, 80, 80));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

private JPanel createRatingPanel(double rating) {
    JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 2, 0));
    panel.setBackground(Color.WHITE);

    URL location = getClass().getResource("/icons/star.png");
    ImageIcon starIcon = null;

    if (location != null) {
        starIcon = new ImageIcon(location);
    } else {
        // Fallback if icon not found
        starIcon = new ImageIcon(createStarIcon());
        System.err.println("[WARNING] Star icon not found, using fallback.");
    }

    JLabel ratingLabel = new JLabel(String.format("%.1f", rating));
    ratingLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
    ratingLabel.setForeground(new Color(255, 153, 0));

    panel.add(new JLabel(starIcon));
    panel.add(ratingLabel);

    return panel;
}


    private Image createStarIcon() {
        // Create simple star icon programmatically if not found
        BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setColor(new Color(255, 153, 0));
        g2.fillPolygon(new int[]{8, 10, 16, 11, 13, 8, 3, 5, 0, 6}, 
                      new int[]{0, 7, 7, 11, 18, 14, 18, 11, 7, 7}, 10);
        g2.dispose();
        return image;
    }

    private void loadPosterImage(String imagePath, JPanel container) {
        new SwingWorker<JComponent, Void>() {
            @Override
            protected JComponent doInBackground() {
                try {
                    PosterDAO posterDAO = new PosterDAO();
                    BufferedImage scaledImage = posterDAO.loadAndScalePoster(imagePath, film.getTitle(), 250, 375);

                    if (scaledImage == null) return createTextPlaceholder(film.getTitle());

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
                    imagePanel.setBackground(new Color(0, 0, 0, 0));
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
        placeholder.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        String letter = title != null && !title.isEmpty()
                ? title.substring(0, 1).toUpperCase()
                : "?";

        JLabel letterLabel = new JLabel(letter);
        letterLabel.setFont(new Font("SansSerif", Font.BOLD, 72));
        letterLabel.setForeground(Color.WHITE);
        placeholder.add(letterLabel);

        return placeholder;
    }

    private JPanel createErrorPlaceholder() {
        JPanel placeholder = new JPanel(new GridBagLayout());
        placeholder.setBackground(new Color(40, 40, 40));
        placeholder.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        JLabel errorLabel = new JLabel("!");
        errorLabel.setFont(new Font("SansSerif", Font.BOLD, 72));
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
}