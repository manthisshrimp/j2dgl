package j2dgl.entity;

import java.awt.Point;
import java.awt.Rectangle;

public abstract class Entity {

    public boolean visible = true;
    private boolean disposeLater = false;

    public double xIncrement = 0;
    public double yIncrement = 0;
    public double x;
    public double y;
    public int width;
    public int height;

    public Point target = new Point();
    private Point location = new Point();

    public Entity(double x, double y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public final void setTarget(int x, int y, double speed) {
        target = new Point(x, y);

        double xChange = target.x - this.x;
        double yChange = target.y - this.y;

        double distToTarget = Math.sqrt(Math.pow(xChange, 2) + Math.pow(yChange, 2));

        if (distToTarget > 0 && speed > 0) {
            xIncrement = xChange / distToTarget * speed;
            yIncrement = yChange / distToTarget * speed;
        }
    }

    protected void moveToTarget() {
        if (yIncrement < 0 && y <= target.y || yIncrement > 0 && y >= target.y) {
            yIncrement = 0;
            y = target.y;
        }

        if (xIncrement < 0 && x <= target.x || xIncrement > 0 && x >= target.x) {
            xIncrement = 0;
            x = target.x;
        }

        x += xIncrement;
        y += yIncrement;
    }

    public abstract void update();

    public boolean intersects(Entity otherEntity) {
        return this.getBounds().intersects(otherEntity.getBounds());
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }

    public boolean disposalRequired() {
        return disposeLater;
    }

    public void disposeLater() {
        disposeLater = true;
    }

    public Point getLocation() {
        location.x = (int) x;
        location.y = (int) y;
        return location;
    }
}
