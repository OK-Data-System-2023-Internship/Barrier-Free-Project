package com.example.testproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testproject.databinding.ActivityConfirmBinding;
import com.example.testproject.databinding.ActivityTwoBinding;

public class TwoActivity extends AppCompatActivity {

    private ActivityTwoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 바인딩 객체 초기화
        binding = ActivityTwoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Log.e("TwoActivity", "Moved to Two");

        binding.button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* main으로 이동하기 */
                // 버튼 클릭 확인 로그
                Log.v("TwoActivity", "22 메인으로 이동");

                // 확인 버튼 클릭 시 MainActivity 페이지로 이동
                // 인텐트 생성 및 MainActivity 시작
                Intent intent = new Intent(TwoActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}