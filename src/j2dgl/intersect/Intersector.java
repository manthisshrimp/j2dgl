package j2dgl.intersect;

import java.util.List;

public interface Intersector {
    
    public void checkIntersections(List<? extends Intersectable> intersectables);
    
}
