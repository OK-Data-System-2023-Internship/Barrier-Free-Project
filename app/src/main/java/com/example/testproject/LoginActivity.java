package com.example.testproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import com.example.testproject.databinding.ActivityLoginBinding;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.testproject.classifier.Classifier;

public class LoginActivity  extends AppCompatActivity {
    private Classifier classifier;
    private ActivityLoginBinding binding;
    LinearLayout container;
    private static final String LOG_TAG = LoginActivity.class.getSimpleName();
    private int loginCnt = 0;
    private void initClassifier() {
        classifier = new Classifier(this);
        Log.v(LOG_TAG, "Classifier initialized");
    }

    private void changeIntent(Class changeToActivity){
        Intent intent = new Intent(LoginActivity.this, changeToActivity);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        container = findViewById(R.id.container);
        initClassifier();

        /* 드로잉 보드가 켜졌습니다. 비밀번호를 입력해 주세요 */
        MediaPlayer mediaPlayer = MediaPlayer.create(LoginActivity.this, R.raw.login_init);

        FingerPaintFragment fingerPaintFragment = new FingerPaintFragment(classifier, (int drawingNum) -> {
            if(++loginCnt >= 6){
                changeIntent(MainActivity.class);
            }
            View circle = new View(LoginActivity.this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(35, 35);
            layoutParams.setMargins(18, 15, 18, 15);
            circle.setLayoutParams(layoutParams);
            circle.setBackgroundResource(R.drawable.circle_shape);
            container.addView(circle);
        }, mediaPlayer);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.finger_paint_fragment_container, fingerPaintFragment)
                    .commit();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // classifier.close();
    }
}
