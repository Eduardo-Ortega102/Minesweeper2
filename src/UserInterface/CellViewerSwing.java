package UserInterface;

import Model.Cell;
import Model.abstractInterface.Image;
import Model.abstractInterface.Observable;
import Model.abstractInterface.Observer;
import UserInterface.abstractInterface.Action;
import UserInterface.abstractInterface.ActionFactory;
import UserInterface.abstractInterface.CellViewer;
import UserInterface.abstractInterface.ImageViewer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class CellViewerSwing extends JPanel implements CellViewer, ImageViewer, Observable {

    private final ActionFactory<Action> factory;
    private final JButton button;
    private final Cell cell;
    private final JLabel label;
    private Image image;
    private boolean marked;
    private Observer controlObserver;

    public CellViewerSwing(Cell cell, ActionFactory<Action> factory) {
        super(new BorderLayout());
        this.factory = factory;
        this.button = createButton();
        this.cell = cell;
        this.label = new JLabel();
        this.cell.addObserver(createObserver());
        this.marked = false;
        this.addComponents();
    }

    private JButton createButton() {
        JButton but = new JButton("     ");
        but.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cell.open();
            }
        });

        but.addMouseListener(new MouseListener() {
            private boolean pressed = false;

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    if (!cell.mark()){
                        marked = false;
                        button.setIcon(null);
                        image = null;
                    }
                } else {
                    pressed = true;
                    Action act = factory.createAction("CellViewerSelected");
                    if (act != null) act.execute();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                pressed = false;
                Action act = factory.createAction("CellViewerUnselected");
                if (act != null) act.execute();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                Action act = null;
                if (pressed) act = factory.createAction("CellViewerSelected");
                if (act != null) act.execute();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                Action act = factory.createAction("CellViewerUnselected");
                if (act != null) act.execute();
            }
        });
        return but;
    }

    private Observer createObserver() {
        return new Observer() {
            @Override
            public void update(final String event) {
                switch (event) {
                    case "Opened":
                        int count = cell.countNeighborsWithMines();
                        refresh((count == 0) ? "" : Integer.toString(count));
                        break;
                    case "Marked":
                        marked = true;
                        notifyObservers("marked");
                        break;
                    case "errorMarked":
                        notifyObservers("errorMarked");
                        break;
                    case "Locked":
                        notifyObservers("*");
                        break;
                    case "Exploded":
                        notifyObservers("**");
                        Action act = factory.createAction("GameOver");
                        if (act != null) act.execute();
                        break;
                    default:
                        restart();
                }
            }
        };
    }

    private void addComponents() {
        this.add(button, BorderLayout.CENTER);
    }

    private void refreshIcon() {
        java.awt.Image imag = (java.awt.Image) (image.getBitmap().getImage());
        button.setIcon(new ImageIcon(imag));
        button.setText("");
        image = null;
    }

    private void refresh(String text) {
        if (!"".equals(text)) setLabelText(text);
        addLabel();
    }

    @Override
    public void setImage(Image image) {
        this.image = image;
        if (marked){
            refreshIcon();
            marked = false;
        }else{
            this.repaint();
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (image != null) {
            this.addLabel();
            this.remove(label);
            java.awt.Image imag = (java.awt.Image) (image.getBitmap().getImage());
            int x = (this.getWidth() - imag.getWidth(null)) / 2;
            int y = (this.getHeight() - imag.getHeight(null)) / 2;
            g.drawImage(imag, x, y, null);
        }
    }

    private void addLabel() {
        this.setBorder(BorderFactory.createLineBorder(Color.gray));
        this.setBackground(Color.lightGray);
        this.remove(button);
        this.add(label, BorderLayout.CENTER);
    }

    @Override
    public void restart() {
        this.button.setIcon(null);
        this.image = null;
        this.setBorder(null);
        this.setBackground(null);
        this.remove(label);
        this.addComponents();
    }

    private void setLabelText(String text) {
        switch (Integer.parseInt(text)) {
            case 1:
                this.label.setForeground(Color.blue);
                break;
            case 2:
                this.label.setForeground(Color.green.darker().darker());
                break;
            case 3:
                this.label.setForeground(Color.red);
                break;
            case 4:
                this.label.setForeground(Color.MAGENTA);
                break;
            case 5:
                this.label.setForeground(Color.red.darker().darker());
                break;
            case 6:
                this.label.setForeground(Color.MAGENTA.darker());
                break;
            case 7:
                this.label.setForeground(Color.gray.darker());
                break;
            case 8:
                this.label.setForeground(Color.black);
                break;
        }
        this.label.setHorizontalAlignment(SwingConstants.CENTER);
        this.label.setText(text);
    }

    @Override
    public void addObserver(Observer oberver) {
        this.controlObserver = oberver;
    }

    @Override
    public void removeObserver(Observer oberver) {
    }

    @Override
    public void notifyObservers(String event) {
        this.controlObserver.update(event);
    }
}
