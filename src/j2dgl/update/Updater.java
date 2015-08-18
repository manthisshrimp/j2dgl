package j2dgl.update;

import j2dgl.entity.Entity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Updater<T extends Entity> {

    protected final List<T> entities = new ArrayList<>();
    private final List<T> removeList = new ArrayList<>();

    private boolean clearRequested = false;

    public final void updateAll() {
        if (!clearRequested) {
            entities.stream().forEach((entity) -> {
                if (entity.needsDisposal()) {
                    removeList.add(entity);
                } else {
                    entity.update();
                }
            });
        } else {
            clearRequested = false;
        }
        removeList.stream().forEach(entities::remove);
        removeList.clear();
        postUpdate();
    }

    protected void postUpdate() {

    }

    public void addEntity(T entity) {
        entities.add(entity);
    }

    public void addEntities(T... entities) {
        this.entities.addAll(Arrays.asList(entities));
    }

    public List<T> getEntities() {
        return new ArrayList<T>(entities);
    }

    public void clear() {
        clearRequested = true;
        removeList.addAll(entities);
    }
}
