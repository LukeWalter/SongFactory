package songfactory.music;

import songfactory.ui.notation.*;
import java.util.HashMap;

public class JMusicNodeFactory {

    private interface INote { JNote make(); } // INote
    private interface IRest { JRest make(); } // IRest
    private interface IAccidental { JAccidental make(); } // IAccidental

    private static boolean initialized = false;

    public static HashMap<Double, INote> noteTable;
    public static HashMap<Double, IRest> restTable;
    public static HashMap<Accidental, IAccidental> accidentalTable;

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

    } // initialize

    public static JNote createNote(double length) {
        return (noteTable.get(length)).make();

    } // createNote

    public static JRest createRest(double length) {
        return (restTable.get(length)).make();

    } // createNote

    public static JAccidental createAccidental(Accidental accidental) {
        if (accidental != null) return (accidentalTable.get(accidental)).make();
        else return null;

    } // createNote

} // JMusicNodeFactory
