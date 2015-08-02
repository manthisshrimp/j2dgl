package j2dgl.input;

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
        Point mouse;
        if (frame.isFullscreen()) {
            mouse = super.getRawMouse();
            mouse.x *= (frame.getContentWidth() / (double) frame.getWidth());
            mouse.y *= (frame.getContentHeight() / (double) frame.getHeight());
        } else {
            mouse = super.getMouse();
        }
        return mouse;
    }
}
