package com.example.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class GamePage extends AppCompatActivity implements SensorEventListener{

    private TextView textViewCountdown;
    private SensorManager sensorManager;
    private Sensor sensor;
    private Integer[] order;
    private Integer numberPerRound, score, continueScore;
    private HashMap<Integer, String> directions = new HashMap<>();
    private String[] directionsOrder;
    private String[] userDirectionInput;
    private int currentIndex = 0;
    private long lastDirectionTimestamp = 0;
    private static final long COOLDOWN_PERIOD = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_page);

        Intent intent = getIntent();
        order = (Integer[]) intent.getSerializableExtra("order");
        numberPerRound = intent.getIntExtra("numberPerRound", 4);
        score = intent.getIntExtra("score", 0);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        directions.put(0, "left");
        directions.put(1, "up");
        directions.put(2, "right");
        directions.put(3, "down");

        directionsOrder = new String[numberPerRound];
        userDirectionInput = new String[numberPerRound];

        for (int i = 0; i < numberPerRound; i++) {
            directionsOrder[i] = directions.get(order[i]);
        }
        textViewCountdown = findViewById(R.id.countdown);
        onResume();
    }

    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        long currentTimestamp = System.currentTimeMillis();
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        // Determine the direction based on accelerometer readings
        String currentDirection = determineDirection(x, y, z);

        // Check if it's a valid direction and there's a cool down period
        if ((currentDirection.equals("up") || currentDirection.equals("down") ||
                currentDirection.equals("left") || currentDirection.equals("right")) &&
                (currentTimestamp - lastDirectionTimestamp > COOLDOWN_PERIOD)) {

            textViewCountdown.setText(currentDirection);

            // Store the user's input direction
            userDirectionInput[currentIndex] = currentDirection;

            // Check if the user has completed the entire sequence
            if (currentIndex == numberPerRound - 1) {
                // User input array is full, compare it to the expected sequence
                checkUserInput();
            } else {
                // Update the index to check the next direction
                currentIndex++;
            }

            // Update the last direction timestamp
            lastDirectionTimestamp = currentTimestamp;
        }
    }

    private void checkUserInput() {
        // Compare the user's input with the expected direction in the sequence
        boolean isCorrect = true;
        for (int i = 0; i < numberPerRound; i++) {
            if (!userDirectionInput[i].equals(directionsOrder[i])) {
                // If any direction is incorrect, set the flag to false
                isCorrect = false;
                break;
            }
        }

        if (isCorrect) {
            // User successfully completed the sequence
            Toast.makeText(getApplicationContext(), "Sequence completed!", Toast.LENGTH_SHORT).show();

            continueScore = numberPerRound;
            numberPerRound = numberPerRound + 2;

            Intent intent = new Intent(GamePage.this, SequencePage.class);
            intent.putExtra("numberPerRound", numberPerRound);
            intent.putExtra("score", continueScore);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Wrong sequence! Try again.", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(GamePage.this, GameOverPage.class);
            intent.putExtra("score", score);
            startActivity(intent);
        }
    }

    private String determineDirection(float x, float y, float z) {
        if (x <= -1.6) {
            return "up";
        } else if (x >= 8.8) {
            return "down";
        } else if (y <= -4) {
            return "left";
        } else if (y >= 4) {
            return "right";
        } else {
            return "";
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }
}