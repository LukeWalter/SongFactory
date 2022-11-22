package songfactory.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;

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

} // TimerBall