package com.example.gleb.telegraph.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.gleb.telegraph.R;

/**
 * Created by Gleb on 29.12.2015.
 */
public class SplashActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGHT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, SignInActivity.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                            SplashActivity.this,
                            new Pair<View, String>(findViewById(R.id.splashImageView),
                            getString(R.string.transition_circle))
                    );
                    ActivityCompat.startActivity(SplashActivity.this, mainIntent, options.toBundle());
                }
                else{
                    startActivity(mainIntent);
                    overridePendingTransition(R.anim.slide_right, R.anim.slide_left);
                }
                finish();
            }
        }, SPLASH_DISPLAY_LENGHT);
    }
}
