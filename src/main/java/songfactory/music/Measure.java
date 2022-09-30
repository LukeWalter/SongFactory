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
        this.combine();
        return seq;

    } // processSequence

    public void combine() {
        System.out.println(nodes);
        HashMap<Integer, Integer> startIndexes = new HashMap<>();
        startIndexes.put(0, 0);

        for (int i = 1; i < this.size(); i++) {

            if (nodes.get(i).getNote() != nodes.get(i - 1).getNote()) {
                startIndexes.put(i, i);

            } // if

        } // for

        System.out.println(startIndexes);
        int sIndex = 0;

        MusicNode start;
        MusicNode curr;

        for (int i = 1; i < nodes.size(); i++) {

            start = nodes.get(sIndex);
            curr = nodes.get(i);

            System.out.println("" + start + " | " + curr);
            if (curr.getNote() != start.getNote()) {
                sIndex = i;

            } else if (start.getNote() == Note.REST) {System.out.println("Removing index " + i);
                start.setLength(start.getLength() + curr.getLength());
                nodes.remove(curr);
                i--;

            } // if

        } // for

        System.out.println(nodes);

        for (int i = 0; i < nodes.size(); i++) {

            curr = nodes.get(i);
            List<MusicNode> replace = curr.split();

            for (MusicNode n : replace) {
                nodes.add(i, n);
                i++;

            } // for

            nodes.remove(curr);
            i--;

        } // for
        System.out.println(nodes);
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
