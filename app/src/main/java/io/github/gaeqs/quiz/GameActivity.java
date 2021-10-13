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

import io.github.gaeqs.quiz.game.QuizGame;
import io.github.gaeqs.quiz.game.QuizGameStatus;

public class GameActivity extends AppCompatActivity {

    private TextView title;
    private ImageView qImage;
    private TextView score;
    private Button confirmButton;

    private final BiConsumer<QuizGame, QuizGameStatus> changeListener = this::onGameStatusChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        title = findViewById(R.id.quiz_title);
        qImage = findViewById(R.id.question_image);
        score = findViewById(R.id.quiz_score);
        confirmButton = findViewById(R.id.quiz_confirm_button);

        confirmButton.setOnClickListener(view -> QuizGame.GAME.nextQuestion());
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onStart() {
        super.onStart();
        QuizGame.GAME.addChangeListener(changeListener);
        title.setText(QuizGame.GAME.getCurrentQuestion().getTitle());

        setQuestionImage();

        score.setText(String.valueOf(QuizGame.GAME.getScore()));
        confirmButton.setVisibility(View.INVISIBLE);
        onGameStatusChange(QuizGame.GAME, QuizGame.GAME.getStatus());
    }

    private void onGameStatusChange(QuizGame game, QuizGameStatus status) {
        switch (status) {
            case ANSWERING:
                title.setText(game.getCurrentQuestion().getTitle());
                setQuestionImage();
                confirmButton.setVisibility(View.INVISIBLE);
                break;
            case ANSWERED:
                confirmButton.setVisibility(View.VISIBLE);
                score.setText(String.valueOf(game.getScore()));
                break;
            case FINISHED:
                Intent intent = new Intent(this, ScoreActivity.class);
                intent.putExtra("score", game.getScore());
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