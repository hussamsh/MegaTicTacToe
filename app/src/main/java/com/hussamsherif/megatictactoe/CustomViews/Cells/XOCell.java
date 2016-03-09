package com.hussamsherif.megatictactoe.CustomViews.Cells;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.hussamsherif.megatictactoe.AI.Noah;
import com.hussamsherif.megatictactoe.CustomViews.Boards.Board;
import com.hussamsherif.megatictactoe.CustomViews.Viewgroups.CellGridLayout;
import com.hussamsherif.megatictactoe.Interfaces.CellParent;
import com.hussamsherif.megatictactoe.Interfaces.XOCellListener;
import com.hussamsherif.megatictactoe.R;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Random;

public class XOCell extends CellGridLayout implements CellParent {

    private final int CELLS_COUNT = 9;
    private ArrayList<Cell> cells = new ArrayList<>(CELLS_COUNT);
    private XOCellListener parent ;
    private boolean isFull ;
    private boolean isGameEnded;
    private int identifier ;
    private int backgroundUnactiveColor;

    //Initialize Int definitions instead of enums
    @IntDef({X,O,EMPTY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface GameValue {}

    public static final int O = 1;
    public static final int X = 2;
    public static final int EMPTY = -1 ;

    public XOCell(Context context , int identifier) {
        super(context);
        this.identifier = identifier;
        boolean isEven = identifier%2 == 0;
        backgroundUnactiveColor = PreferenceManager.getDefaultSharedPreferences(context).getInt(isEven ? "even_cell_color_preference" :
                "odd_cell_color_preference" , ContextCompat.getColor(context, isEven ? R.color.default_even_cell_color : R.color.default_odd_cell_color));
        for (int i = 0; i < CELLS_COUNT; i++){
            Cell cell = new Cell(context , CellPosition.getGravity(i));
            addView(cell);
            cells.add(cell);
        }
    }

    @Override
    public void childCellClicked(int gravity, int value) {
        if (!isFull){
            if (!hasWon(parent.getCurrentPlayer()))
                parent.childCellClicked(gravity);
            else
                parent.onWinnerFound(parent.getCurrentPlayer());

            isFull = checkIfFull();
        }
    }

    public void disable(){
        for (Cell cell: cells)
            cell.setIsParentEnabled(false);
    }

    public void enable(){
        if (isFull()){
            parent.onGameDraw();
            return;
        }
        for (Cell cell: cells)
            cell.setIsParentEnabled(true);
    }

    public void clear(){
        for (Cell cell : cells)
            cell.clear();

        setIsGameEnded(false);
        setIsFull(false);
    }

    public void randomClick(){
        while (true) {
            Random rand = new Random();
            int cellIndex = rand.nextInt(9);
            Cell randomCell = cells.get(cellIndex);
            if (!randomCell.isClicked) {
                randomCell.callOnClick();
                Toast.makeText(getContext() , getContext().getString(R.string.random_click) , Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    public void playCellAt(@CellPosition.Gravity int gravity){
        cells.get(gravity).callOnClick();
    }

    public void aiMove(ArrayList<int[][]> abstractBoard){
        Noah noah = new Noah(getCellValues());
        int bestMove = noah.move(abstractBoard, identifier , parent.getAIPlayer());
        cells.get(bestMove).callOnClick();
    }

    private int[] winningPatterns = {
            0b111000000, 0b000111000, 0b000000111, // rows
            0b100100100, 0b010010010, 0b001001001, // cols
            0b100010001, 0b001010100               // diagonals
    };

    /** Returns true if thePlayer wins */
    private boolean hasWon(int player) {
        int[][] abstractCell = getCellValues();
        int value = player == Board.PLAYER_X ? XOCell.X : XOCell.O;
        int pattern = 0b000000000;  // 9-bit pattern for the 9 cells
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 3; ++col) {
                if (abstractCell[row][col] == value) {
                    pattern |= (1 << (row * 3 + col));
                }
            }
        }
        for (int winningPattern : winningPatterns) {
            if ((pattern & winningPattern) == winningPattern) return true;
        }
        return false;
    }

    public int[][] getCellValues(){
        int[][] result = new int[3][3];
        int counter = 0;
        for (int i = 0 ; i < 3 ; i++){
            for (int j = 0 ; j < 3 ; j++){
                result[i][j] = cells.get(counter++).getValue();
            }
        }
        return result;
    }

    public boolean isFull() {
        return isFull;
    }

    public void setIsFull(boolean isFull) {
        this.isFull = isFull;
    }

    @Override
    public boolean isGameEnded() {
        return isGameEnded;
    }

    @Override
    public int getParentUnactiveColor() {
        return backgroundUnactiveColor;
    }

    @Override
    public int getCurrentPlayer() {
        return parent.getCurrentPlayer();
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
            if (cell.getValue() == EMPTY)
                return false;
        }
        return true;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.parent = (XOCellListener) getParent();
    }

    protected static class Cell extends ImageButton implements OnClickListener{

        private @GameValue int value ;
        private @CellPosition.Gravity int gravity ;

        private boolean isClicked ;
        private boolean isParentEnabled;
        private CellParent parent;
        private int currentColor ;
        private static IconicsDrawable XDrawable ;
        private static IconicsDrawable ODrawable;
        private boolean isAttached ;

        public Cell(Context context , @CellPosition.Gravity int gravity) {
            super(context);
            this.setValue(EMPTY);
            setOnClickListener(this);
            this.gravity = gravity;
            if (XDrawable == null)
            XDrawable = new IconicsDrawable(getContext())
                    .icon(GoogleMaterial.Icon.gmd_clear).sizeDp(15).color(PreferenceManager.getDefaultSharedPreferences(context).getInt("x_color" , ContextCompat.getColor(context, R.color.default_x_color)));
            if (ODrawable == null)
            ODrawable = new IconicsDrawable(getContext())
                    .icon(GoogleMaterial.Icon.gmd_panorama_fish_eye).sizeDp(15).color(PreferenceManager.getDefaultSharedPreferences(context).getInt("o_color" , ContextCompat.getColor(context, R.color.default_o_color)));
        }

        public void setIsParentEnabled(boolean isParentEnabled) {
            this.isParentEnabled = isParentEnabled;
            if (isAttached){
                final int toColor = (isParentEnabled ? PreferenceManager.getDefaultSharedPreferences(getContext()).getInt("active_cell_color" , ContextCompat.getColor(getContext(), R.color.default_active_cell_color)) :
                        parent.getParentUnactiveColor());
                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), currentColor, toColor);
                colorAnimation.setDuration(300);
                colorAnimation.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {}

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        currentColor = toColor;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {}

                    @Override
                    public void onAnimationRepeat(Animator animation) {}
                });
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        setBackgroundColor((int) animator.getAnimatedValue());
                    }
                });
                colorAnimation.start();
            }

        }

        @Override
        public void onClick(View v) {
            if (parent.getCurrentPlayer() == Board.PLAYER_X){
                //static access because variable X already exists
                setValue(XOCell.X);
            }else if (parent.getCurrentPlayer() == Board.PLAYER_O)
                setValue(O);
        }

        private void setValue(@GameValue int value){
            if (value == EMPTY){
                this.value = EMPTY;
                setImageDrawable(null);
                isClicked = false;
                return;
            }

            if (parent.isGameEnded()){
                Snackbar.make(this, getContext().getString(R.string.game_ended), Snackbar.LENGTH_LONG)
                        .setAction(getContext().getString(R.string.play_again), new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                parent.playAgain();
                            }
                        })
                        .show();
                return;
            }

            if (isParentEnabled){
                if (!isClicked){
                    if (value == XOCell.X){
                        this.value = value;
                        setImageDrawable(XDrawable);
                    }
                    else if (value == O){
                        this.value = value;
                        setImageDrawable(ODrawable);
                    }
                    isClicked = true;
                    parent.childCellClicked(gravity, value);
                }else{
                    Snackbar.make(this , getContext().getString(R.string.cell_already_played) , Snackbar.LENGTH_SHORT).show();
                }
            }else
                Snackbar.make(this , getContext().getString(R.string.cell_disabled) , Snackbar.LENGTH_SHORT).show();
        }

        @GameValue
        public int getValue() {
            return value;
        }

        public void clear(){
            setValue(EMPTY);
        }

        @Override
        public void setBackgroundColor(int color) {
            super.setBackgroundColor(color);
            currentColor = color;
        }

        @Override
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            isAttached = true;
            this.parent = (CellParent) getParent();
            currentColor = (isParentEnabled ? PreferenceManager.getDefaultSharedPreferences(getContext()).getInt("active_cell_color" , ContextCompat.getColor(getContext(), R.color.default_active_cell_color)) :
                    parent.getParentUnactiveColor());
            setBackgroundColor(currentColor);
        }
    }


}
