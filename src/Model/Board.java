package Model;

import Model.abstractInterface.Observable;
import Model.abstractInterface.Observer;

public class Board implements Observable {

    private Observer viewerObserver;
    private Clock clock;
    private int minesNumber;
    private Cell[][] board;
    private int high;
    private int width;
    private boolean firstTime;
    private int availableCells;

    public Board() {
        this.clock = new Clock();
    }

    public void setBoard(Cell[][] board, int minesNumber) {
        this.minesNumber = minesNumber;
        this.board = board;
        this.high = board.length;
        this.width = board[0].length;
        this.firstTime = true;
        this.availableCells = (this.high * this.width) - this.minesNumber;
        setObservers();
        
        printBoard();
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
