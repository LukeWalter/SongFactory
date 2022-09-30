package songfactory.music;

import songfactory.ui.MusicView;
import songfactory.ui.notation.*;

import java.awt.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class MusicNode {

    private Note note;
    private Accidental accidental;

    private int octave;
    private double length;

    private JMusicNode image;

    public MusicNode(Note note, double length) {
        this(note, length, 4, null);

    } // Constructor

    public MusicNode(Note note, double length, int octave) {
        this(note, length, octave, null);

    } // Constructor

    public MusicNode(Note note, double length, int octave, Accidental accidental) {

        this.note = note;
        this.length = length;
        this.octave = octave;
        this.accidental = accidental;
        if (Conversion.noteTable.containsKey(this.length)) this.updateImage();

    } // Constructor

    public MusicNode(MusicNode other) {

        if (other != null) {
            this.note = other.note;
            this.length = other.length;
            this.octave = other.octave;
            this.accidental = other.accidental;

        } // if

        if (Conversion.noteTable.containsKey(this.length)) this.updateImage();

    } // Constructor

    public Note getNote() {
        return note;

    } // getNote

    public void setNote(Note note) {
        this.note = note;

    } // setNote

    public void incrementNote() throws RestException {

        if (note == Note.REST) {
            throw new RestException("Rests cannot be incremented.");

        } else if (note == Note.B) {
            note = Note.C;
            octave += 1;

        } else {
            note = Note.values()[note.ordinal() + 1];

        } // if

    } // incrementNote

    public void decrementNote() throws RestException {

        if (note == Note.REST) {
            throw new RestException("Rests cannot be decremented.");

        } else if (note == Note.C) {
            note = Note.B;
            octave -= 1;

        } else {
            note = Note.values()[note.ordinal() - 1];

        } // if

    } // decrementNote

    public Accidental getAccidental() {
        return accidental;

    } // getAccidental

    public void setAccidental(Accidental accidental) {
        this.accidental = accidental;
        updateImage();

    } // setAccidental

    public void incrementSharp() throws RestException {

        if (note == Note.REST) {
            throw new RestException("Rests cannot have accidentals.");

        } else if (accidental == null) {
            accidental = Accidental.SHARP;

        } else if (accidental == Accidental.DOUBLE_SHARP) {
            accidental = null;

        } else {
            accidental = Accidental.values()[accidental.ordinal() + 1];

        } // if

    } // incrementAccidental

    public void incrementFlat() throws RestException {

        if (note == Note.REST) {
            throw new RestException("Rests cannot have accidentals.");

        } else if (accidental == null) {
            accidental = Accidental.FLAT;

        } else if (accidental == Accidental.DOUBLE_FLAT) {
            accidental = null;

        } else {
            accidental = Accidental.values()[accidental.ordinal() - 1];

        } // if

    } // incrementFlat

    public int getOctave() {
        return octave;

    } // getOctave

    public void setOctave(int octave) {
        this.octave = octave;

    } // setOctave

    public double getLength() {
        return length;

    } // getLength

    public void setLength(double length) {
        this.length = length;
        updateImage();

    } // setLength

    public JMusicNode getImage() {
        return image;

    } // getImage

    private void updateImage() {

        if (!Conversion.noteTable.containsKey(length)) {
            return;

        } // if

        if (note == Note.REST) {
            image = JMusicNodeFactory.createRest(length);

        } else {
            JNote noteImage = JMusicNodeFactory.createNote(length);
            noteImage.setAccidental(JMusicNodeFactory.createAccidental(accidental));
            image = noteImage;

        } // if

    } // updateImage

    public List<MusicNode> split() {

        LinkedList<MusicNode> broken = new LinkedList<>();

        if (Conversion.noteTable.containsKey(length)) {
            broken.add(this);
            return broken;

        } // if

        Double[] keys = Conversion.noteTable.keySet().toArray(Double[]::new);
        double lengthOne = extractNode(keys, length);
        double lengthTwo = length - lengthOne;


        MusicNode n1 = new MusicNode(this);
        n1.setLength(lengthOne);

        MusicNode n2 = new MusicNode(this);
        n2.setLength(lengthTwo);

        if (Conversion.noteTable.containsKey(lengthOne)) {
//            System.out.println("Note length of " + n1.getLength() + " exists!");
            broken.addFirst(n1);

        } else {

//            System.out.println("Splitting note length of " + n1.getLength() + "...");
            broken.addAll(0, n1.split());

        } // if

        if (Conversion.noteTable.containsKey(lengthTwo)) {
//            System.out.println("Note length of " + n2.getLength() + " exists!");
            broken.addFirst(n2);

        } else {

//            System.out.println("Splitting note length of " + n2.getLength() + "...");
            broken.addAll(0, n2.split());

        } // if

//        System.out.println(this + " -> " + broken);
        return broken;

    } // split

    private double extractNode(Double[] keys, double target) {

        double solution = 0;

        for (int i = keys.length - 1; i >= 0; i--) {

            if (keys[i] < target) {
                solution = keys[i];
                break;

            } // if

        } // for

        return solution;

    } // twoSum

    @Override
    public boolean equals(Object o) {

        if (!(o instanceof MusicNode)) return false;
        MusicNode other = (MusicNode) o;

        return (this.getNote() == other.getNote())
                && (this.getLength() == other.getLength())
                && (this.getOctave() == other.getOctave())
                && (this.getAccidental() == other.getAccidental());

    } // equals

    @Override
    public String toString() {
        return "" + note + " " + length + " " + octave + " " + accidental;

    } // toString

} // MusicNode
