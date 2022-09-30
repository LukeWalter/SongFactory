package songfactory.ui;

import songfactory.music.*;
import songfactory.ui.notation.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

public class MusicView extends JComponent {

    private SwingApp app;

    public class StaffInfo {
        public int x, y, width, height;
        public Integer line1, line2, line3, line4, line5;
        public Integer space1, space2, space3, space4;

    } // StaffInfo

    private Dimension dimensions;
    private StaffInfo staff;

    private List<Measure> measures;
    private JMusicNode previewNode;

    public MusicView(SwingApp app) {

        super();

        this.app = app;

        this.measures = new LinkedList<>();
        measures.add(new Measure());
        measures.add(new Measure());
        measures.add(new Measure());
        measures.add(new Measure());

        this.dimensions = new Dimension(
                150 + (MusicSequence.getAsSequence(measures).size() + measures.size()) * 50 - 33,
                100
        );
        this.setSize(dimensions);
        this.setPreferredSize(dimensions);

        this.staff = new StaffInfo();
        updateStaff();


        this.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {

                Point mousePosition = new Point(e.getX(), e.getY());

                switch (app.getSelectType()) {

                    case 0: {

                        double length = app.getSelectLength();
                        JMusicNode selected = JMusicNodeFactory.createNote(length);
                        selected.setLocation(mousePosition);
                        previewNode = selected;

                        break;

                    } // 0

                    case 1: {

                        double length = app.getSelectLength();
                        JMusicNode selected = JMusicNodeFactory.createRest(length);
                        selected.setLocation(mousePosition);
                        previewNode = selected;

                        break;

                    } // 0

                    case 2: {

                        JMusicNode selected = JMusicNodeFactory.createAccidental(Accidental.FLAT);
                        selected.setLocation(mousePosition);
                        previewNode = selected;

                        break;

                    } // 0

                    case 3: {

                        JMusicNode selected = JMusicNodeFactory.createAccidental(Accidental.SHARP);
                        selected.setLocation(mousePosition);
                        previewNode = selected;

                        break;

                    } // 0

                    default: break;

                } // switch

                updateComponent();

            } // mousePressed

            public void mouseReleased(MouseEvent e) {
                process(previewNode);
                previewNode = null;

            } // mouseReleased

        });

        this.addMouseMotionListener(new MouseAdapter() {

            public void mouseDragged(MouseEvent e) {
                Point mousePosition = new Point(e.getX(), e.getY());
                previewNode.setLocation(mousePosition);
                snapToLine(previewNode);
                updateComponent();

            } // mouseDragged

        });

    } // Constructor

    public void addMeasure() {
        measures.add(new Measure());
        updateComponent();

    } // addMeasure

    public void removeMeasure() {

        if (measures.size() > 1) {
            measures.remove(measures.size() - 1);
            updateComponent();

        } // if

    } // addMeasure

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


        this.dimensions = new Dimension(
                150 + (MusicSequence.getAsSequence(measures).size() + measures.size()) * 50 - 33,
                100
        );
        this.setSize(dimensions);
        this.setPreferredSize(dimensions);

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
        g2d.drawLine(x + width - 4, y + 5, x + width - 4, y + height - 5);
        g2d.setStroke(new BasicStroke(2f));

        JMusicNode t = new TrebleClef();
        t.setLocation(x + 5, y + height);
        t.paintNode(g);

        JMusicNode c = new CommonTime();
        c.setLocation(x + 60, y + height / 2);
        c.paintNode(g);

        int nodeOffset = x + 150;
        int nodeSpacing = 50;
        int numNodes = 0;

        for (Measure measure : measures) {

            for (MusicNode node : measure.getNodes()) {

                JMusicNode e = node.getImage();
                e.setLocation(new Point(nodeOffset + nodeSpacing * numNodes, staff.line3));
                e.paintNode(g);

                numNodes++;

            } // for

            g2d.drawLine(nodeOffset + nodeSpacing * numNodes, staff.line5, nodeOffset + nodeSpacing * numNodes, staff.line1);
            numNodes++;

        } // for

        if (previewNode != null) {
            previewNode.paintNode(g);

        } // if

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

    public void setPreviewNode(JMusicNode n) {
        previewNode = n;

    } // preview

    public JMusicNode getPreviewNode() {
        return previewNode;

    } // getPreviewNode

    public void process(JMusicNode n) {

        int nx = n.getX();
        int ny = n.getY();

        if (n instanceof JNote && nx >= staff.x + 110 && nx <= staff.x + staff.width - 17) {

            MusicNode newNode = null;

            if (ny == staff.line1) {
                newNode = new MusicNode(Note.E, app.getSelectLength(), 4);

            } else if (ny == staff.space1) {
                newNode = new MusicNode(Note.F, app.getSelectLength(), 4);

            } else if (ny == staff.line2) {
                newNode = new MusicNode(Note.G, app.getSelectLength(), 4);

            } else if (ny == staff.space2) {
                newNode = new MusicNode(Note.A, app.getSelectLength(), 4);

            } else if (ny == staff.line3) {
                newNode = new MusicNode(Note.B, app.getSelectLength(), 4);

            } else if (ny == staff.space3) {
                newNode = new MusicNode(Note.C, app.getSelectLength(), 5);

            } else if (ny == staff.line4) {
                newNode = new MusicNode(Note.D, app.getSelectLength(), 5);

            } else if (ny == staff.space4) {
                newNode = new MusicNode(Note.E, app.getSelectLength(), 5);

            } else if (ny == staff.line5) {
                newNode = new MusicNode(Note.F, app.getSelectLength(), 5);

            } // if

            if (newNode == null) return;

            app.setStatusText("" + newNode.getNote() + newNode.getOctave());

            MusicSequence seq = MusicSequence.getAsSequence(measures);

            for (int i = 0; i < seq.size(); i++) {

                if (nx < seq.get(i).getImage().getX()) {
                    System.out.println(i + " " + seq.get(i));
                    seq.add(i, newNode);
                    break;

                } // if

            } // for

            for (Measure m : measures) {
                seq = m.processSequence(seq);

            } // for

            updateComponent();

        } // if



    } // process

} // MusicView
