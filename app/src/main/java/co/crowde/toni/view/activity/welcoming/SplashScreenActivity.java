package co.crowde.toni.view.activity.welcoming;

import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import co.crowde.toni.R;
import co.crowde.toni.controller.user.UserController;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                UserController.getDataLogin(SplashScreenActivity.this);

            }
        }, 3000);
    }
}