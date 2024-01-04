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
//        setContentView(R.layout.activity_subbanking);
//
////        Log.e("SubbankingActivity", "Moved to SubActivity");
//
//        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
//        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
//        bottomSheetDialog.setContentView(bottomSheetView);
//
//        Button button2 = findViewById(R.id.button_bottom_sheet);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bottomSheetDialog.show();
//            }
//        });

        
        // 바인딩 객체 초기화
        binding = ActivitySubbankingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // BottomSheet View 초기화
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(bottomSheetView);
        
        // 버튼 클릭 리스너 설정
        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 버튼 클릭 확인 로그
                Log.e("SubbankingActivity", "버튼이 클릭되었습니다!");

                // 버튼 클릭 시 BottomSheet 올라오기
                bottomSheetDialog.show();
            }
        });



    }

}
