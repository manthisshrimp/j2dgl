package j2dgl.ui;

import j2dgl.Boalean;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class DrawnButton extends Button {

    public DrawnButton(double x, double y, int width, int height, Point mouse,
            Boalean mouseDown, String text, Runnable runnable) {
        super(x, y, width, height, mouse, mouseDown, text, true, runnable);
    }

    @Override
    protected void mouseHovering(Graphics2D g2) {
        getLabel().setBackground(new Color(0x454545));
    }

    @Override
    protected void mouseDown(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.drawLine(1, 1, 1, height);
        g2.drawLine(1, 1, width, 1);
        getLabel().setBackground(new Color(0x1A1A1A));
    }

    @Override
    protected void defaultLook(Graphics2D g2) {
        getLabel().setBackground(new Color(0x242424));
    }
}
