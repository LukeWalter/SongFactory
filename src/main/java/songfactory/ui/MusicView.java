package songfactory.ui;

import songfactory.music.Measure;
import songfactory.ui.notation.*;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class MusicView extends JComponent {

    private class StaffInfo {
        int x, y, width, height;

    } // StaffInfo

    private Dimension dimensions;
    private StaffInfo staff;
    private List<Measure> measures;

    private List<JMusicNode> musicNodes;

    public MusicView() {

        super();

        this.dimensions = new Dimension(250, 100);
        this.setPreferredSize(dimensions);

        this.staff = new StaffInfo();
        updateStaff();

        this.measures = new LinkedList<>();
        this.musicNodes = new LinkedList<>();

    } // Constructor

    private void updateStaff() {
        staff.x = 0;
        staff.y = (int)(dimensions.getHeight() / 4.0);
        staff.width = (int)(dimensions.getWidth());
        staff.height = (int)(dimensions.getHeight() / 2.0);

    } // updateStaff

    public void updateComponent() {

        dimensions.setSize(dimensions.getWidth() + 10, dimensions.getHeight());
        updateStaff();

        invalidate();
        repaint();

    } // updateComponent

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int width = staff.width;
        int height = staff.height;
        Dimension d = new Dimension(width, height);

        int x = staff.x;
        int y = staff.y;
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

        JMusicNode t = new TrebleClef();
        t.paintNode(g, x + 5, y + height);

        JMusicNode c = new CommonTime();
        c.paintNode(g, x + 60, y + height / 2);

        JMusicNode q = new QuarterNote();
        q.paintNode(g, x + 140, y + height / 2);

        JMusicNode e = new EighthNote();
        e.paintNode(g, x + 140, y + height * 3 / 4);

    } // paintComponent

} // MusicView
