package j2dgl.resource;

import j2dgl.Core;
import j2dgl.exception.J2DGLException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;

public class ResourceManager {

    private final HashMap<String, BufferedImage> imageMap = new HashMap<>();
    private final HashMap<String, ArrayList<BufferedImage>> animationMap = new HashMap<>();

    public void addImage(String name, String path) throws J2DGLException {
        try {
            BufferedImage image = ImageIO.read(Core.class.getResourceAsStream(path));
            imageMap.put(name, image);
        } catch (IOException ex) {
            throw new J2DGLException("Error loading image: " + ex.getMessage());
        }
    }

    public BufferedImage getImage(String name) {
        return imageMap.get(name);
    }

    public void addAnimation(String name, String path) throws J2DGLException {
        URL url = Core.class.getResource(path);
        if (url == null) {
            throw new J2DGLException("Error, missing animation folder.");
        } else {
            try {
                ArrayList<BufferedImage> images = new ArrayList<>();
                File animationFolder = new File(url.toURI());
                for (File frameFile : animationFolder.listFiles()) {
                    images.add(ImageIO.read(frameFile));
                }
                animationMap.put(name, images);
            } catch (URISyntaxException | IOException ex) {
                throw new J2DGLException("Error loading animation: " + ex.getMessage());
            }
        }
    }

    public ArrayList<BufferedImage> getAnimationFrames(String name) {
        return animationMap.get(name);
    }

}
