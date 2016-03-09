package com.hussamsherif.megatictactoe.Interfaces;

import com.hussamsherif.megatictactoe.CustomViews.Boards.Board;
import com.hussamsherif.megatictactoe.CustomViews.Cells.XOCell;
import com.hussamsherif.megatictactoe.CustomViews.Viewgroups.CellGridLayout;

public interface CellParent {
    void childCellClicked(@CellGridLayout.CellPosition.Gravity int gravity, @XOCell.GameValue int value);
    void playAgain();
    int getParentUnactiveColor();
    @Board.Player int getCurrentPlayer();
    boolean isGameEnded();
}
