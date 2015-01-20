package j2dgl.render;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

public class Renderer {

    private final ArrayList<Renderable> renderables = new ArrayList<>();

    public void drawAll(Graphics2D g2) {
        renderables.stream().forEach((renderable) -> {
            Point location = renderable.getLocation();
            g2.drawImage(renderable.getImage(), null, location.x, location.y);
        });
    }

    public void addRenderable(Renderable renderable) {
        renderables.add(renderable);
    }

    public ArrayList<Renderable> getRenderables() {
        return renderables;
    }
}
