package j2dgl.intersect;

import java.awt.Rectangle;

public interface Intersectable {
    
    public Rectangle getBounds();
    
    public void processIntersectionWith(Intersectable intersectable);
    
}
