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
import java.util.Set;

/**
 * Visible Swing component that shows a staff and
 * any number of musical components contained
 * within it.
 */
public class MusicView extends JComponent {

    private SwingApp app; // Reference to overarching Swing application

    /**
     * Represents the sizing and positional
     * values of the staff.
     */
    public class StaffInfo {
        public int x, y, width, height; // Dimensions of staff
        public int line1, line2, line3, line4, line5; // Line y positions
        public int space1, space2, space3, space4; // Space y positions
        public HashMap<Integer, Pair<Note, Integer>> pitchTable; // Maps y positions to pitches

    } // StaffInfo

    private Dimension dimensions; // Dimensions of ENTIRE component
    private StaffInfo staff; // Staff

    private List<Measure> measures; // List of measures stored in staff
    private JMusicNode previewNode; // The node being selected/dragged
    private boolean placing; // Determines if a note is currently being dragged

    /**
     * MusicView Constructor.
     * @param app reference to overarching application
     */
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
                300
        );
        this.setSize(dimensions);
        this.setPreferredSize(dimensions);

        this.staff = new StaffInfo();
        this.staff.pitchTable = new HashMap<>();
        updateStaff();

        placing = false;


        this.addMouseListener(new MouseAdapter() {

            /**
             * Selects a node if available, otherwise
             * creates a new one.
             *
             * @param e mouse event
             */
            public void mousePressed(MouseEvent e) {

                placing = true;
                Point mousePosition = new Point(e.getX(), e.getY());

                for (Measure m : measures) {

                    MusicSequence mNodes = m.getNodes();

                    for (MusicNode n : mNodes) {

                        // Check if image coordinates are inside a note
                        JMusicNode image = n.getImage();
                        if (image.containsPoint(mousePosition)) {

                            // Replace selected note with a rest of equal size
                            int index = mNodes.indexOf(n);
                            mNodes.remove(n);
                            mNodes.add(index, new MusicNode(Note.REST, n.getLength()));

                            // Send new node information to the application
                            app.setSelectLength(n.getLength());
                            app.setSelectTypeStatus((n.getNote() == Note.REST) ? 1 : 0);

                            // Select new node
                            image.setLocation(mousePosition);
                            previewNode = image;
                            snapToLine(previewNode);

                            updateComponent();
                            return;

                        } // if

                    } // for

                } // for

                // Cases based on node type
                switch (app.getSelectType()) {

                    // Note
                    case 0: {

                        // Create new note based on application status
                        double length = app.getSelectLength();
                        JMusicNode selected = JMusicNodeFactory.createNote(length);
                        selected.setLocation(mousePosition);
                        previewNode = selected;
                        snapToLine(previewNode);

                        break;

                    } // 0

                    // Rest
                    case 1: {

                        // Create new rest based on application status
                        double length = app.getSelectLength();
                        JMusicNode selected = JMusicNodeFactory.createRest(length);
                        selected.setLocation(mousePosition);
                        previewNode = selected;
                        snapToLine(previewNode);

                        break;

                    } // 1

                    // Flat
                    case 2: {

                        // Create new accidental
                        JMusicNode selected = JMusicNodeFactory.createAccidental(Accidental.FLAT);
                        selected.setLocation(mousePosition);
                        previewNode = selected;
                        snapToLine(previewNode);

                        break;

                    } // 2

                    // Sharp
                    case 3: {

                        // Create new accidental
                        JMusicNode selected = JMusicNodeFactory.createAccidental(Accidental.SHARP);
                        selected.setLocation(mousePosition);
                        previewNode = selected;
                        snapToLine(previewNode);

                        break;

                    } // Sharp

                    default: break;

                } // switch

                updateComponent();

            } // mousePressed

            public void mouseReleased(MouseEvent e) {

                // Deselect and send note to the measure list
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

                // Drag note with mouse and snap in valid positions
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

    /**
     * Adds a measure to the measure list.
     */
    public void addMeasure() {
        measures.add(new Measure());
        updateComponent();

    } // addMeasure

    /**
     * Removes a measure from the measure list.
     */
    public void removeMeasure() {

        if (measures.size() > 1) {
            measures.remove(measures.size() - 1);
            updateComponent();

        } // if

    } // addMeasure

    /**
     * Updates staff information to accurately reflect
     * the state of the measure list.
     */
    private void updateStaff() {

        staff.x = 50;
        staff.y = (int)(dimensions.getHeight() / 3.0);
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

    /**
     * Updates the underlying and visual
     * states of the staff.
     */
    public void updateComponent() {


        this.dimensions = new Dimension(
                150 + (MusicSequence.getAsSequence(measures).size() + measures.size()) * 50 - 33,
                300
        );
        this.setSize(dimensions);
        this.setPreferredSize(dimensions);

        updateStaff();

        revalidate();
        repaint();

    } // updateComponent

    /**
     * Draws the staff in the application.
     *
     * @param g graphics object
     */
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

        // Draw staff lines
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

        // Draw trable clef and time signature
        JMusicNode t = new TrebleClef();
        t.setLocation(x + 5, y + height);
        t.paintNode(g);

        JMusicNode c = new CommonTime();
        c.setLocation(x + 60, y + height / 2);
        c.paintNode(g);

        // Draw notes

        int nodeOffset = x + 150;
        int nodeSpacing = 50;
        int numNodes = 0;

        HashMap<Pair<Note, Integer>, Integer> locationTable = Conversion.reverse(staff.pitchTable);

        for (Measure measure : measures) {

            for (MusicNode node : measure.getNodes()) {

                // Get each node image in the measure list, draw them with correct spacing
                JMusicNode i = node.getImage();

                if (node.getNote() == Note.REST) {
                    i.setLocation(new Point(nodeOffset + nodeSpacing * numNodes, staff.line3));

                } else {
                    Pair<Note, Integer> pitch = new Pair(node.getNote(), node.getOctave());
                    i.setLocation(new Point(
                            nodeOffset + nodeSpacing * numNodes,
                            locationTable.get(new Pair(node.getNote(), node.getOctave()))
                    ));

                } // if

                i.paintNode(g);

                numNodes++;

            } // for

            // Draw measure line
            g2d.drawLine(
                    nodeOffset + nodeSpacing * numNodes, staff.line5,
                    nodeOffset + nodeSpacing * numNodes, staff.line1
            );
            numNodes++;

        } // for

        // Draw selected node if it exists
        if (previewNode != null) {

            previewNode.paintNode(g);

            Point previewNodePos = previewNode.getLocation();
            int pNodeX = (int)(previewNodePos.getX());
            int pNodeY = (int)(previewNodePos.getY());

            int xChange = 10;
            int yChange = 10;

            // Draw bounding box
            Point[] corners = {
                    new Point(pNodeX + xChange, pNodeY + yChange), // Top right
                    new Point(pNodeX + xChange, pNodeY - yChange), // Bottom right
                    new Point(pNodeX - xChange, pNodeY - yChange), // Bottom left
                    new Point(pNodeX - xChange, pNodeY + yChange)  // Top left

            };

            g2d.setColor(Color.BLUE);

            g2d.drawLine(
                    (int)(corners[0].getX()), (int)(corners[0].getY()),
                    (int)(corners[1].getX()), (int)(corners[1].getY())
            );

            g2d.drawLine(
                    (int)(corners[1].getX()), (int)(corners[1].getY()),
                    (int)(corners[2].getX()), (int)(corners[2].getY())
            );

            g2d.drawLine(
                    (int)(corners[2].getX()), (int)(corners[2].getY()),
                    (int)(corners[3].getX()), (int)(corners[3].getY())
            );

            g2d.drawLine(
                    (int)(corners[3].getX()), (int)(corners[3].getY()),
                    (int)(corners[0].getX()), (int)(corners[0].getY())
            );

        } // if

    } // paintComponent

    /**
     * Horizontal snapping function. Allows
     * for easier line-ups when placing a
     * note on top of another.
     *
     * @param n node with snapping behavior
     */
    private void snapToNode(JMusicNode n) {

        int currX = n.getX();
        int snapX = currX;

        // Search measure list for a node in the correct x position
        for (MusicNode node : MusicSequence.getAsSequence(measures)) {

            JMusicNode image = node.getImage();
            if (image.containsX(currX)) {
                snapX = image.getX();
                break;

            } // if

        } // for

        // Snap to x position of node
        n.setLocation(snapX, n.getY());

    } // snapToNode

    /**
     * Vertical snapping function. Allows
     * for easier line-ups with valid lines
     * and spaces on the staff.
     *
     * @param n node with snapping behavior
     */
    private void snapToLine(JMusicNode n) {

        int currY = n.getY();

        int diff = Integer.MAX_VALUE;
        int snapY = currY;

        // Search staff for a line or space in the correct y position
        Set<Integer> validPositions = staff.pitchTable.keySet();

        for (Integer posY : validPositions) {

            int currDiff = Math.abs(currY - posY);

            if (currDiff < diff) {
                snapY = posY;
                diff = currDiff;

            } // if

        } // for

        // Snap to y position of line/space
        n.setLocation(new Point(n.getX(), snapY));

    } // snapToLine

    /**
     * Sets the node being selected.
     *
     * @param n node to be selected
     */
    public void setPreviewNode(JMusicNode n) {
        previewNode = n;

    } // preview

    /**
     * Returns the node being selected.
     */
    public JMusicNode getPreviewNode() {
        return previewNode;

    } // getPreviewNode

    /**
     * Determines how and where a selected
     * note should be inserted into the measure
     * list based on its position and type.
     *
     * @param n node being inserted into the measure list
     */
    public void process(JMusicNode n) {

        // Get position of node image
        int nx = n.getX();
        int ny = n.getY();

        boolean inXRange = (nx >= staff.x + 110 && nx <= staff.x + staff.width - 17);
        MusicNode newNode = null;

        // Create new note based on position and length
        if (n instanceof JNote && inXRange) {
            JNote note = ((JNote) n);
            JAccidental acc = note.getAccidental();
            Pair<Note, Integer> pitch = staff.pitchTable.get(ny);
            newNode = new MusicNode(pitch.first, app.getSelectLength(), pitch.second);
            if (acc != null) newNode.setAccidental(acc.getAccidental());
            JNote image = (JNote) newNode.getImage();
            image.setLocation(new Point(n.getX(), n.getY()));
            image.setAccidental(acc);

        // Create new rest based on position and length
        } else if (n instanceof JRest && inXRange) {
            newNode = new MusicNode(Note.REST, app.getSelectLength());
            newNode.getImage().setLocation(new Point(n.getX(), staff.line3));

        // Add accidental to an existing note in the measure list based on position
        } else if (n instanceof JAccidental && inXRange) {

            MusicSequence seq = MusicSequence.getAsSequence(measures);

            for (int i = 0; i < seq.size(); i++) {

                JMusicNode oldNode = seq.get(i).getImage();

                if (oldNode instanceof JNote && nx == oldNode.getX()) {
                    JNote note = (JNote) oldNode;
                    JAccidental accidental = (JAccidental) n;
                    note.setAccidental(accidental);
                    note.getNodeRef().setAccidental(accidental.getAccidental());

                } // if

            } // for

        } // if

        if (newNode == null) return;

        // Update the application status bar
        String acc = (newNode.getAccidental() == null) ? "" : "" + newNode.getAccidental();
        String status = (newNode.getNote() == Note.REST) ? "Rest" : "" + newNode.getNote() + acc + newNode.getOctave();
        app.setStatusText(status);

        // Convert measure list to music sequence for editing
        MusicSequence seq = MusicSequence.getAsSequence(measures);
        boolean added = false;

        for (int i = 0; i < seq.size(); i++) {

            MusicNode oldNode = seq.get(i);

            // If new node is snapped to an existing node
            if (nx == oldNode.getImage().getX()) {

                double oldLength = oldNode.getLength();
                double newLength = newNode.getLength();

                // Remove old node
                seq.remove(i);

                // If new node is shorter than existing node
                if (oldLength > newLength) {

                    // Add rests to fill in empty space
                    MusicNode filler = new MusicNode(Note.REST, oldLength - newLength);
                    seq.addAll(filler.split());

                // If new node is longer than existing node
                } else if (newLength > oldLength) {

                    double remaining = newLength - oldLength;
                    int j = 0;

                    // Convert all subsequent notes in range to rests
                    while (remaining > 0) {

                        MusicNode curr = seq.get(i + j);
                        curr.setNote(Note.REST);

                        remaining -= curr.getLength();
                        j++;

                    } // while

                } // if

                // Add new node
                seq.add(i, newNode);
                added = true;
                break;

            // Add without removing if node is between two nodes or at the start of the staff
            } else if (nx < oldNode.getImage().getX()) {
                seq.add(i, newNode);
                added = true;
                break;

            } // if

        } // for

        // Add new node to the end of the sequence
        if (!added) seq.add(newNode);

        // Convert sequence into a list of measures and store it
        for (Measure m : measures) {
            seq = m.processSequence(seq);

        } // for

        // Create new measures until the sequence is fully processed
        while (!seq.isEmpty()) {
            Measure newMeasure = new Measure();
            seq = newMeasure.processSequence(seq);
            measures.add(newMeasure);

        } // while

        // Check if delete buttons should be active
        app.setDeletable(measures.size());
        updateComponent();

    } // process

    /**
     * Returns the number of measures in the measure list.
     *
     * @return number of measures
     */
    public int getNumMeasures() {
        return measures.size();

    } // getSize

} // MusicView