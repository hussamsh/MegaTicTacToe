package hussamsherif.com.tictactoe;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.StringDef;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Utils {

    @StringDef({ACTIVE_CELL_COLOR , FIRST_UNACTIVE_CELL_COLOR , SECOND_UNACTIVE_CELL_COLOR , X_COLOR ,Y_COLOR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ColorPreference{}
    private static final String COLORS_PREFERENCE_KEY = "colors_preference";
    public static final String ACTIVE_CELL_COLOR = "active_cell_color";
    public static final String FIRST_UNACTIVE_CELL_COLOR = "first_unactive_cell_color";
    public static final String SECOND_UNACTIVE_CELL_COLOR = "second_unactive_cell_color";
    public static final String X_COLOR = "x_color";
    public static final String Y_COLOR = "y_color";

    public static void Log(String message){
        Log.i("test" , message);
    }

    public static int getColor(Context context , @ColorPreference String preference) {
        int color ;
        SharedPreferences sharedPreferences = context.getSharedPreferences(preference, Context.MODE_PRIVATE);
        color = sharedPreferences.getInt(preference, -1);
        if (color == -1){
            //Preference doesn't exist , Get default colors
            switch (preference){
                case ACTIVE_CELL_COLOR:
                    return ContextCompat.getColor(context , R.color.default_active_cell_color);
                case FIRST_UNACTIVE_CELL_COLOR:
                    return ContextCompat.getColor(context , R.color.default_first_unactive_cell_color);
                case SECOND_UNACTIVE_CELL_COLOR:
                    return ContextCompat.getColor(context , R.color.default_second_unactive_cell_color);
                case X_COLOR:
                    return ContextCompat.getColor(context , R.color.default_x_color);
                case Y_COLOR:
                    return ContextCompat.getColor(context , R.color.default_y_color);
                default:
                    throw new IllegalArgumentException("Preference is not recognized : " + preference + ", Must be from util.ColorPreferences");
            }
        }
        return color;
    }

    public void saveColor(Context context , @ColorPreference String preference , int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(COLORS_PREFERENCE_KEY , Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putInt(preference, value);
        e.apply();
    }
}
