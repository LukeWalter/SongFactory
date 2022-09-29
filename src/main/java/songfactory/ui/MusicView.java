package songfactory.ui;

import songfactory.music.Measure;
import songfactory.ui.notation.*;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class MusicView extends JComponent {

    private Dimension dimensions;
    private List<Measure> measures;

    private List<JMusicNode> musicNodes;

    public MusicView() {

        super();

        this.dimensions = new Dimension(250, 100);
        this.setMinimumSize(dimensions);
        this.setPreferredSize(dimensions);

        this.measures = new LinkedList<>();
        this.musicNodes = new LinkedList<>();

    } // Constructor

    public void updateComponent() {

        System.out.println(dimensions.getSize());
        dimensions.setSize(dimensions.getWidth() + 10, 75);
        System.out.println(dimensions.getSize());

        invalidate();
        repaint();

    } // updateComponent

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int width = (int)(dimensions.getWidth());
        int height = (int)(dimensions.getHeight() / 2.0);
        Dimension d = new Dimension(width, height);

        int x = 0;
        int y = (int)(dimensions.getHeight() / 4.0);
        Point p = new Point(x, y);

        Rectangle staff = new Rectangle(p, d);
        g2d.setColor(Color.WHITE);
        g2d.draw(staff);
        g2d.fill(staff);

        g2d.setStroke(new BasicStroke(2f));
        g2d.setColor(Color.BLACK);

        g2d.drawLine(x + 1, y + 1, x + 1, y + height - 1);

        g2d.drawLine(x + 1, y + 1, width, y + 1);
        g2d.drawLine(x + 1, y + height / 4, x + width, y + height / 4);
        g2d.drawLine(x + 1, y + height / 2, x + width, y + height / 2);
        g2d.drawLine(x + 1, y + height * 3 / 4, x + width, y + height * 3 / 4);
        g2d.drawLine(x + 1, y + height - 1, x + width, y + height - 1);

        g2d.drawLine(x + width - 17, y + 1, x + width - 17, y + height - 1);
        g2d.setStroke(new BasicStroke(10f));
        g2d.drawLine(x + width - 5, y + 5, x + width - 5, y + height - 5);

        TrebleClef t = new TrebleClef();
        t.paintNode(g, 5, y + height);

    } // paintComponent

    public String toString() {
        return "toString called on MusicView";

    } // toString

} // MusicView
