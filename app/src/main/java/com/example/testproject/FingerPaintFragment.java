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

@FunctionalInterface
interface VoidFunction {
    void execute(int drawingNum);
}
public class FingerPaintFragment extends Fragment {
    private FingerPaintView fingerPaintView;
    private Classifier classifier;
    private boolean isDrawingMode = false;
    private VoidFunction myFunction;

    public FingerPaintFragment(Classifier classifier, VoidFunction function) {
        // Required empty public constructor
        this.classifier = classifier;
        this.myFunction = function;
    }

    public void performAction(int drawingNum){
        myFunction.execute(drawingNum);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_finger_paint, container, false);

        this.fingerPaintView = view.findViewById(R.id.fpv_paint);
        initView(view);

        return view;
    }

    private void initView(View view) {
        fingerPaintView.setBackgroundResource(R.drawable.draw_layout);
        fingerPaintView.setCustomEventListener(new OnWriteEventListener() {
            @Override
            public void onWriteFinish() {
                Recognition result = onDetectImage();
                Log.v(LOG_TAG, String.valueOf(result.getLabel())+"인식됐고, 확률은 "+String.valueOf(result.getConfidence()));
                fingerPaintView.clear();
                performAction(result.getLabel());
            }
        });
        fingerPaintView.post(new Runnable() {
            @Override
            public void run() {
                float viewHeight = fingerPaintView.getHeight();
                fingerPaintView.setTranslationY(viewHeight);

            }
        });

        Button btnDrawingMode = view.findViewById(R.id.btn_drawing_mode);
        btnDrawingMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDrawingMode){
                    onUnmountDrawBoard(fingerPaintView);
                }else{
                    onMountDrawBoard(fingerPaintView);
                }
            }
        });
    };

    public void onMountDrawBoard(FingerPaintView fingerPaintView) {
        isDrawingMode = true;
        fingerPaintView.animate()
                .translationY(0) // 뷰를 원래 위치로 이동시킵니다.
                .setDuration(600)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start(); // 애니메이션 시작
    };

    public void onUnmountDrawBoard(FingerPaintView fingerPaintView) {
        isDrawingMode = false;
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
        } else if (fingerPaintView.isEmpty()) {
            Log.v(LOG_TAG, "글 적어라");
            return null;
        }
        Bitmap image = fingerPaintView.exportToBitmap(
                classifier.getInputShape().getWidth(), classifier.getInputShape().getHeight()
        );
        Recognition result = classifier.classify(image);
        return result;
    }
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
}
