package songfactory.ui;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class TimerBall extends Ellipse2D.Float {

    private float dx, dy;
    private Color color;

    public TimerBall(float x, float y) {
        super(x, y, 16, 16);
        dx = 0;
        dy = 0;
        color = Color.RED;

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

    } // drawBall

} // TimerBall