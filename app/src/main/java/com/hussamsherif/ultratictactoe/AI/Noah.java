package com.hussamsherif.ultratictactoe.AI;

import com.hussamsherif.ultratictactoe.CustomViews.Boards.Board;
import com.hussamsherif.ultratictactoe.CustomViews.Cells.XOCell;
import com.hussamsherif.ultratictactoe.CustomViews.Viewgroups.CellGridLayout;

import java.util.ArrayList;

public class Noah extends AIPlayer {

    public Noah(int[][] cells) {
        super(cells);
    }

    @CellGridLayout.CellPosition.Gravity
    @Override
    public int move(ArrayList<int[][]> abstractBoard , int currentCellPosition , @Board.Player int aiPlayer) {
        int bestScore = Integer.MIN_VALUE;
        int bestCell = -1;
        setSeed(aiPlayer);
        setCells(abstractBoard.get(currentCellPosition));
        ArrayList<Integer> nextMoves = generateMoves();

        //Check for a winning move in current cell
        for (int i = 0; i < nextMoves.size(); i++) {
            int[] next = CellGridLayout.CellPosition.getRowAndCol(nextMoves.get(i));
            cells[next[0]][next[1]] = aiPlayer == Board.PLAYER_X ? XOCell.X : XOCell.O;

            if (hasWon(mySeed))
                return nextMoves.get(i);

            cells[next[0]][next[1]] = XOCell.EMPTY;
        }

        //TODO : this implementation will only block the human player, no foresight for the ai player , reactive plays only not proactive
        //Minimax on all cells and choose the one which is most likely make the ai win
        for (int i = 0; i < 9; i++) {
            if (nextMoves.contains(i)) {
                setCells(abstractBoard.get(i));
                int[] result = minimax(mySeed, Integer.MIN_VALUE, Integer.MAX_VALUE);
                if (result[0] > bestScore) {
                    bestCell = i;
                    bestScore = result[0];
                }
            }
        }

        return CellGridLayout.CellPosition.getGravity(bestCell);
    }


    //Minimax with alpha beta pruning
    private int[] minimax(@Board.Player int player , int alpha , int beta){
        ArrayList<Integer> nextMoves = generateMoves();
        int score ;
        int bestMove = -1;

        if (nextMoves.size() == 0){
            score = evaluate();
            return new int[]{score , bestMove};
        } else {
            for (int i = 0; i < nextMoves.size(); i++) {
                //Try this move for the player
                int[] nextMove = CellGridLayout.CellPosition.getRowAndCol(nextMoves.get(i));
                cells[nextMove[0]][nextMove[1]] = player == Board.PLAYER_X ? XOCell.X : XOCell.O;
                if (player == mySeed){
                    score = minimax(humanSeed , alpha , beta)[0];//Score
                    if (score > alpha){
                        alpha = score;
                        bestMove = nextMoves.get(i);
                    }
                }else{
                    score = minimax(mySeed , alpha , beta)[0];//score
                    if (score < beta){
                        beta = score;
                        bestMove = nextMoves.get(i);
                    }
                }
                //Undo move
                cells[nextMove[0]][nextMove[1]] = XOCell.EMPTY;
                if (alpha>=beta)
                    break;
            }
        }
        return new int[]{(player == mySeed) ? alpha : beta, bestMove};
    }

    private int evaluate() {
        int score = 0;
        // Evaluate score for each of the 8 lines (3 rows, 3 columns, 2 diagonals)
        score += evaluateLine(0, 0, 0, 1, 0, 2);  // row 0
        score += evaluateLine(1, 0, 1, 1, 1, 2);  // row 1
        score += evaluateLine(2, 0, 2, 1, 2, 2);  // row 2
        score += evaluateLine(0, 0, 1, 0, 2, 0);  // col 0
        score += evaluateLine(0, 1, 1, 1, 2, 1);  // col 1
        score += evaluateLine(0, 2, 1, 2, 2, 2);  // col 2
        score += evaluateLine(0, 0, 1, 1, 2, 2);  // diagonal
        score += evaluateLine(0, 2, 1, 1, 2, 0);  // alternate diagonal
        return score;
    }

    /** The heuristic evaluation function for the given line of 3 cells
     @Return +100, +10, +1 for 3-, 2-, 1-in-a-line for computer.
     -100, -10, -1 for 3-, 2-, 1-in-a-line for opponent.
     0 otherwise */

    private int evaluateLine(int row1, int col1, int row2, int col2, int row3, int col3) {
        int score = 0;
        int myValue =  mySeed == Board.PLAYER_X ? XOCell.X :  XOCell.O;
        int opponentValue = myValue == XOCell.X ? XOCell.O : XOCell.X;
        // First cell
        if (cells[row1][col1] == myValue) {
            score = 1;
        } else if (cells[row1][col1] == opponentValue) {
            score = -1;
        }

        // Second cell
        if (cells[row2][col2] == myValue) {
            if (score == 1) {
                score = 10;
            } else if (score == -1) {
                return 0;
            } else {
                score = 1;
            }
        } else if (cells[row2][col2] == opponentValue) {
            if (score == -1) {
                score = -10;
            } else if (score == 1) {
                return 0;
            } else {
                score = -1;
            }
        }

        // Third cell
        if (cells[row3][col3] == myValue) {
            if (score > 0) {
                score *= 10;
            } else if (score < 0) {
                return 0;
            } else {
                score = 1;
            }
        } else if (cells[row3][col3] == opponentValue) {
            if (score < 0) {
                score *= 10;
            } else if (score > 1) {
                return 0;
            } else {
                score = -1;
            }
        }
        return score;
    }

    private ArrayList<Integer> generateMoves(){
        ArrayList<Integer> nextMoves = new ArrayList<>();

        if (hasWon(mySeed) || hasWon(humanSeed)){
            return nextMoves;
        }

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (cells[i][j] == XOCell.EMPTY)
                    nextMoves.add(CellGridLayout.CellPosition.getGravity(i , j));
            }
        }
        return nextMoves;
    }

    private int[] winningPatterns = {
            0b111000000, 0b000111000, 0b000000111, // rows
            0b100100100, 0b010010010, 0b001001001, // cols
            0b100010001, 0b001010100               // diagonals
    };

    /** Returns true if thePlayer wins */
    private boolean hasWon(int player) {
        int value = player == Board.PLAYER_X ? XOCell.X : XOCell.O;
        int pattern = 0b000000000;  // 9-bit pattern for the 9 cells
        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                if (cells[row][col] == value) {
                    pattern |= (1 << (row * COLS + col));
                }
            }
        }
        for (int winningPattern : winningPatterns) {
            if ((pattern & winningPattern) == winningPattern) return true;
        }
        return false;
    }

//    Pure Minimax algorithm

//    private int[] minimax(@Board.Player int player){
//        ArrayList<Integer> nextMoves = generateMoves();
//        int bestScore = (player == mySeed) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
//        int currentScore;
//        int bestMove = -1;
//
//        if (nextMoves.size() == 0)
//            bestScore = evaluate();
//        else {
//            for (int i = 0; i < nextMoves.size(); i++) {
//                //Try this move for the player
//                int[] nextMove = CellGridLayout.CellPosition.getRowAndCol(nextMoves.get(i));
//                cells[nextMove[0]][nextMove[1]] = player == Board.PLAYER_X ? XOCell.X : XOCell.O;
//                if (player == mySeed){
//                    currentScore = minimax(humanSeed)[0];//Score
//                    if (currentScore > bestScore){
//                        bestScore = currentScore;
//                        bestMove = nextMoves.get(i);
//                    }
//                }else{
//                    currentScore = minimax(mySeed)[0];//score
//                    if (currentScore < bestScore){
//                        bestScore = currentScore;
//                        bestMove = nextMoves.get(i);
//                    }
//                }
//                //Undo move
//                cells[nextMove[0]][nextMove[1]] = XOCell.EMPTY;
//            }
//        }
//        return new int[]{bestScore, bestMove};
//    }
}
