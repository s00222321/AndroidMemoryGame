package com.example.memorygame;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class GameDBHandler extends SQLiteOpenHelper {

    public static final String DB_NAME = "game_scores";
    private static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "high_scores";
    public static final String ID_COL = "id";
    public static final String USERNAME_COL = "username";
    public static final String SCORE_COL = "score";

    public GameDBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + USERNAME_COL + " TEXT, "
                + SCORE_COL + " INTEGER)";

        db.execSQL(query);
    }

    public void addHighScore(String username, int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USERNAME_COL, username);
        values.put(SCORE_COL, score);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public int getUsersCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public boolean isHighScore(int score) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to fetch the top five high scores in descending order
        String query = "SELECT " + SCORE_COL + " FROM " + TABLE_NAME +
                " ORDER BY " + SCORE_COL + " DESC LIMIT 5";

        Cursor cursor = db.rawQuery(query, null);

        try {
            int scoreColumnIndex = cursor.getColumnIndex(SCORE_COL);

            while (cursor.moveToNext()) {
                if (scoreColumnIndex != -1) {
                    int highScore = cursor.getInt(scoreColumnIndex);
                    if (score > highScore) {
                        return true; // The given score is a high score
                    }
                }
            }
        } finally {
            cursor.close();
            db.close();
        }

        return false; // The given score is not a high score
    }

    public List<HighScore> getHighScores() {
        List<HighScore> topScores = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to fetch the top five high scores in descending order
        String query = "SELECT * FROM " + TABLE_NAME +
                " ORDER BY " + SCORE_COL + " DESC LIMIT 5";

        Cursor cursor = db.rawQuery(query, null);

        try {
            int idColumnIndex = cursor.getColumnIndex(ID_COL);
            int usernameColumnIndex = cursor.getColumnIndex(USERNAME_COL);
            int scoreColumnIndex = cursor.getColumnIndex(SCORE_COL);

            while (cursor.moveToNext()) {
                if (idColumnIndex != -1 && usernameColumnIndex != -1 && scoreColumnIndex != -1) {
                    int id = cursor.getInt(idColumnIndex);
                    String username = cursor.getString(usernameColumnIndex);
                    int score = cursor.getInt(scoreColumnIndex);

                    HighScore highScore = new HighScore(id, username, score);
                    topScores.add(highScore);
                }
            }
        } finally {
            cursor.close();
            db.close();
        }

        return topScores;
    }
}
