package com.example.testproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testproject.databinding.ActivityBankingBinding;
import com.example.testproject.databinding.ActivitySubbankingBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;


public class SubbankingActivity extends AppCompatActivity {

    // 바인딩 객체 선언
    private ActivitySubbankingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // 바인딩 객체 초기화
        binding = ActivitySubbankingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // BottomSheet View 초기화
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet2, null);
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(bottomSheetView);
        
        // 버튼 클릭 리스너 설정
        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 버튼 클릭 확인 로그
                Log.v("SubbankingActivity", "계좌입력 버튼이 클릭되었습니다!");

                // 버튼 클릭 시 BottomSheet 올라오기
                bottomSheetDialog.show();
            }
        });


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


    }

}
