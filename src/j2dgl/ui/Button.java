package j2dgl.ui;

import j2dgl.InputHandler;
import java.awt.Color;
import java.awt.Graphics2D;

public class Button extends UIComponent {

    private Label label;
    private final Runnable runnable;
    private boolean waitingForRealease = false;

    protected int labelXOffset = 0;
    protected int labelYOffset = 0;

    public Color bgHoverColor = new Color(0x454545);
    public Color bgMouseDownColor = new Color(0x1A1A1A);

    public Button(double x, double y, int width, int height, InputHandler inputHandler,
            String text, Runnable runnable) {
        super(x, y, width, height, inputHandler);
        this.runnable = runnable;
        if (text != null) {
            label = new Label(0, 0, width, height, text, null, inputHandler);
        }
        backgroundColor = new Color(0x242424);
    }

    @Override
    public void drawSelf(Graphics2D g2) {
        drawBackground(g2);
        drawBorder(g2);
        if (getBounds().contains(inputHandler.getMouse())) {
            drawMouseHoverLook(g2);
            if (inputHandler.isMouseDown()) {
                drawMouseDownLook(g2);
                labelXOffset = 1;
                labelYOffset = 1;
            } else {
                labelXOffset = 0;
                labelYOffset = 0;
            }
        } else {
            drawDefaultLook(g2);
            labelXOffset = 0;
            labelYOffset = 0;
        }
        if (label != null) {
            label.draw(g2, labelXOffset, labelYOffset);
        }
    }

    protected void drawMouseHoverLook(Graphics2D g2) {
        label.backgroundColor = bgHoverColor;
    }

    protected void drawMouseDownLook(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.drawLine(1, 1, 1, height);
        g2.drawLine(1, 1, width, 1);
        label.backgroundColor = bgMouseDownColor;
    }

    protected void drawDefaultLook(Graphics2D g2) {
        label.backgroundColor = backgroundColor;
    }

    @Override
    protected void applyLogic() {
        if (getBounds().contains(inputHandler.getMouse())) {
            if (inputHandler.isMouseDown()) {
                waitingForRealease = true;
            }
            if (waitingForRealease && !inputHandler.isMouseDown()) {
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
