package UserInterface.abstractInterface;

import Model.Cell;

public interface CellViewerFactory {

    public CellViewer createCellViewer(Cell cell);
}
