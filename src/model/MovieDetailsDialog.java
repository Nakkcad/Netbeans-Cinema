package model;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Random;
import javax.imageio.ImageIO;

public class MovieDetailsDialog extends JDialog {

    private static final String LOCAL_POSTERS_DIR = "resources/posters/";
    private static final String PLACEHOLDER_PATH = "resources/images/poster_placeholder_large.png";
    private final Film film;

    public MovieDetailsDialog(JFrame parent, Film film) {
        super(parent, film.getTitle(), true);
        this.film = film;

        setSize(600, 500);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Poster container (Left Side)
        JPanel posterContainer = new JPanel(new BorderLayout());
        posterContainer.setPreferredSize(new Dimension(250, 350));
        loadPosterImage(film.getPosterUrl(), posterContainer);

        // Movie Details (Right Side)
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(film.getTitle());
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));

        JLabel genreLabel = new JLabel("Genre: " + film.getGenre());
        JLabel durationLabel = new JLabel("Duration: " + film.getDuration() + " minutes");

        JTextArea synopsisArea = new JTextArea(film.getSynopsis());
        synopsisArea.setEditable(false);
        synopsisArea.setLineWrap(true);
        synopsisArea.setWrapStyleWord(true);
        synopsisArea.setBackground(getBackground());

        JButton bookButton = new JButton("Book Tickets");
        bookButton.addActionListener(e -> {
            new UI.BookingPage().setVisible(true);
            dispose();
        });

        detailsPanel.add(titleLabel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        detailsPanel.add(genreLabel);
        detailsPanel.add(durationLabel);
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        detailsPanel.add(new JLabel("Synopsis:"));
        detailsPanel.add(new JScrollPane(synopsisArea));
        detailsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        detailsPanel.add(bookButton);

        mainPanel.add(posterContainer, BorderLayout.WEST);
        mainPanel.add(detailsPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    // ...
    private void loadPosterImage(String imagePath, JPanel container) {
        new SwingWorker<JComponent, Void>() {
            @Override
            protected JComponent doInBackground() throws Exception {
                try {
                    URL imageUrl = null;
                    String localPath = null;

                    // Check if we have a URL or local path
                    if (imagePath != null && !imagePath.isEmpty() && !imagePath.equals("null")) {
                        if (imagePath.startsWith("http")) {
                            // Generate local filename from URL
                            String filename = "poster_" + film.getTitle().hashCode() + ".jpg";
                            filename = filename.replaceAll("[^a-zA-Z0-9.-]", "_");
                            localPath = LOCAL_POSTERS_DIR + filename;

                            // Check if we already have this file locally
                            File localFile = new File(localPath);
                            if (localFile.exists()) {
                                imageUrl = localFile.toURI().toURL();
                            } else {
                                // Download the image
                                imageUrl = new URL(imagePath);
                                BufferedImage image = ImageIO.read(imageUrl);
                                if (image != null) {
                                    // Save to local resources
                                    File dir = new File(LOCAL_POSTERS_DIR);
                                    if (!dir.exists()) {
                                        dir.mkdirs();
                                    }
                                    ImageIO.write(image, "jpg", localFile);
                                    imageUrl = localFile.toURI().toURL();
                                }
                            }
                        } else {
                            // Local file path
                            File file = new File(imagePath);
                            if (file.exists()) {
                                imageUrl = file.toURI().toURL();
                            }
                        }
                    }

                    // If no valid URL, use placeholder
                    if (imageUrl == null) {
                        File placeholderFile = new File(PLACEHOLDER_PATH);
                        if (placeholderFile.exists()) {
                            imageUrl = placeholderFile.toURI().toURL();
                        } else {
                            return createTextPlaceholder(film.getTitle());
                        }
                    }

                    // Load and scale image with high quality
                    BufferedImage originalImage = ImageIO.read(imageUrl);
                    if (originalImage == null) {
                        return createTextPlaceholder(film.getTitle());
                    }

                    // Calculate aspect ratio preserving dimensions
                    int width = 200;
                    int height = 300;
                    double aspectRatio = (double) originalImage.getWidth() / originalImage.getHeight();

                    if (aspectRatio > (double) width / height) {
                        height = (int) (width / aspectRatio);
                    } else {
                        width = (int) (height * aspectRatio);
                    }

                    // Create high quality scaled image
                    BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2d = scaledImage.createGraphics();
                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

                    g2d.drawImage(originalImage, 0, 0, width, height, null);
                    g2d.dispose();

                    // Create a centered panel for the image
                    JPanel imagePanel = new JPanel(new GridBagLayout()) {
                        @Override
                        protected void paintComponent(Graphics g) {
                            super.paintComponent(g);
                            if (scaledImage != null) {
                                int x = (getWidth() - scaledImage.getWidth()) / 2;
                                int y = (getHeight() - scaledImage.getHeight()) / 2;
                                g.drawImage(scaledImage, x, y, this);
                            }
                        }
                    };
                    imagePanel.setOpaque(false);
                    imagePanel.setBackground(new Color(0, 0, 0, 0));

                    return imagePanel;
                } catch (Exception e) {
                    System.err.println("Error loading image: " + e.getMessage());
                    return createErrorPlaceholder();
                }
            }

            @Override
            protected void done() {
                try {
                    JComponent result = get();
                    if (result != null) {
                        container.removeAll();
                        container.add(result, BorderLayout.CENTER);
                        container.revalidate();
                        container.repaint();
                    }
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

        JLabel errorLabel = new JLabel("!");
        errorLabel.setFont(new Font("SansSerif", Font.BOLD, 72));
        errorLabel.setForeground(new Color(255, 100, 100));
        placeholder.add(errorLabel);

        return placeholder;
    }

    private Color getRandomDarkColor() {
        // Generate aesthetic movie-themed colors
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
