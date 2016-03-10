package com.hussamsherif.ultratictactoe.CustomViews.Viewgroups;

import android.content.Context;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridLayout;

import com.hussamsherif.ultratictactoe.CustomViews.Cells.XOCell;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class CellGridLayout extends GridLayout {

    private final int COLUMN_COUNT = 3;
    private final int ROW_COUNT = 3 ;
    private boolean measured ;

    public CellGridLayout(Context context) {
        super(context);
        initGridLayoutProperties();
    }

    public CellGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGridLayoutProperties();
    }

    private void initGridLayoutProperties() {
        setColumnCount(COLUMN_COUNT);
        setRowCount(ROW_COUNT);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        if (!measured){
            int widthSize = MeasureSpec.getSize(widthSpec);
            int heightSize = MeasureSpec.getSize(heightSpec);

            int cellWidth = widthSize/COLUMN_COUNT;
            int cellHeight = heightSize/ROW_COUNT;

            //references to add children into the view group
            int columnCount = 0;
            int rowCount = 0 ;

            for(int i=0; i<getChildCount(); ++i) {
                if(columnCount == COLUMN_COUNT){
                    columnCount = 0;
                    rowCount++;
                }

                View nextCell = getChildAt(i);
                LayoutParams params = (LayoutParams)nextCell.getLayoutParams();
                params.height = cellHeight;
                params.width = cellWidth;
                if (nextCell instanceof XOCell)
                    params.setMargins(4, 4, 4, 4);
                else
                    params.setMargins(1, 1, 1, 1);
                params.columnSpec = GridLayout.spec(columnCount);
                params.rowSpec = GridLayout.spec(rowCount);
                nextCell.setLayoutParams(params);
                columnCount++;
            }
            measured = true;
        }

        super.onMeasure(widthSpec , heightSpec);
    }

    public static class CellPosition {
        @IntDef({TOP_LEFT , TOP , TOP_RIGHT , CENTER_LEFT , CENTER , CENTER_RIGHT , BOTTOM_LEFT , BOTTOM , BOTTOM_RIGHT})
        @Retention(RetentionPolicy.SOURCE)
        public @interface Gravity{}

        public static final int TOP_LEFT = 0;
        public static final int TOP = 1;
        public static final int TOP_RIGHT = 2;
        public static final int CENTER_LEFT = 3;
        public static final int CENTER = 4;
        public static final int CENTER_RIGHT =5;
        public static final int BOTTOM_LEFT =6;
        public static final int BOTTOM = 7;
        public static final int BOTTOM_RIGHT= 8;

        //TODO: Ugly class with a lot of switches
        @Gravity
        public static int getGravity(int position){
            switch (position){
                case 0:
                    return TOP_LEFT;
                case 1:
                    return TOP;
                case 2:
                    return TOP_RIGHT;
                case 3:
                    return CENTER_LEFT;
                case 4:
                    return CENTER;
                case 5:
                    return CENTER_RIGHT;
                case 6:
                    return BOTTOM_LEFT;
                case 7:
                    return BOTTOM;
                case 8:
                    return BOTTOM_RIGHT;
                default:
                    throw new IllegalArgumentException("int is not a gravity constant");
            }
        }

        @Gravity
        public static int getGravity(int row , int col){
            switch (row){
                case 0:
                    if (col == 0)
                        return TOP_LEFT;
                    else if (col ==1)
                        return TOP;
                    else if (col == 2)
                        return TOP_RIGHT;
                    break;
                case 1:
                    if (col == 0)
                        return CENTER_LEFT;
                    else if (col ==1)
                        return CENTER;
                    else if (col == 2)
                        return CENTER_RIGHT;
                    break;
                case 2:
                    if (col == 0)
                        return BOTTOM_LEFT;
                    else if (col == 1)
                        return BOTTOM;
                    else if (col == 2)
                        return BOTTOM_RIGHT;
                    break;
            }
            throw new IllegalArgumentException("Inputs can't be bigger than 3");
        }

        public static int[] getRowAndCol(int position){
            switch (position){
                case TOP_LEFT:
                    return new int[]{0,0};
                case TOP:
                    return new int[]{0,1};
                case TOP_RIGHT:
                    return new int[]{0,2};
                case CENTER_LEFT:
                    return new int[]{1,0};
                case CENTER:
                    return new int[]{1,1};
                case CENTER_RIGHT:
                    return new int[]{1,2};
                case BOTTOM_LEFT:
                    return new int[]{2,0};
                case BOTTOM:
                    return new int[]{2,1};
                case BOTTOM_RIGHT:
                    return new int[]{2,2};
                default:
                    throw new IllegalArgumentException("int is not a gravity constant");
            }
        }

        @Gravity
        public static int getOppositeCell(@Gravity int gravity){
            switch (gravity){
                case TOP_LEFT:
                    return BOTTOM_RIGHT;
                case TOP_RIGHT:
                    return BOTTOM_LEFT;
                case BOTTOM_LEFT:
                    return TOP_RIGHT;
                case BOTTOM_RIGHT:
                    return TOP_LEFT;
                default:
                    throw new IllegalArgumentException("Gravity doesn't have an opposite");
            }
        }
    }
}
