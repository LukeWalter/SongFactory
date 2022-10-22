package songfactory.music;

import songfactory.ui.notation.*;

import java.util.LinkedList;
import java.util.List;

/**
 * MusicNode class. Stores the underlying
 * data behind notes and rests.
 */
public class MusicNode {

    private List<Note> notes; // Note name
    private List<Accidental> accidentals; // Accidental stored by note

    private List<Integer> octaves; // Octave of note
    private double length; // Length of note

    private List<JMusicNode> images; // Visual representation of note

    /**
     * MusicNode constructor.
     *
     * @param note note name
     * @param length length of note
     */
    public MusicNode(Note note, double length) {
        this(note, length, 4, null);

    } // Constructor

    /**
     * MusicNode constructor.
     *
     * @param note note name
     * @param length length of note
     * @param octave octave of note
     */
    public MusicNode(Note note, double length, int octave) {
        this(note, length, octave, null);

    } // Constructor

    /**
     * MusicNode constructor.
     *
     * @param note note name
     * @param length length of note
     * @param octave octave of note
     * @param accidental accidental stored by note
     */
    public MusicNode(Note note, double length, int octave, Accidental accidental) {

        this.notes = new LinkedList<>();
        this.notes.add(note);

        this.length = length;

        this.octaves = new LinkedList<>();
        this.octaves.add(octave);

        this.accidentals = new LinkedList<>();
        this.accidentals.add(accidental);

        this.images = new LinkedList<>();
        images.add(null);
        this.updateImage();

    } // Constructor

    /**
     * MusicNode copy constructor.
     *
     * @param other MusicNode object being copied
     */
    public MusicNode(MusicNode other) {

        if (other != null) {
            this.notes = other.notes;
            this.length = other.length;
            this.octaves = other.octaves;
            this.accidentals = other.accidentals;
            this.updateImage();

        } // if

    } // Constructor

    /**
     * Returns note name.
     *
     * @return note name
     */
    public Note getNote() {
        return notes.get(0);

    } // getNote

    /**
     * Sets the note name.
     *
     * @param note new note name
     */
    public void setNote(Note note) {
        this.notes.set(0, note);

    } // setNote

//    /**
//     * Increases the pitch of a note by one whole step.
//     *
//     * @throws RestException if called on a rest
//     */
//    public void incrementNote() throws RestException {
//
//        if (note == Note.REST) {
//            throw new RestException("Rests cannot be incremented.");
//
//        } else if (note == Note.B) {
//            note = Note.C;
//            octave += 1;
//
//        } else {
//            note = Note.values()[note.ordinal() + 1];
//
//        } // if
//
//    } // incrementNote
//
//    /**
//     * Decreases the pitch of a note by one whole step.
//     *
//     * @throws RestException if called on a rest
//     */
//    public void decrementNote() throws RestException {
//
//        if (note == Note.REST) {
//            throw new RestException("Rests cannot be decremented.");
//
//        } else if (note == Note.C) {
//            note = Note.B;
//            octave -= 1;
//
//        } else {
//            note = Note.values()[note.ordinal() - 1];
//
//        } // if
//
//    } // decrementNote

    /**
     * Returns accidental stored by note.
     *
     * @return accidental
     */
    public Accidental getAccidental() {
        return accidentals.get(0);

    } // getAccidental

    /**
     * Sets accidental stored by note.
     *
     * @param accidental accidental to be stored
     */
    public void setAccidental(Accidental accidental) {
        this.accidentals.set(0, accidental);
        updateImage();

    } // setAccidental

//    /**
//     * Loops through different types of sharps that
//     * can be stored by a note.
//     *
//     * @throws RestException if called on a rest
//     */
//    public void incrementSharp() throws RestException {
//
//        if (note == Note.REST) {
//            throw new RestException("Rests cannot have accidentals.");
//
//        } else if (accidental == null) {
//            accidental = Accidental.SHARP;
//
//        } else if (accidental == Accidental.DOUBLE_SHARP) {
//            accidental = null;
//
//        } else {
//            accidental = Accidental.values()[accidental.ordinal() + 1];
//
//        } // if
//
//    } // incrementAccidental
//
//    /**
//     * Loops through different types of flats that
//     * can be stored by a note.
//     *
//     * @throws RestException if called on a rest
//     */
//    public void incrementFlat() throws RestException {
//
//        if (note == Note.REST) {
//            throw new RestException("Rests cannot have accidentals.");
//
//        } else if (accidental == null) {
//            accidental = Accidental.FLAT;
//
//        } else if (accidental == Accidental.DOUBLE_FLAT) {
//            accidental = null;
//
//        } else {
//            accidental = Accidental.values()[accidental.ordinal() - 1];
//
//        } // if
//
//    } // incrementFlat

    /**
     * Returns octave of note.
     *
     * @return octave
     */
    public int getOctave() {
        return octaves.get(0);

    } // getOctave

    public void setOctave(int octave) {
        this.octaves.set(0, octave);

    } // setOctave

    /**
     * Returns length of node.
     *
     * @return length
     */
    public double getLength() {
        return length;

    } // getLength

    public void setLength(double length) {
        this.length = length;
        updateImage();

    } // setLength

    /**
     * Returns visual representation of node.
     *
     * @return image
     */
    public JMusicNode getImage() {
        return images.get(0);

    } // getImage

    /**
     * Updates the image stored by the note to
     * accurately reflect data stored in this
     * MusicNode.
     */
    private void updateImage() {

        if (this.images == null) {
            this.images = new LinkedList<>();
            images.add(null);

        } // if

        // Abort if the node type does not exist
        if (!Conversion.noteTable.containsKey(length)) {
            return;

        } // if

        if (notes.get(0) == Note.REST) {
            // Set rest length
            images.set(0, JMusicNodeFactory.createRest(length));
            images.get(0).setNodeRef(this);

        } else {

            // Set note length and accidental
            JNote noteImage = JMusicNodeFactory.createNote(length);
            noteImage.setAccidental(JMusicNodeFactory.createAccidental(accidentals.get(0)));

            images.set(0, noteImage);
            images.get(0).setNodeRef(this);

        } // if

    } // updateImage

    /**
     * Splits a node of invalid length into a
     * set of valid nodes.
     *
     * @return list of valid nodes
     */
    public List<MusicNode> split() {

        LinkedList<MusicNode> broken = new LinkedList<>();

        // Abort if node type is valid
        if (Conversion.noteTable.containsKey(length)) {
            broken.add(this);
            return broken;

        } // if

        // Find the highest valid node length within original length, save remainder
        Double[] keys = Conversion.noteTable.keySet().toArray(Double[]::new);
        double lengthOne = this.extractNode(keys, length);
        double lengthTwo = length - lengthOne;

        // Split invalid node into two potentially valid nodes
        MusicNode n1 = new MusicNode(this);
        n1.setLength(lengthOne);

        MusicNode n2 = new MusicNode(this);
        n2.setLength(lengthTwo);

        // Split further if needed, add to solution list
        if (Conversion.noteTable.containsKey(lengthOne)) {
            broken.addFirst(n1);

        } else {
            broken.addAll(0, n1.split());

        } // if

        if (Conversion.noteTable.containsKey(lengthTwo)) {
            broken.addFirst(n2);

        } else {
            broken.addAll(0, n2.split());

        } // if

        return broken;

    } // split

    /**
     * Finds a valid note length within an
     * invalid note length. Should only be
     * called in combine().
     *
     * @param keys
     * @param target
     * @return
     */
    private double extractNode(Double[] keys, double target) {

        double solution = 0;

        // Searches lookup table keys for a maximum value below target
        for (int i = keys.length - 1; i >= 0; i--) {

            if (keys[i] < target) {
                solution = keys[i];
                break;

            } // if

        } // for

        return solution;

    } // extractNode

    /**
     * toString() implementation.
     *
     * @return String representation of MusicNode.
     */
    @Override
    public String toString() {
        return "" + notes.get(0) + " " + length + " " + octaves.get(0) + " " + accidentals.get(0);

    } // toString

} // MusicNode