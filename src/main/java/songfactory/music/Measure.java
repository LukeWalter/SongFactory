package songfactory.music;

import java.util.HashMap;
import java.util.List;

/**
 * Measure class. Stores a music sequence and
 * alters it based on measure length, clef,
 * and key signature.
 */
public class Measure {

    // Key signature: not implemented
    public static final Note[] orderOfFlats = {Note.B, Note.E, Note.A, Note.D, Note.G, Note.C, Note.F};
    private List<Note> flatNotes;
    private List<Note> sharpNotes;


    private TimeSignature time; // Time signature of measure
    private int key; // Key signature: not implemented

    private MusicSequence nodes; // MusicSequence in measure

    /**
     * Measure constructor.
     */
    public Measure() {

        this.time = new TimeSignature(4, 4); // Set time signature to 4/4 (common time)
        this.key = 0; // Set key signature to no flats, no sharps

        // Set up music sequence with whole rest
        this.nodes = new MusicSequence();
        this.nodes.add(new MusicNode(Note.REST, 1.0));

    } // Constructor

    /**
     * Measure constructor.
     *
     * @param time time signature
     * @param key key signature
     */
    public Measure(TimeSignature time, int key) {

        this.time = time;
        this.key = key;

        this.nodes = new MusicSequence();
        this.nodes.add(new MusicNode(Note.REST, this.time.getMeasureLength()));

    } // Constructor

    /**
     * Measure constructor.
     *
     * @param time time signature
     * @param key key signature
     * @param seq preset music sequence
     */
    public Measure(TimeSignature time, int key, MusicSequence seq) {

        this.time = time;
        this.key = key;

        this.processSequence(seq);

    } // Constructor

    /**
     * Returns the music sequence stored
     * in this measure.
     *
     * @return music sequence
     */
    public MusicSequence getNodes() {
        return nodes;

    } // getNodes

    /**
     * Takes in a music sequence and adds as
     * many of the nodes to the measure's
     * internal music sequence as can fit.
     *
     * @param seq input music sequence
     * @return remaining nodes in the original sequence
     */
    public MusicSequence processSequence(MusicSequence seq) {

        if (seq == null) {
            return null;

        } // if

        double remaining = time.getMeasureLength();
        MusicSequence newNodes = new MusicSequence();

        for (int i = 0; i < seq.size(); i++) {

            // End the loop immediately if there is no space left
            if (remaining <= 0) {
                break;

            // Add the next node in the sequence while space remains
            } else if (seq.get(i).getLength() <= remaining) {

                newNodes.add(seq.get(i));
                remaining -= seq.get(i).getLength();

                seq.remove(i);
                i--;

            // If the final node is too large to be stored in the remaining space
            } else {

                double leftover = seq.get(i).getLength() - remaining;

                // Take a section of the note that can fit in the measure
                MusicNode broken1 = new MusicNode(seq.get(i));
                broken1.setLength(remaining);

                if (Conversion.noteTable.containsKey(remaining)) {
                    // Add note section to measure
                    newNodes.add(broken1);

                } else {
                    // Split note if its length is not recorded in the lookup table
                    newNodes.addAll(broken1.split());

                } // if

                remaining = 0;

                // Add the remaining section of the note back into the sequence
                MusicNode broken2 = new MusicNode(seq.get(i));
                broken2.setLength(leftover);

                seq.remove(i);

                if (Conversion.noteTable.containsKey(leftover)) {
                    seq.add(0, broken2);

                } else {
                    seq.addAll(0, broken2.split());

                } // if

                i--;

            } // if

        } // for

        // Add filler rests if music sequence is too short to fill measure
        if (remaining > 0) {
            newNodes.addAll((new MusicNode(Note.REST, remaining)).split());

        } // if

        // Combine rests where possible
        nodes = newNodes;
        this.combine();
        return seq;

    } // processSequence

    /**
     * Searches for contiguous rests
     * and combines them if applicable.
     */
    public void combine() {

        // Store first instance of any contiguous section of nodes
        HashMap<Integer, Integer> startIndexes = new HashMap<>();
        startIndexes.put(0, 0);

        for (int i = 1; i < this.size(); i++) {

            if (nodes.get(i).getNote() != nodes.get(i - 1).getNote()) {
                startIndexes.put(i, i);

            } // if

        } // for

        int sIndex = 0;

        MusicNode start;
        MusicNode curr;

        for (int i = 1; i < nodes.size(); i++) {

            start = nodes.get(sIndex);
            curr = nodes.get(i);

            // If current node is in a different group of nodes
            if (curr.getNote() != start.getNote()) {
                // Set it as the new starting point
                sIndex = i;

            // If current node is part of a contiguous set of rests
            } else if (start.getNote() == Note.REST) {
                // Add its value to the starting point and remove it
                start.setLength(start.getLength() + curr.getLength());
                nodes.remove(curr);
                i--;

            } // if

        } // for

        // Split any nodes of invalid length
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

    } // combine

    /**
     * Returns the number of nodes stored
     * in the MusicSequence.
     *
     * @return number of nodes in measure
     */
    public int size() {
        return nodes.size();

    } // size

    /**
     * Returns the length of the measure as
     * dictated by the time signature.
     *
     * @return length of measure
     */
    public double length() {
        return time.getMeasureLength();

    } // length

    /**
     * toString() implementation.
     *
     * @return String representation of Measure.
     */
    public String toString() {
        return nodes.toString();

    } // toString

} // Measure