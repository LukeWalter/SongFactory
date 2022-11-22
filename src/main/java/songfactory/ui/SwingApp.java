package songfactory.ui;

import songfactory.Mode;
import songfactory.music.*;
import songfactory.ui.notation.JMusicNode;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Java Swing application.
 */
public class SwingApp {

    private Mode mode;

    private JLabel status; // Editable status text

    private int selectType; // Representation of which node type is active (note, rest, flat, sharp)
    private JSlider noteLength; // Slider that determines note/rest length
    private JButton delete; // "Delete Staff" button on sidebar
    private JMenuItem deleteMenu; // "Delete Staff" button in dropdown menu
    private JButton deleteMeasure; // "Delete Measure" button

    // Radio buttons for each node type
    private JRadioButton note;
    private JRadioButton rest;
    private JRadioButton flat;
    private JRadioButton sharp;

    private ArrayList<MusicView> sheetMusic;

    /**
     * Constructs the swing app and displays it on screen.
     */
    public SwingApp() {

        mode = Mode.SELECT;

        selectType = 0;

        // Initial frame
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());


        // Top screen menu elements
        JPanel menu = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        menu.setBackground(Color.LIGHT_GRAY);
        JLabel title = new JLabel("SF");

        JMenuBar menuBar = new JMenuBar();

        JMenu file = new JMenu("File");
        JMenuItem exit = new JMenuItem("Exit");
        file.add(exit);

        JMenu edit = new JMenu("Edit");
        JMenuItem create = new JMenuItem("New Staff");
        deleteMenu = new JMenuItem("Delete Staff");
        edit.add(create);
        edit.add(deleteMenu);

        JMenu help = new JMenu("Help");

        menuBar.add(file);
        menuBar.add(edit);
        menuBar.add(help);

        menu.add(title);
        menu.add(menuBar);


        // Left side buttons

        JPanel control = new JPanel(new BorderLayout());
        JPanel ctrlButtons = new JPanel();
        ctrlButtons.setLayout(new BoxLayout(ctrlButtons, BoxLayout.Y_AXIS));


        JButton select = new JButton("Select");

        JButton pen = new JButton("Pen");

        JPanel sp = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 30));
        sp.add(select);
        sp.add(pen);
        ctrlButtons.add(sp);

        JSeparator one = new JSeparator();
        ctrlButtons.add(one);


        JButton newStaff = new JButton("New Staff");
        delete = new JButton("Delete Staff");

        JPanel nds = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 30));
        nds.add(newStaff);
        nds.add(delete);
        ctrlButtons.add(nds);

        JSeparator two = new JSeparator();
        ctrlButtons.add(two);

        JButton newMeasure = new JButton("New Measure");
        deleteMeasure = new JButton("Delete Measure");

        JPanel msc = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 30));
        msc.add(newMeasure);
        msc.add(deleteMeasure);
        ctrlButtons.add(msc);

        JSeparator twoandahalf = new JSeparator();
        ctrlButtons.add(twoandahalf);

        JButton play = new JButton("Play");
        JButton stop = new JButton("Stop");

        JPanel ps = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 30));
        ps.add(play);
        ps.add(stop);
        ctrlButtons.add(ps);

        JSeparator three = new JSeparator();
        ctrlButtons.add(three);


        JPanel addSettings = new JPanel();
        addSettings.setLayout(new BoxLayout(addSettings, BoxLayout.Y_AXIS));


        // Node type radio buttons
        note = new JRadioButton("Note");
        note.setSelected(true);
        addSettings.add(note);
        rest = new JRadioButton("Rest");
        addSettings.add(rest);
        flat = new JRadioButton("Flat");
        addSettings.add(flat);
        sharp = new JRadioButton("Sharp");
        addSettings.add(sharp);


        // Note length slider
        noteLength = new JSlider(JSlider.VERTICAL, 0, 4, 2);
        noteLength.setMajorTickSpacing(1);
        noteLength.setSnapToTicks(true);
        Hashtable<Integer, JLabel> labels = new Hashtable<>();
        labels.put(0, new JLabel("  Whole"));
        labels.put(1, new JLabel("  Half"));
        labels.put(2, new JLabel("  Quarter"));
        labels.put(3, new JLabel("  Eighth"));
        labels.put(4, new JLabel("  Sixteenth"));
        noteLength.setLabelTable(labels);
        noteLength.setPaintLabels(true);


        JPanel sets = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 20));
        sets.add(addSettings);
        sets.add(noteLength);
        ctrlButtons.add(sets);

        // Display window elements
        JPanel inScrollPane = new JPanel();
        inScrollPane.setLayout(new BoxLayout(inScrollPane, BoxLayout.Y_AXIS));
        inScrollPane.setBackground(Color.WHITE);

        JLabel songTitle = new JLabel("Welcome to SongFactory!");
        songTitle.setFont(new Font("Serif", Font.BOLD, 36));
        songTitle.setBorder(new EmptyBorder(50,70,0,0));
        inScrollPane.add(songTitle);

        sheetMusic = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            MusicView mv = new MusicView(this);
            sheetMusic.add(mv);
            inScrollPane.add(mv);

        } // for

        JPanel display = new JPanel(new BorderLayout());
        JScrollPane displayArea = new JScrollPane(
                inScrollPane,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS
        );
        display.add(displayArea, BorderLayout.CENTER);


        // Status bar elements
        JPanel license = new JPanel();
        license.setBackground(Color.BLACK);
        status = new JLabel("SongFactory");
        status.setForeground(Color.WHITE);
        license.add(status);


        // Event listeners
        exit.addActionListener(e -> System.exit(0));

        play.addActionListener(e -> {

            setStatusText("Play");

            for (MusicView mv : sheetMusic) {
                mv.playAnimation();

            } // for

        });

        stop.addActionListener(e -> {

            setStatusText("Stop");

            for (MusicView mv : sheetMusic) {
                mv.setPlaying(false);

            } // for

        });

        select.addActionListener(e -> {
            mode = Mode.SELECT;
            setStatusText("Select");

        });

        pen.addActionListener(e -> {
            mode = Mode.DRAW;
            setStatusText("Pen");
        });

        create.addActionListener(e -> {
            setStatusText("New Staff");
            MusicView mv = new MusicView(this, sheetMusic.get(0).getNumMeasures());
            sheetMusic.add(mv);
            inScrollPane.add(mv);
            setDeletableStaff(sheetMusic.size());
            inScrollPane.revalidate();
            inScrollPane.repaint();

        });

        deleteMenu.addActionListener(e -> {
            setStatusText("Delete Staff");
            MusicView mv = sheetMusic.get(sheetMusic.size() - 1);
            sheetMusic.remove(mv);
            inScrollPane.remove(mv);
            setDeletableStaff(sheetMusic.size());
            inScrollPane.revalidate();
            inScrollPane.repaint();

        });

        newStaff.addActionListener(e -> {
            setStatusText("New Staff");
            MusicView mv = new MusicView(this, sheetMusic.get(0).getNumMeasures());
            sheetMusic.add(mv);
            inScrollPane.add(mv);
            setDeletableStaff(sheetMusic.size());
            inScrollPane.revalidate();
            inScrollPane.repaint();

        });

        delete.addActionListener(e -> {
            setStatusText("Delete Staff");
            MusicView mv = sheetMusic.get(sheetMusic.size() - 1);
            sheetMusic.remove(mv);
            inScrollPane.remove(mv);
            setDeletableStaff(sheetMusic.size());
            inScrollPane.revalidate();
            inScrollPane.repaint();

        });

        newMeasure.addActionListener(e -> {
            setStatusText("New Measure");
            for (MusicView mv : sheetMusic) {
                mv.addMeasure();

            } // for
            setDeletableMeasure(sheetMusic.get(0).getNumMeasures());

        });

        deleteMeasure.addActionListener(e -> {
            setStatusText("Delete Measure");
            for (MusicView mv : sheetMusic) {
                mv.removeMeasure();

            } // for
            setDeletableMeasure(sheetMusic.get(0).getNumMeasures());

        });

        note.addActionListener(e -> {
            if (note.isSelected()) {
                // Select Note and deselect other options
                setStatusText("Note");
                rest.setSelected(false);
                flat.setSelected(false);
                sharp.setSelected(false);
                selectType = 0;

            } else {
                // Disallow Note from being deselected
                note.setSelected(true);

            } // if

        });

        rest.addActionListener(e -> {
            if (rest.isSelected()) {
                // Select Rest and deselect other options
                setStatusText("Rest");
                note.setSelected(false);
                flat.setSelected(false);
                sharp.setSelected(false);
                selectType = 1;

            } else {
                // Disallow Rest from being deselected
                rest.setSelected(true);

            } // if

        });

        flat.addActionListener(e -> {
            if (flat.isSelected()) {
                // Select Rest and deselect other options
                setStatusText("Flat");
                note.setSelected(false);
                rest.setSelected(false);
                sharp.setSelected(false);
                selectType = 2;

            } else {
                // Disallow Flat from being deselected
                flat.setSelected(true);

            } // if

        });

        sharp.addActionListener(e -> {
            // Select Sharp and deselect other options
            if (sharp.isSelected()) {
                setStatusText("Sharp");
                note.setSelected(false);
                rest.setSelected(false);
                flat.setSelected(false);
                selectType = 3;

            } else {
                // Disallow Sharp from being deselected
                sharp.setSelected(true);

            } // if

        });


        // Build scene
        frame.setMinimumSize(new Dimension(300, 725));
        frame.setPreferredSize(new Dimension(1200, 725));

        frame.getContentPane().add(menu, BorderLayout.NORTH);
        control.add(ctrlButtons, BorderLayout.NORTH);
        frame.getContentPane().add(control, BorderLayout.WEST);
        frame.getContentPane().add(display, BorderLayout.CENTER);
        frame.getContentPane().add(license, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);

    } // Constructor

    /**
     * Sets the status text at the bottom of the screen.
     *
     * @param operation Name of last performed operation
     */
    public void setStatusText(String operation) {
        status.setText(operation);

    } // setStatusText

    /**
     * Returns the node type shown by the radio buttons.
     * (0 = note, 1 = rest, 2 = flat, 3 = sharp)
     *
     * @return node type shown by radio buttons
     */
    public int getSelectType() {
        return selectType;

    } // getSelectType

    /**
     * Returns the decimal value for the type of
     * note represented by the slider.
     *
     * @return true length of slider value for node length
     */
    public double getSelectLength() {
        return 1 / Math.pow(2.0, noteLength.getValue());

    } // getSelectLength

    /**
     * Sets the position of the node length slider.
     *
     * @param n true length of node length to display on the slider
     */
    public void setSelectLength(double n) {
        noteLength.setValue((int)(Math.log(1.0 / n) / Math.log(2)));

    } // setSelectLength

    /**
     * Click one of the node type radio buttons,
     * causing it to activate.
     * (0 = note, 1 = rest, 2 = flat, 3 = sharp)
     *
     * @param n node type to set radio buttons to
     */
    public void setSelectTypeStatus(int n) {

        switch (n) {

            case 0: note.doClick(); break;
            case 1: rest.doClick(); break;
            case 2: flat.doClick(); break;
            case 3: sharp.doClick(); break;
            default: break;

        } // switch

    } // setNodeTypeStatus

    /**
     * Determines if the delete buttons are active.
     *
     * @param length number of measures in a MusicView
     */
    public void setDeletableStaff(int length) {

        if (length > 1) {
            delete.setEnabled(true);
            deleteMenu.setEnabled(true);

        } else {
            delete.setEnabled(false);
            deleteMenu.setEnabled(false);

        } // if

    } // setDeletableStaff

    /**
     * Determines if the delete buttons are active.
     *
     * @param length number of measures in a MusicView
     */
    public void setDeletableMeasure(int length) {

        if (length > 1) {
            deleteMeasure.setEnabled(true);

        } else {
            deleteMeasure.setEnabled(false);

        } // if

    } // setDeletableMeasure

    /**
     * Returns the editing mode that the
     * application is currently in.
     *
     * @return current editing mode for application
     */
    public Mode getMode() {
        return mode;

    } // getMode

    /**
     * Sets all staffs to a specific measure length.
     *
     * @param length measure length to set staffs to
     */
    public void updateMeasureLength(int length) {

        for (MusicView mv : sheetMusic) {

            while (mv.getNumMeasures() < length) {
                mv.addMeasure();

            } // while

            while (mv.getNumMeasures() > length) {
                mv.removeMeasure();

            } // while

        } // for

    } // updateMeasureLength

} // SwingApp