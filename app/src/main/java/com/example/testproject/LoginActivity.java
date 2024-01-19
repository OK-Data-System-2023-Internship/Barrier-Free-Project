package com.example.testproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;

import com.example.testproject.databinding.ActivityLoginBinding;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.testproject.classifier.Classifier;

public class LoginActivity  extends AppCompatActivity {
    private boolean isLogin = true;

    private Classifier classifier;
    private ActivityLoginBinding binding;
    LinearLayout container;
    private static final String LOG_TAG = LoginActivity.class.getSimpleName();
    private int loginCnt = 0;
    private void initClassifier() {
        classifier = new Classifier(this);
        Log.v(LOG_TAG, "Classifier initialized");
    }

    private void changeIntent(Class changeToActivity){
        Intent intent = new Intent(LoginActivity.this, changeToActivity);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        container = findViewById(R.id.container);
        initClassifier();

        /* 드로잉 보드가 켜졌습니다. 비밀번호를 입력해 주세요 */
        MediaPlayer mediaPlayer = MediaPlayer.create(LoginActivity.this, R.raw.login_init);

        FingerPaintFragment fingerPaintFragment = new FingerPaintFragment(classifier, (int drawingNum) -> {
            if(++loginCnt >= 6){
                // 로그인 페이지 여부를 저장함.
                isLogin = false;
                saveLoginStatus(isLogin);

                changeIntent(MainActivity.class);
            }
            View circle = new View(LoginActivity.this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(35, 35);
            layoutParams.setMargins(31, 24, 31, 15);
            circle.setLayoutParams(layoutParams);
            circle.setBackgroundResource(R.drawable.circle_shape);
            container.addView(circle);
        }, mediaPlayer);

        // Fragment를 화면에 표시하는 코드
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.finger_paint_fragment_container, fingerPaintFragment)
                    .commit();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // classifier.close();
    }

    private void saveLoginStatus(boolean isLogin) {
        // 데이터베이스에 접근하여 isLoggedIn 값을 저장하는 코드 작성
        // 예를 들어, SharedPreferences를 사용하여 저장할 수 있습니다.
        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLogin", isLogin);
        editor.apply();
    }
}
