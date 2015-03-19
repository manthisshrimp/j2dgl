package j2dgl;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.image.BufferStrategy;

public class RenderThread extends Thread {

    private Graphics2D g2;
    private long timeBeginLoop;
    private int fps;
    private long iteration = 0;
    private BufferStrategy buffer;
    private final Core core;
    private boolean rendering = true;
    private Insets insets;

    public RenderThread(Core core) {
        this.core = core;
    }

    @Override
    public void run() {
        timeBeginLoop = System.nanoTime();
        while (true) {
            try {
                if (rendering && buffer != null) {
                    iteration++;
                    if ((iteration % 15) == 0) {
                        fps = (int) ((1 / ((float) (System.nanoTime() - timeBeginLoop))) * 1000000000);
                    }
                    timeBeginLoop = System.nanoTime();

                    g2 = (Graphics2D) buffer.getDrawGraphics();

                    if (core.fullScreen) {
                        double inWidth = core.resolution.width;
                        double ratio = core.frame.getWidth() / inWidth;
                        g2.scale(ratio, ratio);
                    } else {
                        g2.translate(insets.left, insets.top);
                    }

                    g2.setColor(Color.black);
                    g2.fillRect(0, 0, core.resolution.width, core.resolution.height);

                    core.draw(g2);

                    if (core.showDebug) {
                        core.drawDebug(g2);
                        g2.setColor(Color.GREEN);
                        g2.drawString(String.valueOf(fps), 5, 15);
                    }

                    if (!buffer.contentsLost()) {
                        buffer.show();
                    } else {
                        System.out.println("Buffer contents lost");
                    }
//                    rendering = false;
                }

//                try {
//                    Thread.sleep(4);
//                } catch (InterruptedException e) {
//                    System.out.println("RenderThread woke up.");
//                }
            } catch (NullPointerException ex) {
//                 Try to go on...
            } finally {
                if (g2 != null) {
                    g2.dispose();
                }
            }
        }
    }

    public void disableRendering() {
        rendering = false;
    }

    public void enableRendering(BufferStrategy bufferStrategy, Insets insets) {
        this.insets = insets;
        this.buffer = bufferStrategy;
        rendering = true;
    }
}
