package j2dgl.entity;

import j2dgl.intersect.Intersectable;
import j2dgl.render.Drawable;
import j2dgl.update.Updatable;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

public abstract class Entity implements Updatable, Drawable, Intersectable, Disposable {

    private boolean disposeLater = false;

    public double xVelocity = 0;
    public double yVelocity = 0;
    public double x;
    public double y;
    public int width;
    public int height;
    public double mass = 0;
    public double drag = 0;
    public boolean visible = true;

    private Point target = null;
    private Rectangle bounds = null;

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
    
    public Entity(double x, double y, double mass, int width, int height) {
        this(x, y, width, height);
        this.mass = mass;
    }
    
    public Entity(double x, double y, double mass, int width, int height, double drag) {
        this(x, y, mass, width, height);
        this.drag = drag;
    }

    @Override
    public final void update() {
        x += xVelocity;
        y += yVelocity;
        if (target != null) {
            checkTargetReached();
        }
        if (drag > 0) {
            applyResistance();
        }
        applyLogic();
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

    public final void setVelocity(double xDelta, double yDelta, double speed) {
        double tDelta = Math.sqrt(Math.pow(xDelta, 2) + Math.pow(yDelta, 2));
        if (tDelta > 0 && speed > 0) {
            xVelocity = xDelta / tDelta * speed;
            yVelocity = yDelta / tDelta * speed;
        }
    }

    public final void setTarget(int x, int y) {
        target = new Point(x, y);
    }

    public final void setTargetAndMove(int x, int y, double speed) {
        setTarget(x, y);
        setVelocity(x, y, speed);
    }

    private void checkTargetReached() {
        if ((yVelocity < 0 && y <= target.y) || (yVelocity > 0 && y >= target.y)) {
            yVelocity = 0;
            y = target.y;
        }
        if ((xVelocity < 0 && x <= target.x) || (xVelocity > 0 && x >= target.x)) {
            xVelocity = 0;
            x = target.x;
        }
    }

    private void applyResistance() {
//         Calculate current speed.
        double speed = Math.sqrt(Math.pow(xVelocity, 2) + Math.pow(yVelocity, 2));
//        So the object eventually stops.
        if (speed > 0.05) {
//             Calculate current direction.
            double xRatio = xVelocity / speed;
            double yRatio = yVelocity / speed;
//             Calcualte decelaration.
            double deceleration = drag * speed;
//             Apply deceleration.
            xVelocity += (-xRatio) * deceleration;
            yVelocity += (-yRatio) * deceleration;
        } else {
            xVelocity = 0;
            yVelocity = 0;
        }
    }

    public void applyForce(double force, double xDirr, double yDirr) {
        if (mass != 0) {
            // Calculate acceleration to be applied.
            double acceleration = force / mass;
            // Calculate direction.
            double dirDist = Math.sqrt(Math.pow(xDirr, 2) + Math.pow(yDirr, 2));
            double xRatio = xDirr / dirDist;
            double yRatio = yDirr / dirDist;
            // Apply acceleration using direction.
            xVelocity += xRatio * acceleration;
            yVelocity += yRatio * acceleration;
        }
    }

    public final void processMomentumCollision(Entity otherEntity) {
        double m1 = this.mass;
        double m2 = otherEntity.mass;

        // Adjust speeds based on momentum exchange
        double thisNewXSpeed = (((m1 - m2) / (m1 + m2)) * this.xVelocity)
                + (((m2 * 2) / (m1 + m2)) * otherEntity.xVelocity);
        double thisNewYSpeed = (((m1 - m2) / (m1 + m2)) * this.yVelocity)
                + (((m2 * 2) / (m1 + m2)) * otherEntity.yVelocity);

        double otherNewXSpeed = (((m2 - m1) / (m1 + m2)) * otherEntity.xVelocity)
                + (((m1 * 2) / (m1 + m2)) * this.xVelocity);
        double otherNewYSpeed = (((m2 - m1) / (m1 + m2)) * otherEntity.yVelocity)
                + (((m1 * 2) / (m1 + m2)) * this.yVelocity);

        this.xVelocity = thisNewXSpeed;
        this.yVelocity = thisNewYSpeed;

        otherEntity.xVelocity = otherNewXSpeed;
        otherEntity.yVelocity = otherNewYSpeed;
    }

    public boolean intersects(Entity otherEntity) {
        return this.getBounds().intersects(otherEntity.getBounds());
    }

    public boolean aboutToIntersect(Entity otherEntity) {
        Rectangle thisFutureRect = new Rectangle(this.getBounds());
        thisFutureRect.x += this.xVelocity;
        thisFutureRect.y += this.yVelocity;

        Rectangle otherFutureRect = new Rectangle(otherEntity.getBounds());
        otherFutureRect.x += otherEntity.xVelocity;
        otherFutureRect.y += otherEntity.yVelocity;

        return thisFutureRect.intersects(otherFutureRect);
    }

    @Override
    public Rectangle getBounds() {
        if (bounds == null) {
            bounds = new Rectangle((int) x, (int) y, width, height);
        }
        bounds.x = (int) x;
        bounds.y = (int) y;
        bounds.width = width;
        bounds.height = height;
        return bounds;
    }
    
    @Override
    public void processIntersectionWith(Intersectable intersectable) {
        
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
