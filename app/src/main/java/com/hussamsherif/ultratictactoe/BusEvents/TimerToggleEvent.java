package com.hussamsherif.ultratictactoe.BusEvents;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class TimerToggleEvent {

    @IntDef({ON , OFF , COUNT_TIME_CHANGED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TimerState{}
    public static final int ON = 0 ;
    public static final int OFF  = 1;
    public static final int COUNT_TIME_CHANGED = 2;

    private int state ;
    public TimerToggleEvent(@TimerState int state) {
        this.state = state;
    }

    @TimerState
    public int getState() {
        return state;
    }
}
