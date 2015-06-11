package j2dgl.render;

import j2dgl.RenderThread;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

public class J2DGLFrame extends javax.swing.JFrame {

    private final GraphicsDevice screenDevice
            = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    private RenderThread renderThread;
    private final Dimension contentResolution;
    private boolean fullscreen = false;
    private final Drawable drawable;
    private long resizeStarted;
    private boolean selfTriggeredResize = false;

    public J2DGLFrame(Dimension contentResolution, Drawable drawable) throws HeadlessException {
        this.contentResolution = contentResolution;
        this.drawable = drawable;

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
        if (fullscreen || renderThread == null || selfTriggeredResize) {
            selfTriggeredResize = false;
            return;
        }
        if (resizeStarted == 0) {
            resizeStarted = System.nanoTime();
            renderThread.stopRendering();
            new Thread() {

                @Override
                public void run() {
                    while (System.nanoTime() - resizeStarted < 250000000) {
                        // wait
                    }
                    renderThread = new RenderThread(false, getSize(), contentResolution,
                            getBufferStrategy(), getInsets(), drawable);
                    renderThread.start();
                    resizeStarted = 0;
                    double ratio = contentResolution.height / (double) contentResolution.width;
                    selfTriggeredResize = true;
                    setSize(getWidth(), (int) (getWidth() * ratio));
                }

            }.start();
        }
        resizeStarted = System.nanoTime();
    }//GEN-LAST:event_formComponentResized

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    public void setFullscreen(boolean fullscreen) {
        if (renderThread == null) {
            return;
        }
        renderThread.stopRendering();
        this.fullscreen = fullscreen;
        if (fullscreen) {
            renderThread.stopRendering();
            setVisible(false);
            dispose();
            setUndecorated(true);
            screenDevice.setFullScreenWindow(this);
            setLocationRelativeTo(null);
            setVisible(true);
            renderThread = new RenderThread(true, getSize(), contentResolution,
                    getBufferStrategy(), getInsets(), drawable);
            requestFocus();
        } else {
            renderThread.stopRendering();
            setVisible(false);
            dispose();
            setUndecorated(false);
            screenDevice.setFullScreenWindow(null);
            setLocationRelativeTo(null);
            setVisible(true);
            renderThread = new RenderThread(false, getSize(), contentResolution,
                    getBufferStrategy(), getInsets(), drawable);
            requestFocus();
        }
        renderThread.start();
        java.awt.EventQueue.invokeLater(() -> {
            toFront();
        });
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
    public void setVisible(boolean b) {
        super.setVisible(b);
        createBufferStrategy(2);
        if (b && renderThread == null) {
            renderThread = new RenderThread(false, getSize(), contentResolution,
                    getBufferStrategy(), getInsets(), drawable);
            renderThread.start();
        }
    }

}
