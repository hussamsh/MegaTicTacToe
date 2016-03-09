package com.hussamsherif.megatictactoe.BusEvents;

import android.support.annotation.ColorInt;

import com.hussamsherif.megatictactoe.Helpers.Color;

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
