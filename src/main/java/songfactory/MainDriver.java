package songfactory;

import javax.swing.SwingUtilities;
import songfactory.music.*;
import songfactory.ui.SwingApp;

public class MainDriver {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> { SwingApp app = new SwingApp(); });
//        MusicNode n0 = new MusicNode(Note.C, 1.0 / 2);
//        MusicNode n1 = new MusicNode(Note.D, 1.0 / 4);
//        MusicNode n2 = new MusicNode(Note.E, 1.0 / 8);
//        MusicNode n3 = new MusicNode(Note.A, 1.0 / 4);
//        MusicNode n4 = new MusicNode(Note.G, 1.0 / 4);
//        MusicNode n5 = new MusicNode(Note.B, 1.0 / 2);
////        try { n.incrementFlat(); } catch (RestException re) { System.out.println(re.getMessage()); }
//
//        MusicSequence seq = new MusicSequence();
//        seq.addAll(n0, n1, n2, n3, n4, n5);
//        System.out.println(seq);
//        System.out.println(seq.getTotalNodeLength());
//
//        Measure m0 = new Measure(new TimeSignature(4, 4), 0, seq);
//        Measure m1 = new Measure(new TimeSignature(4, 4), 0, seq);
//        System.out.println(m0);
//        System.out.println(m1);


    } // main

} // MainDriver
