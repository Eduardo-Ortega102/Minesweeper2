package Control;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ImagePriority implements Comparable<ImagePriority>{

    public static final ImagePriority HIGH = new ImagePriority();
    public static final ImagePriority LOW = new ImagePriority();
    private static final ImagePriority[] SET = {LOW,HIGH};

    private ImagePriority() {
    }

    @Override
    public int compareTo(ImagePriority element) {
        try {
            return this.getVaule().compareTo(element.getVaule());
        } catch (Exception ex) {
            Logger.getLogger(ImagePriority.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    private Integer getVaule() throws Exception{
        for (int index = 0; index < SET.length; index++)
            if (SET[index] == this) return index;
        throw new Exception("This never happend.");
    }
}
