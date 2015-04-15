package j2dgl.ui;

import utility.BooleanHolder;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import utility.Passback;

public class ListView<T extends Object> extends UIComponent {

    private ArrayList<T> items = new ArrayList<>();
    private T selectedItem = null;
    private int selectedIndex = -1;
    private int hoverIndex = -1;
    private final Passback removeAction;
    private final DeleteItemButton btnDeleteItem;

    public Color alternateBGColor = new Color(0x242424);
    public Color hoverColor = new Color(0x454545);
    public Color selectedColor = new Color(0x4AEBFF);
    public Color selectedBGColor = new Color(0x202020);
    public Color foregroundColor = Color.WHITE;
    public Color selectedFGColor = new Color(0x4AEBFF);

    public ListView(double x, double y, int width, int height, Point mouse,
            BooleanHolder mouseDown, Passback removeAction) {
        super(x, y, width, height, mouse, mouseDown);
        this.removeAction = removeAction;
        btnDeleteItem = new DeleteItemButton(-25, -25, 23, 23, mouse, mouseDown, null, removeAction);
        btnDeleteItem.borderColor = hoverColor;
        btnDeleteItem.backgroundColor = hoverColor;
    }

    @Override
    protected void draw(Graphics2D g2) {
        drawBackground(g2);
        drawBorder(g2);
        drawItems(g2);
    }

    @Override
    protected void drawBackground(Graphics2D g2) {
        int lines = (height / 25) + 1;
        for (int i = 0; i < lines; i++) {
            if (i % 2 == 0) {
                g2.setColor(backgroundColor);
            } else {
                g2.setColor(alternateBGColor);
            }
            g2.fillRect(1, (i * 25) + 1, width - 2, 25);
        }
    }

    private void drawItems(Graphics2D g2) {
        for (int i = 0; i < items.size(); i++) {
            if (i == selectedIndex) {
                g2.setColor(backgroundColor);
                g2.fillRect(-1, (i * 25) + 1, width + 1, 25);
                g2.setColor(selectedColor);
                g2.drawRect(-1, (i * 25) + 1, width, 24);
            } else if (i == hoverIndex) {
                g2.setColor(hoverColor);
                g2.fillRect(-1, (i * 25) + 1, width + 1, 25);
            }
            if (i == hoverIndex) {
                if (i == selectedIndex) {
                    btnDeleteItem.backgroundColor = backgroundColor;
                    btnDeleteItem.borderColor = backgroundColor;
                    btnDeleteItem.overSelected = true;
                } else {
                    btnDeleteItem.borderColor = hoverColor;
                    btnDeleteItem.backgroundColor = hoverColor;
                    btnDeleteItem.overSelected = false;
                }
                g2.drawImage(btnDeleteItem.getImage(), null,
                        width - 24, (i * 25) + 2);
            }
            if (i == selectedIndex) {
                g2.setColor(selectedFGColor);
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
                    removeAction.setPassBackObject(items.get(hoverIndex));
                    btnDeleteItem.update();
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

    public void setItems(ArrayList<T> items) {
        this.items = items;
    }

    public T getSelectedItem() {
        return selectedItem;
    }

    private class DeleteItemButton extends Button {

        boolean overSelected = false;

        public DeleteItemButton(double x, double y, int width, int height, Point mouse,
                BooleanHolder mouseDown, String text, Runnable runnable) {
            super(x, y, width, height, mouse, mouseDown, text, runnable);
        }

        @Override
        protected void setMouseHoverLook(Graphics2D g2) {
            g2.setColor(new Color(0xFC2D3B));
            g2.fillRect(6, 6, 12, 12);
            g2.setColor(new Color(0xE0E0E0));
            g2.drawLine(8, 9, 13, 14);
            g2.drawLine(9, 9, 14, 14);
            g2.drawLine(13, 9, 8, 14);
            g2.drawLine(14, 9, 9, 14);
        }

        @Override
        protected void setMouseDownLook(Graphics2D g2) {
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
        protected void setDefaultLook(Graphics2D g2) {
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
    }
}
