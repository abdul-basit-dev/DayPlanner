package com.mytodoworkplan.mydayplanner;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class SpalshActivity extends AppCompatActivity {


    LottieAnimationView mSplashlottie,splash_main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh);

        mSplashlottie = findViewById(R.id.mSplash);
        splash_main = findViewById(R.id.splash_main);



        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent mainIntent = new Intent(SpalshActivity.this,MainActivity.class);
                startActivity(mainIntent);
                finish();

            }
        },2000);





    }
}
