package j2dgl.render;

import java.awt.Point;
import java.awt.image.BufferedImage;

public interface Renderable {
    
    public BufferedImage getImage();
    
    public Point getLocation();
    
}
