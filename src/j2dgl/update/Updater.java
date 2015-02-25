package j2dgl.update;

import java.util.ArrayList;
import java.util.List;

public class Updater {

    private final List<Updatable> updatables = new ArrayList<>();

    public void updateAll() {
        updatables.stream().forEach((updatable) -> {
            updatable.update();
        });
    }

    public void addUpdatable(Updatable updatable) {
        updatables.add(updatable);
    }

    public List<Updatable> getUpdatables() {
        return updatables;
    }

}
