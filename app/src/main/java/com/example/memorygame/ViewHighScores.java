package com.example.memorygame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

public class ViewHighScores extends AppCompatActivity {

    private GameDBHandler dbHandler;
    private ListView displayHighScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_high_scores);

        dbHandler = new GameDBHandler(this);

        displayHighScores = findViewById(R.id.displayHighScores);

        // add high scores to list view
        List<HighScore> highScores = dbHandler.getHighScores();
        ArrayAdapter<HighScore> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, highScores);
        displayHighScores.setAdapter(adapter);
    }

    public void onPlay(View v) {
        Intent intent = new Intent(ViewHighScores.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}