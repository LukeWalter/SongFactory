package songfactory;

import javax.swing.SwingUtilities;
import songfactory.music.*;
import songfactory.ui.SwingApp;

public class MainDriver {

    /**
     * Main function
     *
     * @param args Input arguments (not used)
     */
    public static void main(String[] args) {
        Conversion.initialize();
        SwingUtilities.invokeLater(() -> { SwingApp app = new SwingApp(); });

    } // main

} // MainDriver