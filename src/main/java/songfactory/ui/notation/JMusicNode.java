package songfactory.ui.notation;

import songfactory.music.MusicNode;

import java.awt.*;
import javax.swing.*;

/**
 * Visual representation of a musical symbol.
 */
public class JMusicNode extends JComponent {

    // Library of images used by extending classes
    protected static final Image TREBLE_CLEF = (new ImageIcon("src/main/resources/trebleClef.png")).getImage();
    protected static final Image COMMON_TIME = (new ImageIcon("src/main/resources/commonTime.png")).getImage();

    protected static final Image FLAT = (new ImageIcon("src/main/resources/flat.png")).getImage();
    protected static final Image SHARP = (new ImageIcon("src/main/resources/sharp.png")).getImage();
    protected static final Image NATURAL = (new ImageIcon("src/main/resources/natural.png")).getImage();

    protected static final Image SIXTEENTH_NOTE = (new ImageIcon("src/main/resources/sixteenthNote.png")).getImage();
    protected static final Image EIGHTH_NOTE = (new ImageIcon("src/main/resources/eighthNote.png")).getImage();
    protected static final Image QUARTER_NOTE = (new ImageIcon("src/main/resources/quarterNote.png")).getImage();
    protected static final Image HALF_NOTE = (new ImageIcon("src/main/resources/halfNote.png")).getImage();
    protected static final Image WHOLE_NOTE = (new ImageIcon("src/main/resources/wholeNote.png")).getImage();

    protected static final Image SIXTEENTH_REST = (new ImageIcon("src/main/resources/sixteenthRest.png")).getImage();
    protected static final Image EIGHTH_REST = (new ImageIcon("src/main/resources/eighthRest.png")).getImage();
    protected static final Image QUARTER_REST = (new ImageIcon("src/main/resources/quarterRest.png")).getImage();
    protected static final Image HALF_REST = (new ImageIcon("src/main/resources/halfRest.png")).getImage();
    protected static final Image WHOLE_REST = (new ImageIcon("src/main/resources/wholeRest.png")).getImage();

    protected int xOffset; // Distance from left side to true middle of image
    protected int yOffset; // Distance from top side to true middle of image
    protected Image image; // Image displayed on MusicView
    protected Dimension size; // Bounding box for image

    protected MusicNode nodeRef; // Reference to back-end data representation of node

    /**
     * JMusicNode constructor.
     *
     * @param image image displayed on MusicView
     * @param xOffset distance from left side to true middle of image
     * @param yOffset distance from top side to true middle of image
     * @param size bounding box for image
     */
    public JMusicNode(Image image, int xOffset, int yOffset, Dimension size) {

        super();

        this.image = image;
        this.xOffset = xOffset;
        this.yOffset = yOffset;

        this.size = size;

    } // JMusicNode

    /**
     * Returns the value of xOffset.
     *
     * @return xOffset
     */
    public int getXOffset() {
        return xOffset;

    } // getXOffset

    /**
     * Returns the value of yOffset.
     *
     * @return yOffset
     */
    public int getYOffset() {
        return yOffset;

    } // getYOffset

    /**
     * Returns the reference to the back-end data node.
     *
     * @return nodeRef
     */
    public MusicNode getNodeRef() {
        return nodeRef;

    } // getNodeRef

    /**
     * Attaches a back-end data node to this object.
     */
    public void setNodeRef(MusicNode nodeRef) {
        this.nodeRef = nodeRef;

    } // setNodeRef

    /**
     * Draws the image to the screen at coordinates
     * based on internal position and offset values.
     *
     * @param g graphics object
     */
    public void paintNode(Graphics g) {
//        super.paintComponent(g);
        g.drawImage(image, this.getX() - xOffset, this.getY() - yOffset, null);

    } // paintComponent

    /**
     * Checks if an x value is within the x range of
     * the bounding box.
     *
     * @param x x position
     * @return true if x position is in range, false otherwise
     */
    public boolean containsX(int x) {

        int minX = this.getX() - (int)(size.getWidth() / 2);
        int maxX = this.getX() + (int)(size.getWidth() / 2);

        return (x >= minX && x <= maxX);

    } // inXRange

    /**
     * Checks if a y value is within the x range of
     * the bounding box.
     *
     * @param y y position
     * @return true if y position is in range, false otherwise
     */
    public boolean containsY(int y) {

        int minY = this.getY() - (int)(size.getHeight() / 2);
        int maxY = this.getY() + (int)(size.getHeight() / 2);

        return (y >= minY && y <= maxY);

    } // inXRange

    /**
     * Checks if a point is inside the bounding box for the image.
     *
     * @param p point being tested
     * @return true if point is inside bounding box, false otherwise
     */
    public boolean containsPoint(Point p) {
        return (this.containsX((int)(p.getX())) && this.containsY((int)(p.getY())));

    } // inBoundingBox

    /**
     * toString() implementation.
     *
     * @return String representation of JMusicNode
     */
    @Override
    public String toString() {
        return "Basic JMusicNode";

    } // toString

} // JMusicNode