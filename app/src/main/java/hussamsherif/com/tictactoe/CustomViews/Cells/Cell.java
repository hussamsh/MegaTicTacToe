package hussamsherif.com.tictactoe.CustomViews.Cells;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.IntDef;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import hussamsherif.com.tictactoe.CustomViews.Boards.Board;
import hussamsherif.com.tictactoe.CustomViews.Viewgroups.CellGridLayout;
import hussamsherif.com.tictactoe.Interfaces.CellParent;
import hussamsherif.com.tictactoe.R;

public class Cell extends ImageButton implements View.OnClickListener{

    @IntDef({X,O,EMPTY})
    @Retention(RetentionPolicy.SOURCE)
    public @interface GameValue {}

    public static final int X = 0;
    public static final int O = 1;
    public static final int EMPTY = -1 ;

    private @GameValue int value ;
    private @CellGridLayout.CellPosition.Gravity
    int gravity ;

    private boolean isClicked ;
    private boolean isParentEnabled;
    private CellParent parent;
    private int currentColor ;

    public Cell(Context context , @CellGridLayout.CellPosition.Gravity int gravity , CellParent parent) {
        super(context);
        this.setValue(EMPTY);
        this.gravity = gravity;
        this.parent = parent;
        setOnClickListener(this);
        currentColor = ContextCompat.getColor(context, R.color.default_first_unactive_cell_color);
        setBackgroundColor(currentColor);
    }

    public void setIsParentEnabled(boolean isParentEnabled) {
        this.isParentEnabled = isParentEnabled;
            final int toColor = isParentEnabled ? R.color.default_active_cell_color : R.color.default_first_unactive_cell_color;
            ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), currentColor, ContextCompat.getColor(getContext() ,
                    toColor));
            colorAnimation.setDuration(300);
            colorAnimation.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    currentColor = toColor;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
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
            setValue(Cell.X);
        }else if (Board.getCurrentPlayer() == Board.PLAYER_O)
            setValue(Cell.O);
    }

    private void setValue(@GameValue int value){
        if (value == EMPTY){
            this.value = EMPTY;
            setImageDrawable(null);
            isClicked = false;
            return;
        }

        if (parent.isGameEnded()){
            Snackbar.make(this , getContext().getString(R.string.game_ended) , Snackbar.LENGTH_LONG)
                    .setAction(getContext().getString(R.string.play_again), new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            parent.playAgain();
                        }
                    })
                    .setActionTextColor(Color.RED).show();
            return;
        }

        if (isParentEnabled){
            if (!isClicked){
                int width = this.getMeasuredWidth();
                int height = this.getMeasuredHeight();
                if (value == X){
                    this.value = value;
                    setImageDrawable(new IconicsDrawable(getContext())
                            .icon(GoogleMaterial.Icon.gmd_clear).sizePxX(height-50).sizePxY(width - 40));
                }
                else if (value == O){
                    this.value = value;
                    setImageDrawable(new IconicsDrawable(getContext())
                            .icon(GoogleMaterial.Icon.gmd_panorama_fish_eye).sizePxX(height-35).sizePxY(width-25));
                }
                isClicked = true;
                parent.cellClicked(gravity, value);
            }else{
                Toast.makeText(getContext() , getContext().getString(R.string.cell_already_played) , Toast.LENGTH_SHORT).show();
            }
        }else
            Toast.makeText(getContext() , getContext().getString(R.string.cell_disabled) , Toast.LENGTH_SHORT).show();
    }

    @GameValue
    public int getValue() {
        return value;
    }

    public void clear(){
        setValue(EMPTY);
    }
}