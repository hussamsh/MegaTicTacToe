package com.hussamsherif.megatictactoe.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.hussamsherif.megatictactoe.CustomViews.Boards.Board;
import com.hussamsherif.megatictactoe.Interfaces.GameEventsListener;
import com.hussamsherif.megatictactoe.R;

public class BoardActivity extends Activity implements GameEventsListener {

    private Board gameBoard;
    private TextView currentPlayerTextView ;
    private String xPlayerName ;
    private String oPlayerName;

    public static final String AI_PLAYER = "ai_player";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_board);
        gameBoard = (Board) findViewById(R.id.game_board);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            gameBoard.setAIPlayer(extras.getInt(AI_PLAYER));
            xPlayerName = gameBoard.getAIPlayer() == Board.PLAYER_X ? getString(R.string.ai_player) : getString(R.string.your);
            oPlayerName = gameBoard.getAIPlayer() == Board.PLAYER_X ? getString(R.string.your) : getString(R.string.ai_player);
        }else{
            xPlayerName = PreferenceManager.getDefaultSharedPreferences(this).getString("first_player_name", "Player X");
            oPlayerName = PreferenceManager.getDefaultSharedPreferences(this).getString("second_player_name", "Player O");
        }

        currentPlayerTextView = (TextView) findViewById(R.id.current_player_textView);
        currentPlayerTextView.setText(gameBoard.getCurrentPlayer() == Board.PLAYER_X ? xPlayerName : oPlayerName);

        gameBoard.start();
    }


    private void animatePlayerNameChange(final String nextPlayer){
        Animation in = new AlphaAnimation(0.0f , 1.0f);
        in.setDuration(500);
        in.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                currentPlayerTextView.setText(nextPlayer);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        currentPlayerTextView.startAnimation(in);
    }

    @Override
    public void onWinnerFound(@Board.Player int winner) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title(this.getString(R.string.winner))
                .content(this.getString(R.string.winner_player, winner == Board.PLAYER_X ? xPlayerName : oPlayerName))
                .positiveText(R.string.play_again)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        gameBoard.restartGame();
                    }
                });

        if (gameBoard.getAIPlayer() != Board.NONE){
            if (winner != gameBoard.getAIPlayer()){
                builder.content(this.getString(R.string.the_winner_is_you));
                Answers.getInstance().logCustom(new CustomEvent("Human Won"));
            }
            else
                Answers.getInstance().logCustom(new CustomEvent("AI Won"));
        }

        builder.build().show();
    }

    @Override
    public void onGameDraw(){
        new MaterialDialog.Builder(this)
                .title(getString(R.string.draw))
                .content(getString(R.string.draw_content)).positiveText(R.string.play_again)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        gameBoard.restartGame();
                    }
                }).build().show();
    }

    @Override
    public void onPlayersTurnChange(@Board.Player int nextPlayer) {
        animatePlayerNameChange(nextPlayer == Board.PLAYER_X ? xPlayerName : oPlayerName);
    }

    @Override
    public void onGameRestarted() {
        animatePlayerNameChange(gameBoard.getCurrentPlayer() == Board.PLAYER_X ? xPlayerName : oPlayerName);
    }
}

