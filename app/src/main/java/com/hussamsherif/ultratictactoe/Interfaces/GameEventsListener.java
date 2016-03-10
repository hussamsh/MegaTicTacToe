package com.hussamsherif.ultratictactoe.Interfaces;

import com.hussamsherif.ultratictactoe.CustomViews.Boards.Board;

public interface GameEventsListener {
    void onGameDraw();
    void onPlayersTurnChange(@Board.Player int nextPlayer);
    void onGameRestarted();
    void onWinnerFound(@Board.Player int player);
}
