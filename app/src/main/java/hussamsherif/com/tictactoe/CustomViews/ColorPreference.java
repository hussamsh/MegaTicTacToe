package hussamsherif.com.tictactoe.CustomViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.afollestad.materialdialogs.color.ColorChooserDialog;

import org.greenrobot.eventbus.Subscribe;

import hussamsherif.com.tictactoe.Activities.SettingActivity;
import hussamsherif.com.tictactoe.BusEvents.ColorChangedEvent;
import hussamsherif.com.tictactoe.Helpers.Bus;
import hussamsherif.com.tictactoe.Helpers.Utils;
import hussamsherif.com.tictactoe.R;

public class ColorPreference extends Preference {

    private int mKey ;
    private int dialogTitleStringId ;
    private int defaultColor ;
    private BorderCircleView borderCircleView ;

    public ColorPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPersistent(true);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setWidgetLayoutResource(R.layout.widget_layout);
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ColorPreference, 0, 0);
            try {
                mKey = a.getInt(R.styleable.ColorPreference_reference, -1);
                Utils.Log("mKey : " + mKey);
            } finally {
                a.recycle();
            }
        }
        parseKey();
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
        Utils.Log(getPersistedInt(defaultColor)+" loaded color");
        borderCircleView.setBackgroundColor(getPersistedInt(defaultColor));
    }

    private void parseKey() {
        switch (mKey){
            case 0:
                Utils.Log("here 1 ");
                dialogTitleStringId = R.string.active_cell_color;
                defaultColor = ContextCompat.getColor(getContext() , R.color.default_active_cell_color);
                break;
            case 1:
                dialogTitleStringId = R.string.odd_cell_color_preference;
                defaultColor = ContextCompat.getColor(getContext() , R.color.default_odd_cell_color);
                break;
            case 2:
                dialogTitleStringId = R.string.even_cell_color_preference;
                defaultColor = ContextCompat.getColor(getContext() , R.color.default_even_cell_color);
                break;
            case 3:
                dialogTitleStringId = R.string.x_color;
                defaultColor = ContextCompat.getColor(getContext() , R.color.default_x_color);
                break;
            case 4:
                dialogTitleStringId = R.string.o_color;
                defaultColor = ContextCompat.getColor(getContext() , R.color.default_o_color);
                break;
            default:
                dialogTitleStringId = R.string.active_cell_color;
                defaultColor = ContextCompat.getColor(getContext() , R.color.colorPrimary);
        }
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onColorChanged(ColorChangedEvent event){
        if (event.getColorPreference() == mKey){
            borderCircleView.setBackgroundColor(event.getNewColor());
            persistInt(event.getNewColor());
        }
    }

    @Override
    protected void onClick() {
        new ColorChooserDialog.Builder((SettingActivity) getContext(), dialogTitleStringId)
                .preselect(getPersistedInt(defaultColor))
                .show();
    }
}
