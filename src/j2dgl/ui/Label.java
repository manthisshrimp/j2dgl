package j2dgl.ui;

import utility.BooleanHolder;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;

public class Label extends UIComponent {

    private boolean determineSizeOnDraw = false;
    
    public Color foregroundColor = Color.BLACK;
    public String text = "";
    public boolean centerTextX = false;
    public boolean centerTextY = true;
    
    public Label(double x, double y, int width, int height, String text, Point mouse, BooleanHolder mouseDown) {
        super(x, y, width, height, mouse, mouseDown);
        this.text = text;
    }

    public Label(double x, double y, int width, int height, String text, Point mouse,
            BooleanHolder mouseDown, Color fgColor, Color bgColor) {
        this(x, y, width, height, text, mouse, mouseDown);
        this.foregroundColor = fgColor;
        this.backgroundColor = bgColor;
    }

    public Label(double x, double y, String text, Point mouse, BooleanHolder mouseDown) {
        super(x, y, 10, 10, mouse, mouseDown);
        determineSizeOnDraw = true;
        this.text = text;
    }

    @Override
    protected void draw(Graphics2D g2) {
        drawBackground(g2);
        if (text != null) {
            FontMetrics fm = g2.getFontMetrics();
            int textHeight = fm.getHeight();
            int textWidth = fm.stringWidth(text);
            if (determineSizeOnDraw) {
                if (width != textWidth || height != textHeight) {
                    width = textWidth;
                    height = textHeight;
                    initializeGraphics();
                }
                determineSizeOnDraw = false;
            }
            int drawHeight;
            if (centerTextY) {
                drawHeight = textHeight + (height - textHeight) - (textHeight / 2);
            } else {
                drawHeight = height;
            }
            g2.setColor(foregroundColor);
            if (centerTextX) {
                g2.drawString(text, (width - textWidth) / 2, drawHeight);
            } else {
                g2.drawString(text, 5, drawHeight);
            }
        }
    }

    @Override
    public void update() {
        // Nothing to update.
    }

    public void setTextAndResize(String text) {
        this.text = text;
        determineSizeOnDraw = true;
    }
}
