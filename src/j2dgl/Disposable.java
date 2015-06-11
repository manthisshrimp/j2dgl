package j2dgl;

public interface Disposable {
    
    public boolean needsDisposal();
    
    public void disposeLater();
    
}
