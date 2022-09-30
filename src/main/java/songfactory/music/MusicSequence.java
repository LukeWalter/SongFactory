package songfactory.music;

import java.util.LinkedList;
import java.util.List;

public class MusicSequence extends LinkedList<MusicNode> {

    public static MusicSequence getAsSequence(List<Measure> measures) {

        MusicSequence output = new MusicSequence();

        for (Measure m : measures) {
            output.addAll(m.getNodes());

        } // for

        return output;

    } // getAsSequence

    public MusicSequence() {
        super();

    } // Constructor

    public void addAll(MusicNode ... nodes) {

        for (MusicNode n : nodes) {
            this.add(n);

        } // for

    } // nodes

    public void addAll(MusicSequence seq) {

        for (MusicNode n : seq) {
            this.add(n);

        } // for

    } // addAll

    public double getTotalNodeLength() {

        double sum = 0;

        for (int i = 0; i < this.size(); i++) {
            sum += this.get(i).getLength();

        } // for

        return sum;

    } // getTotalNoteLength

    @Override
    public boolean isEmpty() {

        if (this.size() == 0) return true;

        for (MusicNode n : this) {
            if (n.getNote() != Note.REST) return false;

        } // for

        return true;

    } // isEmpty

} // MusicSequence