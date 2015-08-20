package j2dgl.world;

import j2dgl.entity.Entity;
import j2dgl.input.InputHandler;
import j2dgl.intersect.Intersectable;
import j2dgl.intersect.Intersector;
import j2dgl.intersect.LinearIntersector;
import j2dgl.render.Drawable;
import j2dgl.update.Updatable;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class World implements Updatable, Drawable {

    private final Rectangle bounds;
    private final WorldView worldView;
    private final InputHandler inputHandler;
    private Intersector intersector = new LinearIntersector();

    private final List<Entity> entities = new ArrayList<>();
    private final List<Entity> removeList = new ArrayList<>();
    private List<Entity> drawableEntities = new ArrayList<>();
    
    private int scrollSpeed = 8;

    private boolean drawLocked = false;

    public World(Rectangle bounds, Rectangle worldViewBounds, InputHandler inputHandler) {
        this.bounds = bounds;
        this.worldView = new WorldView(
                worldViewBounds.x,
                worldViewBounds.y,
                worldViewBounds.width,
                worldViewBounds.height);
        entities.add(worldView);
        this.inputHandler = inputHandler;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    @Override
    public void update() {
        checkInput();
        worldView.visibleEntities.clear();
        intersector.checkIntersections(entities);
        entities.stream().forEach((entity) -> {
            if (entity.needsDisposal()) {
                removeList.add(entity);
            } else {
                entity.update();
            }
        });
        removeList.stream().forEach(entities::remove);
        removeList.clear();
        drawLocked = true;
        drawableEntities = new ArrayList<>(worldView.visibleEntities);
        drawLocked = false;
    }

    private void checkInput() {
        if (inputHandler != null) {
            if (inputHandler.isKeyDown(KeyEvent.VK_LEFT)) {
                worldView.setVelocity(-1, 0, scrollSpeed);
            } else if (inputHandler.isKeyDown(KeyEvent.VK_RIGHT)) {
                worldView.setVelocity(1, 0, scrollSpeed);
            } else if (inputHandler.isKeyDown(KeyEvent.VK_UP)) {
                worldView.setVelocity(0, -1, scrollSpeed);
            } else if (inputHandler.isKeyDown(KeyEvent.VK_DOWN)) {
                worldView.setVelocity(0, 1, scrollSpeed);
            }
            if (inputHandler.isKeysDown(KeyEvent.VK_RIGHT, KeyEvent.VK_UP)) {
                worldView.setVelocity(1, -1, scrollSpeed);
            } else if (inputHandler.isKeysDown(KeyEvent.VK_RIGHT, KeyEvent.VK_DOWN)) {
                worldView.setVelocity(1, 1, scrollSpeed);
            } else if (inputHandler.isKeysDown(KeyEvent.VK_LEFT, KeyEvent.VK_DOWN)) {
                worldView.setVelocity(-1, 1, scrollSpeed);
            } else if (inputHandler.isKeysDown(KeyEvent.VK_LEFT, KeyEvent.VK_UP)) {
                worldView.setVelocity(-1, -1, scrollSpeed);
            }
        }
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void addEntities(Entity... entities) {
        this.entities.addAll(Arrays.asList(entities));
    }

    public void setIntersector(Intersector intersector) {
        this.intersector = intersector;
    }

    @Override
    public void draw(Graphics2D g2, int xOffset, int yOffset) {
        if (drawLocked) {
            return;
        }
        g2.fillRect(0, 0, bounds.width, bounds.height);
        drawableEntities.stream().forEach((Entity entity) -> {
            entity.draw(g2, (int) (xOffset - worldView.x),
                    (int) (yOffset - worldView.y));
        });
    }

    public Entity getViewEntity() {
        return worldView;
    }

    private class WorldView extends Entity {

        private final List<Entity> visibleEntities = new ArrayList<>();

        public WorldView(double x, double y, int width, int height) {
            super(x, y, width, height);
            drag = 0.2;
        }

        @Override
        public void processIntersectionWith(Intersectable intersectable) {
            if (intersectable instanceof Entity) {
                visibleEntities.add(((Entity) intersectable));
            }
        }
    }

}
