package Control;

import Model.ImageSet;
import Model.abstractInterface.Observer;
import UserInterface.abstractInterface.ImageViewer;

public class ObserverImageControl implements Observer {

    private final ImageViewer imageViever;
    private final ImageSet set;

    public ObserverImageControl(ImageViewer imageViever, ImageSet set) {
        this.imageViever = imageViever;
        this.set = set;
    }

    @Override
    public void update(String event) {
        if (event.equalsIgnoreCase("marked")) {
            this.imageViever.setImage(set.get("markedIcon.jpg"));
            
        }else if (event.equalsIgnoreCase("errorMarked")) {
            this.imageViever.setImage(set.get("errorMarkedIcon.jpg"));
            
        }else if (event.equalsIgnoreCase("*")) {
            this.imageViever.setImage(set.get("mineIcon.jpg"));
            
        }else if (event.equalsIgnoreCase("**")) {
            this.imageViever.setImage(set.get("explodedIcon.jpg"));
        }
    }
}
