package dao;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
/**
 * Data Access Object for managing movie poster images.
 * This class provides methods to load, cache, and scale movie poster images
 * from URLs or local paths. It handles the downloading, caching, and resizing
 * of poster images to ensure they are displayed correctly in the application.
 * @author hp
 */
public class PosterDAO {
    /**
     * The local directory where downloaded poster images are cached.
     */
    private static final String LOCAL_POSTERS_DIR = "src/resources/posters/";

    /**
     * Loads and scales the poster image from a URL or local path.
     *This method attempts to load a poster image from the provided path, which can
     * be either a URL or a local file path. If the path is a URL, the image is
     * downloaded and cached locally to avoid repeated downloads. The image is then
     * scaled to the specified dimensions while maintaining its aspect ratio.
     * @param imagePath The image URL or local path.
     * @param title     The film title (used for naming the cached file).
     * @param targetWidth Desired width of the final image.
     * @param targetHeight Desired height of the final image.
     * @return A scaled BufferedImage or null if loading fails.
     */
    public BufferedImage loadAndScalePoster(String imagePath, String title, int targetWidth, int targetHeight) {
        try {
            URL imageUrl = null;
            String localPath = null;

            if (imagePath != null && !imagePath.isEmpty() && !imagePath.equals("null")) {
                if (imagePath.startsWith("http")) {
                    String filename = "poster_" + title.hashCode() + ".jpg";
                    filename = filename.replaceAll("[^a-zA-Z0-9.-]", "_");
                    localPath = LOCAL_POSTERS_DIR + filename;

                    File localFile = new File(localPath);
                    if (!localFile.exists()) {
                        BufferedImage downloadedImage = ImageIO.read(new URL(imagePath));
                        if (downloadedImage != null) {
                            File dir = new File(LOCAL_POSTERS_DIR);
                            if (!dir.exists()) dir.mkdirs();
                            ImageIO.write(downloadedImage, "jpg", localFile);
                        }
                    }

                    imageUrl = new File(localPath).toURI().toURL();
                } else {
                    File file = new File(imagePath);
                    if (file.exists()) {
                        imageUrl = file.toURI().toURL();
                    }
                }
            }

            if (imageUrl != null) {
                BufferedImage originalImage = ImageIO.read(imageUrl);
                if (originalImage != null) {
                    return scaleImage(originalImage, targetWidth, targetHeight);
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading/scaling poster: " + e.getMessage());
        }
        return null;
    }

    /**
     * Scales the given image while maintaining aspect ratio.
     * This private helper method resizes an image to the target dimensions while
     * preserving its aspect ratio. It uses high-quality interpolation and anti-aliasing
     * to ensure the scaled image looks good. The method adjusts either the width or
     * height as needed to maintain the original aspect ratio.
     * @param originalImage the original image to scale
     * @param targetWidth the desired width of the scaled image
     * @param targetHeight the desired height of the scaled image
     * @return a new BufferedImage containing the scaled version of the original image
     */
    private BufferedImage scaleImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        double aspectRatio = (double) originalImage.getWidth() / originalImage.getHeight();

        if (aspectRatio > (double) targetWidth / targetHeight) {
            targetHeight = (int) (targetWidth / aspectRatio);
        } else {
            targetWidth = (int) (targetHeight * aspectRatio);
        }

        BufferedImage scaledImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = scaledImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        g2d.dispose();

        return scaledImage;
    }
}
