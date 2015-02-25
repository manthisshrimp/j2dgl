package j2dgl.ui;

import j2dgl.render.Renderer;
import j2dgl.update.Updater;
import java.awt.Point;
import utility.Boalean;

public abstract class Controller {
    
    protected final Updater updater;
    protected final Renderer renderer;
    protected final Point mouse;
    protected final Boalean mouseDown;

    public Controller(Updater updater, Renderer renderer, Point mouse, Boalean mouseDown) {
        this.updater = updater;
        this.renderer = renderer;
        this.mouse = mouse;
        this.mouseDown = mouseDown;
    }
    
    protected void registerComponents(UIComponent... components) {
        for (UIComponent component : components) {
            updater.addUpdatable(component);
            renderer.addRenderable(component);
        }
    }
    
}
