package j2dgl.render;

import j2dgl.entity.Entity;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Renderer<T extends Entity> {

    private final List<T> drawables = Collections.synchronizedList(new ArrayList<T>());
    private final ArrayList<T> removeList = new ArrayList<>();

    private boolean clearRequested = false;

    public void drawAll(Graphics2D g2) {
        synchronized (drawables) {
            for (T drawable : drawables) {
                if (clearRequested) {
                    clearRequested = false;
                    break;
                }
                if (drawable.needsDisposal()) {
                    removeList.add(drawable);
                } else {
                    drawable.draw(g2, 0, 0);
                }
            }
        }
        removeList.stream().forEach(drawables::remove);
        removeList.clear();
    }

    public void addDrawable(T drawable) {
        drawables.add(drawable);
    }

    public void addDrawables(T... drawables) {
        this.drawables.addAll(Arrays.asList(drawables));
    }

    public List<T> getDrawables() {
        return drawables;
    }

    public void clear() {
        clearRequested = true;
        removeList.addAll(drawables);
    }
}
