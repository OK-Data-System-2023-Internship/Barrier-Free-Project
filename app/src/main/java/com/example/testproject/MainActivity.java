package com.example.testproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.testproject.databinding.ActivityMainBinding;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(bottomSheetView);

        Button button = findViewById(R.id.button_bottom_sheet);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.show();
            }
        });
    }
}