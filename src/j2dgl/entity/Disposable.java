package j2dgl.entity;

public interface Disposable {
    
    public boolean needsDisposal();
    
    public void disposeLater();
    
}
