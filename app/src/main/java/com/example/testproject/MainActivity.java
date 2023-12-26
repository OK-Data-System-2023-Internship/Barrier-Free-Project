package com.example.testproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.testproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.textId1.setText("Nice to meet you. ViewBinding");
    }
}