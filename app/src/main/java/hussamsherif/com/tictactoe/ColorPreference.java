package hussamsherif.com.tictactoe;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;

import hussamsherif.com.tictactoe.CustomViews.BorderCircleView;

public class ColorPreference extends Preference{

    private int mKey ;

    public ColorPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPersistent(false);
        init(context , attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setWidgetLayoutResource(R.layout.widget_layout);
        setPersistent(false);
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ColorPreference, 0, 0);
            try {
                mKey = a.getInt(R.styleable.ColorPreference_reference, -1);
            } finally {
                a.recycle();
            }
        }
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        String preference = parseKey();
        BorderCircleView borderCircleView = (BorderCircleView) view.findViewById(R.id.circle);
        borderCircleView.setBackgroundColor(Utils.getColor(getContext(), preference));
    }

    @Utils.ColorPreference
    private String parseKey() {
        switch (mKey){
            case 0:
                return Utils.ACTIVE_CELL_COLOR;
            case 1:
                return Utils.FIRST_UNACTIVE_CELL_COLOR;
            case 2:
                return Utils.SECOND_UNACTIVE_CELL_COLOR;
            case 3:
                return Utils.X_COLOR;
            case 4:
                return Utils.Y_COLOR;
            default:
                return Utils.ACTIVE_CELL_COLOR;
        }
    }
}
