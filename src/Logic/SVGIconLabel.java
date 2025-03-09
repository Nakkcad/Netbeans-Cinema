package Logic;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import javax.swing.*;

/**
 * A custom JLabel with an embedded SVG icon for easy use in NetBeans GUI Designer.
 */
public class SVGIconLabel extends JLabel implements java.io.Serializable {

    private String svgFileName; // Store only the file name
    private static final String BASE_PATH = "resources/images/"; // Default folder path
    
    /**
     * Default constructor
     */
    public SVGIconLabel() {
        super();
    }
    
    /**
     * Constructor with SVG file name and size
     * @param svgFileName The name of the SVG file (e.g., "icon.svg")
     * @param width The width of the icon
     * @param height The height of the icon
     */
    public SVGIconLabel(String svgFileName, int width, int height) {
        super();
        setSVGIcon(svgFileName, width, height);
    }
    
    /**
     * Set an SVG icon to the label
     * @param svgFileName The name of the SVG file (e.g., "icon.svg")
     * @param width The width of the icon
     * @param height The height of the icon
     */
    public void setSVGIcon(String svgFileName, int width, int height) {
        this.svgFileName = svgFileName;
        FlatSVGIcon svgIcon = new FlatSVGIcon(BASE_PATH + svgFileName, width, height);
        setIcon(svgIcon);
    }

    // Getter and setter to make SVG file name editable in NetBeans GUI
    public String getSvgFileName() {
        return svgFileName;
    }

    public void setSvgFileName(String svgFileName) {
        this.svgFileName = svgFileName;
        setSVGIcon(svgFileName, getWidth(), getHeight());
    }

    @Override
    public java.awt.Dimension getPreferredSize() {
        return new java.awt.Dimension(32, 32); // Default size
    }
}
