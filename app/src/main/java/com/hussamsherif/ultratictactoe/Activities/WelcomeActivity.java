package com.hussamsherif.ultratictactoe.Activities;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.hussamsherif.ultratictactoe.CustomPagerAdapter;
import com.hussamsherif.ultratictactoe.R;

public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new CustomPagerAdapter());
    }
}
