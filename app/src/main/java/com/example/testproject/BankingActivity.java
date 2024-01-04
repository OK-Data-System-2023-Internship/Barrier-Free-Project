package com.example.testproject;

import android.os.Bundle;
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
                // 버튼 클릭 시 수행할 작업

            }
        });
    }

}
