package Persistence;

import Model.abstractInterface.Image;
import Model.RealImage;
import Persistence.abstractInterface.ImageLoader;
import Persistence.abstractInterface.BitmapFactory;

public class FileImageLoader implements ImageLoader {

    private final String filename;
    private final BitmapFactory factory;

    public FileImageLoader(String filename, BitmapFactory factory) {
        this.filename = filename;
        this.factory = factory;
    }

    @Override
    public Image load() {
        System.out.println("creando: " + filename);
        return new RealImage(factory.createBitmap(filename));
    }
}
