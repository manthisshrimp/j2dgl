package j2dgl.ui;

import j2dgl.input.InputHandler;
import j2dgl.render.EntityRenderer;
import j2dgl.update.EntityUpdater;

public abstract class Controller {
    
    protected final EntityUpdater updater;
    protected final EntityRenderer renderer;
    protected final InputHandler inputHandler;

    public Controller(EntityUpdater updater, EntityRenderer renderer, InputHandler inputHandler) {
        this.updater = updater;
        this.renderer = renderer;
        this.inputHandler = inputHandler;
    }
    
    protected void registerComponents(UIComponent... components) {
        for (UIComponent component : components) {
            updater.addEntity(component);
            renderer.addDrawable(component);
        }
    }
    
}
