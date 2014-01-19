package Persistence.abstractInterface;

import Model.abstractInterface.Bitmap;

public interface BitmapFactory<Parameter> {

    public Bitmap createBitmap(Parameter input);
    
}