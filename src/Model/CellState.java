package Model;

public class CellState {

    public static final CellState OPENED = new CellState();
    public static final CellState CLOSED = new CellState();
    public static final CellState MARKED = new CellState();
    public static final CellState LOCKED = new CellState();
    public static final CellState EXPLODED = new CellState();

    private CellState() {
    }
}
