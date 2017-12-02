package com.istea.agustinlema.todoapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.istea.agustinlema.todoapp.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Create XML with default values
        PreferenceManager.setDefaultValues(this, R.xml.settings, true);

        if (shouldShowSplash())
            showSplash();
        else
            goToHome();
    }

    private void goToHome(){
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void showSplash(){
        Thread thread = new Thread() {
            public void run() {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    goToHome();
                }
            }
        };
        thread.start();

        if (shouldVibrate()){
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(1500);
        }
    }

    private boolean shouldShowSplash() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean showSplash = sharedPref.getBoolean("splash_on_startup", false);
        return showSplash;
    }

    private boolean shouldVibrate(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean shouldVibrate = sharedPref.getBoolean("vibrate_on_startup", false);
        return shouldVibrate;
    }
}
