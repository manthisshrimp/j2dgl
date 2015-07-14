package j2dgl;

import j2dgl.render.CoreDrawable;
import j2dgl.render.J2DGLFrame;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;

public abstract class Core implements CoreDrawable {

    // Core Variables
    private int updateRate = 60;
    private long sleepTime;
    private final long ONE_MILLION = 1000000;
    private final long NANO_SECONDS_PER_UPDATE = 1000000000 / updateRate;
    // Core Objects
    protected J2DGLFrame frame;
    protected Dimension contentResolution;
    protected FullScreenAdaptiveIH inputHandler;

    protected boolean showDebug = false;
    protected boolean running = true;

    public Core(int width, int height) {
        this.contentResolution = new Dimension(width, height);
    }

    public Core(int width, int height, int updateRate) {
        this(width, height);
        this.updateRate = updateRate;
    }

    protected void init() {

    }

    public final void startLoop() {

        frame = new J2DGLFrame(contentResolution, this);
        inputHandler = new FullScreenAdaptiveIH(frame);

        init();

        frame.setVisible(true);

        long beginTime;
        long timeTaken;

        while (running) {
            beginTime = System.nanoTime();
            handleCoreKeyEvents();
            update();
            timeTaken = System.nanoTime() - beginTime;
            // Sleeptime is in Miliseconds (1 / 1000).
            sleepTime = (NANO_SECONDS_PER_UPDATE - timeTaken) / ONE_MILLION;
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException ex) {
                    showErrorAndExit(ex.toString());
                }
            }

        }
        beforeClose();
        System.exit(0);
    }

    protected void update() {

    }

    protected void beforeClose() {

    }

    @Override
    public void drawDebug(Graphics2D g2, int xOffset, int yOffset, int fps) {
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, 160, contentResolution.height);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Serif", Font.BOLD, 14));
        g2.drawString("Update Rate: " + updateRate, 8, 40);

        g2.setColor(Color.WHITE);
        g2.drawString("Mouse X: " + inputHandler.getMouse().x, 8, 60);
        g2.drawString("Mouse Y: " + inputHandler.getMouse().y, 8, 80);
        g2.drawString("Mouse Down: " + inputHandler.isMouseDown(), 8, 100);
        g2.drawString("SleepTime: " + sleepTime + "ms", 8, 140);
        g2.drawString("Keys: " + inputHandler.viewPressedKeys(), 8, 140);
    }

    private void handleCoreKeyEvents() {
        if (inputHandler.isKeyDownConsume(KeyEvent.VK_0)) {
            showDebug = !showDebug;
        }
        if (inputHandler.isKeysDownConsume(
                KeyEvent.VK_CONTROL,
                KeyEvent.VK_F)) {
            frame.setFullscreen(!frame.isFullscreen());
        }
    }

    public final void exit() {
        running = false;
    }

    public Dimension getContentResolution() {
        return contentResolution;
    }

    public void showErrorAndExit(String errorMessage) {
        JOptionPane.showMessageDialog(frame, "ERROR: "
                + errorMessage, "AN ERROR HAS OCCURRED!",
                JOptionPane.ERROR_MESSAGE);
        exit();
    }
}
