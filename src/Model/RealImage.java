package Model;

import Model.abstractInterface.Bitmap;
import Model.abstractInterface.Image;

public class RealImage implements Image {

    private Bitmap bitmap;

    public RealImage(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public Bitmap getBitmap() {
        return bitmap;
    }
}
