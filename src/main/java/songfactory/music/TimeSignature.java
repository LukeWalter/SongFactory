package songfactory.music;

public class TimeSignature {

    private int beatUnit;
    private int beatsPerBar;

    private double length;

    public TimeSignature(int beatUnit, int beatsPerBar) {
        this.beatUnit = beatUnit;
        this.beatsPerBar = beatsPerBar;
        this.length = this.calculateMeasureLength();

    } // Constructor

    private double calculateMeasureLength() {
        return (double) beatUnit / (double) beatsPerBar;

    } // getMeasureLength

    public double getMeasureLength() {
        return length;

    } // getLength

    public String toString() {
        return beatUnit + " / " + beatsPerBar;

    } // toString

} // TimeSignature
