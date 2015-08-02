package j2dgl.ui;

import j2dgl.input.InputHandler;
import j2dgl.entity.Entity;
import java.awt.Color;
import java.awt.Graphics2D;

public abstract class UIComponent extends Entity {

    protected InputHandler inputHandler;
    
    public Color borderColor = Color.DARK_GRAY;
    public Color backgroundColor = Color.GRAY;

    public UIComponent(double x, double y, int width, int height, InputHandler inputHandler) {
        super(x, y, width, height);
        this.inputHandler = inputHandler;
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
