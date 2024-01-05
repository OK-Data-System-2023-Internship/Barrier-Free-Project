package com.example.testproject;

import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.Toast;

import com.example.testproject.classifier.Classifier;
import com.example.testproject.classifier.Recognition;

public class FingerPaintFragment extends Fragment {
    private FingerPaintView fingerPaintView;
    private Classifier classifier;
    private ViewBinding binding;
    private boolean isDrawingMode = false;

    public FingerPaintFragment(Classifier classifier, ViewBinding binding) {
        // Required empty public constructor
        this.classifier = classifier;
        this.binding = binding;

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_finger_paint, container, false);

        initView();

        fingerPaintView = view.findViewById(R.id.fpv_paint);
        Button btnMount = view.findViewById(R.id.btn_mount);
        Button btnUnMount = view.findViewById(R.id.btn_unmount);

        // 버튼 클릭 리스너 설정
        btnMount.setOnClickListener(v -> onMountDrawBoard());
        btnUnMount.setOnClickListener(v -> onUnmountDrawBoard());

        return view;
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
            Log.v("no", "글 적어라");
            return null;
        }
        Bitmap image = binding.fpvPaint.exportToBitmap(
                classifier.getInputShape().getWidth(), classifier.getInputShape().getHeight()
        );
        Recognition result = classifier.classify(image);
        return result;
    }


    private void onMountDrawBoard() {
        // Mount 로직
    }

    private void onUnmountDrawBoard() {
        // Unmount 로직
    }

}
