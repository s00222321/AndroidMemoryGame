package com.example.memorygame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class GameOverPage extends AppCompatActivity {

    private Integer score;
    TextView scoreDisplay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over_page);

        Intent intent = getIntent();
        score = intent.getIntExtra("score", 0);

        scoreDisplay = findViewById(R.id.score);

        scoreDisplay.setText(String.valueOf(score));
    }

    public void onPlay(View v){
        Intent intent = new Intent(GameOverPage.this, SequencePage.class);
        startActivity(intent);
    }
}