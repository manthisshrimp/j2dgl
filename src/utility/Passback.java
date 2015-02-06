package utility;

public abstract class Passback implements Runnable {

    protected Object object;

    public void setPassBackObject(Object object) {
        this.object = object;
    }

}
