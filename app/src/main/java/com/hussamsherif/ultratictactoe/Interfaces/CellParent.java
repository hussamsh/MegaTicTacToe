package com.hussamsherif.ultratictactoe.Interfaces;

import com.hussamsherif.ultratictactoe.CustomViews.Boards.Board;
import com.hussamsherif.ultratictactoe.CustomViews.Cells.XOCell;
import com.hussamsherif.ultratictactoe.CustomViews.Viewgroups.CellGridLayout;

public interface CellParent {
    void childCellClicked(@CellGridLayout.CellPosition.Gravity int gravity, @XOCell.GameValue int value);
    void playAgain();
    int getParentUnactiveColor();
    @Board.Player int getCurrentPlayer();
    boolean isGameEnded();
}
