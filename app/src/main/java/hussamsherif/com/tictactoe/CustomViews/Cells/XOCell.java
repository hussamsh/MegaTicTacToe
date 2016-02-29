package hussamsherif.com.tictactoe.CustomViews.Cells;

import android.content.Context;

import java.util.ArrayList;

import hussamsherif.com.tictactoe.CustomViews.Boards.Board;
import hussamsherif.com.tictactoe.CustomViews.Viewgroups.CellGridLayout;
import hussamsherif.com.tictactoe.Interfaces.CellParent;
import hussamsherif.com.tictactoe.Interfaces.BoardController;

public class XOCell extends CellGridLayout implements CellParent {

    private final int CELLS_COUNT = 9;
    private ArrayList<Cell> cells = new ArrayList<>(CELLS_COUNT);

    private BoardController parent ;
    private boolean isFull ;
    private boolean isGameEnded ;

    public XOCell(Context context) {
        super(context);
        this.parent = (BoardController) getContext();
        for (int i = 0; i < CELLS_COUNT; i++){
            Cell cell = new Cell(context , CellPosition.getGravity(i) , this);
            cells.add(cell);
            addView(cell);
        }
    }

    public void disable(){
        for (Cell cell: cells)
            cell.setIsParentEnabled(false);
    }

    public void enable(){
        for (Cell cell: cells)
            cell.setIsParentEnabled(true);
    }

    public void clear(){
        for (Cell cell : cells)
            cell.clear();

        setIsGameEnded(false);
    }


    public boolean isFull() {
        return isFull;
    }

    @Override
    public void cellClicked(int gravity , int value) {
        if (!isFull){
            if (!checkWinner(gravity))
                parent.flipTurns(gravity);

            if (checkIfFull() && !parent.isWinnerAnnounced())
                parent.onDraw();

            isFull = checkIfFull();
        }
    }

    @Override
    public boolean isGameEnded() {
        return isGameEnded;
    }

    @Override
    public void playAgain() {
        parent.restartGame();
    }

    public void setIsGameEnded(boolean isGameEnded) {
        this.isGameEnded = isGameEnded;
    }

    private boolean checkIfFull(){
        for (Cell cell : cells){
            if (cell.getValue() == Cell.EMPTY)
                return false;
        }
        return true;
    }

    private boolean checkWinner(@CellPosition.Gravity int gravity){
        if (gravity == CellPosition.TOP_RIGHT || gravity == CellPosition.TOP_LEFT ||
                gravity == CellPosition.BOTTOM_RIGHT || gravity == CellPosition.BOTTOM_LEFT ||gravity == CellPosition.CENTER){
            return checkDiagonal(gravity) || checkColumn(gravity) || checkRow(gravity);
        }
            return checkColumn(gravity) || checkRow(gravity);
    }

    private boolean checkDiagonal(@CellPosition.Gravity int gravity){
        if (gravity == CellPosition.CENTER){
            return checkDiagonal(CellPosition.TOP_RIGHT) || checkDiagonal(CellPosition.TOP_LEFT);
        }

        return checkIfEqual(cells.get(gravity).getValue() , cells.get(CellPosition.CENTER).getValue() ,
                cells.get(CellPosition.getOppositeCell(gravity)).getValue());
    }

    //Ew !! switch cases :(
    private boolean checkRow(@CellPosition.Gravity int gravity){
        switch (gravity){
            case CellPosition.TOP_LEFT :case CellPosition.TOP:case CellPosition.TOP_RIGHT:
                return checkIfEqual(cells.get(CellPosition.TOP_LEFT).getValue() , cells.get(CellPosition.TOP).getValue() ,
                        cells.get(CellPosition.TOP_RIGHT).getValue());

            case CellPosition.CENTER_LEFT: case CellPosition.CENTER: case CellPosition.CENTER_RIGHT:
                return checkIfEqual(cells.get(CellPosition.CENTER_LEFT).getValue() ,
                        cells.get(CellPosition.CENTER).getValue() , cells.get(CellPosition.CENTER_RIGHT).getValue());

            case CellPosition.BOTTOM_LEFT: case CellPosition.BOTTOM: case CellPosition.BOTTOM_RIGHT:
                return checkIfEqual(cells.get(CellPosition.BOTTOM_LEFT).getValue() ,
                        cells.get(CellPosition.BOTTOM).getValue() , cells.get(CellPosition.BOTTOM_RIGHT).getValue());

            default:
                throw new IllegalArgumentException("int is not a valid gravity");
        }
    }

    private boolean checkColumn(@CellPosition.Gravity int gravity){
        switch (gravity){
            case CellPosition.TOP_LEFT :case CellPosition.CENTER_LEFT:case CellPosition.BOTTOM_LEFT:
                return checkIfEqual(cells.get(CellPosition.TOP_LEFT).getValue() , cells.get(CellPosition.CENTER_LEFT).getValue() ,
                        cells.get(CellPosition.BOTTOM_LEFT).getValue());

            case CellPosition.TOP: case CellPosition.CENTER: case CellPosition.BOTTOM:
                return checkIfEqual(cells.get(CellPosition.TOP).getValue() ,
                        cells.get(CellPosition.CENTER).getValue() , cells.get(CellPosition.BOTTOM).getValue());

            case CellPosition.TOP_RIGHT: case CellPosition.CENTER_RIGHT: case CellPosition.BOTTOM_RIGHT:
                return checkIfEqual(cells.get(CellPosition.TOP_RIGHT).getValue() ,
                        cells.get(CellPosition.CENTER_RIGHT).getValue() , cells.get(CellPosition.BOTTOM_RIGHT).getValue());

            default:
                throw new IllegalArgumentException("int is not a valid gravity");
        }
    }

    private boolean checkIfEqual(@Cell.GameValue int value1 , @Cell.GameValue int value2 , @Cell.GameValue int value3){
        boolean winnerFound = (value1 != Cell.EMPTY && value1 == value2 && value1 == value3);

        if (winnerFound)
            parent.onWinnerFound(value1 == Cell.X ? Board.PLAYER_X : Board.PLAYER_O);

        return winnerFound;
    }

}
