package hussamsherif.com.tictactoe.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.afollestad.materialdialogs.prefs.MaterialEditTextPreference;

import org.greenrobot.eventbus.Subscribe;

import hussamsherif.com.tictactoe.BusEvents.ColorChangedEvent;
import hussamsherif.com.tictactoe.BusEvents.PlayerNameChangedEvent;
import hussamsherif.com.tictactoe.BusEvents.TimerToggleEvent;
import hussamsherif.com.tictactoe.Color;
import hussamsherif.com.tictactoe.CustomViews.Boards.Board;
import hussamsherif.com.tictactoe.CustomViews.TimerPreference;
import hussamsherif.com.tictactoe.Helpers.Bus;
import hussamsherif.com.tictactoe.Helpers.Utils;
import hussamsherif.com.tictactoe.R;

public class SettingActivity extends AppCompatActivity implements ColorChooserDialog.ColorCallback{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getFragmentManager().beginTransaction().replace(R.id.setting_layout , new SettingsFragment()).commit();
    }

    @Override
    public void onColorSelection(ColorChooserDialog dialog, int selectedColor) {
        switch (dialog.getTitle()){
            case R.string.active_cell_color:
                Bus.getBus().post(new ColorChangedEvent(Color.ACTIVE_CELL_COLOR, selectedColor));
                break;
            case R.string.odd_cell_color_preference:
                Bus.getBus().post(new ColorChangedEvent(Color.ODD_CELL_COLOR, selectedColor));
                break;
            case R.string.even_cell_color_preference:
                Bus.getBus().post(new ColorChangedEvent(Color.EVEN_CELL_COLOR, selectedColor));
                break;
            case R.string.x_color:
                Bus.getBus().post(new ColorChangedEvent(Color.X_COLOR, selectedColor));
                break;
            case R.string.o_color:
                Bus.getBus().post(new ColorChangedEvent(Color.O_COLOR, selectedColor));
                break;
        }
    }

    public static class SettingsFragment extends PreferenceFragment{

        private MaterialEditTextPreference xPlayerName ;
        private MaterialEditTextPreference oPlayerName ;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            invalidateSettings();
        }

        private void invalidateSettings(){
            xPlayerName = (MaterialEditTextPreference)findPreference("first_player_name");
            xPlayerName.setSummary(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("first_player_name", "Player X"));
            xPlayerName.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Bus.getBus().post(new PlayerNameChangedEvent(Board.PLAYER_X, (String) newValue));
                    return true;
                }
            });

            oPlayerName = (MaterialEditTextPreference)findPreference("second_player_name");
            oPlayerName.setSummary(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("second_player_name", "Player O"));
            oPlayerName.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Bus.getBus().post(new PlayerNameChangedEvent(Board.PLAYER_O, (String) newValue));
                    return true;
                }
            });

            final TimerPreference timerPreference = (TimerPreference) findPreference("timer_preference");
            timerPreference.setEnabled(PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("enable_timer_preference" , false));
            timerPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Utils.Log("here");
                    Bus.getBus().post(new TimerToggleEvent(TimerToggleEvent.COUNT_TIME_CHANGED));
                    return true;
                }
            });

            SwitchPreference switchPreference = (SwitchPreference) findPreference("enable_timer_preference");
            switchPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, final Object newValue) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Bus.getBus().post(new TimerToggleEvent(((boolean) newValue) ? TimerToggleEvent.ON : TimerToggleEvent.OFF));
                        }
                    },100);
                    timerPreference.setEnabled((boolean) newValue);
                    return true;
                }
            });
        }

        @SuppressWarnings("deprecation")
        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            Bus.getBus().register(this);
        }

        @Override
        public void onDetach() {
            Bus.getBus().unregister(this);
            super.onDetach();
        }

        @Subscribe
        @SuppressWarnings("unused")
        public void onNameChanged(PlayerNameChangedEvent event){
            if (event.getPlayer() == Board.PLAYER_X)
                xPlayerName.setSummary(event.getNewPlayerName());
            else if (event.getPlayer() == Board.PLAYER_O)
                oPlayerName.setSummary(event.getNewPlayerName());
        }
    }
}
