package Persistence;

import Model.abstractInterface.Bitmap;
import Model.abstractInterface.Image;
import Persistence.abstractInterface.ImageLoader;

public class ProxyImage implements Image {

    private Image realImage;
    private ImageLoader loader;

    public ProxyImage(ImageLoader loader) {
        this.loader = loader;
        this.realImage = null;
    }

    @Override
    public Bitmap getBitmap() {
        if (this.realImage == null) this.realImage = this.loader.load();
        return this.realImage.getBitmap();
    }
}
