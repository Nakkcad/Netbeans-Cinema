package Logic;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import javax.swing.*;

public class SVGImage {
    public static void main(String[] args) {
        JFrame frame = new JFrame("SVG Icon Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        JButton button = new JButton("Click Me");
        button.setIcon(new FlatSVGIcon("path/to/icon.svg"));

        frame.add(button);
        frame.setVisible(true);
    }
}
