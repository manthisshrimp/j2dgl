package j2dgl;

import j2dgl.render.J2DGLFrame;
import java.awt.Point;

public class FullScreenAdaptiveIH extends InputHandler {

    private final J2DGLFrame frame;

    public FullScreenAdaptiveIH(J2DGLFrame inputFrame) {
        super(inputFrame);
        this.frame = inputFrame;
    }

    @Override
    public Point getMouse() {
        Point mouse = super.getMouse();
        if (frame.isFullscreen()) {
            mouse.x *= (frame.getContentWidth() / (double) frame.getWidth());
            mouse.y *= (frame.getContentHeight() / (double) frame.getHeight());
        }
        return mouse;
    }
}
