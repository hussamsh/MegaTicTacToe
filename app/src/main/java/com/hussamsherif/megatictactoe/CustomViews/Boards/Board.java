package com.hussamsherif.megatictactoe.CustomViews.Boards;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.IntDef;
import android.util.AttributeSet;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.hussamsherif.megatictactoe.CustomViews.Cells.XOCell;
import com.hussamsherif.megatictactoe.CustomViews.Viewgroups.CellGridLayout;
import com.hussamsherif.megatictactoe.Interfaces.GameEventsListener;
import com.hussamsherif.megatictactoe.Interfaces.XOCellListener;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Random;

//Class representing the tic-tac-toe board
public class Board extends CellGridLayout implements XOCellListener {

    public static final int PLAYER_X = 0;
    public static final int PLAYER_O = 1;

    //Only to be used by Noah
    public static final int NONE = -1 ;
    private int AI = NONE;

    //Create int definitions, rather than enums
    @IntDef({PLAYER_X, PLAYER_O,NONE})
    @Retention(RetentionPolicy.CLASS)
    public @interface Player {}

    //Current Player
    @Player private int CURRENT_PLAYER ;
    private static final int XO_CELLS_COUNT = 9;
    private ArrayList<XOCell> XOCells = new ArrayList<>(XO_CELLS_COUNT);
    private XOCell activeCell ;
    private boolean isDisabled ;
    private GameEventsListener parent ;

    public Board(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCurrentPlayer(PLAYER_X);
        parent = (GameEventsListener)context;
        for (int i = 0; i < XO_CELLS_COUNT; i++){
            XOCell xoCell = new XOCell(context , i);
            addView(xoCell);
            XOCells.add(xoCell);
            xoCell.disable();
        }
    }

    private void enableCellAt(int gravity) {
        XOCell xoCell = XOCells.get(gravity);
        xoCell.enable();
        activeCell = xoCell;
        if (CURRENT_PLAYER == AI){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    activeCell.aiMove(getAbstractBoard());
                }
            },400);
        }
    }

    private void disableCurrentCell() {
        if (activeCell != null)
            activeCell.disable();
        else
            for (XOCell xoCell :XOCells)
                xoCell.disable();
    }

    //Flip turns
    @Override
    public void childCellClicked(@CellPosition.Gravity int gravity){
        if (!isAllCellsFull()){
            if (CURRENT_PLAYER == PLAYER_X)
                CURRENT_PLAYER = PLAYER_O;
            else
                CURRENT_PLAYER = PLAYER_X;

            disableCurrentCell();
            enableCellAt(gravity);
            parent.onPlayersTurnChange(CURRENT_PLAYER);
        }
        else
            this.onGameDraw();
    }

    @Override
    public void onWinnerFound(@Player int player) {
        disable();
        CURRENT_PLAYER = PLAYER_X;
        parent.onWinnerFound(player);
    }

    @Override
    public void onGameDraw() {
        disable();
        parent.onGameDraw();
    }

    @Override
    public void restartGame() {
        for (XOCell xoCell : XOCells)
            xoCell.clear();

        setCurrentPlayer(PLAYER_X);
        activeCell = null;
        start();
        parent.onGameRestarted();
    }

    public boolean isAllCellsFull(){
        for (XOCell xoCell : XOCells)
            if (!xoCell.isFull())
                return false;

        return true;
    }

    @Override
    @Player
    public int getCurrentPlayer() {
        return CURRENT_PLAYER;
    }

    public void setCurrentPlayer(@Player int currentPlayer) {
        if (currentPlayer != PLAYER_X && currentPlayer != PLAYER_O)
        CURRENT_PLAYER = currentPlayer;
    }

    public void setAIPlayer(@Player int player) {
        AI = player;
    }

    public void start(){
        for (XOCell xoCell : XOCells)
            xoCell.enable();

        //If it's the first turn and the aiMove is the one to play , play the center most cell
        if (CURRENT_PLAYER == AI)
            XOCells.get(4).playCellAt(CellPosition.CENTER);

        Answers.getInstance().logCustom(new CustomEvent("New game"));
    }

    public void disable(){
        for (XOCell xoCell : XOCells)
            xoCell.setIsGameEnded(true);
        this.isDisabled = true;
    }

    public void randomClick(){
        if (activeCell == null){
            //Get a random cell and preform a click on one of the cells
            while (true){
                Random rand = new Random();
                int cellIndex = rand.nextInt(9);
                XOCell randomCell = XOCells.get(cellIndex);
                if (!randomCell.isFull()){
                    randomCell.randomClick();
                    break;
                }
            }
        }else if (!activeCell.isFull()){
            //Preform a click on the active cell
            activeCell.randomClick();
        }
    }

    private ArrayList<int[][]> getAbstractBoard(){
        ArrayList<int[][]> result = new ArrayList<>(XOCells.size());
        for (int i = 0; i < XOCells.size(); i++) {
            result.add(XOCells.get(i).getCellValues());
        }
        return result;
    }

    @Override
    public int getAIPlayer() {
        return AI;
    }

    public boolean isDisabled() {
        return isDisabled;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}

