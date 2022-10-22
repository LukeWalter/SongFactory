package songfactory.music;

import java.util.LinkedList;
import java.util.List;

/**
 * MusicSequence class. Contains a list
 * of music node objects that can be easily
 * stored and altered.
 */
public class MusicSequence extends LinkedList<MusicNode> {

    /**
     * Converts a list of measures to a music sequence.
     *
     * @param measures input measure list
     *
     * @return music sequence containing all nodes from measure list
     */
    public static MusicSequence getAsSequence(List<Measure> measures) {

        MusicSequence output = new MusicSequence();

        for (Measure m : measures) {
            output.addAll(m.getNodes());

        } // for

        return output;

    } // getAsSequence

    /**
     * MusicSequence constructor.
     */
    public MusicSequence() {
        super();

    } // Constructor

    /**
     * Adds every node from another music
     * sequence into this music sequence.
     *
     * @param seq other music sequence
     */
    public void addAll(MusicSequence seq) {

        for (MusicNode n : seq) {
            this.add(n);

        } // for

    } // addAll

    /**
     * Calculates the length of this music sequence.
     *
     * @return sum of lengths of all nodes in sequence
     */
    public double getTotalNodeLength() {

        double sum = 0;

        for (int i = 0; i < this.size(); i++) {
            sum += this.get(i).getLength();

        } // for

        return sum;

    } // getTotalNoteLength

    /**
     * isEmpty() implementation.
     *
     * @return true if no notes or entirely full of rests, false otherwise
     */
    @Override
    public boolean isEmpty() {

        if (this.size() == 0) return true;

        for (MusicNode n : this) {
            if (n.getNote(0) != Note.REST) return false;

        } // for

        return true;

    } // isEmpty

} // MusicSequence