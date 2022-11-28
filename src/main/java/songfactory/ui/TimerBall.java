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

    private float dy;
    private Color color;

    private Timer aniTimer;
    private int curr;
    private MusicView mv;
    private float snsFactor;

    public TimerBall(MusicView mv, float x, float y) {

        super(x, y, 16, 16);
        dy = 0;
        color = Color.RED;
        snsFactor = 1;
        this.mv = mv;

    } // Constructor

    public void sns(float factor) {
        this.width = 16 / factor;
        this.height = 16 * factor;

    } // sns

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

    public void start(float distance, int ms) {

        int delay = 10;
        float pauseRatio = 0.1f;

        final int numIterations = ms / delay;
        System.out.println(numIterations);
        final int anticipation = (int)(numIterations * pauseRatio);

        float vo = -1.5f / ((float) ms / 1600);
        float v = -1 * vo;
        float t = numIterations - anticipation;
        float a = (v - vo) / t;

        final float gravity = a;
        final float step = distance / (numIterations - anticipation);
        System.out.println(ms + ":" + anticipation);
        this.curr = 0;

        aniTimer = new Timer(delay, e -> {

            if (this.curr >= numIterations) {
                aniTimer.stop();

            } else if (this.curr >= anticipation) {
                this.x += step;
                this.y += dy;
                this.dy += gravity;

            } else if (this.curr == (anticipation - 1)) {
                this.snsFactor = 1;
                this.dy = vo;

            } else if (this.curr > (anticipation / 2)) {
                this.snsFactor += 0.3 / (anticipation / 2);
                this.dy = 0;

            } else {
                this.snsFactor -= 0.3 / (anticipation / 2);
                this.dy = 0;

            } // if

            SwingUtilities.invokeLater(() -> this.mv.updateComponent());
            this.curr++;

        });

        aniTimer.start();

    } // start

    public void stop() {
        aniTimer.stop();
        mv.setPlaying(false);

    } // stop

    public void sequence() {

        List<Measure> measures = mv.getMeasures();

        Thread aniThread = new Thread(() -> {

            for (int m = 0; m < measures.size() - 1; m++) {

                MusicSequence currMeasure = measures.get(m).getNodes();

                for (int n = 0; n < currMeasure.size() - 1; n++) {

                    if (!mv.isPlaying()) return;

                    float oldX = this.x;
                    float oldY = this.y;

                    float noteLength = (float) currMeasure.get(n).getLength();
                    this.start(50, (int)(1600 * noteLength));
                    while (aniTimer.isRunning());

                    this.x = oldX + 50f;
                    this.y = oldY;

                } // for

                float oldX = this.x;
                float oldY = this.y;

                float noteLength = (float) currMeasure.get(currMeasure.size() - 1).getLength();
                this.start(100, (int)(1600 * noteLength));
                while (aniTimer.isRunning());

                this.x = oldX + 100f;
                this.y = oldY;

            } // for

            MusicSequence currMeasure = measures.get(measures.size() - 1).getNodes();

            for (int n = 0; n < currMeasure.size() - 1; n++) {

                if (!mv.isPlaying()) return;

                float oldX = this.x;
                float oldY = this.y;

                float noteLength = (float) currMeasure.get(n).getLength();
                this.start(50, (int)(1600 * noteLength));
                while (aniTimer.isRunning());

                this.x = oldX + 50f;
                this.y = oldY;

            } // for

            float noteLength = (float) currMeasure.get(currMeasure.size() - 1).getLength();
            this.start(0, (int)(1600 * noteLength));
            while (aniTimer.isRunning());
            mv.setPlaying(false);

        });

        aniThread.setDaemon(true);
        aniThread.start();

    } // sequence

} // TimerBall