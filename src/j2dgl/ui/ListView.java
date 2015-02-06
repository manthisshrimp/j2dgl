package j2dgl.ui;

import utility.Boalean;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import utility.Passback;

public class ListView<T extends Object> extends UIComponent {

    private final ArrayList<T> items = new ArrayList<>();
    private T selectedItem = null;
    private int selectedIndex = -1;
    private int hoverIndex = -1;
    private final Passback removeAction;
    private final DeleteItemButton btnDeleteItem;

    private Color backgroundColor = new Color(0x1A1A1A);
    private Color alternateBackgroundColor = new Color(0x242424);
    private Color hoverColor = new Color(0x454545);
    private Color selectedColor = new Color(0x4AEBFF);
    private Color selectedBackgroundColor = new Color(0x202020);
    private Color foregroundColor = Color.WHITE;
    private Color selectedForegroundColor = new Color(0x4AEBFF);

    public ListView(double x, double y, int width, int height, Point mouse,
            Boalean mouseDown, Passback removeAction) {
        super(x, y, width, height, mouse, mouseDown);
        this.removeAction = removeAction;
        btnDeleteItem = new DeleteItemButton(-25, -25, 23, 23, mouse, mouseDown, null, removeAction);
        btnDeleteItem.setBorderColor(hoverColor);
        btnDeleteItem.setBackground(hoverColor);
    }

    @Override
    protected void draw(Graphics2D g2) {
        drawBackground(g2);
        drawBorder(g2);
        drawItems(g2);
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
                g2.setColor(getSelectedBackgroundColor());
                g2.fillRect(-1, (i * 25) + 1, width + 1, 25);
                g2.setColor(selectedColor);
                g2.drawRect(-1, (i * 25) + 1, width, 24);
            } else if (i == hoverIndex) {
                g2.setColor(hoverColor);
                g2.fillRect(-1, (i * 25) + 1, width + 1, 25);
            }
            if (i == hoverIndex) {
                if (i == selectedIndex) {
                    btnDeleteItem.setBackground(getSelectedBackgroundColor());
                    btnDeleteItem.setBorderColor(getSelectedBackgroundColor());
                    btnDeleteItem.setOverSelected(true);
                } else {
                    btnDeleteItem.setBackground(hoverColor);
                    btnDeleteItem.setBorderColor(hoverColor);
                    btnDeleteItem.setOverSelected(false);
                }
                g2.drawImage(btnDeleteItem.getImage(), null,
                        width - 24, (i * 25) + 2);
            }
            if (i == selectedIndex) {
                g2.setColor(selectedForegroundColor);
            } else {
                g2.setColor(foregroundColor);
            }
            int textLeft = 8;
            int textTop = 12 + (i * 25) + 5;
            g2.drawString(items.get(i).toString(), textLeft, textTop);
        }
    }

    @Override
    public void update() {
        if (getBounds().contains(mouse)) {
            Rectangle currentItemArea;
            for (int i = 0; i < items.size(); i++) {
                currentItemArea = new Rectangle((int) (i + x), (int) (i * 25 + y), width, 25);
                if (currentItemArea.contains(mouse)) {
                    hoverIndex = i;
                    btnDeleteItem.x = currentItemArea.x + width - 25;
                    btnDeleteItem.y = currentItemArea.y;
                    btnDeleteItem.update();
                    removeAction.setPassBackObject(items.get(hoverIndex));
                    if (!btnDeleteItem.getBounds().contains(mouse)
                            && mouseDown.getValue()) {
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

    public Color getSelectedBackgroundColor() {
        return selectedBackgroundColor;
    }

    public void setSelectedBackgroundColor(Color selectedBackgroundColor) {
        this.selectedBackgroundColor = selectedBackgroundColor;
    }

    private class DeleteItemButton extends Button {

        private boolean overSelected = false;

        public DeleteItemButton(double x, double y, int width, int height, Point mouse,
                Boalean mouseDown, String text, Runnable runnable) {
            super(x, y, width, height, mouse, mouseDown, text, false, runnable);
        }

        @Override
        protected void mouseHovering(Graphics2D g2) {
            g2.setColor(new Color(0xFC2D3B));
            g2.fillRect(6, 6, 12, 12);
            g2.setColor(new Color(0xE0E0E0));
            g2.drawLine(8, 9, 13, 14);
            g2.drawLine(9, 9, 14, 14);
            g2.drawLine(13, 9, 8, 14);
            g2.drawLine(14, 9, 9, 14);
        }

        @Override
        protected void mouseDown(Graphics2D g2) {
            g2.setColor(new Color(0xBA232D));
            g2.fillRect(6, 6, 12, 12);
            g2.setColor(new Color(0xE0E0E0));
            g2.drawLine(8, 9, 13, 14);
            g2.drawLine(9, 9, 14, 14);
            g2.drawLine(13, 9, 8, 14);
            g2.drawLine(14, 9, 9, 14);
            g2.setColor(Color.DARK_GRAY);
            g2.drawRect(6, 6, 11, 11);
        }

        @Override
        protected void defaultLook(Graphics2D g2) {
            if (overSelected) {
                g2.setColor(hoverColor);
            } else {
                g2.setColor(backgroundColor);
            }
            g2.drawLine(8, 9, 13, 14);
            g2.drawLine(9, 9, 14, 14);
            g2.drawLine(13, 9, 8, 14);
            g2.drawLine(14, 9, 9, 14);
        }

        public boolean isOverSelected() {
            return overSelected;
        }

        public void setOverSelected(boolean overSelected) {
            this.overSelected = overSelected;
        }

    }
}
