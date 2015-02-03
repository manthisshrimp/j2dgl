package j2dgl;

import j2dgl.entity.Entity;
import j2dgl.render.J2DGLFrame;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public abstract class Core {

    // Core Variables
    protected int updateRate = 60;
    public int scrollChange = 0;
    // Core Objects
    protected MouseEvent lastMouseEvent;
    protected J2DGLFrame frame;
    public RenderThread renderThread;
    protected Dimension resolution;
    private final ArrayList<Integer> keyQueue = new ArrayList<>();
    protected Point mouse = new Point(-1, -1);
    // Core Flags
    private boolean clickDisabled = false;
    protected Boalean mouseDown = new Boalean(false);

    public void forceMouseButtonState(boolean isDown) {
        mouseDown.setValue(isDown);
    }

    protected boolean fullScreen = false;
    protected boolean showDebug = false;
    protected boolean running = true;
    protected boolean waitForDraw = false;

    public Core(int width, int height) {
        resolution = new Dimension(width, height);
    }

    protected abstract void init();

    public final void startLoop() {
        renderThread = new RenderThread(this);
        frame = new J2DGLFrame(keyQueue, lastMouseEvent, resolution, renderThread, mouseDown,
                () -> {
                    exit();
                }, mouse);

        init();

        frame.display();

        long beginTime;
        long timeTaken;
        long sleepTime;

        while (running) {
            beginTime = System.nanoTime();
            coreKeyEvents();
            keyPressed(keyQueue);

            update();

            if (lastMouseEvent != null) {
                mouseDown(lastMouseEvent);
                lastMouseEvent = null;
            }

            timeTaken = System.nanoTime() - beginTime;
            sleepTime = ((1000000000L / updateRate) - timeTaken) / 1000000L;
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException ex) {
                    showErrorAndExit(ex.toString());
                }
            }
            if (clickDisabled) {
                mouseDown.setValue(false);
                clickDisabled = false;
            }
        }
        beforeClose();
        System.exit(0);
    }

    protected abstract void update();

    protected abstract void draw(Graphics2D g2);

    public void drawDebug(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, 160, resolution.height);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Serif", Font.BOLD, 14));
        g2.drawString("Update Rate: " + updateRate, 8, 40);

        g2.setColor(Color.WHITE);
        g2.drawString("Mouse X: " + mouse.x, 8, 60);
        g2.drawString("Mouse Y: " + mouse.y, 8, 80);
        g2.drawString("Mouse Down: " + mouseDown.getValue(), 8, 100);
        g2.drawString("Scroll Amount: " + scrollChange, 8, 120);

        if (keyQueue.size() > 0) {
            String keys = "";
            keys = keyQueue.stream().map((key) -> key + " ").reduce(keys, String::concat);
            g2.drawString("Keys: " + keys, 8, 140);
        }
    }

    protected abstract void keyPressed(ArrayList<Integer> keyQueue);

    private void coreKeyEvents() {
        if (keyQueue.contains(KeyEvent.VK_0)) {
            showDebug = !showDebug;
            keyQueue.remove((Integer) KeyEvent.VK_0);
        }
        if (keyQueue.contains(KeyEvent.VK_CONTROL)
                && keyQueue.contains(KeyEvent.VK_F)) {
            fullScreen = !fullScreen;
            frame.setFullscreen(fullScreen);
            keyQueue.remove((Integer) KeyEvent.VK_F);
        }
        if (keyQueue.contains(KeyEvent.VK_ESCAPE)) {
            exit();
        }
    }

    protected abstract void mouseDown(MouseEvent mouseEvent);

    public boolean isMouseOverEntity(Entity entity) {
        return mouse.x >= entity.x
                && mouse.x <= entity.x + entity.width
                && mouse.y >= entity.y
                && mouse.y <= entity.y + entity.height;
    }

    protected abstract void beforeClose();

    public void exit() {
        running = false;
    }

    public void showErrorAndExit(String errorMessage) {
        JOptionPane.showMessageDialog(frame, "ERROR: "
                + errorMessage, "AN ERROR HAS OCCURRED!",
                JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }

    public void disableClick() {
        this.clickDisabled = true;
    }
}
