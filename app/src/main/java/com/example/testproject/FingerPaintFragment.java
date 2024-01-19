package com.example.testproject;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;

import com.example.testproject.classifier.Classifier;
import com.example.testproject.classifier.Recognition;

@FunctionalInterface
interface VoidFunction {
    void execute(int drawingNum);
}
public class FingerPaintFragment extends Fragment {
    private boolean isLoginActivity = true;

    private FingerPaintView fingerPaintView;
    private Classifier classifier;
    private boolean isDrawingMode = false;
    private VoidFunction drawingCallback;
    private MediaPlayer mediaPlayer;

    public FingerPaintFragment() {
        // Required empty public constructor
        this.classifier = classifier;
        this.drawingCallback = drawingCallback;
    }
    public FingerPaintFragment(Classifier classifier, VoidFunction drawingCallback, MediaPlayer mediaPlayer) {
        // Required empty public constructor
        this.classifier = classifier;
        this.drawingCallback = drawingCallback;
        this.mediaPlayer = mediaPlayer;
    }

    public void performAction(int drawingNum){
        drawingCallback.execute(drawingNum);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_finger_paint, container, false);

        this.fingerPaintView = view.findViewById(R.id.fpv_paint);
        initView(view);

        // 로그인페이지에서 드로잉보드를 활성화 시켰다면,
        // 이후 화면에서도 자동으로 활성화시킴.
        Button btnDrawingMode = view.findViewById(R.id.btn_drawing_mode);
        boolean status = readLoginStatus();
        if(!status){
            btnDrawingMode.performClick();
        }

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
    // 애니메이션을 적용합니다.
    public void onMountDrawBoard(FingerPaintView fingerPaintView) {
        isDrawingMode = true;// 드로잉 보드를 올릴 때
        fingerPaintView.animate()
                .translationY(0) // 뷰를 원래 위치로 이동시킵니다.
                .setDuration(600)// 애니메이션 시간
                .setInterpolator(// 애니메이션 속도 조절
                        new AccelerateDecelerateInterpolator()
                )
                .start(); // 애니메이션 시작
        if(mediaPlayer != null){//음악 재생
            mediaPlayer.start();
        }
    };

    public void onUnmountDrawBoard(FingerPaintView fingerPaintView) {
        isDrawingMode = false;
        float viewHeight = fingerPaintView.getHeight();
        fingerPaintView.animate()
                .translationY(viewHeight) // 뷰를 원래 위치로 이동시킵니다.
                .setDuration(600)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start(); // 애니메이션 시작

        if(mediaPlayer != null){
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
        }
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


    // isLoggedIn 변수를 데이터베이스에서 읽는 메소드
    private boolean readLoginStatus() {
        // 데이터베이스에서 isLoggedIn 값을 읽어오는 코드 작성
        // 예를 들어, SharedPreferences를 사용하여 읽을 수 있습니다.
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login", MODE_PRIVATE);
        return sharedPreferences.getBoolean("isLogin", false);
    }
}
