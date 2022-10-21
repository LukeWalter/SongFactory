package songfactory.ui.notation;

import songfactory.music.Accidental;

import java.awt.Dimension;

/**
 * Natural symbol.
 */
public class Natural extends JAccidental {

    /**
     * Natural constructor.
     */
    public Natural() {
        super(NATURAL, 17, 13, new Dimension(15, 15));
        accidental = Accidental.NATURAL;

    } // Constructor

} // Natural