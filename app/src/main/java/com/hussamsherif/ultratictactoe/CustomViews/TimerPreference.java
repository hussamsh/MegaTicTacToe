package com.hussamsherif.ultratictactoe.CustomViews;

import android.content.Context;
import android.preference.Preference;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.hussamsherif.ultratictactoe.BusEvents.TimerToggleEvent;
import com.hussamsherif.ultratictactoe.Helpers.Bus;
import com.hussamsherif.ultratictactoe.R;

public class TimerPreference extends Preference{

    private TextView timeTextView ;

    public TimerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWidgetLayoutResource(R.layout.timer_widget_layout);
        setDefaultValue(1);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        timeTextView = (TextView) view.findViewById(R.id.time_textView);
        timeTextView.setText(String.valueOf(getPersistedInt(1)));
    }

    @Override
    protected void onClick() {
        final TimeSeekBar timerView = new TimeSeekBar(getContext());
        new MaterialDialog.Builder(getContext())
                .title(getContext().getString(R.string.timer_dialog_title))
                .customView(timerView, false)
                .positiveText(getContext().getString(R.string.set))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        persistInt(timerView.getProgress());
                        timeTextView.setText(String.valueOf(getPersistedInt(1)));
                        Bus.getBus().post(new TimerToggleEvent(TimerToggleEvent.COUNT_TIME_CHANGED));
                    }
                })
                .build().show();
    }
}
