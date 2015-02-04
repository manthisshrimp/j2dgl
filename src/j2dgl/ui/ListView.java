package j2dgl.ui;

import j2dgl.Boalean;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public class ListView<T extends Object> extends UIComponent {

    private final ArrayList<T> items = new ArrayList<>();
    private T selectedItem = null;
    private int selectedIndex = -1;
    private int hoverIndex = -1;

    private Color backgroundColor = new Color(0x1A1A1A);
    private Color alternateBackgroundColor = new Color(0x242424);
    private Color hoverColor = new Color(0x454545);
    private Color selectedColor = new Color(0xE3E3E3);
    private Color foregroundColor = Color.WHITE;
    private Color selectedForegroundColor = Color.BLACK;

    public ListView(double x, double y, int width, int height, Point mouse, Boalean mouseDown) {
        super(x, y, width, height, mouse, mouseDown);
    }

    @Override
    protected void draw(Graphics2D g2) {
        drawBackground(g2);
        drawItems(g2);
        drawBorder(g2);
    }

    private void drawBackground(Graphics2D g2) {
        int lines = (height / 25) + 1;
        for (int i = 0; i < lines; i++) {
            if (i % 2 == 0) {
                g2.setColor(getBackgroundColor());
            } else {
                g2.setColor(getAlternateBackgroundColor());
            }
            g2.fillRect(1, (i * 25) + 1, width - 2, 25);
        }
    }

    private void drawItems(Graphics2D g2) {
        for (int i = 0; i < items.size(); i++) {
            if (i == selectedIndex) {
                g2.setColor(getSelectedColor());
                g2.fillRect(1, (i * 25) + 1, width - 2, 25);
            } else if (i == hoverIndex) {
                g2.setColor(getHoverColor());
                g2.fillRect(1, (i * 25) + 1, width - 2, 25);
            }
            if (i == selectedIndex) {
                g2.setColor(getSelectedForegroundColor());
            } else {
                g2.setColor(getForegroundColor());
            }
            int textLeft = 8;
            int textTop = 12 + (i * 25) + 5;
            g2.drawString(items.get(i).toString(), textLeft, textTop);
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
                    if (mouseDown.getValue()) {
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

    public Color getAlternateBackgroundColor() {
        return alternateBackgroundColor;
    }

    public void setAlternateBackgroundColor(Color alternateBackgroundColor) {
        this.alternateBackgroundColor = alternateBackgroundColor;
    }

    public Color getSelectedForegroundColor() {
        return selectedForegroundColor;
    }

    public void setSelectedForegroundColor(Color selectedForegroundColor) {
        this.selectedForegroundColor = selectedForegroundColor;
    }

}
