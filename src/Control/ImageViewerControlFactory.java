package Control;

import Model.ImageSet;
import UserInterface.abstractInterface.ImageViewer;

public interface ImageViewerControlFactory {

    public ImageViewerControl createImageViewerControl(ImageViewer viewer, ImageSet set);
}
