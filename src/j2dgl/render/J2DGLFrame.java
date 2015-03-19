package j2dgl.render;

import utility.BooleanHolder;
import j2dgl.RenderThread;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import utility.Passback;
import javax.xml.ws.Holder;

public class J2DGLFrame extends javax.swing.JFrame {

    private final GraphicsDevice screenDevice = GraphicsEnvironment
            .getLocalGraphicsEnvironment().getDefaultScreenDevice();
    private final ArrayList<Integer> keyQueue;
    private final Holder<MouseEvent> lastMouseEvent;
    private final RenderThread renderThread;
    private final BooleanHolder mouseDown;
    private final Runnable exitMethod;
    private final Passback keyTypedMethod;
    private Insets insets;
    private final Dimension resolution;
    private final Point mouse;
    private boolean fullscreen = false;

    private double mouseXCorrection = 1;
    private double mouseYCorrection = 1;

    public J2DGLFrame(ArrayList<Integer> keyQueue, Holder<MouseEvent> lastMouseEvent, 
            Dimension resolution, RenderThread renderThread, BooleanHolder mouseDown, 
            Runnable exitMethod, Point mouse, Passback keyTypedMethod) throws HeadlessException {
        this.keyQueue = keyQueue;
        this.lastMouseEvent = lastMouseEvent;
        this.renderThread = renderThread;
        this.mouseDown = mouseDown;
        this.exitMethod = exitMethod;
        this.resolution = resolution;
        this.mouse = mouse;
        this.keyTypedMethod = keyTypedMethod;

        setIgnoreRepaint(true);
        setResizable(false);

        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });
        addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                formMouseWheelMoved(evt);
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                formMouseReleased(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                formKeyTyped(evt);
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

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        if (!keyQueue.contains(evt.getKeyCode())) {
            keyQueue.add(evt.getKeyCode());
        }
    }//GEN-LAST:event_formKeyPressed

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
        if (keyQueue.contains(evt.getKeyCode())) {
            keyQueue.remove((Integer) evt.getKeyCode());
        }
    }//GEN-LAST:event_formKeyReleased

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
//        exitMethod.run();
    }//GEN-LAST:event_formWindowClosed

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        lastMouseEvent.value = evt;
        mouseDown.setValue(true);
    }//GEN-LAST:event_formMousePressed

    private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
        lastMouseEvent.value = evt;
        mouseDown.setValue(false);
    }//GEN-LAST:event_formMouseReleased

    private void formMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_formMouseWheelMoved
        lastMouseEvent.value = evt;
    }//GEN-LAST:event_formMouseWheelMoved

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        exitMethod.run();
    }//GEN-LAST:event_formWindowClosing

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
        updateMouse(evt);
    }//GEN-LAST:event_formMouseMoved

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        updateMouse(evt);
    }//GEN-LAST:event_formMouseDragged

    private void formKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyTyped
        keyTypedMethod.setPassBackObject(evt);
        keyTypedMethod.run();
    }//GEN-LAST:event_formKeyTyped

    private void updateMouse(MouseEvent evt) {
        if (fullscreen) {
            mouse.x = (int) Math.round(evt.getPoint().x * mouseXCorrection);
            mouse.y = (int) Math.round(evt.getPoint().y * mouseYCorrection);
        } else {
            mouse.x = evt.getPoint().x - insets.left;
            mouse.y = evt.getPoint().y - insets.top;
        }
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
        boolean wasResizable = isResizable();
        if (fullscreen) {
            renderThread.disableRendering();
            setVisible(false);
            setResizable(false);
            dispose();
            setUndecorated(true);
            screenDevice.setFullScreenWindow(this);
            setLocationRelativeTo(null);
            setVisible(true);
            mouseXCorrection = resolution.width * 1D / getWidth();
            mouseYCorrection = resolution.height * 1D / getHeight();
            renderThread.enableRendering(getBufferStrategy(), insets);
            requestFocus();
        } else {
            renderThread.disableRendering();
            setVisible(false);
            dispose();
            setUndecorated(false);
            setResizable(wasResizable);
            screenDevice.setFullScreenWindow(null);
            setLocationRelativeTo(null);
            setVisible(true);
            mouseXCorrection = 1;
            mouseYCorrection = 1;
            renderThread.enableRendering(getBufferStrategy(), insets);
            requestFocus();
        }
        java.awt.EventQueue.invokeLater(() -> {
            toFront();
        });
        keyQueue.clear();
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

    public void display() {
        insets = getInsets();
        setSize(resolution.width + insets.left,
                resolution.height + insets.top);
        setLocationRelativeTo(null);
        setVisible(true);
        createBufferStrategy(2);
        renderThread.enableRendering(getBufferStrategy(), insets);
        renderThread.start();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
