package j2dgl.update;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Updater<T extends Updatable> {

    protected final List<T> updatables = new ArrayList<>();

    public final void updateAll() {
        updatables.stream().forEach((updatable) -> {
            executeUpdate(updatable);
        });
    }

    protected void executeUpdate(T updatable) {
        updatable.update();
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

}
