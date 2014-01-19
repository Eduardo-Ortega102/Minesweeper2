package Model;

import Model.abstractInterface.Mine;
import Model.abstractInterface.BoardException;
import Model.abstractInterface.Observable;
import Model.abstractInterface.Observer;
import java.util.Random;

public class Board implements Observable {

    private Cell[][] board;
    private Observer viewerObserver;
    private Clock clock;
    private int minesNumber;
    private int[] minesPerRow;
    private int maxMinePerRow;
    private int availableCells;
    private int high;
    private int width;
    private boolean firstTime;

    public Board(Clock clock) {
        this.clock = clock;
    }

    public Cell[][] getBoard() {
        return board;
    }

    public int getMinesNumber() {
        return minesNumber;
    }

    public int getHigh() {
        return high;
    }

    public int getWidth() {
        return width;
    }

    public Clock getClock() {
        return clock;
    }

    public void buildBoard(int high, int width, int minesNumber) throws BoardException {
        if (!checkParameters(high, width, minesNumber))
            throw new BoardException();
        this.minesNumber = minesNumber;
        this.high = high;
        this.width = width;
        this.maxMinePerRow = 1;
        this.firstTime = true;
        this.board = new Cell[this.high][this.width];
        this.availableCells = (this.high * this.width) - this.minesNumber;
        inicializeMinesPerRow();
        createMineField();
        setObservers();
        this.clock.resetClock();

        printBoard();
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

    private void setObservers() {
        Observer observ = createObserver();
        for (int i = 0; i < this.high; i++)
            for (int j = 0; j < this.width; j++)
                board[i][j].addObserver(observ);
    }

    private Observer createObserver() {
        return new Observer() {
            @Override
            public void update(String event) {
                if (event.equalsIgnoreCase("Opened"))
                    checkAvailableCells();
                if (event.equalsIgnoreCase("Exploded")) {
                    lockBoard("E");
                    clock.stopClock();
                }
            }
        };
    }

    private void checkAvailableCells() {
        this.availableCells--;
        if (firstTime) {
            this.clock.startClock();
            firstTime = false;
        }
        if (this.availableCells == 0) {
            this.clock.stopClock();
            lockBoard("W");
            notifyObservers("Win");
        }
    }

    private void lockBoard(String mode) {
        for (int i = 0; i < this.high; i++)
            for (int j = 0; j < this.width; j++)
                board[i][j].lock(mode);
    }

    public void restart() {
        this.firstTime = true;
        this.clock.resetClock();
        this.availableCells = (this.high * this.width) - minesNumber;
        for (int i = 0; i < this.high; i++)
            for (int j = 0; j < this.width; j++)
                board[i][j].close();
    }

    private void printBoard() {
        int count = 0;
        System.out.println("CAMPO RESULTANTE: ");
        for (int i = 0; i < board.length; i++) {
            System.out.print("(");
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j].hasMine()) count++;
                System.out.print(" " + board[i][j].toString() + " ");
            }
            System.out.println(")");
        }
        System.out.println("Numero de minas = " + count);
    }

    @Override
    public void addObserver(Observer oberver) {
        this.viewerObserver = oberver;
    }

    @Override
    public void removeObserver(Observer oberver) {
    }

    @Override
    public void notifyObservers(String event) {
        this.viewerObserver.update(event);
    }
}
