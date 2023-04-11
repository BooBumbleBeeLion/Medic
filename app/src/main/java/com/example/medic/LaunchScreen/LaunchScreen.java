package com.example.medic.LaunchScreen;

import android.content.*;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.medic.R;
import com.example.medic.SignIn.SignIn;

public class LaunchScreen extends AppCompatActivity {

    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_screen);

        prefs = getSharedPreferences("checkFirstLaunch", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (prefs.getBoolean("firstrun", true)) {
                    Intent intent = new Intent(LaunchScreen.this, SignIn.class);
                    startActivity(intent);
                    finish();
                    // prefs.edit().putBoolean("firstrun", false).commit();
                }
            }
        },1000);
    }
}