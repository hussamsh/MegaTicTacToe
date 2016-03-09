package com.hussamsherif.megatictactoe.Helpers;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.hussamsherif.megatictactoe.CustomViews.Boards.Board;
import com.hussamsherif.megatictactoe.R;

public class Utils {

    private static final String PREFERENCE_KEY = "names_preference";
    private static final String X_PLAYER_NAME_PREFERENCE = "x_player_name";
    private static final String O_PLAYER_NAME_PREFERENCE = "y_player_name";
    private static final String AI_PLAYER_PREFERENCE = "ai_player";

    public static void Log(String message){
        Log.i("test", message);
    }

    public static String convertToReadableTime(long milliSeconds){
        double time = (double) milliSeconds /1000/60 ;
        int minutes = (int)time ;
        double tmp = (time - minutes) * 60 ;
        int seconds = (int) tmp;

        if (seconds < 10)
            return minutes + ":0" + seconds ;
        return minutes + ":" + seconds  ;
    }

    public static int[][] getCopy(int[][] array){
        int[][] copyArray = new int[3][3];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(array[i], 0, copyArray[i], 0, 3);
        }
        return copyArray;
    }

    //TODO : review this code, and the structure behind it's implementation
    //I'm just too tired to write good code
    public static void setAIPlayer(Context context , @Board.Player int player , String oldPlayerName){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_KEY , Context.MODE_PRIVATE);
        SharedPreferences.Editor e = sharedPreferences.edit();
        e.putString(player == Board.PLAYER_X ? X_PLAYER_NAME_PREFERENCE : O_PLAYER_NAME_PREFERENCE , oldPlayerName);
        e.putInt(AI_PLAYER_PREFERENCE , player);
        e.apply();
    }

    @Board.Player
    public static int getAIPlayer(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_KEY , Context.MODE_PRIVATE);
        return sharedPreferences.getInt(AI_PLAYER_PREFERENCE , Board.NONE);
    }

    public static String getXPlayerOriginalName(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_KEY , Context.MODE_PRIVATE);
        return sharedPreferences.getString(X_PLAYER_NAME_PREFERENCE, context.getString(R.string.x_player));
    }

    public static String getOPlayerOriginalName(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_KEY , Context.MODE_PRIVATE);
        return sharedPreferences.getString(O_PLAYER_NAME_PREFERENCE , context.getString(R.string.o_player));
    }
}
