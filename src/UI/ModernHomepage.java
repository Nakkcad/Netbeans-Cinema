package UI;

import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.UnsupportedLookAndFeelException;
import Utils.UserSession;
import admin.AdminPanel;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import dao.FilmDAO;
import dao.PosterDAO;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import model.Film;
/**
 * ModernHomepage class represents the main application screen with a modern UI design.
 * It displays movie categories, allows searching for movies, and provides navigation
 * to other parts of the application like booking history and admin panel.
 * @author hp
 */
public class ModernHomepage extends javax.swing.JFrame {

    /**
     * Background color for UI components
     */
    private static final Color BACKGROUND_COLOR = new Color(30, 32, 34);
    /**
     * Secondary color for UI components
     */
    private static final Color SECONDARY_COLOR = new Color(60, 63, 65);
    /**
     * Accent color for UI components
     */
    private static final Color ACCENT_COLOR = new Color(255, 204, 0);
    /**
     * Text color for UI components
     */
    private static final Color TEXT_COLOR = new Color(220, 220, 220);
    /**
     * Font for title elements
     */
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 28);
    /**
     * Font for category headings
     */
    private static final Font CATEGORY_FONT = new Font("Segoe UI", Font.BOLD, 18);
    /**
     * Flag indicating whether the view is in search mode
     */
    private boolean isSearchMode = false;
    /**
     * Timer for delayed search execution
     */
    private Timer searchTimer;

    // Components
    /**
     * Main panel containing all UI elements
     */
    private JPanel mainPanel;
    /**
     * Scroll pane for movie content
     */
    private JScrollPane moviesScrollPane;
    /**
     * Container for movie categories and cards
     */
    private JPanel moviesContainer;
    /**
     * Search input field 
     */
    private JTextField film_searchbar;
    /**
     * Welcome message label
     */
    private JLabel welcomeLabel;
    /**
    * Constructs a ModernHomepage instance.
    * Initializes components, sets up the UI, and loads movie categories.
    */
    
    public ModernHomepage() {
        initComponents();
        setLocationRelativeTo(null);
        setupUI();
        loadMovieCategoriesInBackground();
    }
    /**
     * Initializes the UI components.
     * Sets up the main layout, panels, and event listeners.
     */
    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("CinemaApp - Homepage");
        setPreferredSize(new Dimension(1200, 800));
        setMinimumSize(new Dimension(1000, 700));

        // Main panel with dark theme
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create menu bar
        JPanel menuBar = createMenuBar();
        mainPanel.add(menuBar, BorderLayout.NORTH);

        // Movies container setup
        moviesContainer = new JPanel();
        moviesContainer.setLayout(new BoxLayout(moviesContainer, BoxLayout.Y_AXIS));
        moviesContainer.setBackground(BACKGROUND_COLOR);
        moviesContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        moviesScrollPane = new JScrollPane(moviesContainer);
        moviesScrollPane.setBorder(null);
        moviesScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        setupScrollingSpeed();

        mainPanel.add(moviesScrollPane, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent evt) {
                for (Window window : Window.getWindows()) {
                    window.dispose();
                }
                new Login().setVisible(true);
            }
        });

        getContentPane().add(mainPanel);
        pack();
    }
    /**
     * Creates the menu bar with welcome message, search functionality, and navigation buttons.
     * @return A panel containing the menu bar components
     */
    private JPanel createMenuBar() {
        JPanel menuBar = new JPanel(new BorderLayout());
        menuBar.setBackground(SECONDARY_COLOR);
        menuBar.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        menuBar.setPreferredSize(new Dimension(100, 60));

        // Welcome label
        welcomeLabel = new JLabel("Welcome, " + UserSession.getUsername());
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        welcomeLabel.setForeground(TEXT_COLOR);

        // Search panel
        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setBackground(SECONDARY_COLOR);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        searchLabel.setForeground(TEXT_COLOR);

        film_searchbar = new JTextField();
        film_searchbar.setPreferredSize(new Dimension(250, 30));
        film_searchbar.setBackground(SECONDARY_COLOR.darker());
        film_searchbar.setForeground(TEXT_COLOR);
        film_searchbar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(SECONDARY_COLOR.brighter(), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        film_searchbar.setCaretColor(ACCENT_COLOR);

        film_searchbar.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    clearSearch();
                }
            }

            public void keyTyped(KeyEvent evt) {
                if (searchTimer != null && searchTimer.isRunning()) {
                    searchTimer.stop();
                }

                searchTimer = new Timer(300, e -> performSearch());
                searchTimer.setRepeats(false);
                searchTimer.start();
            }
        });

        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(film_searchbar, BorderLayout.CENTER);
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton mybookButton = new JButton("My Booking");
        mybookButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        mybookButton.setBackground(ACCENT_COLOR);
        mybookButton.setForeground(Color.BLACK);
        mybookButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_COLOR.darker(), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        mybookButton.setFocusPainted(false);
        mybookButton.addActionListener(e -> {
            new BookingHistoryUI(this).setVisible(true);
        });
        centerPanel.add(mybookButton);

        if ("admin".equalsIgnoreCase(UserSession.getRole())) {

            JButton adminButton = new JButton("Admin Panel");
            adminButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
            adminButton.setBackground(new Color(70, 130, 180)); // Steel blue color
            adminButton.setForeground(Color.WHITE);
            adminButton.setFocusPainted(false);
            adminButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

            adminButton.addActionListener(e -> {
                AdminPanel adminPanel = new AdminPanel();
                adminPanel.setVisible(true);
            });

            centerPanel.add(adminButton);
        }
        menuBar.add(centerPanel, BorderLayout.CENTER);
        menuBar.add(welcomeLabel, BorderLayout.WEST);
        menuBar.add(searchPanel, BorderLayout.EAST);

        return menuBar;
    }
    /**
     * Sets up the UI look and feel.
     * Applies FlatDarkLaf theme to the application.
     */
    private void setupUI() {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            System.err.println("Failed to set FlatDarkLaf look and feel");
        }

        SwingUtilities.updateComponentTreeUI(this);
    }
        /**
     * Sets up custom scrolling speed for the movies scroll pane.
     * Enhances user experience by providing smoother scrolling behavior.
     */
    private void setupScrollingSpeed() {
        moviesScrollPane.addMouseWheelListener(e -> {
            if (!e.isShiftDown()) {
                JScrollBar verticalScrollBar = moviesScrollPane.getVerticalScrollBar();
                int scrollAmount = verticalScrollBar.getUnitIncrement() * 3;

                if (e.getWheelRotation() < 0) {
                    verticalScrollBar.setValue(verticalScrollBar.getValue() - scrollAmount);
                } else {
                    verticalScrollBar.setValue(verticalScrollBar.getValue() + scrollAmount);
                }
                e.consume();
            }
        });
    }
    /**
     * Loads movie categories in a background thread.
     * Creates a SwingWorker to load categories without blocking UI.
     */
    private void loadMovieCategoriesInBackground() {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                FilmDAO filmDAO = new FilmDAO();

                // Add scheduled films first
                addMovieCategory("Now Showing", filmDAO.getAllFilmsWithSchedule());

                // Add other categories
                addMovieCategory("Top Rated", filmDAO.getTopRatedFilms(10));
                addMovieCategory("New Releases", filmDAO.getFilmsByNewest(10));

                // Add genre categories
                String[] popularGenres = {"Action", "Comedy", "Drama", "Sci-Fi", "Horror"};
                for (String genre : popularGenres) {
                    addMovieCategory(genre, filmDAO.getFilmsByGenre(genre, 8));
                }

                return null;
            }

            @Override
            protected void done() {
                updateContainerSize();
            }
        };
        worker.execute();
    }
    /**
     * Adds a category of movies to the container.
     * Creates a category panel with title and a horizontal scrolling panel for movies.
     * @param categoryTitle The title of the category
     * @param films The list of films in this category
     */
    private void addMovieCategory(String categoryTitle, List<Film> films) {
        if (films.isEmpty()) {
            return;
        }

        SwingUtilities.invokeLater(() -> {
            // Category panel
            JPanel categoryPanel = new JPanel();
            categoryPanel.setLayout(new BoxLayout(categoryPanel, BoxLayout.Y_AXIS));
            categoryPanel.setBackground(BACKGROUND_COLOR);
            categoryPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 25, 0));

            // Category title
            JLabel titleLabel = new JLabel(categoryTitle);
            titleLabel.setFont(CATEGORY_FONT);
            titleLabel.setForeground(TEXT_COLOR);
            titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 0));
            titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            categoryPanel.add(titleLabel);

            // Create scroll container
            JPanel scrollContainer = new JPanel(new BorderLayout());
            scrollContainer.setBackground(BACKGROUND_COLOR);
            scrollContainer.setAlignmentX(Component.LEFT_ALIGNMENT);

            // Movies panel with horizontal flow
            JPanel moviesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
            moviesPanel.setBackground(BACKGROUND_COLOR);

            for (Film film : films) {
                MovieCard card = new MovieCard(film, () -> openMovieDetails(film));
                moviesPanel.add(card);
            }

            // Create horizontal scroll pane with faster scrolling
            JScrollPane horizontalScroll = new JScrollPane(moviesPanel);
            horizontalScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            horizontalScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
            horizontalScroll.setBorder(null);
            horizontalScroll.getViewport().setBackground(BACKGROUND_COLOR);

            // Increase scroll speed (3x faster than default)
            horizontalScroll.getHorizontalScrollBar().setUnitIncrement(30);

            // Simple mouse wheel scrolling (no shift key required)
            horizontalScroll.addMouseWheelListener(e -> {
                JScrollBar hBar = horizontalScroll.getHorizontalScrollBar();
                int amount = e.getUnitsToScroll() * hBar.getUnitIncrement();
                hBar.setValue(hBar.getValue() + amount);
                e.consume();
            });

            scrollContainer.add(horizontalScroll, BorderLayout.CENTER);
            categoryPanel.add(scrollContainer);
            moviesContainer.add(categoryPanel);
            moviesContainer.add(Box.createRigidArea(new Dimension(0, 10)));
            updateContainerSize();
        });
    }
    /**
     * Performs search based on the text in the search bar.
     * Handles the transition between search mode and category mode, displays loading indicators, and filters movies based on search text.
     */
    private void performSearch() {
        String searchText = film_searchbar.getText().toLowerCase().trim();
        boolean shouldBeInSearchMode = !searchText.isEmpty();

        if (shouldBeInSearchMode != isSearchMode) {
            isSearchMode = shouldBeInSearchMode;
            setupMoviesContainer();
        }

        if (isSearchMode) {
            moviesContainer.removeAll();
            JLabel loadingLabel = new JLabel("Searching...", SwingConstants.CENTER);
            loadingLabel.setForeground(TEXT_COLOR);
            loadingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            moviesContainer.add(loadingLabel);
            moviesContainer.revalidate();
        }

        new SwingWorker<List<Film>, Void>() {
            @Override
            protected List<Film> doInBackground() {
                return new FilmDAO().getFilms(null);
            }

            @Override
            protected void done() {
                try {
                    List<Film> allFilms = get();
                    moviesContainer.removeAll();

                    if (!isSearchMode) {
                        loadMovieCategoriesInBackground();
                        return;
                    }

                    int matchCount = 0;
                    for (Film film : allFilms) {
                        if (film.getTitle().toLowerCase().contains(searchText)
                                || film.getGenre().toLowerCase().contains(searchText)) {
                            addMovieCard(film);
                            matchCount++;
                        }
                    }

                    if (matchCount == 0) {
                        showNoResultsMessage(searchText);
                    }
                } catch (Exception ex) {
                    showErrorMessage("Search failed: " + ex.getMessage());
                } finally {
                    moviesContainer.revalidate();
                    moviesContainer.repaint();
                    updateContainerSize();
                    moviesScrollPane.getVerticalScrollBar().setValue(0);
                }
            }
        }.execute();
    }
    /**
     * Adds a movie card to the container.
     * Creates a new MovieCard with the film and click handler.
     * @param film The film to add as a card
     */
    private void addMovieCard(Film film) {
        MovieCard card = new MovieCard(film, () -> openMovieDetails(film));
        moviesContainer.add(card);
    }
    /**
     * Shows a message when no search results are found.
     * Creates a panel with "No results found" message and suggestion to try different keywords.
     * @param searchText The search text that yielded no results
     */
    private void showNoResultsMessage(String searchText) {
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBackground(BACKGROUND_COLOR);

        JLabel noResults = new JLabel("No results for: '" + searchText + "'", SwingConstants.CENTER);
        noResults.setForeground(TEXT_COLOR);
        noResults.setFont(new Font("Segoe UI", Font.ITALIC, 16));

        JLabel suggestion = new JLabel("Try different keywords", SwingConstants.CENTER);
        suggestion.setForeground(TEXT_COLOR.brighter());
        suggestion.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        messagePanel.add(noResults, BorderLayout.CENTER);
        messagePanel.add(suggestion, BorderLayout.SOUTH);
        messagePanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));

        moviesContainer.add(messagePanel);
    }
    /**
     * Shows an error message in the movies container.
     * Creates a label with the error message in red.
     * @param message The error message to display
     */
    private void showErrorMessage(String message) {
        JLabel errorLabel = new JLabel(message, SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        moviesContainer.add(errorLabel);
    }
    /**
     * Sets up the movies container layout based on current mode.
     * Uses FlowLayout for search mode (grid of results) and
     * BoxLayout for category mode (vertical categories).
     */
    private void setupMoviesContainer() {
        moviesContainer.removeAll();

        if (isSearchMode) {
            moviesContainer.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
        } else {
            moviesContainer.setLayout(new BoxLayout(moviesContainer, BoxLayout.Y_AXIS));
        }

        moviesContainer.setBackground(BACKGROUND_COLOR);
        moviesContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
    /**
     * Updates the container size based on its content.
     * Calculates size differently for search mode (grid layout) and category mode (vertical layout).
     */
    private void updateContainerSize() {
        if (isSearchMode) {
            int width = moviesScrollPane.getViewport().getWidth() - 20;
            int cardWidth = 220 + 20; // card width + spacing
            int cardsPerRow = Math.max(1, width / cardWidth);
            int rows = (int) Math.ceil((double) moviesContainer.getComponentCount() / cardsPerRow);
            int totalHeight = rows * 350 + 40; // card height + spacing

            moviesContainer.setPreferredSize(new Dimension(width, totalHeight));
        } else {
            int totalHeight = 0;
            for (Component comp : moviesContainer.getComponents()) {
                totalHeight += comp.getPreferredSize().height;
            }
            totalHeight += 40;

            int width = moviesScrollPane.getViewport().getWidth() - 20;
            moviesContainer.setPreferredSize(new Dimension(width, totalHeight));
        }

        moviesContainer.revalidate();
    }
    /**
     * Opens the movie details dialog for a film.
     * @param film The film to display details for
     */
    private void openMovieDetails(Film film) {
        MovieDetailsDialog detailsDialog = new MovieDetailsDialog(this, film);
        detailsDialog.setVisible(true);
    }
    /**
     * Clears the search and returns to category view.
     * Resets the search bar text, sets search mode to false, and reloads movie categories.
     */
    private void clearSearch() {
        film_searchbar.setText("");
        isSearchMode = false;
        setupMoviesContainer();
        loadMovieCategoriesInBackground();
    }
    /**
     * Inner class representing a movie card in the UI.
     * Displays movie poster, title, and handles user interactions.
     */
    // Modern MovieCard implementation
    class MovieCard extends JPanel {
        /**
         * Width of the card
         */
        private static final int CARD_WIDTH = 220;
        /**
         * Height of the card
         */
        private static final int CARD_HEIGHT = 330;
        /**
         * Background color of the card
         */
        private static final Color CARD_BG = SECONDARY_COLOR;
        /**
         * Hover effect color
         */
        private static final Color HOVER_COLOR = ACCENT_COLOR;
        /**
         * The film associated with this card
         */
        private final Film film;
        /**
         * Progress of hover animation
         */
        private float hoverProgress = 0f;
        /**
         * Timer for hover animation
         */
        private Timer hoverTimer;
        /**
         * Creates a new movie card with the specified film and click handler.
         * @param film The film to display
         * @param onClick The action to perform when the card is clicked
         */
        
        public MovieCard(Film film, Runnable onClick) {
            this.film = film;

            setLayout(new BorderLayout());
            setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT));
            setBackground(CARD_BG);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(CARD_BG.brighter(), 1),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            // Poster panel
            JPanel posterPanel = createPosterPanel();
            add(posterPanel, BorderLayout.CENTER);

            // Title panel
            JPanel titlePanel = createTitlePanel();
            add(titlePanel, BorderLayout.SOUTH);

            // Hover effects
            setupHoverEffects();

            // Click handler
            addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    onClick.run();
                }
            });
        }
        /**
         * Creates the panel for displaying the movie poster.
         * @return The poster panel
         */
        
        private JPanel createPosterPanel() {
            JPanel panel = new JPanel(new BorderLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    // Gradient overlay
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
                    g2d.setPaint(new GradientPaint(0, 0, new Color(0, 0, 0, 0),
                            0, getHeight(), new Color(0, 0, 0, 100)));
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                    g2d.dispose();
                }
            };
            panel.setPreferredSize(new Dimension(CARD_WIDTH - 10, CARD_HEIGHT - 50));
            panel.setBackground(new Color(40, 40, 40));

            // Loading indicator
            JLabel loadingLabel = new JLabel("Loading...", SwingConstants.CENTER);
            loadingLabel.setForeground(TEXT_COLOR);
            panel.add(loadingLabel, BorderLayout.CENTER);

            // Load poster image
            loadPosterImage(film.getPosterUrl(), panel);

            return panel;
        }
        /**
         * Creates the panel for displaying the movie title.
         * @return The title panel
         */
        private JPanel createTitlePanel() {
            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(CARD_BG);
            panel.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));

            JLabel titleLabel = new JLabel("<html><center>" + truncateText(film.getTitle(), 30) + "</center></html>");
            titleLabel.setForeground(TEXT_COLOR);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

            panel.add(titleLabel, BorderLayout.CENTER);

            return panel;
        }
        /**
         * Loads the poster image asynchronously.
         * @param imagePath The path to the image
         * @param container The container to add the image to
         */
        private void loadPosterImage(String imagePath, JPanel container) {
            new SwingWorker<JComponent, Void>() {
                @Override
                protected JComponent doInBackground() throws Exception {
                    PosterDAO posterDAO = new PosterDAO();
                    BufferedImage scaledImage = posterDAO.loadAndScalePoster(imagePath, film.getTitle(), 200, 300);

                    if (scaledImage == null) {
                        return createTextPlaceholder(film.getTitle());
                    }

                    return new JPanel() {
                        @Override
                        protected void paintComponent(Graphics g) {
                            super.paintComponent(g);
                            int x = (getWidth() - scaledImage.getWidth()) / 2;
                            int y = (getHeight() - scaledImage.getHeight()) / 2;
                            g.drawImage(scaledImage, x, y, this);
                        }
                    };
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
        /**
         * Creates a placeholder when image is not available.
         * @param title The movie title
         * @return A placeholder panel with the first letter of the title
         */
        
        private JPanel createTextPlaceholder(String title) {
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBackground(getRandomDarkColor());

            String letter = title != null && !title.isEmpty()
                    ? title.substring(0, 1).toUpperCase() : "?";

            JLabel label = new JLabel(letter);
            label.setFont(new Font("Segoe UI", Font.BOLD, 72));
            label.setForeground(Color.WHITE);
            panel.add(label);

            return panel;
        }
        /**
         * Creates a placeholder for error cases.
         * @return An error placeholder panel
         */
        private JPanel createErrorPlaceholder() {
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBackground(new Color(50, 50, 50));

            JLabel label = new JLabel("!");
            label.setFont(new Font("Segoe UI", Font.BOLD, 72));
            label.setForeground(new Color(255, 100, 100));
            panel.add(label);

            return panel;
        }
        /**
         * Generates a random dark color for placeholders.
         * @return A random color from a predefined set of movie-themed colors
         */
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
        /**
         * Truncates text if it exceeds maximum length.
         * @param text The text to truncate
         * @param maxLength The maximum length
         * @return The truncated text with ellipsis if needed
         */
        private String truncateText(String text, int maxLength) {
            return text.length() > maxLength ? text.substring(0, maxLength - 3) + "..." : text;
        }
        /**
         * Sets up hover animation effects.
         * Creates a timer for smooth transitions between normal and hover states.
         */
        private void setupHoverEffects() {
            hoverTimer = new Timer(10, e -> {
                if (isHovered()) {
                    hoverProgress = Math.min(1f, hoverProgress + 0.05f);
                } else {
                    hoverProgress = Math.max(0f, hoverProgress - 0.05f);
                }

                float intensity = easeInOut(hoverProgress);
                Color borderColor = interpolateColor(CARD_BG.brighter(), HOVER_COLOR, intensity);

                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(borderColor, intensity < 0.5f ? 1 : 2),
                        BorderFactory.createEmptyBorder(5, 5, 5, 5)
                ));

                if ((isHovered() && hoverProgress >= 1f) || (!isHovered() && hoverProgress <= 0f)) {
                    ((Timer) e.getSource()).stop();
                }

                repaint();
            });

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    if (!hoverTimer.isRunning()) {
                        hoverTimer.start();
                    }
                }

                public void mouseExited(MouseEvent e) {
                    if (!hoverTimer.isRunning()) {
                        hoverTimer.start();
                    }
                }
            });
        }
        /**
         * Checks if the mouse is currently hovering over the card.
         * @return true if the card is being hovered over, false otherwise
         */
        private boolean isHovered() {
            Point p = MouseInfo.getPointerInfo().getLocation();
            SwingUtilities.convertPointFromScreen(p, this);
            return contains(p);
        }
        /**
         * Cubic ease in-out function for smooth animation.
         * @param t The input value between 0 and 1
         * @return The eased value between 0 and 1
         */
        private float easeInOut(float t) {
            return t < 0.5f ? 4 * t * t * t : 1 - (float) Math.pow(-2 * t + 2, 3) / 2;
        }
        /**
         * Interpolates between two colors.
         * @param c1 The first color
         * @param c2 The second color
         * @param ratio The interpolation ratio (0 to 1)
         * @return The interpolated color
         */
        private Color interpolateColor(Color c1, Color c2, float ratio) {
            int r = (int) (c1.getRed() + (c2.getRed() - c1.getRed()) * ratio);
            int g = (int) (c1.getGreen() + (c2.getGreen() - c1.getGreen()) * ratio);
            int b = (int) (c1.getBlue() + (c2.getBlue() - c1.getBlue()) * ratio);
            return new Color(r, g, b);
        }
    }
    /**
     * The main entry point for the application.
     * Sets up the look and feel and creates the homepage.
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            System.err.println("Failed to set FlatDarkLaf look and feel");
        }

        EventQueue.invokeLater(() -> {
            new ModernHomepage().setVisible(true);
        });
    }
}
