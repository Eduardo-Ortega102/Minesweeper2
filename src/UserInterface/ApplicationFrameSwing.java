package UserInterface;

import UserInterface.abstractInterface.ApplicationFrame;
import UserInterface.abstractInterface.ActionFactory;
import UserInterface.abstractInterface.BoardViewer;
import UserInterface.abstractInterface.InfoPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ApplicationFrameSwing extends JFrame implements ApplicationFrame {

    private final ActionFactory<ActionListener> factory;
    private BoardViewer boardViewer;
    private final InfoPanel infoPanel;

    public ApplicationFrameSwing(ActionFactory factory, BoardViewer boardViewer,
            InfoPanel infoPanel) throws HeadlessException {
        super("MinesWeeper");
        this.factory = factory;
        this.boardViewer = boardViewer;
        this.infoPanel = infoPanel;
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.addComponents();
    }

    private void addComponents() {
        this.add(createToolbar(), BorderLayout.NORTH);
        this.add((Component) this.infoPanel, BorderLayout.SOUTH);
        this.add((Component) boardViewer, BorderLayout.CENTER);
    }

    private JPanel createToolbar() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.BLUE);
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.add(createMenus());
        return panel;
    }

    private JMenuBar createMenus() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createGameMenu());
        menuBar.add(createHelpMenu());
        return menuBar;
    }

    private JMenu createGameMenu() {
        JMenu game = new JMenu("Game");
        game.add(createItem("NewGame", "New Game"));
        game.add(createItem("Options", "Options"));
        game.add(createItem("Exit", "Exit"));
        return game;
    }

    private JMenu createHelpMenu() {
        JMenu help = new JMenu("Help");
        help.add(createItem("Help", "How to play"));
        return help;
    }

    private JMenuItem createItem(String action, String label) {
        JMenuItem item = new JMenuItem(label);
        item.addActionListener(factory.createAction(action));
        return item;
    }
    
    @Override
    public BoardViewer getBoardViewer() {
        return this.boardViewer;
    }

    @Override
    public void execute() {
        this.setScreenLocation();
        this.setVisible(true);
    }

    private void setScreenLocation() {
        this.pack();
        Toolkit tk = Toolkit.getDefaultToolkit();
        this.setLocation(
                (int) ((tk.getScreenSize().getWidth() - this.getWidth()) / 2),
                (int) ((tk.getScreenSize().getHeight() - this.getHeight()) / 2));
    }
}
