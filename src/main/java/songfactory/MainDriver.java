package songfactory;

import javax.swing.SwingUtilities;
import songfactory.music.*;
import songfactory.ui.SwingApp;

public class MainDriver {

    public static void main(String[] args) {
        Conversion.initialize();
        SwingUtilities.invokeLater(() -> { SwingApp app = new SwingApp(); });

    } // main

} // MainDriver
