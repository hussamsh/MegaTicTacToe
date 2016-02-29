package hussamsherif.com.tictactoe.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import hussamsherif.com.tictactoe.CustomViews.Boards.Board;
import hussamsherif.com.tictactoe.CustomViews.Viewgroups.CellGridLayout;
import hussamsherif.com.tictactoe.Interfaces.BoardController;
import hussamsherif.com.tictactoe.R;

public class MainActivity extends AppCompatActivity implements BoardController {

    private Board gameBoard ;
    private boolean isWinnerAnnounced = false ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameBoard = (Board) findViewById(R.id.game_board);
    }

    @Override
    public void flipTurns(@CellGridLayout.CellPosition.Gravity int gravity) {
        if (!gameBoard.isAllCellsFull())
            gameBoard.flipTurns(gravity);
        else
            onDraw();
    }

    @Override
    public void onWinnerFound(@Board.Player int winner) {
        isWinnerAnnounced = true;
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .title(this.getString(R.string.winner))
                .content(this.getString(R.string.winner_player , winner == Board.PLAYER_X ? "Player X" : "Player Y"))
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
    }

    @Override
    protected void onDestroy() {
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
}

