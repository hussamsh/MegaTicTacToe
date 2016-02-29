package hussamsherif.com.tictactoe.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.afollestad.materialdialogs.prefs.MaterialEditTextPreference;

import org.greenrobot.eventbus.Subscribe;

import hussamsherif.com.tictactoe.BusEvents.PlayerNameChangedEvent;
import hussamsherif.com.tictactoe.CustomViews.Boards.Board;
import hussamsherif.com.tictactoe.Helpers.Bus;
import hussamsherif.com.tictactoe.BusEvents.ColorChangedEvent;
import hussamsherif.com.tictactoe.CustomViews.ColorPreference;
import hussamsherif.com.tictactoe.R;
import hussamsherif.com.tictactoe.Helpers.Utils;

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
                Bus.getBus().post(new ColorChangedEvent(Utils.ACTIVE_CELL_COLOR , selectedColor));
                Utils.saveInPreferences(this, Utils.ACTIVE_CELL_COLOR, selectedColor);
                break;
            case R.string.odd_cell_color_preference:
                Bus.getBus().post(new ColorChangedEvent(Utils.ODD_CELL_COLOR_PREFERENCE, selectedColor));
                Utils.saveInPreferences(this, Utils.ODD_CELL_COLOR_PREFERENCE, selectedColor);
                break;
            case R.string.even_cell_color_preference:
                Bus.getBus().post(new ColorChangedEvent(Utils.EVEN_CELL_COLOR_PREFERENCE , selectedColor));
                Utils.saveInPreferences(this, Utils.EVEN_CELL_COLOR_PREFERENCE, selectedColor);
                break;
            case R.string.x_color:
                Bus.getBus().post(new ColorChangedEvent(Utils.X_COLOR , selectedColor));
                Utils.saveInPreferences(this, Utils.X_COLOR, selectedColor);
                break;
            case R.string.o_color:
                Bus.getBus().post(new ColorChangedEvent(Utils.O_COLOR, selectedColor));
                Utils.saveInPreferences(this, Utils.O_COLOR, selectedColor);
                break;
        }
    }

    public static class SettingsFragment extends PreferenceFragment{

        MaterialEditTextPreference xPlayerName = (MaterialEditTextPreference)findPreference("first_player_name");
        MaterialEditTextPreference oPlayerName = (MaterialEditTextPreference)findPreference("second_player_name");


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
            xPlayerName.setSummary(Utils.getPlayerName(getActivity() , Utils.PLAYER_X_PREFERENCE));

            xPlayerName.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Utils.saveInPreferences(getActivity(), Utils.PLAYER_X_PREFERENCE, (String) newValue);
                    Bus.getBus().post(new PlayerNameChangedEvent(Board.PLAYER_X, (String) newValue));
                    return true;
                }
            });

            oPlayerName = (MaterialEditTextPreference)findPreference("second_player_name");
            oPlayerName.setSummary(Utils.getPlayerName(getActivity(), Utils.PLAYER_O_PREFERENCE));
            oPlayerName.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Utils.saveInPreferences(getActivity(), Utils.PLAYER_O_PREFERENCE, (String) newValue);
                    Bus.getBus().post(new PlayerNameChangedEvent(Board.PLAYER_O, (String) newValue));
                    return true;
                }
            });
            ColorPreference activeColorPreference = (ColorPreference) findPreference("active_cell_color");
            activeColorPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    new ColorChooserDialog.Builder((SettingActivity)getActivity() , R.string.active_cell_color)
                            .preselect(Utils.getColor(getActivity(), Utils.ACTIVE_CELL_COLOR))
                            .show();
                    return true;
                }
            });

            ColorPreference oddCellPreference= (ColorPreference) findPreference("odd_cell_color_preference");
            oddCellPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    new ColorChooserDialog.Builder((SettingActivity) getActivity(), R.string.odd_cell_color_preference)
                            .preselect(Utils.getColor(getActivity(), Utils.ODD_CELL_COLOR_PREFERENCE))
                            .show();
                    return true;
                }
            });

            ColorPreference evenCellPreference = (ColorPreference) findPreference("even_cell_color_preference");
            evenCellPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    new ColorChooserDialog.Builder((SettingActivity) getActivity(), R.string.even_cell_color_preference)
                            .preselect(Utils.getColor(getActivity(), Utils.EVEN_CELL_COLOR_PREFERENCE))
                            .show();
                    return true;
                }
            });

            ColorPreference xColorPreference =  (ColorPreference) findPreference("x_color");
            xColorPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    new ColorChooserDialog.Builder((SettingActivity) getActivity(), R.string.x_color)
                            .preselect(Utils.getColor(getActivity(), Utils.X_COLOR))
                            .show();
                    return true;
                }
            });

            ColorPreference yColorPreference  = (ColorPreference) findPreference("o_color");
            yColorPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    new ColorChooserDialog.Builder((SettingActivity) getActivity(), R.string.o_color)
                            .preselect(Utils.getColor(getActivity(), Utils.O_COLOR))
                            .show();
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
