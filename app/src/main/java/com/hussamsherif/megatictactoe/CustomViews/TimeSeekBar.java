package com.hussamsherif.megatictactoe.CustomViews;

import android.content.Context;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hussamsherif.megatictactoe.R;

public class TimeSeekBar extends FrameLayout{

    private CircularSeekBar circularSeekBar;
    private TextView secondsTextView;

    public TimeSeekBar(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.timer_view, this);
        circularSeekBar= (CircularSeekBar) findViewById(R.id.circular_seekBar);
        circularSeekBar.setMax(90);
        int progress = PreferenceManager.getDefaultSharedPreferences(context).getInt("timer_preference", 1);
        secondsTextView = (TextView) findViewById(R.id.seconds_textView);
        secondsTextView.setText(String.valueOf(progress));
        circularSeekBar.setProgress(progress);
        circularSeekBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {

            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
                if (progress != 0)
                    secondsTextView.setText(String.valueOf(progress));
                else
                    secondsTextView.setText(String.valueOf(1));
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });
    }

    public int getProgress() {
        if (circularSeekBar.getProgress() != 0)
            return circularSeekBar.getProgress();
        else
            return 1;
    }
}
