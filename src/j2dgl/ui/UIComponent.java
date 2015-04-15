package j2dgl.ui;

import utility.BooleanHolder;
import j2dgl.entity.DrawableEntity;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public abstract class UIComponent extends DrawableEntity {

    protected final Point mouse;
    protected final BooleanHolder mouseDown;
    public Color borderColor = Color.DARK_GRAY;
    public Color backgroundColor = Color.GRAY;

    public UIComponent(double x, double y, int width, int height, Point mouse, BooleanHolder mouseDown) {
        super(x, y, width, height);
        this.mouse = mouse;
        this.mouseDown = mouseDown;
    }

    protected void drawBorder(Graphics2D g2) {
        if (borderColor != null) {
            g2.setColor(borderColor);
            g2.drawRect(0, 0, width - 1, height - 1);
        }
    }

    protected void drawBackground(Graphics2D g2) {
        if (backgroundColor != null) {
            g2.setColor(backgroundColor);
            g2.fillRect(0, 0, width, height);
        }
    }
}
