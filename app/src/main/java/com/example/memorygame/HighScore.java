package com.example.memorygame;

public class HighScore {
    private int id;
    private String username;
    private int score;

    public HighScore(int id, String username, int score) {
        this.id = id;
        this.username = username;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return username + " - " + score;
    }
}

