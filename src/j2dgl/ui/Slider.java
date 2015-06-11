package j2dgl.ui;

import j2dgl.InputHandler;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

public class Slider extends UIComponent {

    private int minValue;
    private int value;
    private int maxValue;
    private int sidePadding = 20;
    private int lineWidth;

    private final int GRABBER_SIZE = 6;
    private boolean dragging = false;

    private final Rectangle grabber;
    private final Label valueLabel;

    public Color foregroundColor = new Color(0x4AEBFF);
    public Color lineColor = Color.WHITE;

    public Slider(double x, double y, int width, int height, InputHandler inputHandler,
            int minValue, int maxValue, int initialValue) {
        super(x, y, width, height, inputHandler);
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.value = initialValue;
        lineWidth = width - sidePadding / 2;
        // Rectangle in screen level space to facilitate comparisons with mouse.
        grabber = new Rectangle((int) x + sidePadding, (int) (height - GRABBER_SIZE - 3 + y), GRABBER_SIZE, GRABBER_SIZE);
        setValue(value);
        // The value label's coordinates are in local space.
        valueLabel = new Label((int) (grabber.x - x), -6, "" + minValue, null, inputHandler);
        valueLabel.foregroundColor = foregroundColor;
        valueLabel.backgroundColor = Color.BLACK;
        valueLabel.centerTextX = true;
        valueLabel.centerTextY = false;
        valueLabel.setTextAndResize("" + value);
        borderColor = null;
    }

    @Override
    public void drawSelf(Graphics2D g2) {
        drawBackground(g2);
        drawBorder(g2);
        // Draw the line
        g2.setColor(lineColor);
        g2.drawLine(sidePadding, height / 2, width - sidePadding, height / 2);
        // Draw the grabber
        g2.setColor(foregroundColor);
        g2.drawRect((int) (grabber.x - x), (int) (grabber.y - y), grabber.width, grabber.height);
        g2.drawLine((int) (grabber.x - x + grabber.width / 2), (int) (grabber.y - y),
                (int) (grabber.x - x + grabber.width / 2), (int) (grabber.y - y - (grabber.y - y - height / 2) * 2) + 3);
        if (grabber.contains(inputHandler.getMouse())) {
            if (inputHandler.isMouseDown()) {
                g2.setColor(Color.DARK_GRAY);
            } else {
                g2.setColor(lineColor);
            }
            g2.fillRect((int) (grabber.x - x) + 1, (int) (grabber.y - y) + 1, grabber.width - 1, grabber.height - 1);
        }
        // Draw the label
        int drawX = (int) (grabber.x - x + grabber.width / 2 - valueLabel.width / 2);
//        g2.drawImage(valueLabel.getImage(), null, drawX, (int) valueLabel.y);
//        valueLabel.draw(g2, drawX, height);
    }

    @Override
    protected void applyLogic() {
        Point mouse = inputHandler.getMouse();
        if (inputHandler.isMouseDown()) {
            if (mouse.x > x + sidePadding + 3 && mouse.x < x + width - sidePadding - 4) {
                if (this.getBounds().contains(mouse)) {
                    calculateNewValue(mouse.x);
                }
            }
        }
    }

    private void calculateNewValue(int x) {
        grabber.x = x - 3;
        double realLineWidth = width - sidePadding * 2;
        double realLineValue = (int) (x - this.x - sidePadding);
        double ratio = realLineValue / realLineWidth;
        value = (int) (((maxValue + (maxValue / 10) - minValue) * ratio) + minValue);
        if (value > maxValue) {
            value = maxValue;
        }
        valueLabel.setTextAndResize("" + value);
    }

    public void setBackgroundColorAll(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        valueLabel.backgroundColor = backgroundColor;
    }

    public int getSidePadding() {
        return sidePadding;
    }

    public void setSidePadding(int sidePadding) {
        this.sidePadding = sidePadding;
        this.lineWidth = width - sidePadding / 2;
    }

    public int getValue() {
        return value;
    }

    public final void setValue(int value) {
        if (value > getMaxValue()) {
            this.value = getMaxValue();
        }
        if (value < getMinValue()) {
            this.value = getMinValue();
        }
        double ratio = (double) value / getMaxValue();
        grabber.x = (int) (this.x + sidePadding + ratio * lineWidth);
        if (valueLabel != null) {
            valueLabel.setTextAndResize("" + value);
        }
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
        setValue(value);
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
        setValue(value);
    }
}
