package com.example.memorygame;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class GameOverPage extends AppCompatActivity {

    private Integer score;
    private TextView scoreDisplay;
    private EditText usernameInput;
    private GameDBHandler dbHandler;
    private Button playButton, highScoresButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over_page);

        dbHandler = new GameDBHandler(this);

        Intent intent = getIntent();
        score = intent.getIntExtra("score", 0);

        scoreDisplay = findViewById(R.id.score);
        usernameInput = findViewById(R.id.usernameInput);

        playButton = findViewById(R.id.playBtn);
        highScoresButton = findViewById(R.id.highScoreBtn);

        scoreDisplay.setText(String.valueOf(score));

        checkAndInsertHighScore();
    }

    private void checkAndInsertHighScore() {
        int highScoreCount = dbHandler.getUsersCount();

        // if there are less than 5 entries or if it is a high score
        if (highScoreCount < 5 || dbHandler.isHighScore(score)) {
            promptForUsername();
        }
    }

    private void promptForUsername() {
        scoreDisplay.setVisibility(View.INVISIBLE);
        playButton.setVisibility(View.INVISIBLE);
        highScoresButton.setVisibility(View.INVISIBLE);
        usernameInput.setVisibility(View.VISIBLE);
    }

    public void onSaveScore(View v) {
        String username = usernameInput.getText().toString().trim();

        if (!username.isEmpty()) {
            hideKeyboard();

            dbHandler.addHighScore(username, score);

            usernameInput.setVisibility(View.GONE);
            scoreDisplay.setVisibility(View.VISIBLE);
            playButton.setVisibility(View.VISIBLE);
            highScoresButton.setVisibility(View.VISIBLE);
        }
    }

    private void hideKeyboard() {
        // close keyboard on screen
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(usernameInput.getWindowToken(), 0);
    }

    public void onPlay(View v) {
        Intent intent = new Intent(GameOverPage.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void SeeHighScore(View v) {
        Intent intent = new Intent(GameOverPage.this, ViewHighScores.class);
        startActivity(intent);
        finish();
    }
}
