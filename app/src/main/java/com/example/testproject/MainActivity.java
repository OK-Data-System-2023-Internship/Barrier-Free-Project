package com.example.testproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import com.example.testproject.databinding.ActivityMainBinding;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.Toast;

import com.example.testproject.classifier.Classifier;
import com.example.testproject.classifier.Recognition;

public class MainActivity extends AppCompatActivity {
    private Classifier classifier;
    private ActivityMainBinding binding;

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private void initClassifier() {
        classifier = new Classifier(this);
        Log.v(LOG_TAG, "Classifier initialized");
    }

    private void changeIntent(Class changeToActivity){
        Intent intent = new Intent(MainActivity.this, changeToActivity);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initClassifier();

        FingerPaintFragment fingerPaintFragment = new FingerPaintFragment(classifier, (int drawingNum) -> {
            switch(drawingNum) {
                case 1:
                    changeIntent(SubbankingActivity.class);
                case 2:
                    changeIntent(BankingActivity.class);
                case 3:
                    changeIntent(ConfirmActivity.class);
                default:
                    Log.v(LOG_TAG,"숫자를 다시 입력");
            }
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.finger_paint_fragment_container, fingerPaintFragment)
                    .commit();
        }

        /* 드로잉 보드가 켜졌습니다. 전체 계좌를 보시기 원하시면 1번, 이체를 원하시면 2번, 돈 모으기를 원하시면 3번, 대출을 원하시면 4번, 대출 상환을 원하시면 5번을 적어주세요 */
        MediaPlayer mediaplay = MediaPlayer.create(MainActivity.this, R.raw.main_init);
        mediaplay.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // classifier.close();
    }
}