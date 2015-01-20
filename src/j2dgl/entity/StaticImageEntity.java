package j2dgl.entity;

import j2dgl.Entity;

public abstract class StaticImageEntity extends Entity {

    private final String image;

    public StaticImageEntity(double x, double y, int width, int height, String image) {
        super(x, y, width, height);
        this.image = image;
    }

    public String getImage() {
        return image;
    }
}
