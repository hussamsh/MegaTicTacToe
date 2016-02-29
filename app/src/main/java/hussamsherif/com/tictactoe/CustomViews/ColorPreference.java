package hussamsherif.com.tictactoe.CustomViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;

import org.greenrobot.eventbus.Subscribe;

import hussamsherif.com.tictactoe.BusEvents.ColorChangedEvent;
import hussamsherif.com.tictactoe.Helpers.Bus;
import hussamsherif.com.tictactoe.Helpers.Utils;
import hussamsherif.com.tictactoe.R;

public class ColorPreference extends Preference{

    private int mKey ;
    private BorderCircleView borderCircleView ;

    public ColorPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPersistent(false);
        init(context, attrs);
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
    protected void onAttachedToActivity() {
        Bus.getBus().register(this);
    }


    @Override
    protected void onPrepareForRemoval() {
        Bus.getBus().unregister(this);
        super.onPrepareForRemoval();
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        borderCircleView = (BorderCircleView) view.findViewById(R.id.circle);
        borderCircleView.setBackgroundColor(Utils.getColor(getContext(), parseKey()));
    }

    @Utils.ColorPreference
    private String parseKey() {
        switch (mKey){
            case 0:
                return Utils.ACTIVE_CELL_COLOR;
            case 1:
                return Utils.ODD_CELL_COLOR_PREFERENCE;
            case 2:
                return Utils.EVEN_CELL_COLOR_PREFERENCE;
            case 3:
                return Utils.X_COLOR;
            case 4:
                return Utils.O_COLOR;
            default:
                return Utils.ACTIVE_CELL_COLOR;
        }
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onColorChanged(ColorChangedEvent event){
        if (event.getColorPreference().equals(parseKey())){
            borderCircleView.setBackgroundColor(event.getNewColor());
        }
    }
}
