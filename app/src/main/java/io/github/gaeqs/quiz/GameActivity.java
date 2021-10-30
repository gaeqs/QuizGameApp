package io.github.gaeqs.quiz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.function.BiConsumer;

import io.github.gaeqs.quiz.data.User;
import io.github.gaeqs.quiz.database.AppDatabase;
import io.github.gaeqs.quiz.game.QuizGame;
import io.github.gaeqs.quiz.game.QuizGameStatus;

public class GameActivity extends AppCompatActivity {

    private TextView title;
    private ImageView qImage;
    private TextView progress;
    private TextView results;

    private final BiConsumer<QuizGame, QuizGameStatus> changeListener = this::onGameStatusChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        title = findViewById(R.id.quiz_title);
        qImage = findViewById(R.id.question_image);
        progress = findViewById(R.id.progressnum_text);
        results = findViewById(R.id.resultsnum_text);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onStart() {
        super.onStart();
        QuizGame.GAME.addChangeListener(changeListener);
        title.setText(QuizGame.GAME.getCurrentQuestion().getTitle());
        results.setText("0 / 0");

        progress.setText("0 / "
                + String.valueOf(QuizGame.GAME.getMaxQuestions()));

        setQuestionImage();
        onGameStatusChange(QuizGame.GAME, QuizGame.GAME.getStatus());
    }

    @SuppressLint("SetTextI18n")
    private void onGameStatusChange(QuizGame game, QuizGameStatus status) {
        switch (status) {
            case ANSWERING:
                title.setText(game.getCurrentQuestion().getTitle());
                setQuestionImage();
                break;
            case ANSWERED:
                progress.setText(game.getAnsweredQuestions() + " / "
                    + game.getMaxQuestions());

                results.setText(game.getCorrectAnswers() + " / "
                        + game.getWrongAnswers());
                break;
            case FINISHED:
                Intent intent = new Intent(this, ScoreActivity.class);
                intent.putExtra("score", game.getScore());
                intent.putExtra("finishTime", game.getFinishTime());
                startActivity(intent);
                break;
        }

    }

    private void setQuestionImage() {
        String imgRes = QuizGame.GAME.getCurrentQuestion().getImage();
        if (imgRes != null) {
            qImage.setImageResource(getResources().getIdentifier(imgRes, "drawable", getPackageName()));

        } else {
            qImage.setImageResource(0);
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        QuizGame.GAME.removeChangeListener(changeListener);
    }


}