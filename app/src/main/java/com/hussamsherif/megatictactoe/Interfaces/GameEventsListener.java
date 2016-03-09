package com.hussamsherif.megatictactoe.Interfaces;

import com.hussamsherif.megatictactoe.CustomViews.Boards.Board;

public interface GameEventsListener {
    void onGameDraw();
    void onPlayersTurnChange(@Board.Player int nextPlayer);
    void onGameRestarted();
    void onWinnerFound(@Board.Player int player);
}
