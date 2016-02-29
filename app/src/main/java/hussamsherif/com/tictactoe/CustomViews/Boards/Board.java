package hussamsherif.com.tictactoe.CustomViews.Boards;

import android.content.Context;
import android.support.annotation.IntDef;
import android.util.AttributeSet;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

import hussamsherif.com.tictactoe.CustomViews.Viewgroups.CellGridLayout;
import hussamsherif.com.tictactoe.CustomViews.Cells.XOCell;

public class Board extends CellGridLayout {

    public static final int PLAYER_X = 0;
    public static final int PLAYER_O = 1;

    //Create int definitions, rather than enums
    @IntDef({PLAYER_X, PLAYER_O})
    @Retention(RetentionPolicy.CLASS)
    public @interface Player {}

    //Current Player
    @Player private static int CURRENT_PLAYER ;

    private static final int XO_CELLS_COUNT = 9;

    private ArrayList<XOCell> XOCells = new ArrayList<>(XO_CELLS_COUNT);

    private XOCell activeCell ;

    public Board(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCurrentPlayer(PLAYER_X);
        for (int i = 0; i < XO_CELLS_COUNT; i++){
            XOCell xoCell = new XOCell(context);
            xoCell.disable();
            XOCells.add(xoCell);
            addView(xoCell);
        }
        activeCell = XOCells.get(4);
        activeCell.enable();
    }

    private void enableCellAt(int gravity) {
        XOCell xoCell = XOCells.get(gravity);
        xoCell.enable();
        activeCell = xoCell;
    }

    private void disableCurrentCell() {
        activeCell.disable();
    }

    public void flipTurns(@CellPosition.Gravity int gravity){
        if (Board.CURRENT_PLAYER == PLAYER_X)
            CURRENT_PLAYER = PLAYER_O;
        else
            CURRENT_PLAYER = PLAYER_X;

        disableCurrentCell();
        enableCellAt(gravity);
    }

    public boolean isAllCellsFull(){
        for (XOCell xoCell : XOCells)
            if (!xoCell.isFull())
                return false;

        return true;
    }

    @Player
    public static int getCurrentPlayer() {
        return CURRENT_PLAYER;
    }

    public static void setCurrentPlayer(@Player int currentPlayer) {
        if (currentPlayer != PLAYER_X && currentPlayer != PLAYER_O)
        CURRENT_PLAYER = currentPlayer;
    }


    public void restart(){
        for (XOCell xoCell : XOCells)
            xoCell.clear();

        disableCurrentCell();
        enableCellAt(CellPosition.CENTER);
    }

    public void disable(){
        for (XOCell xoCell : XOCells)
            xoCell.setIsGameEnded(true);
    }

}
