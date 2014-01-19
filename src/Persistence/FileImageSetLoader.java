package Persistence;

import Model.ImageSet;
import Persistence.abstractInterface.ImageSetLoader;
import Persistence.abstractInterface.BitmapFactory;

public class FileImageSetLoader implements ImageSetLoader {

    private final String path;
    private final String[] filenames;
    private final BitmapFactory factory;

    public FileImageSetLoader(BitmapFactory factory, String path, String... filenames) {
        this.path = path;
        this.factory = factory;
        this.filenames = filenames;
    }

    @Override
    public ImageSet loadImageSet() {
        ImageSet set = new ImageSet();
        for (String filename : filenames) 
            set.put(filename, new ProxyImage(new FileImageLoader(path + "\\" + filename, factory)));
        return set;
    }
}
