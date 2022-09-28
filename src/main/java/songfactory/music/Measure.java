package songfactory.music;

import java.util.Collections;
import java.util.List;

public class Measure {

    public static final Note[] orderOfFlats = {Note.B, Note.E, Note.A, Note.D, Note.G, Note.C, Note.F};
    private List<Note> flatNotes;
    private List<Note> sharpNotes;


    private TimeSignature time;
    private int key;

    private MusicSequence nodes;

    public Measure() {

        this.time = new TimeSignature(4, 4);
        this.key = 0;

        this.nodes = new MusicSequence();
        this.nodes.add(new MusicNode(Note.REST, 1.0));

    } // Constructor

    public Measure(TimeSignature time, int key) {

        this.time = time;
        this.key = key;

        this.nodes = new MusicSequence();
        this.nodes.add(new MusicNode(Note.REST, this.time.getMeasureLength()));

    } // Constructor

    public Measure(TimeSignature time, int key, MusicSequence seq) {

        this.time = time;
        this.key = key;

        this.processSequence(seq);

    } // Constructor

    public MusicSequence getNodes() {
        return nodes;

    } // getNodes

    public MusicSequence processSequence(MusicSequence seq) {

        if (seq == null) {
            return null;

        } // if

        double remaining = time.getMeasureLength();
        MusicSequence newNodes = new MusicSequence();

        for (int i = 0; i < seq.size(); i++) {

            if (remaining <= 0) {
                break;

            } else if (seq.get(i).getLength() <= remaining) {

                newNodes.add(seq.get(i));
                remaining -= seq.get(i).getLength();

                seq.remove(i);
                i--;

            } else {

                MusicNode broken1 = new MusicNode(seq.get(i));
                MusicNode broken2 = new MusicNode(seq.get(i));

                broken1.setLength(remaining);
                broken2.setLength(seq.get(i).getLength() - remaining);

                seq.remove(i);
                i--;

                newNodes.add(broken1);
                seq.add(0, broken2);

                remaining = 0;

            } // if

        } // for

        if (remaining > 0) {
            newNodes.add(new MusicNode(Note.REST, remaining));

        } // if

        nodes = newNodes;
        return seq;

    } // processSequence

    public int size() {
        return nodes.size();

    } // size

    public double length() {
        return time.getMeasureLength();

    } // length

    public String toString() {
        return nodes.toString();

    } // toString

} // Measure
