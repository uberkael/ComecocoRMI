import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
class ImageS extends Image implements java.io.Serializable {
    public int getWidth(ImageObserver observer) {
        return 0;
    }
    public int getHeight(ImageObserver observer) {
        return 0;
    }
    public ImageProducer getSource() {
        return null;
    }
    public Graphics getGraphics() {
        return null;
    }
    public Object getProperty(String name, ImageObserver observer) {
        return null;
    }
}
