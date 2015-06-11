package j2dgl.update;

import j2dgl.entity.Entity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Updater<T extends Entity> {

    protected final List<T> updatables = new ArrayList<>();
    private final List<T> removeList = new ArrayList<>();

    private boolean clearRequested = false;

    public final void updateAll() {
        for (T updatable : updatables) {
            if (clearRequested) {
                clearRequested = false;
                break;
            }
            if (updatable.needsDisposal()) {
                removeList.add(updatable);
            } else {
                executeUpdate(updatable);
            }
        }
        removeList.stream().forEach(updatables::remove);
        removeList.clear();
        postUpdate();
    }

    protected void executeUpdate(T updatable) {
        updatable.update();
    }

    protected void postUpdate() {

    }

    public void addUpdatable(T updatable) {
        updatables.add(updatable);
    }

    public void addUpdatables(T... updatables) {
        this.updatables.addAll(Arrays.asList(updatables));
    }

    public List<T> getUpdatables() {
        return updatables;
    }

    public void clear() {
        clearRequested = true;
        removeList.addAll(updatables);
    }
}
