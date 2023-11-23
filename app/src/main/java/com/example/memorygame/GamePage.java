package com.example.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class GamePage extends AppCompatActivity {

    private TextView textViewCountdown;
    private int countdownValue = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_page);

        textViewCountdown = findViewById(R.id.countdown);

        startCountdown();
    }

    private void startCountdown() {
        final Handler handler = new Handler();
        final Runnable countdownRunnable = new Runnable() {
            @Override
            public void run() {
                textViewCountdown.setText(String.valueOf(countdownValue));

                if (countdownValue == 0) {
                    textViewCountdown.setText("Go");
                    // You can add additional actions here when the countdown reaches 0
                } else {
                    countdownValue--;

                    handler.postDelayed(this, 1000);
                }
            }
        };
        handler.post(countdownRunnable);
    }
}