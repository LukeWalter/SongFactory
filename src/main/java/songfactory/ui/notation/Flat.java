package songfactory.ui.notation;

import songfactory.music.Accidental;

import java.awt.Dimension;

/**
 * Flat symbol.
 */
public class Flat extends JAccidental {

    /**
     * Flat constructor.
     */
    public Flat() {
        super(FLAT, 17, 14, new Dimension(15, 15));
        accidental = Accidental.FLAT;

    } // Constructor

} // Flat