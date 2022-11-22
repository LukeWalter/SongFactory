package songfactory.ui;

import songfactory.Mode;
import songfactory.Pair;
import songfactory.music.*;
import songfactory.recognition.*;
import songfactory.ui.notation.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
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
    private Mode mode;

    private ArrayList<Point2D> stroke; // Pen stroke
    private int alpha; // Opacity of pen stroke

    private TimerBall animation;
    private boolean playing;

    /**
     * Represents the sizing and positional
     * values of the staff.
     */
    public class StaffInfo {
        public int x, y, width, height; // Dimensions of staff
        public int line1, line2, line3, line4, line5; // Line y positions
        public int lline1, lline2, lline3, lline4; // Ledger line y positions
        public int space1, space2, space3, space4; // Space y positions
        public int lspace1, lspace2, lspace3, lspace4, lspace5, lspace6; // Space y positions outside staff
        public HashMap<Integer, Pair<Note, Integer>> pitchTable; // Maps y positions to pitches

    } // StaffInfo

    private Dimension dimensions; // Dimensions of ENTIRE component
    private StaffInfo staff; // Staff

    private List<Measure> measures; // List of measures stored in staff
    private JMusicNode previewNode; // The node being selected/dragged
    private boolean placing; // Determines if a note is currently being dragged
    private boolean drawing; // Determines if a shape is being drawn

    /**
     * MusicView Constructor.
     * @param app reference to overarching application
     */
    public MusicView(SwingApp app) {
        this(app, 4);

    } // Constructor

    /**
     * MusicView Constructor.
     * @param app reference to overarching application
     * @param numMeasures number of measures to initialize staff with
     */
    public MusicView(SwingApp app, int numMeasures) {

        super();

        this.app = app;

        this.measures = new LinkedList<>();

        for (int i = 0; i < numMeasures; i++) {
            measures.add(new Measure());

        } // for

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

                mode = app.getMode();
                Point mousePosition = new Point(e.getX(), e.getY());

                switch (mode) {

                    case SELECT:

                        placing = true;

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

                        break;

                    case DRAW:

                        // Initialize new stroke
                        drawing = true;
                        alpha = 255;
                        stroke = new ArrayList<>();
                        stroke.add(mousePosition);
                        updateComponent();

                        break;

                    default: break;

                } // switch

            } // mousePressed

            public void mouseReleased(MouseEvent e) {

                switch (mode) {

                    case SELECT:

                        // Deselect and send note to the measure list
                        if (placing) {
                            process(previewNode);
                            previewNode = null;
                            updateComponent();
                            placing = false;

                        } // if

                        break;

                    case DRAW:

                        // Recognize shape and process into measure list
                        if (drawing) {

                            JMusicNode newNode = recognizeShape(stroke);

                            if (newNode != null) {
                                newNode.setLocation((Point)stroke.get(0));
                                snapToLine(newNode);
                                snapToNode(newNode);
                                process(newNode);
                                stroke = null;

                            } // if

                            drawing = false;
                            updateComponent();

                        } // if

                        break;

                    default: break;

                } // switch

            } // mouseReleased

        });

        this.addMouseMotionListener(new MouseAdapter() {

            public void mouseDragged(MouseEvent e) {

                Point mousePosition = new Point(e.getX(), e.getY());

                switch (mode) {

                    case SELECT:

                        // Drag node with mouse and snap in valid positions
                        if (placing) {
                            previewNode.setLocation(mousePosition);

                            snapToLine(previewNode);
                            snapToNode(previewNode);

                            // Snap accidental to note position
                            if (previewNode instanceof JNote) {
                                JNote note = (JNote) previewNode;
                                JAccidental acc = note.getAccidental();

                                if (acc != null) {
                                    acc.setLocation(previewNode.getLocation());
                                    snapToLine(acc);
                                    snapToNode(acc);

                                } // if

                            } // if

                            updateComponent();

                        } // if

                        break;

                    case DRAW:

                        // Add new mouse position to drawn shape
                        if (drawing) {
                            stroke.add(mousePosition);
                            revalidate();
                            repaint();

                        } // if

                        break;

                    default: break;

                } // switch

            } // mouseDragged

        });

        this.playing = false;

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

        // Line positions

        staff.lline1 = staff.y + staff.height - 1 + staff.height / 2;
        staff.lline2 = staff.y + staff.height - 1 + staff.height / 4;

        staff.line1 = staff.y + staff.height - 1;
        staff.line2 = staff.y + staff.height * 3 / 4;
        staff.line3 = staff.y + staff.height / 2;
        staff.line4 = staff.y + staff.height / 4;
        staff.line5 = staff.y + 1;

        staff.lline3 = staff.y - staff.height / 4;
        staff.lline4 = staff.y - staff.height / 2;

        // Space positions

        staff.lspace1 = staff.y + staff.height * 13 / 8;
        staff.lspace2 = staff.y + staff.height * 11 / 8;
        staff.lspace3 = staff.y + staff.height * 9 / 8;

        staff.space1 = staff.y + staff.height * 7 / 8;
        staff.space2 = staff.y + staff.height * 5 / 8;
        staff.space3 = staff.y + staff.height * 3 / 8;
        staff.space4 = staff.y + staff.height * 1 / 8;

        staff.lspace4 = staff.y - staff.height * 1 / 8;
        staff.lspace5 = staff.y - staff.height * 3 / 8;
        staff.lspace6 = staff.y - staff.height * 5 / 8;

        // Pitch table

        staff.pitchTable.clear();

        staff.pitchTable.put(staff.lspace1, new Pair(Note.G, 3));
        staff.pitchTable.put(staff.lline1, new Pair(Note.A, 3));
        staff.pitchTable.put(staff.lspace2, new Pair(Note.B, 3));
        staff.pitchTable.put(staff.lline2, new Pair(Note.C, 4));
        staff.pitchTable.put(staff.lspace3, new Pair(Note.D, 4));

        staff.pitchTable.put(staff.line1, new Pair(Note.E, 4));
        staff.pitchTable.put(staff.space1, new Pair(Note.F, 4));
        staff.pitchTable.put(staff.line2, new Pair(Note.G, 4));
        staff.pitchTable.put(staff.space2, new Pair(Note.A, 4));
        staff.pitchTable.put(staff.line3, new Pair(Note.B, 4));
        staff.pitchTable.put(staff.space3, new Pair(Note.C, 5));
        staff.pitchTable.put(staff.line4, new Pair(Note.D, 5));
        staff.pitchTable.put(staff.space4, new Pair(Note.E, 5));
        staff.pitchTable.put(staff.line5, new Pair(Note.F, 5));

        staff.pitchTable.put(staff.lspace4, new Pair(Note.G, 5));
        staff.pitchTable.put(staff.lline3, new Pair(Note.A, 5));
        staff.pitchTable.put(staff.lspace5, new Pair(Note.B, 5));
        staff.pitchTable.put(staff.lline4, new Pair(Note.C, 6));
        staff.pitchTable.put(staff.lspace6, new Pair(Note.D, 6));

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

        // Draw treble clef and time signature
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

                    i.setLocation(new Point(
                            nodeOffset + nodeSpacing * numNodes,
                            locationTable.get(new Pair(node.getNote(), node.getOctave()))
                    ));

                    // Draw ledger lines

                    int iy = i.getY();

                    if (iy > staff.lspace3) {
                        int ix = i.getX();
                        g2d.drawLine(ix - 8, staff.lline2, ix + 8, staff.lline2);

                    } // if

                    if (iy > staff.lspace2) {
                        int ix = i.getX();
                        g2d.drawLine(ix - 8, staff.lline1, ix + 8, staff.lline1);

                    } // if

                    if (iy < staff.lspace4) {
                        int ix = i.getX();
                        g2d.drawLine(ix - 8, staff.lline3, ix + 8, staff.lline3);

                    } // if

                    if (iy < staff.lspace5) {
                        int ix = i.getX();
                        g2d.drawLine(ix - 8, staff.lline4, ix + 8, staff.lline4);

                    } // if

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

            // Draw ledger lines

            int iy = previewNode.getY();

            if (iy > staff.lspace3) {
                int ix = previewNode.getX();
                g2d.drawLine(ix - 8, staff.lline2, ix + 8, staff.lline2);

            } // if

            if (iy > staff.lspace2) {
                int ix = previewNode.getX();
                g2d.drawLine(ix - 8, staff.lline1, ix + 8, staff.lline1);

            } // if

            if (iy < staff.lspace4) {
                int ix = previewNode.getX();
                g2d.drawLine(ix - 8, staff.lline3, ix + 8, staff.lline3);

            } // if

            if (iy < staff.lspace5) {
                int ix = previewNode.getX();
                g2d.drawLine(ix - 8, staff.lline4, ix + 8, staff.lline4);

            } // if

        } // if

        if (playing) {
            animation.sns(1f);
            animation.draw(g2d);

        } // if

        // Draw pen shape
        if (stroke != null) {

            if (stroke.size() > 1) {

                g2d.setStroke(new BasicStroke(3f));
                g2d.setColor(new Color(0, 0, 255, alpha));

                for (int i = 0; i < stroke.size() - 1; i++) {

                    Point2D a = stroke.get(i);
                    Point2D b = stroke.get(i + 1);

                    g2d.drawLine((int)a.getX(), (int)a.getY(), (int)b.getX(), (int)b.getY());

                } // for

            } // if

            if (!drawing) {
                alpha--;
                if (alpha < 1) stroke = null;
                revalidate();
                repaint();

            } // if

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

        int numMeasures = measures.size(); // Old measure size

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

        // Check if delete measure button should be active
        app.setDeletableMeasure(measures.size());
        updateComponent();

        // Keep measure count consistent across MusicViews
        if (measures.size() - numMeasures != 0) {
            app.updateMeasureLength(measures.size());

        } // if

    } // process

    public List<Measure> getMeasures() {
        return measures;

    } // getMeasures

    /**
     * Returns the number of measures in the measure list.
     *
     * @return number of measures
     */
    public int getNumMeasures() {
        return measures.size();

    } // getSize

    /**
     * Converts a drawn image to a usable JMusicNode
     * that can be placed on the staff.
     *
     * @param shape shape to be recognized
     * @return JMusicNode recognized by DollarRecognizer
     */
    public JMusicNode recognizeShape(ArrayList<Point2D> shape) {

        // Recognize shape
        DollarRecognizer dollar = new DollarRecognizer();
        String name = dollar.recognize(shape).getName();

        // Convert result to JMusicNode
        if (!Conversion.recognitionTable.containsKey(name)) return null;
        JMusicNode recognized = Conversion.recognitionTable.get(name).make();

        // Alter UI to match node type and length
        if (recognized instanceof JNote) {
            app.setSelectTypeStatus(0);

            JNote newNote = (JNote) recognized;
            app.setSelectLength(newNote.getLength());

        } else if (recognized instanceof JRest) {
            app.setSelectTypeStatus(1);

            JRest newNote = (JRest) recognized;
            app.setSelectLength(newNote.getLength());

        } else if (recognized instanceof Flat) {
            app.setSelectTypeStatus(2);

        } else if (recognized instanceof Sharp) {
            app.setSelectTypeStatus(3);

        } // if

        return recognized;

    } // recognizeShape

    public boolean isPlaying() {
        return playing;

    } // isPlaying

    public void setPlaying(boolean playing) {
        this.playing = playing;

    } // setPlaying

    public void playAnimation() {

        if (!this.isPlaying()) {
            this.setPlaying(true);
            this.animation = new TimerBall(this, staff.x + 150, staff.y - 8);
            animation.sequence();

        } // if

    } // playAnimation

} // MusicView