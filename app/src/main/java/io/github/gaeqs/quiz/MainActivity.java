package io.github.gaeqs.quiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import io.github.gaeqs.quiz.game.QuizGame;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void play(View view) {
        SharedPreferences preferences = getSharedPreferences(ConfigurationActivity.PREFERENCES, 0);
        String username = preferences.getString(ConfigurationActivity.PREFERENCES_USER, null);
        QuizGame.startNewGame(this, username);
        startActivity(new Intent(this, GameActivity.class));
    }

    public void configuration(View view) {
        startActivity(new Intent(this, ConfigurationActivity.class));
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}