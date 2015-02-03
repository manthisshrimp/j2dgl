package j2dgl.entity;

import j2dgl.render.Renderable;
import java.awt.Point;
import java.awt.image.BufferedImage;

public abstract class StaticImageEntity extends Entity implements Renderable {

    private final BufferedImage image;

    public StaticImageEntity(double x, double y, int width, int height, BufferedImage image) {
        super(x, y, width, height);
        this.image = image;
    }

    @Override
    public BufferedImage getImage() {
        return image;
    }
    
    @Override
    public Point getLocation() {
        return super.getLocation();
    }
}
