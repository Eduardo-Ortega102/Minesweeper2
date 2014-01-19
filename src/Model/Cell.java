package Model;

import Model.abstractInterface.Mine;
import Model.abstractInterface.Observer;
import Model.abstractInterface.Observable;
import java.util.ArrayList;

public class Cell implements Observable {

    private CellState state;
    private ArrayList<Observer> observers;
    private ArrayList<Cell> neighbors;
    private Mine mine;

    public Cell() {
        this.state = CellState.CLOSED;
        this.observers = new ArrayList<>();
        this.neighbors = new ArrayList<>();
    }

    public void addNeighbor(Cell neighbor) {
        this.neighbors.add(neighbor);
    }

    public void setMine(Mine mine) {
        this.mine = mine;
    }

    public boolean hasMine() {
        return this.mine != null;
    }

    public boolean mark() {
        if (this.state == CellState.MARKED) {
            this.state = CellState.CLOSED;
            return false;
        }
        if (this.state != CellState.CLOSED) return false;
        this.state = CellState.MARKED;
        notifyObservers("Marked");
        return true;
    }

    public void close() {
        this.state = CellState.CLOSED;
        notifyObservers("Closed");
    }

    public void lock(String mode) {
        if (this.state == CellState.MARKED) {
            this.state = CellState.LOCKED;
            if (!hasMine()) notifyObservers("errorMarked");
        }
        if (this.state != CellState.CLOSED) return;
        this.state = CellState.LOCKED;
        if (hasMine() && mode.equalsIgnoreCase("E")) notifyObservers("Locked");
    }

    public void open() {
        if (this.state != CellState.CLOSED) return;
        if (hasMine()) {
            explodeMine();
        } else {
            this.state = CellState.OPENED;
            notifyObservers("Opened");
            if (countNeighborsWithMines() == 0) openNeighbors();
        }
    }

    public void explodeMine() {
        this.state = CellState.EXPLODED;
        this.mine.explode();
    }

    public int countNeighborsWithMines() {
        int count = 0;
        for (Cell cell : neighbors)
            if (cell.hasMine()) count++;
        return count;
    }

    private void openNeighbors() {
        for (Cell cell : neighbors)
            cell.open();
    }

    @Override
    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(Observer oberver) {
    }

    @Override
    public void notifyObservers(final String event) {
        for (Observer observer : observers) {
            observer.update(event);
        }
    }

    @Override
    public String toString() {
        return (hasMine()) ? "T" : "" + countNeighborsWithMines();
    }
}
