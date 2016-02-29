package hussamsherif.com.tictactoe.BusEvents;

import android.support.annotation.ColorInt;

import hussamsherif.com.tictactoe.Helpers.Utils;

public class ColorChangedEvent {

    private String colorPreference ;
    private int newColor;

    public ColorChangedEvent(@Utils.ColorPreference String colorPreference, @ColorInt int newColor){
        this.colorPreference = colorPreference ;
        this.newColor = newColor;
    }

    public int getNewColor() {
        return newColor;
    }

    @Utils.ColorPreference
    public String getColorPreference() {
        return colorPreference;
    }
}
