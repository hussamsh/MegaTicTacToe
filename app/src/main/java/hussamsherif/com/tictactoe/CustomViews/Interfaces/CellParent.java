package hussamsherif.com.tictactoe.CustomViews.Interfaces;

import hussamsherif.com.tictactoe.CustomViews.Cells.XOCell;
import hussamsherif.com.tictactoe.CustomViews.Viewgroups.CellGridLayout;

public interface CellParent {
    void childCellClicked(@CellGridLayout.CellPosition.Gravity int gravity, @XOCell.GameValue int value);
    boolean isGameEnded();
    int getParentUnactiveColor();
    void playAgain();
}
