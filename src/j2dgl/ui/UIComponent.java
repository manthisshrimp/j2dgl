package j2dgl.ui;

import utility.BooleanHolder;
import j2dgl.entity.DrawableEntity;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public abstract class UIComponent extends DrawableEntity {

    protected final Point mouse;
    protected final BooleanHolder mouseDown;
    private Color borderColor = Color.DARK_GRAY;

    public UIComponent(double x, double y, int width, int height, Point mouse, BooleanHolder mouseDown) {
        super(x, y, width, height);
        this.mouse = mouse;
        this.mouseDown = mouseDown;
    }
    
    protected void drawBorder(Graphics2D g2) {
        g2.setColor(borderColor);
        g2.drawRect(0, 0, width - 1, height - 1);
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }
}
