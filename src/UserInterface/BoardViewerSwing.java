package UserInterface;

import Model.Board;
import Model.Cell;
import Model.abstractInterface.Observer;
import UserInterface.abstractInterface.Action;
import UserInterface.abstractInterface.ActionFactory;
import UserInterface.abstractInterface.BoardViewer;
import UserInterface.abstractInterface.CellViewer;
import UserInterface.abstractInterface.CellViewerFactory;
import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.JPanel;

public class BoardViewerSwing extends JPanel implements BoardViewer {

    private final CellViewerFactory cellViewerFactory;
    private final ActionFactory<Action> actionFactory;
    private CellViewer[][] viewers;
    private Board board;

    public BoardViewerSwing(CellViewerFactory cellFactory, ActionFactory<Action> factory) {
        super();
        this.cellViewerFactory = cellFactory;
        this.actionFactory = factory;
        this.viewers = null;
    }

    @Override
    public void load(Board board) {
        clearBoardViewer();
        this.board = board;
        this.board.addObserver(new Observer() {
            @Override
            public void update(String event) {
                if(event.equalsIgnoreCase("Win")) gameWinned();
            }
        });
        this.viewers = new CellViewer[this.board.getHigh()][this.board.getWidth()];
        this.setLayout(new GridLayout(this.board.getHigh(), this.board.getWidth()));
        this.createCellViewers(board.getBoard());
        this.addCellViewers();
    }

    private void clearBoardViewer() {
        this.removeAll();
        this.viewers = null;
    }

    private void gameWinned() {
        Action act = actionFactory.createAction("YouWin");
        if (act != null) act.execute();
    }

    private void createCellViewers(Cell[][] board) {
        for (int i = 0; i < viewers.length; i++)
            for (int j = 0; j < viewers[0].length; j++)
                viewers[i][j] = this.cellViewerFactory.createCellViewer(board[i][j]);
    }

    private void addCellViewers() {
        for (int i = 0; i < viewers.length; i++)
            for (int j = 0; j < viewers[0].length; j++)
                this.add((Component) viewers[i][j]);
    }

    @Override
    public void restart() {
        board.restart();
    }
}
