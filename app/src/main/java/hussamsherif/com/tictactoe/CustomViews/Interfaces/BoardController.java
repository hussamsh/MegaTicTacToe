package hussamsherif.com.tictactoe.CustomViews.Interfaces;

import hussamsherif.com.tictactoe.CustomViews.Boards.Board;
import hussamsherif.com.tictactoe.CustomViews.Viewgroups.CellGridLayout;

public interface BoardController {
    void flipTurns(@CellGridLayout.CellPosition.Gravity int gravity);
    void onWinnerFound(@Board.Player int player);
    boolean isWinnerAnnounced();
    void onDraw();
    void restartGame();
}
