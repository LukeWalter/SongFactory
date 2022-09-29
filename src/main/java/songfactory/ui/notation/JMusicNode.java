package songfactory.ui.notation;

import java.awt.*;
import javax.swing.*;

public abstract class JMusicNode extends JComponent {

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
    protected Dimension dimensions;

    public JMusicNode(Image image, int xOffset, int yOffset) {

        super();

        this.image = image;
        this.xOffset = xOffset;
        this.yOffset = yOffset;

        this.dimensions = new Dimension(this.image.getWidth(this), this.image.getHeight(this));

    } // JMusicNode

    public void paintNode(Graphics g, int x, int y) {
        super.paintComponent(g);
        g.drawImage(image, x - xOffset, y - yOffset, null);

    } // paintComponent

} // JMusicNode