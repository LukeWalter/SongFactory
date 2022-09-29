package songfactory.ui.notation;

import java.awt.*;
import javax.swing.*;

public abstract class JMusicNode extends JComponent {

    protected static Image trebleClefImage = (new ImageIcon("res/trebleClef.png")).getImage();

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