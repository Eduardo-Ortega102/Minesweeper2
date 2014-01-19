package Control;

import Model.ImageSet;
import UserInterface.abstractInterface.ImageViewer;
import java.util.HashMap;

public final class ImageViewerControl {

    private final ImageViewer imageViever;
    private ImagePriority lastImagePriority;
    private HashMap<String, ImagePriority> imagePriorityMap;
    private final ImageSet imageSet;

    public ImageViewerControl(ImageViewer imageViever, ImageSet set, HashMap<String, ImagePriority> imagePriorityMap) {
        this.imageViever = imageViever;
        this.imageSet = set;
        this.imagePriorityMap = imagePriorityMap;
        this.reset();
    }

    public void reset() {
        lastImagePriority = ImagePriority.LOW;
    }

    public void viewImage(String input) {
        if (imagePriorityMap.get(input).compareTo(lastImagePriority) >= 0) {
            this.imageViever.setImage(imageSet.get(input));
            lastImagePriority = imagePriorityMap.get(input);
        }
    }

}
