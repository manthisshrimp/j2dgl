package j2dgl.ui;

import j2dgl.input.InputHandler;
import j2dgl.render.Renderer;
import j2dgl.update.Updater;

public abstract class Controller {
    
    protected final Updater updater;
    protected final Renderer renderer;
    protected final InputHandler inputHandler;

    public Controller(Updater updater, Renderer renderer, InputHandler inputHandler) {
        this.updater = updater;
        this.renderer = renderer;
        this.inputHandler = inputHandler;
    }
    
    protected void registerComponents(UIComponent... components) {
        for (UIComponent component : components) {
            updater.addUpdatable(component);
            renderer.addDrawable(component);
        }
    }
    
}
