package UserInterface;

import Model.Board;
import Model.abstractInterface.Observer;
import UserInterface.abstractInterface.ImageViewer;
import UserInterface.abstractInterface.InfoPanel;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class InfoPanelSwing extends JPanel implements InfoPanel {

    private JLabel timerLabel;
    private JLabel minesLabel;
    private final Board board;
    private String time;
    private String mines;
    private final ImageViewer viewer;

    public InfoPanelSwing(Board board, ImageViewer viewer) {
        super(new FlowLayout(FlowLayout.CENTER));
        this.board = board;
        this.viewer = viewer;
        this.timerLabel = new JLabel();
        this.minesLabel = new JLabel();
        this.addComponents();
    }

    private void addComponents() {
        this.add(createTimerPanel());
        this.add((Component) this.viewer);
        this.add(createMinesNumberPanel());
        this.board.getClock().addObserver(createObserver());
    }

    private Observer createObserver() {
        return new Observer() {
            @Override
            public void update(String playedTime) {
                time = playedTime;
                refresh();
            }
        };
    }

    private JPanel createTimerPanel() {
        JPanel panel = new JPanel();
        this.timerLabel.setText("Played Time: ");
        this.timerLabel.setForeground(Color.red);
        panel.setBackground(Color.BLACK);
        panel.add(this.timerLabel);
        return panel;
    }

    private JPanel createMinesNumberPanel() {
        JPanel panel = new JPanel();
        this.minesLabel.setText("Mines Number: ");
        this.minesLabel.setForeground(Color.red);
        panel.setBackground(Color.BLACK);
        panel.add(this.minesLabel);
        return panel;
    }

    @Override
    public void refresh() {
        this.timerLabel.setText("Played Time: " + time);
        if (time.equalsIgnoreCase("00:00")) {
            mines = Integer.toString(board.getMinesNumber());
            this.minesLabel.setText("Mines Number: " + mines);
        }
    }
}
