package hussamsherif.com.tictactoe.CustomViews;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import hussamsherif.com.tictactoe.R;

public class TimerPreference extends Preference{

    private TextView timeTextView ;

    public TimerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWidgetLayoutResource(R.layout.timer_widget_layout);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        timeTextView = (TextView) view.findViewById(R.id.time_textView);
        timeTextView.setText(String.valueOf(getPersistedInt(0)));
    }

    @Override
    protected void onClick() {
        final TimerView timerView = new TimerView(getContext());
        new MaterialDialog.Builder(getContext())
                .title(getContext().getString(R.string.timer_dialog_title))
                .customView(timerView, false)
                .positiveText(getContext().getString(R.string.set))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        persistInt(timerView.getProgress());
                        timeTextView.setText(String.valueOf(getPersistedInt(0)));
                    }
                })
                .build().show();
    }
}
