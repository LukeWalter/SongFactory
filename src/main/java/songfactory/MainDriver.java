package songfactory;

import javax.swing.SwingUtilities;
import songfactory.music.*;
import songfactory.ui.SwingApp;

public class MainDriver {

    public static void main(String[] args) {
        JMusicNodeFactory.initialize();
        SwingUtilities.invokeLater(() -> { SwingApp app = new SwingApp(); });
        MusicNode n0 = new MusicNode(Note.REST, 1.0 / 2);
        System.out.println(n0.getImage());

    } // main

} // MainDriver
