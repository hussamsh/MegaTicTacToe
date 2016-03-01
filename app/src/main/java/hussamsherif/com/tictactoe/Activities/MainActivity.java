package hussamsherif.com.tictactoe.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import org.greenrobot.eventbus.Subscribe;

import hussamsherif.com.tictactoe.BusEvents.GameStartedEvent;
import hussamsherif.com.tictactoe.BusEvents.PlayerNameChangedEvent;
import hussamsherif.com.tictactoe.BusEvents.TimerToggleEvent;
import hussamsherif.com.tictactoe.CustomViews.Boards.Board;
import hussamsherif.com.tictactoe.CustomViews.Interfaces.BoardController;
import hussamsherif.com.tictactoe.CustomViews.Viewgroups.CellGridLayout;
import hussamsherif.com.tictactoe.Helpers.Bus;
import hussamsherif.com.tictactoe.Helpers.Utils;
import hussamsherif.com.tictactoe.R;

public class MainActivity extends AppCompatActivity implements BoardController {

    private Board gameBoard ;
    private TextView currentPlayerTextView ;
    private TextView timerTextView ;
    private String xPlayerName ;
    private String oPlayerName;
    private boolean isWinnerAnnounced = false ;
    @Board.Player int currentPlayer;

    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        xPlayerName = PreferenceManager.getDefaultSharedPreferences(this).getString("first_player_name" , "Player X");
        oPlayerName = PreferenceManager.getDefaultSharedPreferences(this).getString("second_player_name" , "Player O");
        currentPlayer = Board.PLAYER_X;
        currentPlayerTextView = (TextView) findViewById(R.id.current_player_textView);
        currentPlayerTextView.setText(currentPlayer == Board.PLAYER_X ? xPlayerName : oPlayerName);
        timerTextView = (TextView) findViewById(R.id.timer_textView);
//        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("enable_timer_preference" , false))
        gameBoard = (Board) findViewById(R.id.game_board);
        final View startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameBoard.start();
                startButton.setVisibility(View.GONE);
                View view = findViewById(R.id.button_layout);
                view.setVisibility(View.GONE);
            }
        });


        countDownTimer = new CountDownTimer(PreferenceManager.getDefaultSharedPreferences(this).getInt("timer_preference" , 0) * 1000 ,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerTextView.setText(Utils.convertToReadableTime(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                timerTextView.setText("Timer finished");
                gameBoard.randomClick();
            }
        };
        Bus.getBus().register(this);
    }

    @Override
    public void flipTurns(@CellGridLayout.CellPosition.Gravity int gravity) {
        if (!gameBoard.isAllCellsFull()){
            gameBoard.flipTurns(gravity);
            animatePlayerNameChange(currentPlayer == Board.PLAYER_X? oPlayerName : xPlayerName);
            currentPlayer = currentPlayer == Board.PLAYER_X ? Board.PLAYER_O: Board.PLAYER_X;
            resetTimer();
        }
        else
            onDraw();
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
        isWinnerAnnounced = true;
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title(this.getString(R.string.winner))
                .content(this.getString(R.string.winner_player, winner == Board.PLAYER_X ? xPlayerName : oPlayerName))
                .positiveText(R.string.play_again)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        restartGame();
                    }
                });

        builder.build().show();
        gameBoard.disable();
    }

    @Override
    public boolean isWinnerAnnounced() {
        return isWinnerAnnounced;
    }

    @Override
    public void onDraw() {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title(getString(R.string.draw))
                .content(getString(R.string.draw_content)).positiveText(R.string.play_again)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        restartGame();
                    }
                });
        builder.build().show();
        gameBoard.disable();
    }

    @Override
    public void restartGame() {
        isWinnerAnnounced = false;
        gameBoard.restart();
        animatePlayerNameChange(xPlayerName);
    }

    @Override
    protected void onDestroy() {
        Bus.getBus().unregister(this);
        super.onDestroy();
        //Kill the background processes and exit the app
        System.exit(0);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.setting_menu_action_button:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onPlayerNameChange(PlayerNameChangedEvent event){
        if (event.getPlayer() == Board.PLAYER_X){
            xPlayerName = event.getNewPlayerName();
            if (currentPlayer == Board.PLAYER_X)
                currentPlayerTextView.setText(xPlayerName);
        } else if (event.getPlayer() == Board.PLAYER_O){
            oPlayerName = event.getNewPlayerName();
            if (currentPlayer == Board.PLAYER_O)
                currentPlayerTextView.setText(xPlayerName);
        }
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void onTimerToggle(TimerToggleEvent event){
        switch (event.getState()){
            case TimerToggleEvent.OFF:
                countDownTimer.cancel();
                timerTextView.setText(getString(R.string.timer_disabled));
                break;
            case TimerToggleEvent.ON:
                resetTimer();
                break;
            case TimerToggleEvent.COUNT_TIME_CHANGED:
                Utils.Log("heard you");
                countDownTimer.cancel();
                countDownTimer = new CountDownTimer(PreferenceManager.getDefaultSharedPreferences(this).getInt("timer_preference" , 0) *1000 , 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        timerTextView.setText(Utils.convertToReadableTime(millisUntilFinished));
                    }

                    @Override
                    public void onFinish() {
                        timerTextView.setText("Timer finished");
                        gameBoard.randomClick();
                    }
                }.start();

        }
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void gameStarted(GameStartedEvent event){
        resetTimer();
    }

    private void resetTimer(){
        if (countDownTimer != null)
            countDownTimer.cancel();


        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("enable_timer_preference" , false))
            countDownTimer.start();
    }
}

