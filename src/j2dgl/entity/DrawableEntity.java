package j2dgl.entity;

import j2dgl.render.Renderable;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public abstract class DrawableEntity extends Entity implements Renderable {

    private BufferedImage image;
    private Graphics2D g2;
    protected boolean drawRequired = true;

    public DrawableEntity(double x, double y, int width, int height) {
        super(x, y, width, height);
        initializeGraphics();
    }

    protected abstract void draw(Graphics2D g2);

    @Override
    public final BufferedImage getImage() {
        if (drawRequired) {
            draw(g2);
        }
//        drawRequired = false;
        return image;
    }

    @Override
    public Point getLocation() {
        return super.getLocation();
    }
    
    public final void initializeGraphics() {
        image = new BufferedImage(width, height, BufferedImage.SCALE_FAST);
        g2 = (Graphics2D) image.getGraphics();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
    }
}
