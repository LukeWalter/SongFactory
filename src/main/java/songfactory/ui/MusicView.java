package songfactory.ui;

import songfactory.Pair;
import songfactory.music.*;
import songfactory.ui.notation.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class MusicView extends JComponent {

    private SwingApp app;

    public class StaffInfo {
        public int x, y, width, height;
        public int line1, line2, line3, line4, line5;
        public int space1, space2, space3, space4;
        public HashMap<Integer, Pair<Note, Integer>> pitchTable;

    } // StaffInfo

    private Dimension dimensions;
    private StaffInfo staff;

    private List<Measure> measures;
    private JMusicNode previewNode;
    private boolean placing;

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
                500
        );
        this.setSize(dimensions);
        this.setPreferredSize(dimensions);

        this.staff = new StaffInfo();
        this.staff.pitchTable = new HashMap<>();
        updateStaff();

        placing = false;


        this.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {

                placing = true;

                Point mousePosition = new Point(e.getX(), e.getY());

                for (Measure m : measures) {

                    MusicSequence mNodes = m.getNodes();

                    for (MusicNode n : mNodes) {

                        JMusicNode image = n.getImage();
                        if (image.containsPoint(mousePosition)) {

                            System.out.println(mNodes);

                            int index = mNodes.indexOf(n);
                            mNodes.remove(n);
                            mNodes.add(index, new MusicNode(Note.REST, n.getLength()));

                            System.out.println(app.getSelectLength() + " " + n.getLength());
                            app.setSelectLength(n.getLength());
                            System.out.println(app.getSelectLength());
                            app.setNodeTypeStatus((n.getNote() == Note.REST) ? 1 : 0);

                            image.setLocation(mousePosition);
                            previewNode = image;
                            snapToLine(previewNode);

                            System.out.println(mNodes + " " + previewNode);
                            updateComponent();
                            return;

                        } // if

                    } // for

                } // for


                switch (app.getSelectType()) {

                    case 0: {

                        double length = app.getSelectLength();
                        JMusicNode selected = JMusicNodeFactory.createNote(length);
                        selected.setLocation(mousePosition);
                        previewNode = selected;
                        snapToLine(previewNode);

                        break;

                    } // 0

                    case 1: {

                        double length = app.getSelectLength();
                        JMusicNode selected = JMusicNodeFactory.createRest(length);
                        selected.setLocation(mousePosition);
                        previewNode = selected;
                        snapToLine(previewNode);

                        break;

                    } // 0

                    case 2: {

                        JMusicNode selected = JMusicNodeFactory.createAccidental(Accidental.FLAT);
                        selected.setLocation(mousePosition);
                        previewNode = selected;
                        snapToLine(previewNode);

                        break;

                    } // 0

                    case 3: {

                        JMusicNode selected = JMusicNodeFactory.createAccidental(Accidental.SHARP);
                        selected.setLocation(mousePosition);
                        previewNode = selected;
                        snapToLine(previewNode);

                        break;

                    } // 0

                    default: break;

                } // switch

                updateComponent();

            } // mousePressed

            public void mouseReleased(MouseEvent e) {

                if (placing) {
                    process(previewNode);
                    previewNode = null;
                    updateComponent();
                    placing = false;

                } // if

            } // mouseReleased

        });

        this.addMouseMotionListener(new MouseAdapter() {

            public void mouseDragged(MouseEvent e) {

                if (placing) {

                    Point mousePosition = new Point(e.getX(), e.getY());
                    previewNode.setLocation(mousePosition);
                    snapToLine(previewNode);
                    snapToNode(previewNode);
                    updateComponent();

                } // if

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
        staff.y = (int)(dimensions.getHeight() * 2.0 / 5.0);
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

        staff.pitchTable.clear();
        staff.pitchTable.put(staff.line1, new Pair(Note.E, 4));
        staff.pitchTable.put(staff.space1, new Pair(Note.F, 4));
        staff.pitchTable.put(staff.line2, new Pair(Note.G, 4));
        staff.pitchTable.put(staff.space2, new Pair(Note.A, 4));
        staff.pitchTable.put(staff.line3, new Pair(Note.B, 4));
        staff.pitchTable.put(staff.space3, new Pair(Note.C, 5));
        staff.pitchTable.put(staff.line4, new Pair(Note.D, 5));
        staff.pitchTable.put(staff.space4, new Pair(Note.E, 5));
        staff.pitchTable.put(staff.line5, new Pair(Note.F, 5));

    } // updateStaff

    public void updateComponent() {


        this.dimensions = new Dimension(
                150 + (MusicSequence.getAsSequence(measures).size() + measures.size()) * 50 - 33,
                500
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

        HashMap<Pair<Note, Integer>, Integer> locationTable = Conversion.reverse(staff.pitchTable);
//        System.out.println(locationTable);

        for (Measure measure : measures) {

            for (MusicNode node : measure.getNodes()) {

                JMusicNode i = node.getImage();

                if (node.getNote() == Note.REST) {
                    i.setLocation(new Point(nodeOffset + nodeSpacing * numNodes, staff.line3));

                } else {
//                    System.out.println(node);
                    Pair<Note, Integer> pitch = new Pair(node.getNote(), node.getOctave());
//                    System.out.println();
                    i.setLocation(new Point(
                            nodeOffset + nodeSpacing * numNodes,
                            locationTable.get(new Pair(node.getNote(), node.getOctave()))
                    ));

                } // if

                i.paintNode(g);

                numNodes++;

            } // for

            g2d.drawLine(nodeOffset + nodeSpacing * numNodes, staff.line5, nodeOffset + nodeSpacing * numNodes, staff.line1);
            numNodes++;

        } // for

        if (previewNode != null) {
            previewNode.paintNode(g);

        } // if

    } // paintComponent

    private void snapToNode(JMusicNode n) {

        int currX = n.getX();
        int snapX = currX;

        for (MusicNode node : MusicSequence.getAsSequence(measures)) {

            JMusicNode image = node.getImage();
            if (image.containsX(currX)) {
                snapX = image.getX();
                break;

            } // if

        } // for

        n.setLocation(snapX, n.getY());

    } // snapToNode

    private void snapToLine(JMusicNode n) {

        int currY = n.getY();

        int diff = Integer.MAX_VALUE;
        int snapY = currY;

        int[] validPositions = {
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

        boolean inXRange = (nx >= staff.x + 110 && nx <= staff.x + staff.width - 17);

        MusicNode newNode = null;

        if (n instanceof JNote && inXRange) {
            Pair<Note, Integer> pitch = staff.pitchTable.get(ny);
            newNode = new MusicNode(pitch.first, app.getSelectLength(), pitch.second);
            newNode.getImage().setLocation(new Point(n.getX(), n.getY()));

        } else if (n instanceof JRest && inXRange) {
            newNode = new MusicNode(Note.REST, app.getSelectLength());
            newNode.getImage().setLocation(new Point(n.getX(), staff.line3));

        } else if (n instanceof JAccidental && inXRange) {
            // Next assignment

        } // if

        if (newNode == null) return;

        String status = (newNode.getNote() == Note.REST) ? "Rest" : "" + newNode.getNote() + newNode.getOctave();
        app.setStatusText(status);

        MusicSequence seq = MusicSequence.getAsSequence(measures);
        boolean added = false;

        for (int i = 0; i < seq.size(); i++) {

            MusicNode oldNode = seq.get(i);

            if (nx == oldNode.getImage().getX()) {

                double oldLength = oldNode.getLength();
                double newLength = newNode.getLength();

                seq.remove(i);

                if (oldLength > newLength) {
                    MusicNode filler = new MusicNode(Note.REST, oldLength - newLength);
                    seq.addAll(filler.split());

                } else if (newLength > oldLength) {

                    double remaining = newLength - oldLength;
                    int j = 0;

                    while (remaining > 0) {

                        MusicNode curr = seq.get(i + j);
                        curr.setNote(Note.REST);

                        remaining -= curr.getLength();
                        j++;

                    } // while

                } // if

                seq.add(i, newNode);
                added = true;
                break;

            } else if (nx < oldNode.getImage().getX()) {
//                System.out.println(i + " " + seq.get(i));
                seq.add(i, newNode);
                added = true;
                break;

            } // if

        } // for

        if (!added) seq.add(newNode);

        for (Measure m : measures) {
            seq = m.processSequence(seq);

        } // for

        while (!seq.isEmpty()) {
            Measure newMeasure = new Measure();
            seq = newMeasure.processSequence(seq);
            measures.add(newMeasure);

        } // while

        app.setDeletable(measures.size());
        updateComponent();

    } // process

    public int getNumMeasures() {
        return measures.size();

    } // getSize

} // MusicView
