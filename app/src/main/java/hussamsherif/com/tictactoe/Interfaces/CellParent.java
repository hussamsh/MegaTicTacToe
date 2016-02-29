package hussamsherif.com.tictactoe.Interfaces;

import hussamsherif.com.tictactoe.CustomViews.Viewgroups.CellGridLayout;
import hussamsherif.com.tictactoe.CustomViews.Cells.Cell;

public interface CellParent {
    void cellClicked(@CellGridLayout.CellPosition.Gravity int gravity , @Cell.GameValue int value);
    boolean isGameEnded();
    void playAgain();
}
