package songfactory.ui;

import javax.swing.*;
import java.awt.*;

public class MusicView extends JComponent {

    private Dimension dimensions;

    public MusicView() {
        super();
        this.dimensions = new Dimension(250, 75);
        this.setMinimumSize(dimensions);
        this.setPreferredSize(dimensions);

    } // Constructor

    private void updateComponent() {


    } // updateComponent

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        Rectangle staff = new Rectangle(dimensions);
        g2d.setColor(Color.WHITE);
        g2d.draw(staff);
        g2d.fill(staff);

        Dimension d = staff.getSize();
        g2d.setStroke(new BasicStroke(2f));
        g2d.setColor(Color.BLACK);

        g2d.drawLine(1, 1, 1, (int)(d.getHeight() - 1));

        g2d.drawLine(1, 1, (int)(d.getWidth()), 1);
        g2d.drawLine(1, (int)(d.getHeight() / 4.0), (int)(d.getWidth()), (int)(d.getHeight() / 4.0));
        g2d.drawLine(1, (int)(d.getHeight() / 2.0), (int)(d.getWidth()), (int)(d.getHeight() / 2.0));
        g2d.drawLine(1, (int)(d.getHeight() * 3.0 / 4.0), (int)(d.getWidth()), (int)(d.getHeight() * 3.0 / 4.0));
        g2d.drawLine(1, (int)(d.getHeight() - 1.0), (int)(d.getWidth()), (int)(d.getHeight() - 1.0));

        g2d.drawLine((int)(d.getWidth() - 17), 1, (int)(d.getWidth() - 17), (int)(d.getHeight() - 1));
        g2d.setStroke(new BasicStroke(10f));
        g2d.drawLine((int)(d.getWidth() - 5), 1, (int)(d.getWidth() - 5), (int)(d.getHeight() - 1));

    } // paintComponent

    public String toString() {
        return "toString called on MusicView";

    } // toString

} // MusicView
