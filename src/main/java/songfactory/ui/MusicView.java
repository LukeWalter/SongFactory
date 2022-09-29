package songfactory.ui;

import songfactory.music.Accidental;
import songfactory.music.Measure;
import songfactory.music.MusicNode;
import songfactory.music.Note;
import songfactory.ui.notation.*;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class MusicView extends JComponent {

    public class StaffInfo {
        public int x, y, width, height;
        public Integer line1, line2, line3, line4, line5;
        public Integer space1, space2, space3, space4;

    } // StaffInfo

    private Dimension dimensions;
    private StaffInfo staff;

    private List<Measure> measures;

    public MusicView() {

        super();

        this.dimensions = new Dimension(250, 100);
        this.setPreferredSize(dimensions);

        this.staff = new StaffInfo();
        updateStaff();

        this.measures = new LinkedList<>();
        measures.add(new Measure());

    } // Constructor

    private void updateStaff() {

        staff.x = 0;
        staff.y = (int)(dimensions.getHeight() / 4.0);
        staff.width = (int)(dimensions.getWidth());
        staff.height = 50;

        staff.line1 = staff.y + staff.height - 1;
        staff.line2 = staff.y + staff.height * 3 / 4;
        staff.line3 = staff.y + staff.height / 2;
        staff.line4 = staff.y + staff.height / 4;
        staff.line5 = staff.y + 1;

        staff.space1 = staff.y + staff.height * 7 / 8;
        staff.space2 = staff.y + staff.height * 5 / 8;
        staff.space3 = staff.y + staff.height * 3 / 8;
        staff.space4 = staff.y + staff.height * 1 / 8;

    } // updateStaff

    public void updateComponent() {

        dimensions.setSize(dimensions.getWidth() + 10, dimensions.getHeight());
        updateStaff();

        revalidate();
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

        Rectangle rect = new Rectangle(p, d);
        g2d.setColor(Color.WHITE);
        g2d.draw(rect);
        g2d.fill(rect);

        g2d.setStroke(new BasicStroke(2f));
        g2d.setColor(Color.BLACK);

        g2d.drawLine(x + 1, staff.line5, x + 1, staff.line1);

        g2d.drawLine(x + 1, staff.line5, x + width, staff.line5);
        g2d.drawLine(x + 1, staff.line4, x + width, staff.line4);
        g2d.drawLine(x + 1, staff.line3, x + width, staff.line3);
        g2d.drawLine(x + 1, staff.line2, x + width, staff.line2);
        g2d.drawLine(x + 1, staff.line1, x + width, staff.line1);

        g2d.drawLine(x + width - 17, staff.line5, x + width - 17, staff.line1);
        g2d.setStroke(new BasicStroke(10f));
        g2d.drawLine(x + width - 5, y + 5, x + width - 5, y + height - 5);

        JMusicNode t = new TrebleClef();
        t.setLocation(x + 5, y + height);
        t.paintNode(g);

        JMusicNode c = new CommonTime();
        c.setLocation(x + 60, y + height / 2);
        c.paintNode(g);

        int nodeOffset = x + 150;
        int nodeSpacing = 30;
        int numNodes = 0;

        for (Measure measure : measures) {

            for (MusicNode node : measure.getNodes()) {

                JMusicNode e = node.getImage();
                e.setLocation(new Point(nodeOffset + nodeSpacing * numNodes, staff.line3));
                e.paintNode(g);

                numNodes++;

            } // for

        } // for

        MusicNode n = new MusicNode(Note.C, 0.5, 4, Accidental.FLAT);

    } // paintComponent

    private void snapToLine(JMusicNode n) {

        int currY = n.getY();

        int diff = Integer.MAX_VALUE;
        int snapY = currY;

        Integer[] validPositions = {
                staff.line1, staff.space1, staff.line2,
                staff.space2, staff.line3, staff.space3,
                staff.line4, staff.space4, staff.line5
        };

        for (Integer posY : validPositions) {

            int currDiff = Math.abs(currY - posY);

            if (currDiff < diff) {
                snapY = posY;
                diff = currDiff;

            } // if

        } // for

        n.setLocation(new Point(n.getX(), snapY));

    } // snapToLine

} // MusicView
