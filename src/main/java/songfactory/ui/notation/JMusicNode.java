package songfactory.ui.notation;

import songfactory.music.MusicNode;

import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * Visual representation of a musical symbol.
 */
public class JMusicNode extends JComponent {

    private static boolean initialized;

    // Library of images used by extending classes
    protected static Image TREBLE_CLEF, COMMON_TIME;
    protected static Image FLAT, SHARP, NATURAL;
    protected static Image SIXTEENTH_NOTE, EIGHTH_NOTE, QUARTER_NOTE, HALF_NOTE, WHOLE_NOTE;
    protected static Image SIXTEENTH_REST, EIGHTH_REST, QUARTER_REST, HALF_REST, WHOLE_REST;

    protected int xOffset; // Distance from left side to true middle of image
    protected int yOffset; // Distance from top side to true middle of image
    protected Image image; // Image displayed on MusicView
    protected Dimension size; // Bounding box for image

    protected MusicNode nodeRef; // Reference to back-end data representation of node

    /**
     * Initializes the note, rest, accidental, and other
     * images for the staff.
     */
    public static void initializeImages() {

        if (!initialized) {

            try {

                TREBLE_CLEF = ImageIO.read(ClassLoader.getSystemResource("trebleClef.png"));
                COMMON_TIME = ImageIO.read(ClassLoader.getSystemResource("commonTime.png"));

                FLAT = ImageIO.read(ClassLoader.getSystemResource("flat.png"));
                SHARP = ImageIO.read(ClassLoader.getSystemResource("sharp.png"));
                NATURAL = ImageIO.read(ClassLoader.getSystemResource("natural.png"));

                SIXTEENTH_NOTE = ImageIO.read(ClassLoader.getSystemResource("sixteenthNote.png"));
                EIGHTH_NOTE = ImageIO.read(ClassLoader.getSystemResource("eighthNote.png"));
                QUARTER_NOTE = ImageIO.read(ClassLoader.getSystemResource("quarterNote.png"));
                HALF_NOTE = ImageIO.read(ClassLoader.getSystemResource("halfNote.png"));
                WHOLE_NOTE = ImageIO.read(ClassLoader.getSystemResource("wholeNote.png"));

                SIXTEENTH_REST = ImageIO.read(ClassLoader.getSystemResource("sixteenthRest.png"));
                EIGHTH_REST = ImageIO.read(ClassLoader.getSystemResource("eighthRest.png"));
                QUARTER_REST = ImageIO.read(ClassLoader.getSystemResource("quarterRest.png"));
                HALF_REST = ImageIO.read(ClassLoader.getSystemResource("halfRest.png"));
                WHOLE_REST = ImageIO.read(ClassLoader.getSystemResource("wholeRest.png"));

                initialized = true;

            } catch (IOException ioe) {
                System.out.println("There was an issue with your imports.");
                System.exit(0);

            } catch (IllegalArgumentException iae) {
                System.out.println("There was an issue with your imports.");
                System.exit(0);

            } // try

        } // if

    } // initializeImages

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

        initializeImages();

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