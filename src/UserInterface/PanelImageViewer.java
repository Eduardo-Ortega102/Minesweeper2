package UserInterface;

import Model.abstractInterface.Image;
import UserInterface.abstractInterface.ImageViewer;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

public class PanelImageViewer extends JPanel implements ImageViewer{

  private Image image;

    public PanelImageViewer(){
        super();
    }
    
    @Override
    public void setImage(Image image){
        this.image = image;
        this.setPreferredSize(new Dimension(
                ((java.awt.Image)(image.getBitmap().getImage())).getWidth(null),
                ((java.awt.Image)(image.getBitmap().getImage())).getHeight(null)));
        this.repaint();
    }

    @Override
    public void paint(Graphics g) {
        clearPanel(g);
        g.drawImage((java.awt.Image)(image.getBitmap().getImage()), 0, 0, null);
    }

    private void clearPanel(Graphics g) {
        super.paint(g);
    }
       
}
