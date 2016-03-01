package hussamsherif.com.tictactoe.CustomViews;

import android.content.Context;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import hussamsherif.com.tictactoe.R;

public class TimerView extends FrameLayout{

    private CircularSeekBar circularSeekBar;
    private TextView secondsTextView;
    public TimerView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.timer_view, this);
        circularSeekBar= (CircularSeekBar) findViewById(R.id.circular_seekBar);
        circularSeekBar.setMax(90);
        int progress = PreferenceManager.getDefaultSharedPreferences(context).getInt("timer_preference", 0);
        secondsTextView = (TextView) findViewById(R.id.seconds_textView);
        secondsTextView.setText(String.valueOf(progress));
        circularSeekBar.setProgress(progress);
        circularSeekBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {

            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
                secondsTextView.setText(String.valueOf(progress));
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
        return circularSeekBar.getProgress();
    }
}
