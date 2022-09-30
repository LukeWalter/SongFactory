package songfactory.ui.notation;

import songfactory.music.MusicNode;

import java.awt.*;
import javax.swing.*;

public class JMusicNode extends JComponent {

    protected static final Image TREBLE_CLEF = (new ImageIcon("res/trebleClef.png")).getImage();
    protected static final Image COMMON_TIME = (new ImageIcon("res/commonTime.png")).getImage();

    protected static final Image FLAT = (new ImageIcon("res/flat.png")).getImage();
    protected static final Image SHARP = (new ImageIcon("res/sharp.png")).getImage();
    protected static final Image NATURAL = (new ImageIcon("res/natural.png")).getImage();

    protected static final Image SIXTEENTH_NOTE = (new ImageIcon("res/sixteenthNote.png")).getImage();
    protected static final Image EIGHTH_NOTE = (new ImageIcon("res/eighthNote.png")).getImage();
    protected static final Image QUARTER_NOTE = (new ImageIcon("res/quarterNote.png")).getImage();
    protected static final Image HALF_NOTE = (new ImageIcon("res/halfNote.png")).getImage();
    protected static final Image WHOLE_NOTE = (new ImageIcon("res/wholeNote.png")).getImage();

    protected static final Image SIXTEENTH_REST = (new ImageIcon("res/sixteenthRest.png")).getImage();
    protected static final Image EIGHTH_REST = (new ImageIcon("res/eighthRest.png")).getImage();
    protected static final Image QUARTER_REST = (new ImageIcon("res/quarterRest.png")).getImage();
    protected static final Image HALF_REST = (new ImageIcon("res/halfRest.png")).getImage();
    protected static final Image WHOLE_REST = (new ImageIcon("res/wholeRest.png")).getImage();

    protected int xOffset;
    protected int yOffset;
    protected Image image;
    protected Dimension size;
    protected Dimension dimensions;

    protected MusicNode nodeRef;

    public JMusicNode(Image image, int xOffset, int yOffset, Dimension size) {

        super();

        this.image = image;
        this.xOffset = xOffset;
        this.yOffset = yOffset;

        this.dimensions = new Dimension(this.image.getWidth(this), this.image.getHeight(this));
        this.size = size;

    } // JMusicNode

    public int getXOffset() {
        return xOffset;

    } // getXOffset

    public int getYOffset() {
        return yOffset;

    } // getYOffset

    public MusicNode getNodeRef() {
        return nodeRef;

    } // getNodeRef

    public void setNodeRef(MusicNode nodeRef) {
        this.nodeRef = nodeRef;

    } // setNodeRef

    public void paintNode(Graphics g) {
//        super.paintComponent(g);
        g.drawImage(image, this.getX() - xOffset, this.getY() - yOffset, null);

    } // paintComponent

    public boolean containsX(int x) {

        int minX = this.getX() - (xOffset / 2);
        int maxX = this.getX() + (xOffset / 2);

        return (x >= minX && x <= maxX);

    } // inXRange

    public boolean containsY(int y) {

        int minY = this.getY() - (yOffset / 2);
        int maxY = this.getY() + (yOffset / 2);

        return (y >= minY && y <= maxY);

    } // inXRange

    public boolean containsPoint(Point p) {
        return (this.containsX((int)(p.getX())) && this.containsY((int)(p.getY())));

    } // inBoundingBox

    @Override
    public String toString() {
        return "Basic JMusicNode";

    } // toString

} // JMusicNode