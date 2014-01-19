package Model;

import Model.abstractInterface.Image;
import java.util.HashMap;

public class ImageSet {

    private HashMap<String, Image> imageSet;

    public ImageSet() {
        this.imageSet = new HashMap<>();
    }

    public void put(String name, Image image) {
        this.imageSet.put(name, image);
    }

    public Image get(String name) {
        return this.imageSet.get(name);
    }
}
