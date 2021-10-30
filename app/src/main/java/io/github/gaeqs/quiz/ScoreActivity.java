package io.github.gaeqs.quiz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import io.github.gaeqs.quiz.database.UserStorage;
import io.github.gaeqs.quiz.game.QuizGame;

public class ScoreActivity extends AppCompatActivity {

    private TextView score;
    private TextView time;
    private UserStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        score = findViewById(R.id.score_score);
        time = findViewById(R.id.time_text);

        storage = new ViewModelProvider(this).get(UserStorage.class);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        if (intent != null) {
            int receivedScore = intent.getIntExtra("score", 0);
            long receiveTime = intent.getLongExtra("finishTime", 0) / 1000;
            String scoreMessage = getResources().getString(R.string.score, receivedScore);
            String timeMessage = getResources().getString(R.string.finishTime, receiveTime);
            score.setText(scoreMessage);
            time.setText(timeMessage);


        }
    }

    public void playAgain(View view) {
        QuizGame.startNewGame(this);
        startActivity(new Intent(this, GameActivity.class));
    }

    public void mainMenu(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}