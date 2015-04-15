package j2dgl.ui;

import utility.BooleanHolder;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class Button extends UIComponent {

    private Label label;
    private final Runnable runnable;
    private boolean waitingForRealease = false;
    
    protected int labelXOffset = 0;
    protected int labelYOffset = 0;
    
    public Color bgHoverColor = new Color(0x454545);
    public Color bgMouseDownColor = new Color(0x1A1A1A);

    public Button(double x, double y, int width, int height, Point mouse, BooleanHolder mouseDown,
            String text, Runnable runnable) {
        super(x, y, width, height, mouse, mouseDown);
        this.runnable = runnable;
        if (text != null) {
            label = new Label(0, 0, width, height, text, mouse, mouseDown);
        }
        backgroundColor = new Color(0x242424);
    }

    @Override
    protected void draw(Graphics2D g2) {
        drawBackground(g2);
        drawBorder(g2);
        if (getBounds().contains(mouse)) {
            setMouseHoverLook(g2);
            if (mouseDown.getValue()) {
                setMouseDownLook(g2);
                labelXOffset = 1;
                labelYOffset = 1;
            } else {
                labelXOffset = 0;
                labelYOffset = 0;
            }
        } else {
            setDefaultLook(g2);
            labelXOffset = 0;
            labelYOffset = 0;
        }
        if (label != null) {
            g2.drawImage(getLabel().getImage(), null, labelXOffset, labelYOffset);
        }
    }

    protected void setMouseHoverLook(Graphics2D g2) {
        label.backgroundColor = bgHoverColor;
    }

    protected void setMouseDownLook(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.drawLine(1, 1, 1, height);
        g2.drawLine(1, 1, width, 1);
        label.backgroundColor = bgMouseDownColor;
    }

    protected void setDefaultLook(Graphics2D g2) {
        label.backgroundColor = backgroundColor;
    }

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
}
