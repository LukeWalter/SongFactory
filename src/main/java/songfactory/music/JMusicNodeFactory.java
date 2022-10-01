package songfactory.music;

import songfactory.ui.notation.*;

/**
 * JMusicNode factory class. Uses conversion
 * lookup tables to create nodes of given
 * lengths.
 */
public class JMusicNodeFactory {

    /**
     * Creates a node based on input length. Invalid
     * lengths not stored in the table return null.
     *
     * @param length input length
     *
     * @return new note
     */
    public static JNote createNote(double length) {
        return (Conversion.noteTable.get(length)).make();

    } // createNote

    /**
     * Creates a node based on input length. Invalid
     * lengths not stored in the table return null.
     *
     * @param length input length
     *
     * @return new rest
     */
    public static JRest createRest(double length) {
        return (Conversion.restTable.get(length)).make();

    } // createRest

    /**
     * Creates a node based on accidental enumeration.
     * Invalid lengths not stored in the table return null.
     *
     * @param accidental type of accidental to be created.
     *
     * @return new accidental
     */
    public static JAccidental createAccidental(Accidental accidental) {
        if (accidental != null) return (Conversion.accidentalTable.get(accidental)).make();
        else return null;

    } // createAccidental

} // JMusicNodeFactory