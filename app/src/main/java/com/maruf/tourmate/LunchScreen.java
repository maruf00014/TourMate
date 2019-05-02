package com.maruf.tourmate;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LunchScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1200;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch_screen);


        new Handler().postDelayed(new Runnable(){
            @Override
            public  void run(){

                Intent splash = new Intent(LunchScreen.this, RegisterLogInActivity.class);
                startActivity(splash);
                finish();
            }

        },SPLASH_TIME_OUT );

    }
}
