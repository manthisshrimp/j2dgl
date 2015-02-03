package j2dgl.ui;

import j2dgl.entity.DrawableEntity;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public class ListView<T extends Object> extends DrawableEntity {

    private final ArrayList<T> items = new ArrayList<>();
    private T selectedItem = null;
    private int selectedIndex = -1;
    private int hoverIndex = -1;

    private Color backgroundColor = Color.DARK_GRAY;
    private Color lineColor = Color.GRAY;
    private Color hoverColor = Color.YELLOW;
    private Color selectedColor = Color.ORANGE;
    private Color foregroundColor = Color.BLACK;

    private final Point mouse;
    private final Boolean mouseDown;

    public ListView(double x, double y, int width, int height, Point mouse, Boolean mouseDown) {
        super(x, y, width, height);
        this.mouse = mouse;
        this.mouseDown = mouseDown;
    }

    @Override
    protected void draw(Graphics2D g2) {
        g2.setColor(backgroundColor);
        g2.fillRect(0, 0, width, height);
        g2.setColor(lineColor);
        g2.drawRect(0, 0, width - 1, height - 1);
        for (int i = 0; i < items.size(); i++) {
            if (i == selectedIndex) {
                g2.setColor(selectedColor);
                g2.fillRect(1, (i * 25) + 1, width - 2, 23);
            } else if (i == hoverIndex) {
                g2.setColor(hoverColor);
                g2.fillRect(1, (i * 25) + 1, width - 2, 23);
            }
            g2.setColor(lineColor);
            g2.drawLine(3, (i * 25) + 25, width - 4, (i * 25) + 25);
            g2.setColor(foregroundColor);
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth("" + hoverIndex);
            int textLeft = width / 2 - textWidth / 2;
            int textTop = 12 + (i * 25) + 5;
            g2.drawString("" + hoverIndex, textLeft, textTop);
        }
    }

    @Override
    public void update() {
        if (getBounds().contains(mouse)) {
            Point localMouse = new Point(mouse.x - (int) Math.round(x), mouse.y - (int) Math.round(y));
            Rectangle currentItemArea;
            for (int i = 0; i < items.size(); i++) {
                currentItemArea = new Rectangle(i, i * 25, width, 25);
                if (currentItemArea.contains(localMouse)) {
                    hoverIndex = i;
                    if (mouseDown) {
                        selectedIndex = i;
                        selectedItem = items.get(selectedIndex);
                    }
                    return;
                }
            }
            hoverIndex = -1;
        } else {
            hoverIndex = -1;
        }

    }

    public void addItem(T item) {
        items.add(item);
    }

    public void removeItem(T item) {
        items.remove(item);
    }

    public T getSelectedItem() {
        return selectedItem;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Color getLineColor() {
        return lineColor;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    public Color getHoverColor() {
        return hoverColor;
    }

    public void setHoverColor(Color hoverColor) {
        this.hoverColor = hoverColor;
    }

    public Color getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(Color selectedColor) {
        this.selectedColor = selectedColor;
    }

    public Color getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

}
