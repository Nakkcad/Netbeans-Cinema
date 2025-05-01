package dao;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;

public class PosterDAO {

    private static final String LOCAL_POSTERS_DIR = "src/resources/posters/";

    /**
     * Loads and scales the poster image from a URL or local path.
     *
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
