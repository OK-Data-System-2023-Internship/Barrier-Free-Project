package com.example.testproject;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testproject.classifier.Classifier;
import com.example.testproject.databinding.ActivityBankingBinding;
import com.example.testproject.databinding.ActivitySubbankingBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;


public class SubbankingActivity extends AppCompatActivity {

    private String accountNums="";

    // 바인딩 객체 선언
    private ActivitySubbankingBinding binding;

    private static final String LOG_TAG = MainActivity.class.getSimpleName();


    private void initClassifier() {
        classifier = new Classifier(this);
        Log.v(LOG_TAG, "Classifier initialized");
    }

    private void changeIntent(Class changeToActivity){
        Intent intent = new Intent(SubbankingActivity.this, changeToActivity);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // 바인딩 객체 초기화
        binding = ActivitySubbankingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initClassifier();

        // Bottom Sheet를 표시하려면 이 코드를 호출합니다
        BottomSheetFragment bottomSheet = new BottomSheetFragment();
        bottomSheet.show(getSupportFragmentManager(), "BottomSheetFragment");


        /* 드로잉 보드가 켜졌습니다. 계좌번호를 입력하시고 드로잉 보드를 닫아주세요 */
        MediaPlayer mediaPlayerInit = MediaPlayer.create(SubbankingActivity.this, R.raw.subbanking_init);

        FingerPaintFragment fingerPaintFragment = new FingerPaintFragment(classifier, (int drawingNum) -> {
            // 계좌번호 드로잉 입력
            accountNums += String.valueOf(drawingNum);      // 계좌번호

            // textView 바인딩
            binding.textView2.setText(accountNums);         // 입력받은 계좌번호 화면에 표시

        },mediaPlayerInit);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.finger_paint_fragment_container, fingerPaintFragment)
                    .commit();
        }


        binding.button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 버튼 클릭 확인 로그
                Log.v("SubbankingActivity", "확인 버튼이 클릭되었습니다!");

                // 버튼 클릭 시 ConfirmActivity 페이지로 이동
                // 인텐트 생성 및 ConfirmActivity 시작
                Intent intent = new Intent(SubbankingActivity.this, ConfirmActivity.class);
                startActivity(intent);
            }
        });

        /* 은행을 선택해주세요 */
        MediaPlayer mediaPlayer = MediaPlayer.create(SubbankingActivity.this, R.raw.subbanking_create);
        mediaPlayer.start();

    }

    private Classifier classifier;

}
