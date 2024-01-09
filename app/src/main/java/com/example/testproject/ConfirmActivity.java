package com.example.testproject;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testproject.classifier.Classifier;
import com.example.testproject.databinding.ActivityConfirmBinding;
import com.example.testproject.databinding.ActivitySubbankingBinding;

public class ConfirmActivity extends AppCompatActivity {

    private ActivityConfirmBinding binding;

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private void initClassifier() {
        classifier = new Classifier(this);
        Log.v(LOG_TAG, "Classifier initialized");
    }

    private void changeIntent(Class changeToActivity){
        Intent intent = new Intent(ConfirmActivity.this, changeToActivity);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 페이지 이동 확인 로그
        Log.v("ConfirmActivity", "Moved to ConfirmActivity");

        // 바인딩 객체 초기화
        binding = ActivityConfirmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initClassifier();

        /* 드로잉보드가 켜졌습니다. 김가은님에게 2000원을 이체하시겠습니까? 맞으면 1 아니면 0을 입력해주세요 */
        MediaPlayer mediaPlayer = MediaPlayer.create(ConfirmActivity.this, R.raw.confirm_init);

        FingerPaintFragment fingerPaintFragment = new FingerPaintFragment(classifier, (int drawingNum) -> {
//            switch (drawingNum) {
//                case 0:
//                    // 이체 재실행
//                    changeIntent(BankingActivity.class);
//                    break;
//                case 1:
                    // MainActivity 페이지로 이동
                    changeIntent(MainActivity.class);        // 시연 중 오류 방지 위해 이동 페이지 고정함.
//                    break;
//                default:
//                    Log.v(LOG_TAG, "숫자를 다시 입력");
//            }
        }, mediaPlayer);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.finger_paint_fragment_container, fingerPaintFragment)
                    .commit();
        }
    }

    private Classifier classifier;

}