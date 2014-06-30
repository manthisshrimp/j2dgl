package j2dgl;

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
    protected Point mouse = new Point(0, 0);
    protected MouseEvent lastMouseEvent;
    protected J2dglFrame gameFrame;
    public RenderThread renderThread;
    protected Dimension resolution;
    // Core Flags
    protected boolean mouseDown = false;
    protected boolean doubleClicked = false;
    protected boolean performingClick = false;

    public boolean isMouseDown() {
        return mouseDown;
    }

    public boolean isPerformingClick() {
        return performingClick;
    }

    public void setPerformingClick(boolean clickPerformed) {
        this.performingClick = clickPerformed;
    }

    public void forceMouseButtonState(boolean isDown) {
        this.mouseDown = isDown;
    }

    protected boolean fullScreen = false;
    protected boolean showDebug = false;
    protected boolean running = true;
    protected boolean waitForDraw = false;

    public Core(int width, int height) {
        resolution = new Dimension(width, height);
    }

    protected abstract void init();

    public Point getMouse() {
        return mouse;
    }

    public void startLoop() {
        gameFrame = new J2dglFrame(this);
        gameFrame.setSize(resolution.width + 16, resolution.height + 30);
        gameFrame.setIgnoreRepaint(true);
        gameFrame.createBufferStrategy(2);
        renderThread = new RenderThread(gameFrame.getBufferStrategy(), this);
        renderThread.start();

        init();

        long beginTime;
        long timeTaken;
        long sleepTime;

        while (running) {
            beginTime = System.nanoTime();

            keyPressed(gameFrame.keyQueue);
            
            mouseDown(lastMouseEvent);

            update();

            doubleClicked = false;

            timeTaken = System.nanoTime() - beginTime;

            sleepTime = ((1000000000L / updateRate) - timeTaken) / 1000000L;

            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException ex) {
                    showErrorAndExit(ex.toString());
                }
            }
        }

        exit();
        System.exit(0);
    }

    protected abstract void update();

    protected abstract void draw(Graphics2D g2);

    public void drawDebug(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, 160, gameFrame.getHeight());
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Serif", Font.BOLD, 14));
        g2.drawString("Update Rate: " + updateRate, 8, 90);

        g2.setColor(Color.WHITE);
        g2.drawString("Mouse X: " + mouse.x, 8, 160);
        g2.drawString("Mouse Y: " + mouse.y, 8, 180);
        g2.drawString("Mouse Down: " + mouseDown, 8, 200);
        g2.drawString("Performing click: " + performingClick, 8, 220);

        if (gameFrame.keyQueue.size() > 0) {
            String keys = "";
            for (KeyEvent evt : gameFrame.keyQueue) {
                keys += evt.getKeyChar() + " ";
            }
            g2.drawString("Keys: " + keys, 8, 320);
        }

        g2.drawString("Scroll Amount: " + scrollChange, 8, 280);
    }

    protected abstract void keyPressed(ArrayList<KeyEvent> keyQueue);
    
    protected abstract void mouseDown(MouseEvent mouseEvent);

    public boolean isMouseOverEntity(Entity entity) {
        return mouse.x >= entity.x && mouse.x <= entity.x + entity.width 
                && mouse.y >= entity.y && mouse.y <= entity.y + entity.height;
    }

    protected abstract void exit();

    public void showErrorAndExit(String errorMessage) {
        JOptionPane.showMessageDialog(gameFrame, "ERROR: "
                + errorMessage, "AN ERROR HAS OCCURRED!",
                JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }
}
