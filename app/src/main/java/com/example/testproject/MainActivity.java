package com.example.testproject;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.testproject.databinding.ActivityMainBinding;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.Toast;

import com.example.testproject.classifier.Classifier;
import com.example.testproject.classifier.Recognition;

public class MainActivity extends AppCompatActivity {
    private Classifier classifier;
    private ActivityMainBinding binding;
    private boolean isDrawingMode = false;

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
        fingerPaintView.post(new Runnable() {
            @Override
            public void run() {
                float viewHeight = fingerPaintView.getHeight();
                Log.v("main",viewHeight+"높이 입니다");
                fingerPaintView.setTranslationY(viewHeight);

            }
        });

        Button btnDrawingMode = findViewById(R.id.btn_drawing_mode);

        btnDrawingMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDrawingMode){
                    onUnmountDrawBoard(fingerPaintView);
                    isDrawingMode = false;
                }else{
                    onMountDrawBoard(fingerPaintView);
                    isDrawingMode = true;
                }
            }
        });
    };

    public void onMountDrawBoard(FingerPaintView fingerPaintView) {
        fingerPaintView.animate()
                .translationY(0) // 뷰를 원래 위치로 이동시킵니다.
                .setDuration(600)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start(); // 애니메이션 시작
    };

    public void onUnmountDrawBoard(FingerPaintView fingerPaintView) {
        float viewHeight = fingerPaintView.getHeight();
        fingerPaintView.animate()
                .translationY(viewHeight) // 뷰를 원래 위치로 이동시킵니다.
                .setDuration(600)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start(); // 애니메이션 시작
    };


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