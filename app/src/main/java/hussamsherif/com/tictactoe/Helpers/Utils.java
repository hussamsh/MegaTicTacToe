package hussamsherif.com.tictactoe.Helpers;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.StringDef;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import hussamsherif.com.tictactoe.R;

public class Utils {

    @StringDef({ACTIVE_CELL_COLOR , ODD_CELL_COLOR_PREFERENCE, EVEN_CELL_COLOR_PREFERENCE, X_COLOR , O_COLOR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ColorPreference{}
    private static final String MEGA_TIC_TAC_TOE_PREFERENCES_KEY = "colors_preference";
    public static final String ACTIVE_CELL_COLOR = "active_cell_color";
    public static final String ODD_CELL_COLOR_PREFERENCE = "first_unactive_cell_color";
    public static final String EVEN_CELL_COLOR_PREFERENCE = "second_unactive_cell_color";
    public static final String X_COLOR = "x_color";
    public static final String O_COLOR = "O_color";

    @StringDef({PLAYER_X_PREFERENCE, PLAYER_O_PREFERENCE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PlayerPreferences{};
    public static final String PLAYER_X_PREFERENCE = "player_x";
    public static final String PLAYER_O_PREFERENCE = "player_o";

    public static void Log(String message){
        Log.i("test" , message);
    }

    public static int getColor(Context context , @ColorPreference String preference) {
        int color ;
        SharedPreferences sharedPreferences = context.getSharedPreferences(MEGA_TIC_TAC_TOE_PREFERENCES_KEY, Context.MODE_PRIVATE);
        color = sharedPreferences.getInt(preference, -1);
        Utils.Log("get color : " + color);
        if (color == -1){
            //Preference doesn't exist , Get default colors
            switch (preference){
                case ACTIVE_CELL_COLOR:
                    return ContextCompat.getColor(context , R.color.default_active_cell_color);
                case ODD_CELL_COLOR_PREFERENCE:
                    return ContextCompat.getColor(context , R.color.default_first_unactive_cell_color);
                case EVEN_CELL_COLOR_PREFERENCE:
                    return ContextCompat.getColor(context , R.color.default_second_unactive_cell_color);
                case X_COLOR:
                    return ContextCompat.getColor(context , R.color.default_x_color);
                case O_COLOR:
                    return ContextCompat.getColor(context , R.color.default_o_color);
                default:
                    throw new IllegalArgumentException("Preference is not recognized : " + preference +
                            ", Must be from util.ColorPreferences");
            }
        }
        return color;
    }

    public static String getPlayerName(Context context , @PlayerPreferences String preference){
        SharedPreferences sharedPreferences = context.getSharedPreferences(MEGA_TIC_TAC_TOE_PREFERENCES_KEY , Context.MODE_PRIVATE);
        return sharedPreferences.getString(preference , preference.equals(PLAYER_X_PREFERENCE)? "Player X" : "Player O");
    }

    public static void saveInPreferences(Context context, @ColorPreference String preference, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MEGA_TIC_TAC_TOE_PREFERENCES_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putInt(preference, value);
        e.apply();
    }

    public static void saveInPreferences(Context context, @PlayerPreferences String preference, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MEGA_TIC_TAC_TOE_PREFERENCES_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putString(preference, value);
        e.apply();
    }

}
