package j2dgl.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import utility.BooleanHolder;

public class Panel extends UIComponent {

    public Panel(double x, double y, int width, int height, Point mouse,
            BooleanHolder mouseDown, Color bgColor, Color borderColor) {
        super(x, y, width, height, mouse, mouseDown);
        this.backgroundColor = bgColor;
        this.borderColor = borderColor;
    }

    @Override
    protected void draw(Graphics2D g2) {
        drawBackground(g2);
        drawBorder(g2);
    }

    @Override
    public void update() {
        // Nothing to update.
    }
}
