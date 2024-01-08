package com.example.testproject;

import android.content.Intent;
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

        FingerPaintFragment fingerPaintFragment = new FingerPaintFragment(classifier, (int drawingNum) -> {
//            switch(drawingNum) {
//                // 0~9 드로잉을 통해 계좌번호 입력
//                case 1:
//                    changeIntent(MainActivity.class);
//                    break;
//                case 2:
//                    changeIntent(TwoActivity.class);
//                    break;
//                case 3:
//                    changeIntent(ThreeActivity.class);
//                    break;
//                default:
//                    Log.v(LOG_TAG,"숫자를 다시 입력");
//            }

            amount += String.valueOf(drawingNum);      // 계좌번호

            // textView 바인딩
            binding.textView1.setText(amount);         // 입력받은 계좌번호 화면에 표시

        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.finger_paint_fragment_container, fingerPaintFragment)
                    .commit();
        }

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
