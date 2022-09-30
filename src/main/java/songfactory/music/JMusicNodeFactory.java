package songfactory.music;

import songfactory.ui.notation.*;
import java.util.HashMap;

public class JMusicNodeFactory {

    public static JNote createNote(double length) {
        return (Conversion.noteTable.get(length)).make();

    } // createNote

    public static JRest createRest(double length) {
        return (Conversion.restTable.get(length)).make();

    } // createRest

    public static JAccidental createAccidental(Accidental accidental) {
        if (accidental != null) return (Conversion.accidentalTable.get(accidental)).make();
        else return null;

    } // createAccidental

} // JMusicNodeFactory
