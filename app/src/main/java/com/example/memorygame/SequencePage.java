package com.example.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class SequencePage extends AppCompatActivity {

    private TextView textViewCountdown;

    private ImageView blueCircle, yellowCircle, pinkCircle, blackCircle;
    private int countdownValue = 3;
    private Integer numberPerRound;

    HashMap<Integer, ImageView> circles = new HashMap<>();

    ArrayList<ImageView> orderOfCircles = new ArrayList<>();

    Integer[] order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sequence_page);

        textViewCountdown = findViewById(R.id.countdown);

        if (numberPerRound == null){
            numberPerRound = 4;
        }

        blackCircle = findViewById(R.id.blackCircle);
        blueCircle = findViewById(R.id.blueCircle);
        pinkCircle = findViewById(R.id.pinkCircle);
        yellowCircle = findViewById(R.id.yellowCircle);

        circles.put(0, blackCircle);
        circles.put(1, blueCircle);
        circles.put(2, pinkCircle);
        circles.put(3, yellowCircle);

        startCountdown();
    }

    private void startCountdown() {
        final Handler handler = new Handler();
        final Runnable countdownRunnable = new Runnable() {
            @Override
            public void run() {
                textViewCountdown.setText(String.valueOf(countdownValue));

                if (countdownValue == 0) {
                    textViewCountdown.setText("");
                    startFlashingCombination();
                } else {
                    countdownValue--;

                    handler.postDelayed(this, 1000);
                }
            }
        };
        handler.post(countdownRunnable);
    }

    private void startFlashingCombination() {
        order = new Integer[numberPerRound];
        final Handler handler = new Handler();

        int totalDelay = 0;

        for (int i = 0; i < numberPerRound; i++) {
            int randomIndex = generateRandomNumber();
            ImageView imageView = circles.get(randomIndex);

            order[i] = randomIndex;

            final int delay = totalDelay; // Use the cumulative delay

            handler.postDelayed(() -> {
                flashCircle(imageView);
            }, delay);

            handler.postDelayed(() -> {
                revertFlash(imageView);
            }, delay + 1500);

            totalDelay += 3000; // Add the delay for the next iteration
        }

        // Calculate total time needed for all circles to flash
        int totalTime = numberPerRound * 3000;

        // Navigate to the next page after all circles have flashed
        handler.postDelayed(() -> {
            navigateToGame();
        }, totalTime);
    }



    private void flashCircle(ImageView imageView) {
        imageView.setColorFilter(getResources().getColor(android.R.color.white));
    }

    private void revertFlash(ImageView imageView) {
        imageView.clearColorFilter();
    }

    private static int generateRandomNumber() {
        Random random = new Random();
        return random.nextInt(4) ;
    }

    private void navigateToGame() {
        Intent intent = new Intent(SequencePage.this, GamePage.class);
        intent.putExtra("order", order);
        intent.putExtra("numberPerRound", numberPerRound);
        startActivity(intent);
    }
}