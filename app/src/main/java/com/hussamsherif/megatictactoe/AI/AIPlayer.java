package com.hussamsherif.megatictactoe.AI;

import com.hussamsherif.megatictactoe.CustomViews.Boards.Board;

import java.util.ArrayList;

public abstract class AIPlayer {

    protected int ROWS = 3;  // number of rows
    protected int COLS = 3;
    int[][] cells ;
    protected int mySeed ; //The computer seed
    protected int humanSeed ; //The human seed

    public AIPlayer(int[][] cells){
        this.cells = cells;
    }

    public void setSeed(int seed){
        this.mySeed = seed;
        this.humanSeed = seed == Board.PLAYER_O ? Board.PLAYER_X : Board.PLAYER_O;
    }

    public void setCells(int[][] cells) {
        this.cells = cells;
    }

    public abstract int move(ArrayList<int[][]> abstractBoard , int currentCellPosition , @Board.Player int aiPlayer);
}
