package com.hussamsherif.ultratictactoe.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.andexert.library.RippleView;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.hussamsherif.ultratictactoe.CustomViews.Boards.Board;
import com.hussamsherif.ultratictactoe.R;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        checkIfFirstTime();
        setContentView(R.layout.activity_main);
        RippleView singlePlayerRippleView = (RippleView) findViewById(R.id.single_player_rippleView);
        singlePlayerRippleView.setRippleDuration(300);
        singlePlayerRippleView.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                Intent intent = new Intent(MainActivity.this, BoardActivity.class);
                intent.putExtra(BoardActivity.AI_PLAYER, Board.PLAYER_O);
                startActivity(intent);
                Answers.getInstance().logCustom(new CustomEvent("Single game"));
            }
        });
        RippleView multiPlayerRippleView = (RippleView) findViewById(R.id.multi_player_rippleView);
        multiPlayerRippleView.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                Intent intent = new Intent(MainActivity.this, BoardActivity.class);
                startActivity(intent);
                Answers.getInstance().logCustom(new CustomEvent("MultiPlayer game"));
            }
        });
        multiPlayerRippleView.setRippleDuration(300);
        RippleView shareRippleView = (RippleView) findViewById(R.id.share_ripple_view);
        shareRippleView.setRippleDuration(300);
        shareRippleView.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                new MaterialDialog.Builder(MainActivity.this)
                        .title(R.string.share_via)
                        .items(R.array.sharing_networks)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                Intent shareIntent = new Intent();
                                shareIntent.setAction(Intent.ACTION_SEND);
                                String appPackage = "";
                                switch (which) {
                                    case 0:
                                        appPackage = "com.facebook.katana";
                                        break;
                                    case 1:
                                        appPackage = "com.twitter.android";
                                        break;
                                    case 2:
                                        appPackage = "com.google.android.apps.plus";
                                        break;
                                }
                                shareIntent.setPackage(appPackage);
                                shareIntent.setType("text/plain");
                                // Start the specific social application
                                startActivity(shareIntent);
                            }
                        }).build().show();
            }
        });
    }

    private void checkIfFirstTime(){
        SharedPreferences sharedPreferences = getSharedPreferences("Mega" , Context.MODE_PRIVATE);
        if (!sharedPreferences.getBoolean("hasVisited" , false)){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("hasVisited" , true);
            editor.apply();
            startActivity(new Intent(this, WelcomeActivity.class));
        }

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
            case R.id.about_menu_action_button:
                new MaterialDialog.Builder(this)
                        .title(getString(R.string.about))
                        .contentLineSpacing(1.6f)
                        .content(Html.fromHtml(getString(R.string.about_summary)))
                        .build().show();
            default:
                return false;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Kill the app and all it's processes
        System.exit(0);
    }
}
