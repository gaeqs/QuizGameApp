package io.github.gaeqs.quiz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import io.github.gaeqs.quiz.data.User;
import io.github.gaeqs.quiz.database.AppDatabase;
import io.github.gaeqs.quiz.database.UserStorage;
import io.github.gaeqs.quiz.game.QuizGame;

public class ScoreActivity extends AppCompatActivity {

    private TextView score;
    private TextView time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        score = findViewById(R.id.score_score);
        time = findViewById(R.id.time_text);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();

        if (intent != null) {
            int receivedScore = intent.getIntExtra("score", 0);
            long receiveTime = intent.getLongExtra("finishTime", 0);
            String username = intent.getStringExtra("username");

            String scoreMessage = getResources().getString(R.string.score, receivedScore);
            String timeMessage = getResources().getString(R.string.finishTime, receiveTime / 1000);
            score.setText(scoreMessage);
            time.setText(timeMessage);

            updateUser(username, receivedScore, receiveTime);
        }
    }

    public void playAgain(View view) {
        SharedPreferences preferences = getSharedPreferences(ConfigurationActivity.PREFERENCES, 0);
        String username = preferences.getString(ConfigurationActivity.PREFERENCES_USER, null);
        QuizGame.startNewGame(this, username);
        startActivity(new Intent(this, GameActivity.class));
    }

    public void mainMenu(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private void updateUser(String username, int score, long timeMillis) {
        if (username == null) return;

        AppDatabase.databaseWriteExecutor.execute(() -> {
            User user = AppDatabase.INSTANCE.userDao().getUser(username);
            if (user == null) return;
            if (user.getMaximumScore() > score) return;
            if (user.getMaximumScore() < score
                    || user.getMaximumScore() == score && user.getMaximumScoreTime() > timeMillis) {
                user.setMaximumScore(score);
                user.setMaximumScoreTime(timeMillis);

            }
            AppDatabase.INSTANCE.userDao().updateUsers(user);
        });
    }
}