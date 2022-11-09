package songfactory.music;

import songfactory.ui.notation.*;
import java.util.HashMap;

/**
 * Conversion class. Stores useful lookup
 * tables for converting note lengths to
 * note types and vice versa.
 */
public class Conversion {
    protected interface INote { JNote make(); } // Functional interface for note constructors
    protected interface IRest { JRest make(); } // Functional interface for rest constructors
    protected interface IAccidental { JAccidental make(); } // Functional interface for accidental constructors
    public interface INode { JMusicNode make(); } // Functional interface for JMusicNode constructors

    private static boolean initialized = false;

    public static HashMap<Double, INote> noteTable; // Maps node lengths to note types
    public static HashMap<Double, IRest> restTable; // Maps node lengths to rest types
    public static HashMap<Accidental, IAccidental> accidentalTable; // Maps accidental enums to accidental types

    public static HashMap<String, INode> recognitionTable; // Maps recognizer names to JMusicNode types

    /**
     * Initializes and populates the lookup tables.
     */
    public static void initialize() {

        if (initialized) return;

        noteTable = new HashMap<>();
        noteTable.put(0.0625, SixteenthNote::new);
        noteTable.put(0.125, EighthNote::new);
        noteTable.put(0.25, QuarterNote::new);
        noteTable.put(0.5, HalfNote::new);
        noteTable.put(1.0, WholeNote::new);

        restTable = new HashMap<>();
        restTable.put(0.0625, SixteenthRest::new);
        restTable.put(0.125, EighthRest::new);
        restTable.put(0.25, QuarterRest::new);
        restTable.put(0.5, HalfRest::new);
        restTable.put(1.0, WholeRest::new);

        accidentalTable = new HashMap<>();
        accidentalTable.put(Accidental.FLAT, Flat::new);
        accidentalTable.put(Accidental.NATURAL, Natural::new);
        accidentalTable.put(Accidental.SHARP, Sharp::new);

        recognitionTable = new HashMap<>();
        recognitionTable.put("circle", WholeNote::new);
        recognitionTable.put("half note", HalfNote::new);
        recognitionTable.put("quarter note", QuarterNote::new);
        recognitionTable.put("eighth note", EighthNote::new);
        recognitionTable.put("sixteenth note", SixteenthNote::new);
        recognitionTable.put("rectangle", WholeRest::new);
        recognitionTable.put("half rest", HalfRest::new);
        recognitionTable.put("right curly brace", QuarterRest::new);
        recognitionTable.put("eighth rest", EighthRest::new);
        recognitionTable.put("sixteenth rest", SixteenthRest::new);
        recognitionTable.put("flat", Flat::new);
        recognitionTable.put("star", Sharp::new);

    } // initialize

    /**
     * Switches the keys and values in a hashmap.
     *
     * @param original hashmap to be reversed
     * @param <K> key data type for original hashmap
     * @param <V> value data type for original hashmap
     *
     * @return new hashmap with reversed entries
     */
    public static <K, V> HashMap<V, K> reverse(HashMap<K, V> original) {

        HashMap<V, K> reversed = new HashMap<>();
        for (HashMap.Entry<K, V> entry : original.entrySet()) {
            reversed.put(entry.getValue(), entry.getKey());

        } // for

        return reversed;

    } // reverse

} // Conversion