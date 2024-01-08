package com.example.testproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import com.example.testproject.databinding.ActivityMainBinding;
import android.util.Log;

import com.example.testproject.classifier.Classifier;

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
                // 모든 case를 확인하여, case3만 출력하는 문제.
                // case에 해당하는 동작 후 종료하기 위해
                // break; 추가

                case 1:
                    changeIntent(BankingActivity.class);
//                    Log.v(LOG_TAG,"111111");
                    break;
                case 2:
                    changeIntent(TwoActivity.class);
//                    Log.v(LOG_TAG,"22222");
                    break;
                case 3:
                    changeIntent(ThreeActivity.class);
//                    Log.v(LOG_TAG,"33333");
                    break;
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