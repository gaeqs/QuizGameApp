package io.github.gaeqs.quiz.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.github.gaeqs.quiz.R;
import io.github.gaeqs.quiz.data.Question;
import io.github.gaeqs.quiz.database.AppDatabase;
import io.github.gaeqs.quiz.game.Difficulty;
import io.github.gaeqs.quiz.game.QuizGame;
import io.github.gaeqs.quiz.util.QuestionUtils;

public class MainActivity extends AppCompatActivity {

    private Button playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playButton = findViewById(R.id.main_play);
    }

    public void play(View view) {
        SharedPreferences preferences = getSharedPreferences(ConfigurationActivity.PREFERENCES, 0);
        String username = preferences.getString(ConfigurationActivity.PREFERENCES_USER, null);

        playButton.setEnabled(false);

        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                QuestionUtils.fillDatabaseWithQuestions(this, R.raw.quizzes);
                Difficulty difficulty = Difficulty.values()
                        [preferences.getInt(ConfigurationActivity.PREFERENCES_DIFFICULTY, 1)];
                List<Question> questions = QuestionUtils
                        .getQuestionsFromDatabase(this, difficulty,
                                Locale.getDefault().getLanguage());
                QuizGame.startNewGame(this, username, questions);

                runOnUiThread(() -> {
                    playButton.setEnabled(true);
                    startActivity(new Intent(this, GameActivity.class));
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void configuration(View view) {
        startActivity(new Intent(this, ConfigurationActivity.class));
    }

    public void ranking(View view) {
        startActivity(new Intent(this, RankingActivity.class));
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}