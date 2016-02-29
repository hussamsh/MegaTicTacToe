package hussamsherif.com.tictactoe.CustomViews.Viewgroups;

import android.content.Context;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class CellGridLayout extends GridLayout {

    private final int COLUMN_COUNT = 3;
    private final int ROW_COUNT = 3 ;

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
        super.onMeasure(widthSpec, heightSpec);

        int widthSize = MeasureSpec.getSize(widthSpec);
        int heightSize = MeasureSpec.getSize(heightSpec);

        int cellWidth = widthSize/3;
        int cellHeight = heightSize/3;

        //references to add children into the view group
        int columnCount = 0;
        int rowCount = 0 ;

        for(int i=0; i<getChildCount(); ++i) {
            if(columnCount == COLUMN_COUNT){
                columnCount = 0;
                rowCount++;
            }
            View nextCell = getChildAt(i);
            GridLayout.LayoutParams params = (GridLayout.LayoutParams)nextCell.getLayoutParams();
            params.height = cellHeight;
            params.width = cellWidth;
            params.setMargins(1, 1, 1, 1);
            params.columnSpec = GridLayout.spec(columnCount);
            params.rowSpec = GridLayout.spec(rowCount);
            nextCell.setLayoutParams(params);
            columnCount++;
        }
    }

    public static class CellPosition {

        @IntDef({TOP_LEFT , TOP , TOP_RIGHT , CENTER_LEFT , CENTER , CENTER_RIGHT , BOTTOM_LEFT , BOTTOM , BOTTOM_RIGHT})
        @Retention(RetentionPolicy.SOURCE)
        public @interface Gravity{}
        public static final int TOP_LEFT = 0 ;
        public static final int TOP = 1 ;
        public static final int TOP_RIGHT = 2;
        public static final int CENTER_LEFT = 3;
        public static final int CENTER = 4;
        public static final int CENTER_RIGHT =5;
        public static final int BOTTOM_LEFT =6;
        public static final int BOTTOM = 7;
        public static final int BOTTOM_RIGHT= 8;

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
