package hussamsherif.com.tictactoe.BusEvents;

import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import hussamsherif.com.tictactoe.Color;
import hussamsherif.com.tictactoe.Helpers.Utils;

public class ColorChangedEvent {

    private int colorPreference ;
    private int newColor;

    public ColorChangedEvent(@Color.ColorIdentifier int colorPreference, @ColorInt int newColor){
        this.colorPreference = colorPreference ;
        this.newColor = newColor;
    }

    public int getNewColor() {
        return newColor;
    }

    @Color.ColorIdentifier
    public int getColorPreference() {
        return colorPreference;
    }
}
