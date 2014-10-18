package j2dgl;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public abstract class Entity {

    private BufferedImage image;
    private final Graphics2D g2;

    public boolean visible = true;
    private boolean disposeLater = false;
    public boolean drawRequired = false;

    public double xSpeed;
    public double ySpeed;
    public double x;
    public double y;
    public int width;
    public int height;

    public Point target = new Point();

    public Entity(double x, double y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.xSpeed = 0;
        this.ySpeed = 0;

        image = new BufferedImage(width, height, BufferedImage.SCALE_FAST);
        g2 = (Graphics2D) image.getGraphics();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        image = getImage();
    }

    public final void setTarget(int x, int y, double speed) {
        target = new Point(x, y);

        double xChange = target.x - this.x;
        double yChange = target.y - this.y;

        double distToTarget = Math.sqrt(Math.pow(xChange, 2) + Math.pow(yChange, 2));

        if (distToTarget > 0 && speed > 0) {
            xSpeed = xChange / distToTarget * speed;
            ySpeed = yChange / distToTarget * speed;
        }
    }

    protected void moveToTarget() {
        if (ySpeed < 0 && y <= target.y || ySpeed > 0 && y >= target.y) {
            ySpeed = 0;
            y = target.y;
        }

        if (xSpeed < 0 && x <= target.x || xSpeed > 0 && x >= target.x) {
            xSpeed = 0;
            x = target.x;
        }

        x += xSpeed;
        y += ySpeed;
    }

    public abstract void update();

    public final BufferedImage getImage() {
        draw(g2);
        drawRequired = false;
        return image;
    }

    protected abstract void draw(Graphics2D g2);

    public boolean detectCollision(Entity otherEntity) {
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
    
    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
