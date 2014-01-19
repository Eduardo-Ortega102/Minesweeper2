package UserInterface;

import Model.abstractInterface.Bitmap;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class BitmapSwing implements Bitmap<BufferedImage> {

    private BufferedImage image;

    public BitmapSwing(String filename) throws IOException {
        this.image = ImageIO.read(new File(filename));
    }

    @Override
    public BufferedImage getImage() {
        return image;
    }
}
