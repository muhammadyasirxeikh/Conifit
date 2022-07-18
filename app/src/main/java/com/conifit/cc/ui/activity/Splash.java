package com.conifit.cc.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import com.conifit.cc.R;
import com.conifit.cc.databinding.ActivitySplashBinding;

public class Splash extends AppCompatActivity {

    private ActivitySplashBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Animation a=AnimationUtils.loadAnimation(this,R.anim.fade_in);
        binding.image.startAnimation(a);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.removeCallbacks(this);



                Intent intent=new Intent(Splash.this,MainActivity.class);
                startActivity(intent);
                finish();

            }
        }, 3000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    }
