package com.azis.skripsiproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import com.azis.skripsiproject.Login.LoginActivity;
import com.azis.skripsiproject.User.Dashboard.DashboardActivity;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        final MediaPlayer mpmulai = MediaPlayer.create(this, R.raw.selamatdatang);
        mpmulai.start();

        new Handler().postDelayed(new Runnable() {
            @Override

            public void run() {

                String loginstatus = sharedPreferences.getString(getResources().getString(R.string.prefLoginState),"");
                if (loginstatus.equals("LoggedIn")){
                    startActivity(new Intent(SplashActivity.this, DashboardActivity.class));
                    overridePendingTransition(0,0);
                    finish();
                }
                else{
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    overridePendingTransition(0,0);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }
}