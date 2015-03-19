package j2dgl.ui;

import utility.BooleanHolder;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public abstract class Button extends UIComponent {

    private Label label;
    protected int labelXOffset = 0;
    protected int labelYOffset = 0;
    private final Runnable runnable;
    private boolean waitingForRealease = false;
    private Color background;

    public Button(double x, double y, int width, int height, Point mouse, BooleanHolder mouseDown, 
            String text, boolean labeled, Runnable runnable) {
        super(x, y, width, height, mouse, mouseDown);
        this.runnable = runnable;
        if (labeled) {
            label = new Label(0, 0, width, height, text, mouse, mouseDown);
            label.setBackground(null);
            label.setCenterText(true);
        }
    }

    @Override
    protected void draw(Graphics2D g2) {
        drawBackground(g2);
        if (label != null) {
            g2.drawImage(getLabel().getImage(), null, labelXOffset, labelYOffset);
        }
        drawBorder(g2);
    }

    protected void drawBackground(Graphics2D g2) {
        if (getBackground() != null) {
            g2.setColor(getBackground());
            g2.fillRect(0, 0, width, height);
        }
        if (getBounds().contains(mouse)) {
            mouseHovering(g2);
            if (mouseDown.getValue()) {
                mouseDown(g2);
                labelXOffset = 1;
                labelYOffset = 1;
            } else {
                labelXOffset = 0;
                labelYOffset = 0;
            }
        } else {
            defaultLook(g2);
            labelXOffset = 0;
            labelYOffset = 0;
        }
    }
    
    protected abstract void mouseHovering(Graphics2D g2);
    
    protected abstract void mouseDown(Graphics2D g2);
    
    protected abstract void defaultLook(Graphics2D g2);

    @Override
    public void update() {
        if (getBounds().contains(mouse)) {
            if (mouseDown.getValue()) {
                waitingForRealease = true;
            }
            if (waitingForRealease && !mouseDown.getValue()) {
                waitingForRealease = false;
                runnable.run();
            }
        } else {
            waitingForRealease = false;
        }
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public Color getBackground() {
        return background;
    }

    public void setBackground(Color background) {
        this.background = background;
    }
}
