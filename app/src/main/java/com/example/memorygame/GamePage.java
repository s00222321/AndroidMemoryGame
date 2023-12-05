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
    private Integer numberPerRound, score;
    private HashMap<Integer, String> directions = new HashMap<>();
    private String[] directionsOrder, userDirectionInput;
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

        String currentDirection = determineDirection(x, y, z);

        if ((currentDirection.equals("up") || currentDirection.equals("down") ||
                currentDirection.equals("left") || currentDirection.equals("right")) &&
                (currentTimestamp - lastDirectionTimestamp > COOLDOWN_PERIOD)) {

            userDirectionInput[currentIndex] = currentDirection;

            if (currentIndex == numberPerRound - 1) {
                checkUserInput();
            } else {
                currentIndex++;
            }
            lastDirectionTimestamp = currentTimestamp;
        }
    }

    private void checkUserInput() {
        boolean isCorrect = true;
        for (int i = 0; i < numberPerRound; i++) {
            if (!userDirectionInput[i].equals(directionsOrder[i])) {
                isCorrect = false;
                break;
            }
        }

        if (isCorrect) {
            // if sequence was correct
            Toast.makeText(getApplicationContext(), "Sequence completed!", Toast.LENGTH_SHORT).show();

            score = numberPerRound;
            numberPerRound = numberPerRound + 2;

            Intent intent = new Intent(GamePage.this, SequencePage.class);
            intent.putExtra("numberPerRound", numberPerRound);
            intent.putExtra("score", score);
            startActivity(intent);
            finish();
        } else {
            // if sequence was incorrect
            Intent intent = new Intent(GamePage.this, GameOverPage.class);
            intent.putExtra("score", score);
            startActivity(intent);
            finish();
        }
    }

    private String determineDirection(float x, float y, float z) {
        // using values from experimentation
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