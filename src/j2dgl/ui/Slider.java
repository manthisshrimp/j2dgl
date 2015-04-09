package j2dgl.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import utility.BooleanHolder;

public class Slider extends UIComponent {

    private int minValue;
    private int value = minValue;
    private int maxValue = 10000;

    private Color lineColor = Color.WHITE;
    private Color grabberColor = new Color(0x4AEBFF);
    private Color backgroundColor = Color.GRAY;

    private final int GRABBER_SIZE = 6;
    private int sidePadding = 20;
    private boolean dragging = false;
    
    private final Rectangle grabber;
    private final Label valueLabel;

    public Slider(double x, double y, int width, int height, Point mouse, BooleanHolder mouseDown, int minValue, int maxValue) {
        super(x, y, width, height, mouse, mouseDown);
        this.minValue = minValue;
        this.maxValue = maxValue;
        
        // Rectangle in screen level space to facilitate comparisons with mouse
        grabber = new Rectangle((int) x + sidePadding, (int) (height - GRABBER_SIZE - 3 + y), GRABBER_SIZE, GRABBER_SIZE);
        // The value label's coordinates are in local space
        valueLabel = new Label((int) (grabber.x - x), -6, "" + minValue, mouse, mouseDown);
        
        valueLabel.setForeground(new Color(0x4AEBFF));
        valueLabel.setBackground(Color.BLACK);
        valueLabel.setCenterText(true);
        valueLabel.setTextCenteredOnY(false);
    }

    @Override
    protected void draw(Graphics2D g2) {
        if (backgroundColor != null) {
            g2.setColor(backgroundColor);
            g2.fillRect(0, 0, width, height);
        }
        // Draw the line
        g2.setColor(lineColor);
        g2.drawLine(sidePadding, height / 2, width - sidePadding, height / 2);
        // Draw the grabber
        g2.setColor(grabberColor);
        g2.drawRect((int) (grabber.x - x), (int) (grabber.y - y), grabber.width, grabber.height);
        g2.drawLine((int) (grabber.x - x + grabber.width / 2), (int) (grabber.y - y),
                (int) (grabber.x - x + grabber.width / 2), (int) (grabber.y - y - (grabber.y - y - height / 2) * 2) + 3);
        if (grabber.contains(mouse)) {
            if (mouseDown.getValue()) {
                g2.setColor(Color.DARK_GRAY);
            } else {
                g2.setColor(lineColor);
            }
            g2.fillRect((int) (grabber.x - x) + 1, (int) (grabber.y - y) + 1, grabber.width - 1, grabber.height - 1);
        }
        // Draw the label
        int drawX = (int) (grabber.x - x + grabber.width / 2 - valueLabel.width / 2);
        g2.drawImage(valueLabel.getImage(), null, drawX, (int) valueLabel.y);
    }

    @Override
    public void update() {
        if (mouseDown.getValue()) {
            if (grabber.contains(mouse)) {
                dragging = true;
            }
        } else {
            dragging = false;
        }
        if (dragging) {
            if (mouse.x > x + sidePadding + 3 && mouse.x < x + width - sidePadding - 4) {
                grabber.x = mouse.x - 3;
                // calculate the new value
                double realLineWidth = width - sidePadding * 2;
                double realLineValue = (int) (grabber.x - x - sidePadding);
                double ratio = realLineValue / realLineWidth;
                value = (int) (((maxValue + (maxValue / 10) - minValue) * ratio) + minValue);
                if (value > maxValue) {
                    value = maxValue;
                }
                valueLabel.setTextAndResize("" + value);
            }
        }
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
    public void setBackgroundColorFull(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
        valueLabel.setBackground(backgroundColor);
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getValue() {
        return value;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public Color getLineColor() {
        return lineColor;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    public Color getGrabberColor() {
        return grabberColor;
    }

    public void setGrabberColor(Color grabberColor) {
        this.grabberColor = grabberColor;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public Label getValueLabel() {
        return valueLabel;
    }

    public int getSidePadding() {
        return sidePadding;
    }

    public void setSidePadding(int sidePadding) {
        this.sidePadding = sidePadding;
    }
}
