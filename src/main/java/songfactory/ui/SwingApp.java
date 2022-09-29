package songfactory.ui;

import songfactory.ui.notation.JMusicNode;
import songfactory.ui.notation.TrebleClef;

import java.awt.*;
import java.util.Hashtable;
import javax.swing.*;
import javax.swing.ScrollPaneConstants.*;

public class SwingApp {

    int staffNum; // Number of staves
    private JLabel placeholder; // Editable text in display panel
    private JLabel status; // Editable status text

    public SwingApp() {

        staffNum = 1;

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
        JMenuItem delete = new JMenuItem("Delete Staff");
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


        JButton newStaff = new JButton("New Staff");
        JButton deleteStaff = new JButton("Delete Staff");
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
        JSlider noteLength = new JSlider(JSlider.VERTICAL, 0, 4, 2);
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


        // Music Components

        MusicView sheetMusic = new MusicView();
//        JMusicNode t = new TrebleClef();

        // Display window elements
        JPanel display = new JPanel(new BorderLayout());
        JPanel inScrollPane = new JPanel(new FlowLayout(FlowLayout.LEADING, 20, 20));
        inScrollPane.setBackground(Color.WHITE);
        placeholder = new JLabel("Welcome to SongFactory!");
//        inScrollPane.add(placeholder);
        inScrollPane.add(sheetMusic);


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
            setStatusText("New Staff");
            setPlaceholderText(++staffNum + " Staves");
            delete.setEnabled(true);
            deleteStaff.setEnabled(true);
            sheetMusic.updateComponent();
        });

        delete.addActionListener(e -> {
            // Decrement staffNum and disable delete buttons if not enough staves
            setStatusText("Delete Staff");
            if (staffNum > 1) {
                setPlaceholderText(--staffNum + " Staves");
                if (staffNum == 1) {
                    delete.setEnabled(false);
                    deleteStaff.setEnabled(false);

                } // if

            } // if
        });

        newStaff.addActionListener(e -> {
            // Add to staffNum and enable delete buttons
            setStatusText("New Staff");
            setPlaceholderText(++staffNum + " Staves");
            delete.setEnabled(true);
            deleteStaff.setEnabled(true);

        });

        deleteStaff.addActionListener(e -> {
            // Decrement staffNum and disable delete buttons if not enough staves
            setStatusText("Delete Staff");
            if (staffNum > 1) {
                setPlaceholderText(--staffNum + " Staves");
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

    private void setPlaceholderText(String newText) {
        placeholder.setText(newText);

    } // setPlaceholderText

    private void setStatusText(String operation) {
        status.setText(operation);

    } // setStatusText

} // SwingApp