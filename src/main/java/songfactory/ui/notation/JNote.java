package songfactory.ui.notation;

import java.awt.*;
import java.util.function.Function;

public class JNote extends JMusicNode {

    public JAccidental accidental;

    public JNote(Image image, int xOffset, int yOffset) {
        super(image, xOffset, yOffset);

    } // JNote

    @Override
    public void paintNode(Graphics g) {
        g.drawImage(image, this.getX() - xOffset, this.getY() - yOffset, null);
        if (accidental != null) this.getAccidental().paintNode(g);

    } // paintNode

    @Override
    public void setLocation(Point p) {

        int x = (int)(p.getX());
        int y = (int)(p.getY());

        this.setLocation(x, y);
        this.setAccidentalLocation(x, y);

    } // setLocation

    public JAccidental getAccidental() {
        return accidental;

    } // getAccidental

    public void setAccidental(JAccidental accidental) {
        this.accidental = accidental;
        this.setAccidentalLocation(this.getX(), this.getY());

    } // setAccidental

    public void setAccidentalLocation(int x, int y) {

        if (accidental != null) {
            accidental.setLocation(x, y);

        } // if

    } // setAccidentalLocation

    @Override
    public String toString() {
        return "Basic JNote";

    } // toString

} // JNote