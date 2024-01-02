package com.example.testproject;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.testproject.databinding.ActivityMainBinding;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.example.testproject.classifier.Classifier;
import com.example.testproject.classifier.Recognition;
import com.nex3z.fingerpaintview.FingerPaintView;

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
        classifier = new Classifier(this);
        Log.v(LOG_TAG, "Classifier initialized");
    }

    private void initView() {
        binding.btnDetect.setOnClickListener(v -> onDetectClick());
        binding.btnClear.setOnClickListener(v -> clearResult());

        FingerPaintView fingerPaintView = findViewById(R.id.fpv_paint);
        fingerPaintView.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getActionMasked();

                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // 사용자가 화면에 터치했을 때의 처리
                        Log.v("touch event", "사용자가 화면에 터치했을 때의 처리");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        // 사용자가 화면을 터치한 채로 움직일 때의 처리
                        Log.v("touch event", "사용자가 화면을 터치한 채로 움직일 때의 처리");
                        break;
                    case MotionEvent.ACTION_UP:
                        // 사용자가 터치를 떼었을 때의 처리
                        Log.v("touch event", "사용자가 터치를 떼었을 때의 처리");
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        // 터치 이벤트가 취소되었을 때의 처리
                        Log.v("touch event", "터치 이벤트가 취소되었을 때의 처리");
                        break;
                }
                return true;
            }

        });
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