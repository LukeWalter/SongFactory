package songfactory.ui;

import songfactory.music.*;
import songfactory.ui.notation.JMusicNode;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Hashtable;
import javax.swing.*;

public class SwingApp {

    private int staffNum; // Number of staves
    private JLabel placeholder; // Editable text in display panel
    private JLabel status; // Editable status text

    private JMusicNode selected;
    private int selectType;
    private JSlider noteLength;

    public SwingApp() {

        staffNum = 4;
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
        JMenuItem create = new JMenuItem("New Measure");
        JMenuItem delete = new JMenuItem("Delete Measure");
        delete.setEnabled(false);
        edit.add(create);
        edit.add(delete);

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


        JButton newStaff = new JButton("New Measure");
        JButton deleteStaff = new JButton("Delete Measure");
        deleteStaff.setEnabled(false);

        JPanel nds = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 30));
        nds.add(newStaff);
        nds.add(deleteStaff);
        ctrlButtons.add(nds);

        JSeparator two = new JSeparator();
        ctrlButtons.add(two);

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

        JRadioButton note = new JRadioButton("Note");
        note.setSelected(true);
        addSettings.add(note);
        JRadioButton rest = new JRadioButton("Rest");
        addSettings.add(rest);
        JRadioButton flat = new JRadioButton("Flat");
        addSettings.add(flat);
        JRadioButton sharp = new JRadioButton("Sharp");
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
        JPanel inScrollPane = new JPanel(new FlowLayout(FlowLayout.LEADING, 20, 20));
        inScrollPane.setBackground(Color.WHITE);
        MusicView sheetMusic = new MusicView(this);
        placeholder = new JLabel("Welcome to SongFactory!");
//        inScrollPane.add(placeholder);
        inScrollPane.add(sheetMusic);

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
        select.addActionListener(e -> setStatusText("Select"));
        pen.addActionListener(e -> setStatusText("Pen"));
        play.addActionListener(e -> setStatusText("Play"));
        stop.addActionListener(e -> setStatusText("Stop"));

        create.addActionListener(e -> {
            // Add to staffNum and enable delete buttons
            setStatusText("New Measure");
            setPlaceholderText(++staffNum + " Staves");
            delete.setEnabled(true);
            deleteStaff.setEnabled(true);
            sheetMusic.addMeasure();
        });

        delete.addActionListener(e -> {
            // Decrement staffNum and disable delete buttons if not enough staves
            setStatusText("Delete Measure");
            if (staffNum > 1) {

                setPlaceholderText(--staffNum + " Staves");
                sheetMusic.removeMeasure();

                if (staffNum == 1) {
                    delete.setEnabled(false);
                    deleteStaff.setEnabled(false);

                } // if

            } // if
        });

        newStaff.addActionListener(e -> {
            // Add to staffNum and enable delete buttons
            setStatusText("New Measure");
            setPlaceholderText(++staffNum + " Staves");
            delete.setEnabled(true);
            deleteStaff.setEnabled(true);
            sheetMusic.addMeasure();

        });

        deleteStaff.addActionListener(e -> {
            // Decrement staffNum and disable delete buttons if not enough staves
            setStatusText("Delete Measure");
            if (staffNum > 1) {

                setPlaceholderText(--staffNum + " Staves");
                sheetMusic.removeMeasure();

                if (staffNum == 1) {
                    delete.setEnabled(false);
                    deleteStaff.setEnabled(false);

                } // if

            } // if

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
        frame.setMinimumSize(new Dimension(300, 650));
        frame.setPreferredSize(new Dimension(700, 650));

        frame.getContentPane().add(menu, BorderLayout.NORTH);
        control.add(ctrlButtons, BorderLayout.NORTH);
        frame.getContentPane().add(control, BorderLayout.WEST);
        frame.getContentPane().add(display, BorderLayout.CENTER);
        frame.getContentPane().add(license, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);

    } // Constructor

    public void setPlaceholderText(String newText) {
        placeholder.setText(newText);

    } // setPlaceholderText

    public void setStatusText(String operation) {
        status.setText(operation);

    } // setStatusText

    public int getSelectType() {
        return selectType;

    } // getSelectType

    public double getSelectLength() {
        return 1 / Math.pow(2.0, noteLength.getValue());

    } // getSelectLength

} // SwingApp