package songfactory.music;

/**
 * Time signature class. Represents
 * the length and rhythm of a measure.
 */
public class TimeSignature {

    private int beatUnit; // Type of note that gets the beat
    private int beatsPerBar; // Number of unit beats in a measure

    private double length; // Numerical length of a measure

    /**
     * TimeSignature constructor.
     *
     * @param beatUnit type of note that gets the beat
     * @param beatsPerBar number of unit beats in a measure
     */
    public TimeSignature(int beatUnit, int beatsPerBar) {
        this.beatUnit = beatUnit;
        this.beatsPerBar = beatsPerBar;
        this.length = this.calculateMeasureLength();

    } // Constructor

    /**
     * Calculates the numerical length
     * of the time signature. Should be
     * called whenever the values of the
     * time signature change.
     *
     * @return length
     */
    private double calculateMeasureLength() {
        return (double) beatUnit / (double) beatsPerBar;

    } // getMeasureLength

    /**
     * Returns numerical length of the time signature.
     *
     * @return length
     */
    public double getMeasureLength() {
        return length;

    } // getLength

    /**
     * toString() implementation.
     *
     * @return String representation of TimeSignature.
     */
    public String toString() {
        return beatUnit + " / " + beatsPerBar;

    } // toString

} // TimeSignature