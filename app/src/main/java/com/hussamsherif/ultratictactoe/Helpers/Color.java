package com.hussamsherif.ultratictactoe.Helpers;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class Color {
    @IntDef({ACTIVE_CELL_COLOR,ODD_CELL_COLOR , EVEN_CELL_COLOR ,X_COLOR ,O_COLOR })
    @Retention(RetentionPolicy.SOURCE)
    public @interface ColorIdentifier{}
    public static final int ACTIVE_CELL_COLOR = 0;
    public static final int ODD_CELL_COLOR = 1;
    public static final int EVEN_CELL_COLOR =2;
    public static final int X_COLOR = 3;
    public static final int O_COLOR =4;
}
