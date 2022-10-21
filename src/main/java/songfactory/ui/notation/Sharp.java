package songfactory.ui.notation;

import songfactory.music.Accidental;

import java.awt.Dimension;

/**
 * Sharp symbol.
 */
public class Sharp extends JAccidental {

    /**
     * Sharp constructor.
     */
    public Sharp() {
        super(SHARP, 17, 12, new Dimension(15, 15));
        accidental = Accidental.SHARP;

    } // Constructor

} // Sharp