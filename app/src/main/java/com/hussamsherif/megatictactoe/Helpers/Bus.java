package com.hussamsherif.megatictactoe.Helpers;

import org.greenrobot.eventbus.EventBus;

public class Bus {
    private static EventBus bus = new EventBus();

    public static EventBus getBus(){
        return bus;
    }
}