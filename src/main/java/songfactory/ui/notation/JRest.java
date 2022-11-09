package songfactory.ui.notation;

import java.awt.*;

/**
 * General class for rest symbols.
 */
public class JRest extends JMusicNode {

    double length; // Length of associated MusicNode

    /**
     * JRest constructor.
     *
     * @param image image displayed on MusicVies
     * @param xOffset distance from left side to true middle of image
     * @param yOffset distance from top side to true middle of image
     * @param size bounding box for image
     */
    public JRest(Image image, double length, int xOffset, int yOffset, Dimension size) {
        super(image, xOffset, yOffset, size);
        this.length = length;

    } // JRest

    /**
     * Returns the length of the associated MusicNode.
     *
     * @return length
     */
    public double getLength() {
        return length;

    } // getLength

    /**
     * toString() implementation.
     *
     * @return String representation of JRest
     */
    @Override
    public String toString() {
        return "Basic JRest";

    } // toString

} // JRest