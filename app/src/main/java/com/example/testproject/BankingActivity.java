package com.example.testproject;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testproject.classifier.Classifier;
import com.example.testproject.databinding.ActivityBankingBinding;

public class BankingActivity extends AppCompatActivity {
    private String amount="";

    // 바인딩 객체 선언
    private ActivityBankingBinding binding;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private void initClassifier() {
        classifier = new Classifier(this);
        Log.v(LOG_TAG, "Classifier initialized");
    }

    private void changeIntent(Class changeToActivity){
        Intent intent = new Intent(BankingActivity.this, changeToActivity);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e("BankingActivity", "Moved to BankingPage!");

        // 바인딩 객체 초기화
        binding = ActivityBankingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initClassifier();

        /* 드로잉 보드가 켜졌습니다. 금액을 입력하신 후에 드로잉 보드를 종료해주세요 */
        MediaPlayer mediaPlayer = MediaPlayer.create(BankingActivity.this, R.raw.banking_init);
        FingerPaintFragment fingerPaintFragment = new FingerPaintFragment(classifier, (int drawingNum) -> {
            // 금액 드로잉 입력
            amount += String.valueOf(drawingNum);      // 금액

            // textView 바인딩
            binding.textView1.setText(amount);         // 입력받은 금액 화면에 표시

        }, mediaPlayer);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.finger_paint_fragment_container, fingerPaintFragment)
                    .commit();
        }
//
//        // make a function that change intent to SubbankingActivity when button1 is clicked
//        binding.button1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                changeIntent(SubbankingActivity.class);
//            }
//        });

         // 버튼 클릭 리스너 설정
        binding.button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 버튼 클릭 확인 로그
                Log.e("BankingActivity", "버튼이 클릭되었습니다!");


                // 버튼 클릭 시 SubbankingActivity 페이지로 이동
                // 인텐트 생성 및 SubbankingActivity 시작
                Intent intent = new Intent(BankingActivity.this, SubbankingActivity.class);
                startActivity(intent);

            }
        });
    }

    private Classifier classifier;


}
