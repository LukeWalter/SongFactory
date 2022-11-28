package songfactory.ui;

import songfactory.music.Measure;
import songfactory.music.MusicSequence;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.List;

/**
 * Class for an animated ball during play mode.
 */
public class TimerBall extends Ellipse2D.Float {

    private float dy; // Vertical acceleration
    private Color color; // Color of ball

    private Timer aniTimer; // Swing timer for animation
    private int curr; // Current frame of animation
    private MusicView mv; // Reference to parent MusicView
    private float snsFactor; // Squash and stretch factor

    /**
     * Constructor for a TimerBall.
     *
     * @param mv reference to parent MusicView
     * @param x initial x position
     * @param y initial y position
     */
    public TimerBall(MusicView mv, float x, float y) {

        super(x, y, 16, 16);
        dy = 0;
        color = Color.RED;
        snsFactor = 1;
        this.mv = mv;

    } // Constructor

    /**
     * Controls squash and stretch of TimerBall.
     *
     * @param factor amount ball is squished
     */
    public void sns(float factor) {
        this.width = 16 / factor;
        this.height = 16 * factor;

    } // sns

    /**
     * Draws the TimerBall at its position and
     * sns factor. The bottom of the ball is its
     * central point.
     *
     * @param g2d graphics 2d object to draw with
     */
    public void draw(Graphics2D g2d) {

        this.sns(snsFactor);

        this.x -= this.width / 2;
        this.y -= this.height - 8;

        g2d.setColor(color);
        g2d.draw(this);
        g2d.fill(this);

        this.x += this.width / 2;
        this.y += this.height - 8;

    } // draw

    /**
     * Runs the animation for a bounce.
     *
     * @param distance horizontal distance between start and end points
     * @param ms total time to complete animation
     */
    public void start(float distance, int ms) {

        int delay = 10; // Frame delay between timer calls
        float pauseRatio = 0.1f; // Percentage of total time dedicated to anticipation frames

        final int numIterations = ms / delay; // Total number of timer calls
        final int anticipation = (int)(numIterations * pauseRatio); // Number of anticipation frame timer calls

        // Bounce kinematics
        float vo = -1.5f / ((float) ms / 1600);
        float v = -1 * vo;
        float t = numIterations - anticipation;
        float a = (v - vo) / t;

        final float gravity = a; // Vertical acceleration per timer call
        final float step = distance / (numIterations - anticipation); // Horizontal movement per timer call
        this.curr = 0;

        aniTimer = new Timer(delay, e -> {

            // Stop animation once iterations are complete
            if (this.curr >= numIterations) {
                aniTimer.stop();

            // Accelerate downwards while in the air
            } else if (this.curr >= anticipation) {
                this.x += step;
                this.y += dy;
                this.dy += gravity;

            // Prepare to bounce
            } else if (this.curr == (anticipation - 1)) {
                this.snsFactor = 1;
                this.dy = vo;

            // Decrease sns to prepare to bounce
            } else if (this.curr > (anticipation / 2)) {
                this.snsFactor += 0.3 / (anticipation / 2);
                this.dy = 0;

            // Increase sns at the start of animation
            } else {
                this.snsFactor -= 0.3 / (anticipation / 2);
                this.dy = 0;

            } // if

            SwingUtilities.invokeLater(() -> this.mv.updateComponent());
            this.curr++;

        });

        // Activate timer
        aniTimer.start();

    } // start

    /**
     * Stops the bounce animation.
     */
    public void stop() {
        aniTimer.stop();
        mv.setPlaying(false);

    } // stop

    /**
     * Iterates through each node in the MusicView and
     * runs the bounce animation for each.
     */
    public void sequence() {

        List<Measure> measures = mv.getMeasures();

        // Start a new thread to handle full animation
        Thread aniThread = new Thread(() -> {

            for (int m = 0; m < measures.size() - 1; m++) {

                MusicSequence currMeasure = measures.get(m).getNodes();

                for (int n = 0; n < currMeasure.size() - 1; n++) {

                    // Kill thread if animation is not playing
                    if (!mv.isPlaying()) return;

                    float oldX = this.x;
                    float oldY = this.y;

                    // Start a bounce animation
                    float noteLength = (float) currMeasure.get(n).getLength();
                    this.start(50, (int)(1600 * noteLength));
                    while (aniTimer.isRunning());

                    // Manually set position for pixel-perfect distance
                    this.x = oldX + 50f;
                    this.y = oldY;

                } // for

                float oldX = this.x;
                float oldY = this.y;

                // Start a bounce animation
                float noteLength = (float) currMeasure.get(currMeasure.size() - 1).getLength();
                this.start(100, (int)(1600 * noteLength));
                while (aniTimer.isRunning());

                // Manually set position for pixel-perfect distance
                this.x = oldX + 100f;
                this.y = oldY;

            } // for

            MusicSequence currMeasure = measures.get(measures.size() - 1).getNodes();

            for (int n = 0; n < currMeasure.size() - 1; n++) {

                // Kill thread if animation is not playing
                if (!mv.isPlaying()) return;

                float oldX = this.x;
                float oldY = this.y;

                // Start a bounce animation
                float noteLength = (float) currMeasure.get(n).getLength();
                this.start(50, (int)(1600 * noteLength));
                while (aniTimer.isRunning());

                // Manually set position for pixel-perfect distance
                this.x = oldX + 50f;
                this.y = oldY;

            } // for

            // Start the final bounce animation
            float noteLength = (float) currMeasure.get(currMeasure.size() - 1).getLength();
            this.start(0, (int)(1600 * noteLength));
            while (aniTimer.isRunning());
            mv.setPlaying(false);

        });

        // Activate thread in the background
        aniThread.setDaemon(true);
        aniThread.start();

    } // sequence

} // TimerBall