package j2dgl.render;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;

public class RenderThread extends Thread {

    private final long ONE_SECOND_IN_NS = 1000000000;

    private Graphics2D g2;
    private final BufferStrategy buffer;
    private final Insets insets;

    private int fps;
    private int frameCount = 0;

    private final boolean fullScreen;
    private final Dimension targetResolution;
    private final Dimension contentResolution;

    private boolean rendering = true;
    private final CoreDrawable coreDrawable;

    public RenderThread(boolean fullScreen, Dimension targetResolution, Dimension contentResolution,
            BufferStrategy buffer, Insets insets, CoreDrawable coreDrawable) {
        this.fullScreen = fullScreen;
        this.targetResolution = targetResolution;
        this.contentResolution = contentResolution;
        this.buffer = buffer;
        this.insets = insets;
        this.coreDrawable = coreDrawable;
    }

    @Override
    public void run() {
        long lastFPSUpdate = System.nanoTime();
        while (rendering) {
            try {
                if (buffer != null) {
                    if (System.nanoTime() > lastFPSUpdate + ONE_SECOND_IN_NS) {
                        fps = frameCount;
                        frameCount = 0;
                        lastFPSUpdate = System.nanoTime();
                    }
                    g2 = (Graphics2D) buffer.getDrawGraphics();
                    g2.setRenderingHint(
                            RenderingHints.KEY_TEXT_ANTIALIASING,
                            RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

                    if (!fullScreen) {
                        g2.translate(insets.left, insets.top);
                    }

                    double ratio = targetResolution.width / (double) contentResolution.width;
                    g2.scale(ratio, ratio);

                    g2.setColor(Color.black);
                    g2.fillRect(0, 0, contentResolution.width, contentResolution.height);

                    coreDrawable.draw(g2, 0, 0);
                    coreDrawable.drawDebug(g2, 0, 0, fps);

                    if (!buffer.contentsLost()) {
                        buffer.show();
                    } else {
                        System.out.println("Buffer contents lost");
                    }
                    frameCount++;
                }
            } catch (NullPointerException ex) {
                // Try to go on...
            } finally {
                if (g2 != null) {
                    g2.dispose();
                }
            }
        }
    }

    public void stopRendering() {
        rendering = false;
    }
}
