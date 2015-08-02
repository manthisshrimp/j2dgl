package j2dgl.entity;

import j2dgl.render.Drawable;
import j2dgl.update.Updatable;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

public abstract class Entity implements Updatable, Drawable {

    private boolean disposeLater = false;

    public double xIncrement = 0;
    public double yIncrement = 0;
    public double x;
    public double y;
    public int width;
    public int height;
    public double mass = 0;
    public double drag = 0;
    public boolean visible = true;

    public Point target = null;

    public Entity(double x, double y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Entity(double x, double y, int width, int height, double drag) {
        this(x, y, width, height);
        this.drag = drag;
    }

    @Override
    public final void update() {
        applyLogic();
        x += xIncrement;
        y += yIncrement;
        if (target != null) {
            checkTargetReached();
        }
        if (drag > 0) {
            applyResistance();
        }
    }

    protected void applyLogic() {

    }

    @Override
    public final void draw(Graphics2D g2, int xOffset, int yOffset) {
        if (visible) {
            AffineTransform oldTransform = g2.getTransform();
            g2.translate(x + xOffset, y + yOffset);
            drawSelf(g2);
            g2.setTransform(oldTransform);
        }
    }

    protected void drawSelf(Graphics2D g2) {

    }

    public final void applyMovement(double xDelta, double yDelta, double speed) {
        double tDelta = Math.sqrt(Math.pow(xDelta, 2) + Math.pow(yDelta, 2));
        if (tDelta > 0 && speed > 0) {
            xIncrement = xDelta / tDelta * speed;
            yIncrement = yDelta / tDelta * speed;
        }
    }

    public final void setTarget(int x, int y) {
        target = new Point(x, y);
    }

    public final void setTargetAndMove(int x, int y, double speed) {
        setTarget(x, y);
        applyMovement(x, y, speed);
    }

    private void checkTargetReached() {
        if (yIncrement < 0 && y <= target.y || yIncrement > 0 && y >= target.y) {
            yIncrement = 0;
            y = target.y;
        }
        if (xIncrement < 0 && x <= target.x || xIncrement > 0 && x >= target.x) {
            xIncrement = 0;
            x = target.x;
        }
    }

    private void applyResistance() {
        // Calculate current speed
        double speed = Math.sqrt(Math.pow(xIncrement, 2) + Math.pow(yIncrement, 2));
        if (speed > 0.05) {
            // Calculate current direction
            double xRatio = xIncrement / speed;
            double yRatio = yIncrement / speed;
            // Calcualte decelarating force
            double dec = drag * speed;
            // Apply decelaration
            xIncrement += (-xRatio) * dec;
            yIncrement += (-yRatio) * dec;
        } else {
            xIncrement = 0;
            yIncrement = 0;
        }
    }

    public void applyForce(double force, double xDirr, double yDirr) {
        if (mass != 0) {
            // Calculate accelaration to be applied
            double acc = force / mass;
            // Calculate direction
            double dirDist = Math.sqrt(Math.pow(xDirr, 2) + Math.pow(yDirr, 2));
            double xRatio = xDirr / dirDist;
            double yRatio = yDirr / dirDist;
            // Apply accelaration using direction
            xIncrement += xRatio * acc;
            yIncrement += yRatio * acc;
        }
    }

    public final void processCollision(Entity otherEntity) {
        double m1 = this.mass;
        double m2 = otherEntity.mass;

        // Adjust speeds based on momentum exchange
        double thisNewXSpeed = (((m1 - m2) / (m1 + m2)) * this.xIncrement)
                + (((m2 * 2) / (m1 + m2)) * otherEntity.xIncrement);
        double thisNewYSpeed = (((m1 - m2) / (m1 + m2)) * this.yIncrement)
                + (((m2 * 2) / (m1 + m2)) * otherEntity.yIncrement);

        double otherNewXSpeed = (((m2 - m1) / (m1 + m2)) * otherEntity.xIncrement)
                + (((m1 * 2) / (m1 + m2)) * this.xIncrement);
        double otherNewYSpeed = (((m2 - m1) / (m1 + m2)) * otherEntity.yIncrement)
                + (((m1 * 2) / (m1 + m2)) * this.yIncrement);

        this.xIncrement = thisNewXSpeed;
        this.yIncrement = thisNewYSpeed;

        otherEntity.xIncrement = otherNewXSpeed;
        otherEntity.yIncrement = otherNewYSpeed;
    }

    public boolean intersects(Entity otherEntity) {
        return this.getBounds().intersects(otherEntity.getBounds());
    }

    public boolean aboutToIntersect(Entity otherEntity) {
        Rectangle thisFutureRect = new Rectangle(this.getBounds());
        thisFutureRect.x += this.xIncrement;
        thisFutureRect.y += this.yIncrement;

        Rectangle otherFutureRect = new Rectangle(otherEntity.getBounds());
        otherFutureRect.x += otherEntity.xIncrement;
        otherFutureRect.y += otherEntity.yIncrement;

        return thisFutureRect.intersects(otherFutureRect);
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }

    public Point getLocation() {
        return new Point((int) x, (int) y);
    }

    @Override
    public boolean needsDisposal() {
        return disposeLater;
    }

    @Override
    public void disposeLater() {
        disposeLater = true;
    }
}
