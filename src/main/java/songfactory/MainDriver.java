package songfactory;

import javax.swing.SwingUtilities;
import songfactory.music.*;
import songfactory.ui.SwingApp;

import java.util.List;

public class MainDriver {

    public static void main(String[] args) {
        Conversion.initialize();
        SwingUtilities.invokeLater(() -> { SwingApp app = new SwingApp(); });

//        MusicNode n0 = new MusicNode(Note.REST, 0.25);
//        MusicNode n1 = new MusicNode(Note.REST, 0.25);
//
//        Pair<Note, Integer> p0 = new Pair<>(n0.getNote(), n0.getOctave());
//        Pair<Note, Integer> p1 = new Pair<>(n1.getNote(), n1.getOctave());
//
//        System.out.println(p0.equals(p1));

    } // main

} // MainDriver
