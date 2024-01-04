package com.example.testproject;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.testproject.databinding.ActivityMainBinding;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.example.testproject.classifier.Classifier;
import com.example.testproject.classifier.Recognition;

public class MainActivity extends AppCompatActivity {
    private Classifier classifier;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();

    }
    private void init() {
        initClassifier();
        initView();
    }

    private void initClassifier() {
        classifier = new Classifier(this);
        Log.v(LOG_TAG, "Classifier initialized");
    }

    private void initView() {
        FingerPaintView fingerPaintView = findViewById(R.id.fpv_paint);
        fingerPaintView.setBackgroundResource(R.drawable.draw_layout);
        fingerPaintView.setCustomEventListener(new OnWriteEventListener() {
            @Override
            public void onWriteFinish() {
                Recognition result = onDetectImage();
                Log.v("Main-onWriteFinish", String.valueOf(result.getLabel())+"인식됐고, 확률은 "+String.valueOf(result.getConfidence()));
                fingerPaintView.clear();
            }
        });
    }

    private Recognition onDetectImage() {
        if (classifier == null) {
            Log.e(LOG_TAG, "onDetectClick(): Classifier is not initialized");
            return null;
        } else if (binding.fpvPaint.isEmpty()) {
            Toast.makeText(this, R.string.please_write_a_digit, Toast.LENGTH_SHORT).show();
            return null;
        }
        Bitmap image = binding.fpvPaint.exportToBitmap(
                classifier.getInputShape().getWidth(), classifier.getInputShape().getHeight()
        );
        Recognition result = classifier.classify(image);
        return result;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // classifier.close();
    }

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
}