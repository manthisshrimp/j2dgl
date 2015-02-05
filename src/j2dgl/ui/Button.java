package j2dgl.ui;

import j2dgl.Boalean;
import java.awt.Graphics2D;
import java.awt.Point;

public abstract class Button extends UIComponent {

    private Label label;
    protected int labelXOffset = 0;
    protected int labelYOffset = 0;
    private final Runnable runnable;
    private boolean waitingForRealease = false;

    public Button(double x, double y, int width, int height, Point mouse, Boalean mouseDown, 
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

    protected abstract void drawBackground(Graphics2D g2);

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
}
