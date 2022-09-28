package songfactory.music;

public class MusicNode {

    private Note note;
    private Accidental accidental;

    private int octave;
    private double length;

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

    } // Constructor

    public MusicNode(MusicNode other) {
        this.note = other.note;
        this.length = other.length;
        this.octave = other.octave;
        this.accidental = other.accidental;

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

    } // setLength

    public String toString() {
        return "" + note + " " + length + " " + octave + " " + accidental;

    } // toString

} // MusicNode
