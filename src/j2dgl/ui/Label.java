package j2dgl.ui;

import j2dgl.Boalean;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;

public class Label extends UIComponent {

    private String text = "";
    private Color foreground = Color.BLACK;
    private Color background = Color.GRAY;
    private boolean centerText = false;

    public Label(double x, double y, int width, int height, String text, Point mouse, Boalean mouseDown) {
        super(x, y, width, height, mouse, mouseDown);
        this.text = text;
    }

    @Override
    protected void draw(Graphics2D g2) {
        if (background != null) {
            g2.setColor(background);
            g2.fillRect(0, 0, width, height);
        }

        if (text != null) {
            FontMetrics fm = g2.getFontMetrics();
            int textHeight = fm.getHeight();
            int drawHeight = textHeight + (height - textHeight) - (textHeight / 2);
            g2.setColor(foreground);
            if (isCenterText()) {
                int textWidth = fm.stringWidth(text);
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Color getForeground() {
        return foreground;
    }

    public void setForeground(Color foreground) {
        this.foreground = foreground;
    }

    public Color getBackground() {
        return background;
    }

    public void setBackground(Color background) {
        this.background = background;
    }

    public boolean isCenterText() {
        return centerText;
    }

    public void setCenterText(boolean centerText) {
        this.centerText = centerText;
    }

}
