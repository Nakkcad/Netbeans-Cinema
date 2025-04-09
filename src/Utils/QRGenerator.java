package Utils;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import io.nayuki.qrcodegen.QrCode;
import io.nayuki.qrcodegen.QrCode.Ecc;

public class QRGenerator {
    private int scale;
    private int border;
    private Ecc errorCorrectionLevel;

    public QRGenerator() {
        // Default values
        this.scale = 4;
        this.border = 10;
        this.errorCorrectionLevel = QrCode.Ecc.MEDIUM;
    }

    public QRGenerator(int scale, int border, Ecc errorCorrectionLevel) {
        this.scale = scale;
        this.border = border;
        this.errorCorrectionLevel = errorCorrectionLevel;
    }

    /**
     * Generates a QR code image from the given URL/text
     * @param content The URL or text to encode in the QR code
     * @return BufferedImage containing the QR code
     * @throws IllegalArgumentException if content is null or empty
     */
    public BufferedImage generateQRImage(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be null or empty");
        }
        
        QrCode qr = QrCode.encodeText(content, errorCorrectionLevel);
        return toImage(qr, scale, border);
    }

    /**
     * Generates a QR code image and saves it to a file
     * @param content The URL or text to encode
     * @param filePath Path to save the image file
     * @throws IOException if there's an error writing the file
     */
    public void generateAndSaveQR(String content, String filePath) throws IOException {
        BufferedImage img = generateQRImage(content);
        ImageIO.write(img, "png", new File(filePath));
    }

    /**
     * Converts QR code to BufferedImage
     */
    private BufferedImage toImage(QrCode qr, int scale, int border) {
        int size = qr.size + border * 2;
        BufferedImage image = new BufferedImage(size * scale, size * scale, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                boolean color = (x >= border && x < qr.size + border && 
                               y >= border && y < qr.size + border) && 
                               qr.getModule(x - border, y - border);
                int rgb = color ? 0x000000 : 0xFFFFFF;
                for (int dy = 0; dy < scale; dy++) {
                    for (int dx = 0; dx < scale; dx++) {
                        image.setRGB(x * scale + dx, y * scale + dy, rgb);
                    }
                }
            }
        }
        return image;
    }

    // Getters and setters
    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public int getBorder() {
        return border;
    }

    public void setBorder(int border) {
        this.border = border;
    }

    public Ecc getErrorCorrectionLevel() {
        return errorCorrectionLevel;
    }

    public void setErrorCorrectionLevel(Ecc errorCorrectionLevel) {
        this.errorCorrectionLevel = errorCorrectionLevel;
    }
}