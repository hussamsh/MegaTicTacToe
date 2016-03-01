package hussamsherif.com.tictactoe.CustomViews.Cells;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.preference.PreferenceManager;
import android.support.annotation.IntDef;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import org.greenrobot.eventbus.Subscribe;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Random;

import hussamsherif.com.tictactoe.Color;
import hussamsherif.com.tictactoe.Helpers.Bus;
import hussamsherif.com.tictactoe.BusEvents.ColorChangedEvent;
import hussamsherif.com.tictactoe.CustomViews.Boards.Board;
import hussamsherif.com.tictactoe.CustomViews.Viewgroups.CellGridLayout;
import hussamsherif.com.tictactoe.CustomViews.Interfaces.CellParent;
import hussamsherif.com.tictactoe.CustomViews.Interfaces.BoardController;
import hussamsherif.com.tictactoe.Helpers.Utils;
import hussamsherif.com.tictactoe.R;

public class XOCell extends CellGridLayout implements CellParent {

    private final int CELLS_COUNT = 9;
    private ArrayList<Cell> cells = new ArrayList<>(CELLS_COUNT);
    private BoardController parent ;
    private boolean isFull ;
    private boolean isGameEnded ;
    private boolean isEnabled ;
    private static int count = 0 ;
    private int identifier ;
    private int backgroundUnactiveColor;

    //Initialize Int definitions instead of enums
    @IntDef({X,O,EMPTY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface GameValue {}
    public static final int O = 1;
    public static final int X = 0;
    public static final int EMPTY = -1 ;

    public XOCell(Context context) {
        super(context);
        identifier = ++count;
        boolean isEven = identifier%2 ==0;
        backgroundUnactiveColor = PreferenceManager.getDefaultSharedPreferences(context).getInt(isEven ? "even_cell_color_preference" :
                "odd_cell_color_preference" , ContextCompat.getColor(context , isEven ? R.color.default_even_cell_color : R.color.default_odd_cell_color));
        this.parent = (BoardController) getContext();
        for (int i = 0; i < CELLS_COUNT; i++){
            Cell cell = new Cell(context , CellPosition.getGravity(i) , this);
            cells.add(cell);
            addView(cell);
        }
    }

    @Override
    public void childCellClicked(int gravity, int value) {
        if (!isFull){
            if (!checkWinner(gravity))
                parent.flipTurns(gravity);

            if (checkIfFull() && !parent.isWinnerAnnounced())
                parent.onDraw();

            isFull = checkIfFull();
        }
    }


    public void disable(){
        for (Cell cell: cells)
            cell.setIsParentEnabled(false);
        this.isEnabled = false;
    }

    public void enable(){
        for (Cell cell: cells)
            cell.setIsParentEnabled(true);
        this.isEnabled = true;
    }

    public void clear(){
        for (Cell cell : cells)
            cell.clear();

        setIsGameEnded(false);
    }

    public void randomClick(){
        while (true) {
            Random rand = new Random();
            int cellIndex = rand.nextInt(9);
            Cell randomCell = cells.get(cellIndex);
            if (!randomCell.isClicked) {
                randomCell.performClick();
                break;
            }
        }
    }

    public boolean isFull() {
        return isFull;
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

    private boolean checkIfEqual(@GameValue int value1 , @GameValue int value2 , @GameValue int value3){
        boolean winnerFound = (value1 != EMPTY && value1 == value2 && value1 == value3);

        if (winnerFound)
            parent.onWinnerFound(value1 == X ? Board.PLAYER_X : Board.PLAYER_O);

        return winnerFound;
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onColorChanged(ColorChangedEvent event){
        switch (event.getColorPreference()){
            case Color.ACTIVE_CELL_COLOR:
                if (isEnabled)
                    setCellsBackground(event.getNewColor());
                break;
            case Color.ODD_CELL_COLOR:
                if (identifier%2 !=0 && !isEnabled){
                    setCellsBackground(event.getNewColor());
                    backgroundUnactiveColor = event.getNewColor();
                }
                break;
            case Color.EVEN_CELL_COLOR:
                if (identifier%2 == 0 && !isEnabled){
                    setCellsBackground(event.getNewColor());
                    backgroundUnactiveColor = event.getNewColor();
                }
                break;
            case Color.X_COLOR:
                for (Cell cell : cells)
                    cell.setXColor(event.getNewColor());
                break;
            case Color.O_COLOR:
                for (Cell cell : cells)
                    cell.setYColor(event.getNewColor());
                break;
        }
    }

    public boolean isCellEnabled() {
        return isEnabled;
    }

    private void setCellsBackground(int color){
        for (Cell cell : cells)
            cell.setBackgroundColor(color);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Bus.getBus().register(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        Bus.getBus().unregister(this);
        super.onDetachedFromWindow();
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

        public Cell(Context context , @CellPosition.Gravity int gravity , CellParent parent) {
            super(context);
            this.setValue(EMPTY);
            this.gravity = gravity;
            this.parent = parent;
            setOnClickListener(this);
            currentColor = (isParentEnabled ? PreferenceManager.getDefaultSharedPreferences(context).getInt("active_cell_color" , ContextCompat.getColor(context , R.color.default_active_cell_color)) :
                    parent.getParentUnactiveColor());
            setBackgroundColor(currentColor);
            if (XDrawable == null)
            XDrawable = new IconicsDrawable(getContext())
                    .icon(GoogleMaterial.Icon.gmd_clear).sizeDp(15).color(PreferenceManager.getDefaultSharedPreferences(context).getInt("x_color" , ContextCompat.getColor(context , R.color.default_x_color)));
            if (ODrawable == null)
            ODrawable = new IconicsDrawable(getContext())
                    .icon(GoogleMaterial.Icon.gmd_panorama_fish_eye).sizeDp(15).color(PreferenceManager.getDefaultSharedPreferences(context).getInt("o_color" , ContextCompat.getColor(context , R.color.default_o_color)));
        }

        public void setIsParentEnabled(boolean isParentEnabled) {
            this.isParentEnabled = isParentEnabled;
            final int toColor = (isParentEnabled ? PreferenceManager.getDefaultSharedPreferences(getContext()).getInt("active_cell_color" , ContextCompat.getColor(getContext() , R.color.default_active_cell_color)) :
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

        @Override
        public void onClick(View v) {
            if (Board.getCurrentPlayer() == Board.PLAYER_X){
                //static access because variable X already exists
                setValue(XOCell.X);
            }else if (Board.getCurrentPlayer() == Board.PLAYER_O)
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

        public void setXColor(int color){
            XDrawable = XDrawable.color(color);
        }

        public void setYColor(int color){
            ODrawable = ODrawable.color(color);
        }

        @Override
        public void setBackgroundColor(int color) {
            super.setBackgroundColor(color);
            currentColor = color;
        }
    }
}
