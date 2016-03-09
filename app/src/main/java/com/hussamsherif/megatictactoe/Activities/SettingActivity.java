package com.hussamsherif.megatictactoe.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.afollestad.materialdialogs.prefs.MaterialEditTextPreference;
import com.hussamsherif.megatictactoe.BusEvents.ColorChangedEvent;
import com.hussamsherif.megatictactoe.BusEvents.PlayerNameChangedEvent;
import com.hussamsherif.megatictactoe.CustomViews.Boards.Board;
import com.hussamsherif.megatictactoe.Helpers.Bus;
import com.hussamsherif.megatictactoe.Helpers.Color;
import com.hussamsherif.megatictactoe.R;

import org.greenrobot.eventbus.Subscribe;

public class SettingActivity extends AppCompatActivity implements ColorChooserDialog.ColorCallback{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getFragmentManager().beginTransaction().replace(R.id.setting_layout , new SettingsFragment()).commit();
        setTitle(R.string.settings_activity);
    }

    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, int selectedColor) {
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
