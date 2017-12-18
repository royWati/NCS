package com.example.roywati.ncs.defaults;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.example.roywati.ncs.R;

public class Splash extends Activity {
    private static int SPLASH_TIME_OUT = 3000;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Splash.this.startActivity(new Intent(Splash.this, LoginActivity.class));
                Splash.this.finish();
            }
        }, (long) SPLASH_TIME_OUT);
    }
}
