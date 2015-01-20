package j2dgl.entity;

import j2dgl.Entity;
import java.util.ArrayList;

public class AnimatedEntity extends Entity {

    private final ArrayList<String> imageNames;
    private int currentIndex = 0;
    private final int waitTicks;
    private int waitedTicks = 0;
    private final int interLoopTicks;
    private int interLoopWaitedTicks = 0;

    public AnimatedEntity(double x, double y, int width, int height,
            int waitTicks, int interLoopTicks, ArrayList<String> imageNames) {
        super(x, y, width, height);
        this.waitTicks = waitTicks;
        this.interLoopTicks = interLoopTicks;
        this.imageNames = imageNames;
    }

    @Override
    public void update() {
        // Delay between loops.
        if (interLoopWaitedTicks == interLoopTicks) {
            // Delay between frames.
            if (waitedTicks == waitTicks) {
                // Check if at end of animation.
                if (currentIndex + 1 == imageNames.size()) {
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

    public String getCurrentFrame() {
        return imageNames.get(currentIndex);
    }
}
