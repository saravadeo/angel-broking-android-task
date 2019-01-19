package com.onkar.angelbroking;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.onkar.helper.Constants;

public class MainActivity extends AppCompatActivity {

    private int progressBarStatus = 0;
    private Handler progressBarbHandler = new Handler();
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        final ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar);

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (progressBarStatus < 100) {
                    progressBarStatus = progressBarStatus + 20;
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    progressBarbHandler.post(new Runnable() {
                        public void run() {
                            pb.setProgress(progressBarStatus);
                        }
                    });
                }
                if (progressBarStatus >= 100) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    finish();
                    redirectToLoginOrDashboard();
                }
            }
        }).start();
    }

    private void redirectToLoginOrDashboard() {
        sharedPreferences = getSharedPreferences(Constants.ANGEL, Context.MODE_PRIVATE);
        String username = sharedPreferences.getString(Constants.USERNAME, null);
        if (username == null) {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
        } else {
            Intent i = new Intent(MainActivity.this, DashboardActivity.class);
            startActivity(i);
        }
    }
}
