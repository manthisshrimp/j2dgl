package j2dgl;

import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class J2dglFrame extends javax.swing.JFrame {

    private final GraphicsDevice screenDevice = GraphicsEnvironment
            .getLocalGraphicsEnvironment().getDefaultScreenDevice();
    private final Core coreRef;
    public ArrayList<KeyEvent> keyQueue = new ArrayList<>();

    private double mouseXCorrection = 1;
    private double mouseYCorrection = 1;

    public J2dglFrame(Core coreReference) {
        this.coreRef = coreReference;

        initComponents();
//        setContentPane(new DrawCanvas());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                formMouseWheelMoved(evt);
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                formMouseReleased(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
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

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
        coreRef.mouse = getCorrectedMouse(evt);
    }//GEN-LAST:event_formMouseMoved

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
        KeyEvent foundKeyEvt = getKeyEventFromListByKeyCode(evt.getKeyCode());
        if (foundKeyEvt == null) {
            keyQueue.add(evt);
        }
    }//GEN-LAST:event_formKeyPressed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        coreRef.exit();
    }//GEN-LAST:event_formWindowClosing

    private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
        KeyEvent foundKeyEvt = getKeyEventFromListByKeyCode(evt.getKeyCode());
        if (keyQueue.contains(foundKeyEvt)) {
            keyQueue.remove(foundKeyEvt);
        }
    }//GEN-LAST:event_formKeyReleased

    private void formMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_formMouseWheelMoved
        coreRef.scrollChange = evt.getUnitsToScroll() * -1;
    }//GEN-LAST:event_formMouseWheelMoved

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        coreRef.mouseDown = true;
        coreRef.lastMouseEvent = evt;
    }//GEN-LAST:event_formMousePressed

    private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
        coreRef.mouseDown = false;
        coreRef.performingClick = false;
    }//GEN-LAST:event_formMouseReleased

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        if (evt.getClickCount() == 2) {
            coreRef.doubleClicked = true;
        }
    }//GEN-LAST:event_formMouseClicked

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        coreRef.mouse = getCorrectedMouse(evt);
    }//GEN-LAST:event_formMouseDragged

    private Point getCorrectedMouse(MouseEvent evt) {
        double frameCorrectedMouseX = evt.getPoint().x - 3;
        double frameCorrectedMouseY = evt.getPoint().y - 25;
        if (!coreRef.fullScreen) {
            return new Point(evt.getPoint().x - 3, evt.getPoint().y - 25);
        } else {
            double correctedMouseX = frameCorrectedMouseX * mouseXCorrection;
            double correctedMouseY = frameCorrectedMouseY * mouseYCorrection;
            return new Point((int) correctedMouseX, (int) correctedMouseY);
        }
    }

    private KeyEvent getKeyEventFromListByKeyCode(int keyCode) {
        for (KeyEvent evt : keyQueue) {
            if (evt.getKeyCode() == keyCode) {
                return evt;
            }
        }
        return null;
    }

    public void toggleFullscreen() {
        boolean wasResizable = isResizable();

        if (coreRef.fullScreen) {
            coreRef.renderThread.stopRendering();
            coreRef.fullScreen = false;
            setVisible(false);
            dispose();
            setUndecorated(false);
            setResizable(wasResizable);
            screenDevice.setFullScreenWindow(null);
//            setExtendedState(java.awt.Frame.NORMAL);
            setVisible(true);
            mouseXCorrection = 1;
            mouseYCorrection = 1;
            coreRef.renderThread.startRendering(this.getBufferStrategy());
        } else {
            coreRef.renderThread.stopRendering();
            double oldWidth = getWidth();
            double oldHeight = getHeight();
            coreRef.fullScreen = true;
            setVisible(false);
            setResizable(false);
            dispose();
            setUndecorated(true);
            screenDevice.setFullScreenWindow(this);
//            setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
            setLocationRelativeTo(null);
            setVisible(true);
            mouseXCorrection = oldWidth / getWidth();
            mouseYCorrection = oldHeight / getHeight();
            coreRef.renderThread.startRendering(this.getBufferStrategy());
        }

        keyQueue = new ArrayList<>();
    }

    @Override
    public void update(Graphics g) {
        repaint();
    }

//    private class DrawCanvas extends JPanel {
//
//        @Override
//        public void paintComponent(Graphics g) {
//            super.paintComponent(g);
//            Graphics2D g2 = (Graphics2D) g;
//
//            BufferedImage inImg = coreRef.offscreenImg;
//
//            double inWidth = inImg.getWidth();
//            double ratio = getWidth() / inWidth;
//
//            AffineTransform at = AffineTransform.getScaleInstance(ratio, ratio);
//            g2.drawImage(inImg, at, null);
//
//            if (coreRef.showDebug) {
//                g2.drawImage(coreRef.debugImg, 0, 0, null);
//            }
//
//            coreRef.waitForDraw = false;
//        }
//    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
