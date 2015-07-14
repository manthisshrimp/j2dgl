package j2dgl.render;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

public class J2DGLFrame extends javax.swing.JFrame {

    private final GraphicsDevice screenDevice
            = GraphicsEnvironment
            .getLocalGraphicsEnvironment()
            .getDefaultScreenDevice();
    private RenderThread renderThread;
    private final Dimension contentResolution;
    private final CoreDrawable coreDrawable;
    private boolean fullscreen = false;
//    private long resizeStarted;
//    private boolean selfTriggeredResize = false;

    public J2DGLFrame(Dimension contentResolution, CoreDrawable coreDrawable) throws HeadlessException {
        this.contentResolution = contentResolution;
        this.coreDrawable = coreDrawable;

        initComponents();

        Insets insets = getInsets();
        setSize(contentResolution.width + insets.left + insets.right,
                contentResolution.height + insets.top + insets.bottom);

        setIgnoreRepaint(true);
        setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
//        if (fullscreen || renderThread == null || selfTriggeredResize) {
//            selfTriggeredResize = false;
//            return;
//        }
//        if (resizeStarted == 0) {
//            resizeStarted = System.nanoTime();
//            renderThread.stopRendering();
//            new Thread() {
//
//                @Override
//                public void run() {
//                    while (System.nanoTime() - resizeStarted < 250000000) {
//                        // wait
//                    }
//                    renderThread = new RenderThread(false, getSize(), contentResolution,
//                            getBufferStrategy(), getInsets(), drawable);
//                    renderThread.start();
//                    resizeStarted = 0;
//                    double ratio = contentResolution.height / (double) contentResolution.width;
//                    selfTriggeredResize = true;
//                    setSize(getWidth(), (int) (getWidth() * ratio));
//                }
//
//            }.start();
//        }
//        resizeStarted = System.nanoTime();
    }//GEN-LAST:event_formComponentResized

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    public void setFullscreen(boolean makeFullscreen) {
        if (renderThread == null) {
            return;
        }
        this.fullscreen = makeFullscreen;
        renderThread.stopRendering();
        setVisible(false);
        dispose();
        Dimension targetResolution;
        if (makeFullscreen) {
            setUndecorated(true);
            screenDevice.setFullScreenWindow(this);
            targetResolution = getSize();
        } else {
            setUndecorated(false);
            screenDevice.setFullScreenWindow(null);
            targetResolution = contentResolution;
        }
        setState(Frame.NORMAL);
        setLocationRelativeTo(null);
        setVisible(true);
        requestFocus();
        toFront();
        renderThread = new RenderThread(makeFullscreen, targetResolution, contentResolution,
                getBufferStrategy(), getInsets(), coreDrawable);
        renderThread.start();
    }

    public void setMouseVisible(boolean mouseVisible) {
        if (mouseVisible) {
            BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
            Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                    cursorImg, new Point(0, 0), "blank cursor");
            getContentPane().setCursor(blankCursor);
        } else {
            setCursor(Cursor.getDefaultCursor());
        }
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public int getContentWidth() {
        return contentResolution.width;
    }

    public int getContentHeight() {
        return contentResolution.height;
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        createBufferStrategy(2);
        if (visible && renderThread == null) {
            renderThread = new RenderThread(false, contentResolution, contentResolution,
                    getBufferStrategy(), getInsets(), coreDrawable);
            renderThread.start();
        }
    }

}
