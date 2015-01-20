package j2dgl.entity;

import j2dgl.Entity;
import j2dgl.render.Renderable;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class AnimatedEntity extends Entity implements Renderable {

    private final ArrayList<BufferedImage> images;
    private int currentIndex = 0;
    private final int waitTicks;
    private int waitedTicks = 0;
    private final int interLoopTicks;
    private int interLoopWaitedTicks = 0;

    public AnimatedEntity(double x, double y, int width, int height,
            int waitTicks, int interLoopTicks, ArrayList<BufferedImage> images) {
        super(x, y, width, height);
        this.waitTicks = waitTicks;
        this.interLoopTicks = interLoopTicks;
        this.images = images;
    }

    @Override
    public void update() {
        // Delay between loops.
        if (interLoopWaitedTicks == interLoopTicks) {
            // Delay between frames.
            if (waitedTicks == waitTicks) {
                // Check if at end of animation.
                if (currentIndex + 1 == images.size()) {
                    currentIndex = 0;
                    interLoopWaitedTicks = 0;
                } else {
                    currentIndex++;
                }
                waitedTicks = 0;
            } else {
                waitedTicks++;
            }
        } else {
            interLoopWaitedTicks++;
        }
    }

    @Override
    public BufferedImage getImage() {
        return images.get(currentIndex);
    }

    @Override
    public Point getLocation() {
        return super.getLocation();
    }
}
