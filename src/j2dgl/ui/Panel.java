package j2dgl.ui;

import j2dgl.InputHandler;
import java.awt.Color;
import java.awt.Graphics2D;

public class Panel extends UIComponent {

    public Panel(double x, double y, int width, int height, InputHandler inputHandler, 
            Color bgColor, Color borderColor) {
        super(x, y, width, height, inputHandler);
        this.backgroundColor = bgColor;
        this.borderColor = borderColor;
    }

    @Override
    public void drawSelf(Graphics2D g2) {
        drawBackground(g2);
        drawBorder(g2);
    }
}
