package j2dgl.ui;

import j2dgl.input.InputHandler;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Label extends UIComponent {

    private Font font;

    public Color foregroundColor = Color.BLACK;
    public String text = "";
    public boolean centerTextX = false;
    public boolean centerTextY = true;

    public Label(double x, double y, int width, int height, String text, Font font, InputHandler inputHandler) {
        super(x, y, width, height, inputHandler);
        this.text = text;
        this.font = font;
    }

    public Label(double x, double y, int width, int height, String text, Font font,
            InputHandler inputHandler, Color fgColor, Color bgColor) {
        this(x, y, width, height, text, font, inputHandler);
        this.foregroundColor = fgColor;
        this.backgroundColor = bgColor;
    }

    public Label(double x, double y, String text, Font font, InputHandler inputHandler) {
        super(x, y, 10, 10, inputHandler);
        this.font = font;
        this.text = text;
        resize();
    }

    @Override
    public void drawSelf(Graphics2D g2) {
        Font oldFont = g2.getFont();
        if (font != null) {
            g2.setFont(font);
        }
        drawBackground(g2);
        if (text != null) {
            FontMetrics fm = g2.getFontMetrics();
            int textHeight = fm.getHeight();
            int textWidth = fm.stringWidth(text);
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
        g2.setFont(oldFont);
    }

    public void setTextAndResize(String text) {
        this.text = text;
        resize();
    }

    private void resize() {
        Graphics2D g2 = new BufferedImage(2, 2, BufferedImage.SCALE_FAST).createGraphics();
        g2.setFont(font);
        FontMetrics fm = g2.getFontMetrics();
        height = fm.getHeight();
        width = fm.stringWidth(text);
    }
}
