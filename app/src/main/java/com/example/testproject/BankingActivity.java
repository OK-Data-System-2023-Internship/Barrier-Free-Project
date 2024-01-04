package com.example.testproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testproject.databinding.ActivityBankingBinding;

public class BankingActivity extends AppCompatActivity {
    // 바인딩 객체 선언
    private ActivityBankingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 바인딩 객체 초기화
        binding = ActivityBankingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

}
