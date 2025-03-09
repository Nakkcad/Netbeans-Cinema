package Logic;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import javax.swing.*;

/**
 * A custom JLabel with an embedded SVG icon for easy use in NetBeans GUI Designer.
 */
public class SVGIconLabel extends JLabel implements java.io.Serializable {

    private String svgPath; // Store the SVG path
    
    /**
     * Default constructor
     */
    public SVGIconLabel() {
        super();
    }
    
    /**
     * Constructor with SVG path and size
     * @param svgPath The path to the SVG file (e.g., "/Logic/resources/icon.svg")
     * @param width The width of the icon
     * @param height The height of the icon
     */
    public SVGIconLabel(String svgPath, int width, int height) {
        super();
        setSVGIcon(svgPath, width, height);
    }
    
    /**
     * Set an SVG icon to the label
     * @param svgPath The path to the SVG file
     * @param width The width of the icon
     * @param height The height of the icon
     */
    public void setSVGIcon(String svgPath, int width, int height) {
        this.svgPath = svgPath;
        FlatSVGIcon svgIcon = new FlatSVGIcon(svgPath, width, height);
        setIcon(svgIcon);
    }

    // Getter and setter to make SVG path editable in NetBeans GUI
    public String getSvgPath() {
        return svgPath;
    }

    public void setSvgPath(String svgPath) {
        this.svgPath = svgPath;
        setSVGIcon(svgPath, getWidth(), getHeight());
    }

    @Override
    public java.awt.Dimension getPreferredSize() {
        return new java.awt.Dimension(32, 32); // Default size
    }
}
