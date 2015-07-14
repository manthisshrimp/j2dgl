package j2dgl.render;

import java.awt.Graphics2D;

public interface DebugDrawable {
    
    public void drawDebug(Graphics2D g2, int xOffset, int yOffset, int fps);
    
}
