package songfactory.music;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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

                double leftover = seq.get(i).getLength() - remaining;

                MusicNode broken1 = new MusicNode(seq.get(i));
                broken1.setLength(remaining);

                if (Conversion.noteTable.containsKey(remaining)) {
                    newNodes.add(broken1);

                } else {
//                    System.out.println("For this measure | " + broken1);
                    newNodes.addAll(broken1.split());

                } // if

                remaining = 0;


                MusicNode broken2 = new MusicNode(seq.get(i));
                broken2.setLength(leftover);

                seq.remove(i);

                if (Conversion.noteTable.containsKey(leftover)) {
                    seq.add(0, broken2);

                } else {
//                    System.out.println("For next measure | " + broken2);
                    seq.addAll(0, broken2.split());

                } // if

                i--;


            } // if

        } // for

        if (remaining > 0) {
            newNodes.addAll((new MusicNode(Note.REST, remaining)).split());

        } // if

        nodes = newNodes;
        return seq;

    } // processSequence

    public void combine() {

        HashMap<Integer, Integer> startIndexes = new HashMap<>();

        for (int i = 0; i < this.size(); i++) {

            if (i == 0) {
                startIndexes.put(0, 0);
                continue;

            } // if

            if (nodes.get(i).getNote() != nodes.get(i - 1).getNote()) {
                startIndexes.put(i, i);

            } // if

        } // for

        System.out.println(startIndexes);
        throw new UnsupportedOperationException("Still working on this!");

    } // combine

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
