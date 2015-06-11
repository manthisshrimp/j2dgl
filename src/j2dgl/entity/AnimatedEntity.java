package j2dgl.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class AnimatedEntity extends Entity {

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
    protected void applyLogic() {
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
    protected void drawSelf(Graphics2D g2) {
        g2.drawImage(images.get(currentIndex), null, 0, 0);
    }

}
