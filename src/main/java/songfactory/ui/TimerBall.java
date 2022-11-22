package songfactory.ui;

import songfactory.music.Measure;
import songfactory.music.MusicNode;
import songfactory.music.MusicSequence;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class TimerBall extends Ellipse2D.Float {

    private float dx, dy;
    private Color color;

    private Timer aniTimer;
    private int curr;
    private MusicView mv;

    public TimerBall(MusicView mv, float x, float y) {

        super(x, y, 16, 16);
        dx = 0;
        dy = 0;
        color = Color.RED;

        this.mv = mv;

    } // Constructor

    public void updateLocation() {
        this.x += dx;
        this.y += dy;

    } // update

    public void updateVelocity(float d2x, float d2y) {
        this.dx += d2x;
        this.dy += d2y;

    } // updateVelocity

    public void sns(float factor) {
        this.width = 16 / factor;
        this.height = 16 * factor;

    } // sns

    public void draw(Graphics2D g2d) {

        this.x -= this.width / 2;
        this.y -= this.height - 8;

        g2d.setColor(color);
        g2d.draw(this);
        g2d.fill(this);

        this.x += this.width / 2;
        this.y += this.height - 8;

    } // draw

    public void start(float distance, int ms) {

        int delay = 25;

        final float numIterations = (float) ms / delay;
        final float step = distance / numIterations;
        this.curr = 0;

        aniTimer = new Timer(delay, e -> {

            if (this.curr++ >= (int) numIterations) aniTimer.stop();
            else this.x += step;

            SwingUtilities.invokeLater(() -> this.mv.updateComponent());

        });

        aniTimer.start();

    } // start

    public void sequence() {

        List<Measure> measures = mv.getMeasures();

        Thread aniThread = new Thread(() -> {

            for (int m = 0; m < measures.size() - 1; m++) {

                MusicSequence currMeasure = measures.get(m).getNodes();

                for (int n = 0; n < currMeasure.size() - 1; n++) {
                    float noteLength = (float) currMeasure.get(n).getLength();
                    this.start(50, (int)(1600 * noteLength));
                    while (aniTimer.isRunning());

                } // for

                float noteLength = (float) currMeasure.get(currMeasure.size() - 1).getLength();
                this.start(100, (int)(1600 * noteLength));
                while (aniTimer.isRunning());

            } // for

            MusicSequence currMeasure = measures.get(measures.size() - 1).getNodes();

            for (int n = 0; n < currMeasure.size() - 1; n++) {
                float noteLength = (float) currMeasure.get(currMeasure.size() - 1).getLength();
                this.start(50, (int)(1600 * noteLength));
                while (aniTimer.isRunning());

            } // for

            mv.setPlaying(false);

        });

        aniThread.setDaemon(true);
        aniThread.start();

    } // sequence

} // TimerBall