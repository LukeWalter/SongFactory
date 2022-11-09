package songfactory.ui.notation;

import java.awt.*;

/**
 * General class for note symbols.
 */
public class JNote extends JMusicNode {

    private JAccidental accidental; // Accidental for note
    private double length; // Length of associated MusicNode

    /**
     * JNote constructor.
     *
     * @param image image displayed on MusicVies
     * @param xOffset distance from left side to true middle of image
     * @param yOffset distance from top side to true middle of image
     * @param size bounding box for image
     */
    public JNote(Image image, double length, int xOffset, int yOffset, Dimension size) {
        super(image, xOffset, yOffset, size);
        this.length = length;

    } // JNote

    /**
     * Draws the note, and its accidental
     * if applicable.
     *
     * @param g graphics object
     */
    @Override
    public void paintNode(Graphics g) {
        g.drawImage(image, this.getX() - xOffset, this.getY() - yOffset, null);
        if (accidental != null) this.getAccidental().paintNode(g);

    } // paintNode

    /**
     * Sets the location of the note and its
     * accidental within MusicView.
     *
     * @param p coordinate position to be set to
     */
    @Override
    public void setLocation(Point p) {

        int x = (int)(p.getX());
        int y = (int)(p.getY());

        this.setLocation(x, y);
        this.setAccidentalLocation(x, y);

    } // setLocation

    /**
     * Returns the accidental stored in
     * the note.
     *
     * @return accidental
     */
    public JAccidental getAccidental() {
        return accidental;

    } // getAccidental

    /**
     * Sets the value of the accidental
     * stored in the note.
     *
     * @param accidental accidental to be stored
     */
    public void setAccidental(JAccidental accidental) {
        this.accidental = accidental;
        this.setAccidentalLocation(this.getX(), this.getY());

    } // setAccidental

    /**
     * Sets the accidental's location. Should only
     * be used in conjunction with functions that
     * alter the location of the note.
     *
     * @param x x position to move to
     * @param y y position to move to
     */
    private void setAccidentalLocation(int x, int y) {

        if (accidental != null) {
            accidental.setLocation(x, y);

        } // if

    } // setAccidentalLocation

    /**
     * Returns the length of the associated MusicNode.
     *
     * @return length
     */
    public double getLength() {
        return length;

    } // getLength

    /**
     * toString() implementation
     *
     * @return String representation of JNote
     */
    @Override
    public String toString() {
        return "Basic JNote";

    } // toString

} // JNote