package songfactory.ui.notation;

import songfactory.music.Accidental;

import java.awt.*;

/**
 * General class for accidental symbols.
 */
public class JAccidental extends JMusicNode {

    protected Accidental accidental;

    /**
     * JAccidental constructor.
     *
     * @param image image displayed on MusicVies
     * @param xOffset distance from left side to true middle of image
     * @param yOffset distance from top side to true middle of image
     * @param size bounding box for image
     */
    public JAccidental(Image image, int xOffset, int yOffset, Dimension size) {
        super(image, xOffset, yOffset, size);

    } // JAccidental

    public Accidental getAccidental() {
        return accidental;

    } // getAccidental

    /**
     * toString() implementation.
     *
     * @return String representation of JAccidental
     */
    @Override
    public String toString() {
        return "Basic JAccidental";

    } // toString

} // JAccidental