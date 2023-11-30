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
import java.util.HashMap;

public class GamePage extends AppCompatActivity implements SensorEventListener{

    private TextView textViewCountdown;
    private SensorManager sensorManager;
    private Sensor sensor;
    private Integer[] order;
    private Integer numberPerRound;
    private HashMap<Integer, String> directions = new HashMap<>();
    private String[] directionsOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_page);

        Intent intent = getIntent();
        order = (Integer[]) intent.getSerializableExtra("order");
        numberPerRound = intent.getIntExtra("numberPerRound", 4);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        directions.put(0, "left");
        directions.put(1, "up");
        directions.put(2, "right");
        directions.put(3, "down");

        directionsOrder = new String[numberPerRound];

        for (int i = 0; i < numberPerRound; i++){
            directionsOrder[i] = directions.get(order[i]);
        }
        textViewCountdown = findViewById(R.id.countdown);
    onResume();
//        Toast.makeText(getApplicationContext(), "Your message here", Toast.LENGTH_SHORT).show();
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
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        // Determine the direction based on accelerometer readings
        String currentDirection = determineDirection(x, y, z);
        textViewCountdown.setText(String.valueOf(z));
        // Compare with the expected direction in your sequence
        checkDirection(currentDirection);
    }

    private String determineDirection(float x, float y, float z) {
        // Implement your logic to determine the direction based on accelerometer readings
        // You may need to experiment and set threshold values for different directions
        // For example, if (x > threshold) return "left";

        return ""; // Replace with your actual direction determination logic
    }

    private void checkDirection(String currentDirection) {
        // Compare the current direction with the expected direction in your sequence
        // Update the index to check the next direction in the sequence
        // If the sequence is correct, continue; otherwise, the user made a mistake
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}