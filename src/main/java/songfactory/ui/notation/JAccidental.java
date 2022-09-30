package songfactory.ui.notation;

import java.awt.*;

public class JAccidental extends JMusicNode {

    public JAccidental(Image image, int xOffset, int yOffset, Dimension size) {
        super(image, xOffset, yOffset, size);

    } // JAccidental

    @Override
    public String toString() {
        return "Basic JAccidental";

    } // toString

} // JAccidental
