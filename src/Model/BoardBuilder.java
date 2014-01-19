package Model;

import Model.abstractInterface.BuilderException;
import Model.abstractInterface.Mine;
import java.util.Random;

public class BoardBuilder {

    private Cell[][] board;
    private int minesNumber;
    private int[] minesPerRow;
    private int maxMinePerRow;
    private int high;
    private int width;

    public BoardBuilder() {
    }

    public Cell[][] getBoard() {
        return board;
    }

    public int getMinesNumber() {
        return minesNumber;
    }

    public void buildBoard(int high, int width, int minesNumber) throws BuilderException {
        if (!checkParameters(high, width, minesNumber))
            throw new BuilderException();
        this.minesNumber = minesNumber;
        this.high = high;
        this.width = width;
        this.maxMinePerRow = 1;
        this.board = new Cell[this.high][this.width];

        inicializeMinesPerRow();
        createMineField();
    }

    private boolean checkParameters(int high, int width, int minesNumber) {
        if (high < 9 || high > 24) return false;
        if (width < 9 || width > 30) return false;
        if (minesNumber < 10 || minesNumber >= high * width || minesNumber > 668)
            return false;
        return true;
    }

    private void inicializeMinesPerRow() {
        minesPerRow = new int[this.high];
        for (int i = 0; i < minesPerRow.length; i++)
            minesPerRow[i] = 0;
    }

    private void createMineField() {
        for (int i = 0; i < this.high; i++)
            for (int j = 0; j < this.width; j++)
                board[i][j] = new Cell();
        setNeighbors();
        setMines();
    }

    private void setNeighbors() {
        for (int i = 0; i < this.high; i++)
            for (int j = 0; j < this.width; j++)
                addNeighbors(i, j);
    }

    private void addNeighbors(int i, int j) {
        for (int k = i - 1; k <= i + 1; k++)
            for (int m = j - 1; m <= j + 1; m++)
                if (existNeighbor(k, m))
                    board[i][j].addNeighbor(board[k][m]);
    }

    private boolean existNeighbor(int i, int j) {
        if (i < 0 || i > this.high - 1) return false;
        if (j < 0 || j > this.width - 1) return false;
        return true;
    }

    private void setMines() {
        int cellAmount = this.high * this.width;
        Random rand = new Random();
        int minesSet = this.minesNumber;
        while (minesSet > 0) {
            for (int iteration = 0; iteration < cellAmount && minesSet > 0; iteration++) {
                final int i = rand.nextInt(this.high);
                final int j = rand.nextInt(this.width);
                if (minesPerRow[i] == maxMinePerRow) continue;
                if (board[i][j].hasMine()) continue;

                if (putMine(minesSet)) {
                    board[i][j].setMine(new Mine() {
                        @Override
                        public void explode() {
                            board[i][j].notifyObservers("Exploded");
                        }
                    });
                    minesPerRow[i]++;
                    minesSet--;
                }
            }
            maxMinePerRow += 1;
        }
    }

    private boolean putMine(int minesSet) {
        if (minesSet == 0) return false;
        return ((Math.random()) > (1.0 / 2)) ? false : true;
    }
}
