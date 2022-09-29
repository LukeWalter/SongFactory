package songfactory.ui.notation;

import java.awt.*;
import javax.swing.*;

public abstract class JMusicNode extends JComponent {

    protected static Image trebleClefImage = (new ImageIcon("res/trebleClef.png")).getImage();
    protected static Image commonTimeImage = (new ImageIcon("res/commonTime.png")).getImage();

    protected static Image flatImage = (new ImageIcon("res/flat.png")).getImage();
    protected static Image sharpImage = (new ImageIcon("res/sharp.png")).getImage();
    protected static Image naturalImage = (new ImageIcon("res/natural.png")).getImage();

    protected static Image sixteenthNoteImage = (new ImageIcon("res/sixteenthNote.png")).getImage();
    protected static Image eighthNoteImage = (new ImageIcon("res/eighthNote.png")).getImage();
    protected static Image quarterNoteImage = (new ImageIcon("res/quarterNote.png")).getImage();
    protected static Image halfNoteImage = (new ImageIcon("res/halfNote.png")).getImage();
    protected static Image wholeNoteImage = (new ImageIcon("res/wholeNote.png")).getImage();

    protected static Image sixteenthRestImage = (new ImageIcon("res/sixteenthRest.png")).getImage();
    protected static Image eightRestImage = (new ImageIcon("res/eighthRest.png")).getImage();
    protected static Image quarterRestImage = (new ImageIcon("res/quarterRest.png")).getImage();
    protected static Image halfRestImage = (new ImageIcon("res/halfRest.png")).getImage();
    protected static Image wholeRestImage = (new ImageIcon("res/wholeRest.png")).getImage();

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