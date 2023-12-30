package com.example.testproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.testproject.databinding.ActivityMainBinding;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import android.view.View;
import android.widget.Button;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testproject.classifier.Classifier;
import com.example.testproject.classifier.Recognition;
import com.example.testproject.databinding.ActivityMainBinding;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private Classifier classifier;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();

//        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
//        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
//        bottomSheetDialog.setContentView(bottomSheetView);
//
//        Button button = findViewById(R.id.button_bottom_sheet);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bottomSheetDialog.show();
//            }
//        });

    }
    private void init() {
        initClassifier();
        initView();
    }

    private void initClassifier() {
//        try {
        classifier = new Classifier(this);
        Log.v(LOG_TAG, "Classifier initialized");
//        } catch (IOException e) {
//            Toast.makeText(this, R.string.failed_to_create_classifier, Toast.LENGTH_LONG).show();
//            Log.e(LOG_TAG, "init(): Failed to create Classifier", e);
//        }
    }

    private void initView() {
        binding.btnDetect.setOnClickListener(v -> onDetectClick());
        binding.btnClear.setOnClickListener(v -> clearResult());
    }

    private void onDetectClick() {
        if (classifier == null) {
            Log.e(LOG_TAG, "onDetectClick(): Classifier is not initialized");
            return;
        } else if (binding.fpvPaint.isEmpty()) {
            Toast.makeText(this, R.string.please_write_a_digit, Toast.LENGTH_SHORT).show();
            return;
        }
        Bitmap image = binding.fpvPaint.exportToBitmap(
                classifier.getInputShape().getWidth(), classifier.getInputShape().getHeight()
        );
        Recognition result = classifier.classify(image);
        renderResult(result);
    }

    private void renderResult(Recognition result) {
        binding.tvPrediction.setText(String.valueOf(result.getLabel()));
        binding.tvProbability.setText(String.valueOf(result.getConfidence()));
        binding.tvTimecost.setText(String.format(
                getString(R.string.timecost_value),
                result.getTimeCost()
        ));
    }

    private void clearResult() {
        binding.fpvPaint.clear();
        binding.tvPrediction.setText(R.string.empty);
        binding.tvProbability.setText(R.string.empty);
        binding.tvTimecost.setText(R.string.empty);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // classifier.close();
    }

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
}