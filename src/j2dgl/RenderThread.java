package j2dgl;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.image.BufferStrategy;

public class RenderThread extends Thread {

    private static Graphics2D g2;
    private static long timeBeginLoop;
    private static int fps;
    private static long iteration = 0;
    private static BufferStrategy buffer;
    private final Core coreRef;
    private boolean rendering = true;

    public RenderThread(BufferStrategy b, Core core) {
        this.coreRef = core;
        buffer = b;
    }

    @Override
    public void run() {
        timeBeginLoop = System.nanoTime();
        while (true) {
            try {
                iteration++;
                if ((iteration % 15) == 0) {
                    fps = (int) ((1 / ((float) (System.nanoTime() - timeBeginLoop))) * 1000000000);
                }
                timeBeginLoop = System.nanoTime();

                if (rendering && buffer != null) {
                    g2 = (Graphics2D) buffer.getDrawGraphics();
                    if (!coreRef.fullScreen) {
                        Insets insets = coreRef.gameFrame.getInsets();
                        g2.translate(insets.left, insets.top);
                    }
                    double inWidth = coreRef.resolution.width;
                    double ratio = coreRef.gameFrame.getWidth() / inWidth;
                    g2.scale(ratio, ratio);
                    coreRef.draw(g2);

                    g2.setColor(Color.GREEN);
                    g2.drawString(String.valueOf(fps), 10, 10);

                    if (coreRef.showDebug) {
                        coreRef.drawDebug(g2);
                    }

                    if (!buffer.contentsLost()) {
                        buffer.show();
                    }
                }

                try {
                    Thread.sleep(4);
                } catch (InterruptedException e) {
                    System.out.println("RenderThread woke up.");
                }
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

    public void startRendering(BufferStrategy b) {
        buffer = b;
        rendering = true;
    }
}
