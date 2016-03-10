package com.hussamsherif.ultratictactoe.Interfaces;

import com.hussamsherif.ultratictactoe.CustomViews.Boards.Board;
import com.hussamsherif.ultratictactoe.CustomViews.Viewgroups.CellGridLayout;

public interface XOCellListener {
    void childCellClicked(@CellGridLayout.CellPosition.Gravity int gravity);
    void onWinnerFound(@Board.Player int player);
    void onGameDraw();
    void restartGame();
    @Board.Player int getCurrentPlayer();
    @Board.Player int getAIPlayer();
}
