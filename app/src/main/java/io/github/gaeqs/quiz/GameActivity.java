package io.github.gaeqs.quiz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.function.BiConsumer;

import io.github.gaeqs.quiz.data.QuizAdapter;
import io.github.gaeqs.quiz.game.QuizGame;
import io.github.gaeqs.quiz.game.QuizGameStatus;

public class GameActivity extends AppCompatActivity {

    private TextView title;
    private TextView score;
    private Button confirmButton;
    private QuizAdapter adapter;
    private BiConsumer<QuizGame, QuizGameStatus> changeListener = this::onGameStatusChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        title = findViewById(R.id.quiz_title);
        score = findViewById(R.id.quiz_score);
        confirmButton = findViewById(R.id.quiz_confirm_button);
        RecyclerView recyclerView = findViewById(R.id.quiz_recycle_view);

        adapter = new QuizAdapter(this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        confirmButton.setOnClickListener(view -> {
            QuizGame.GAME.nextQuestion();
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onStart() {
        super.onStart();
        QuizGame.GAME.addChangeListener(changeListener);
        title.setText(QuizGame.GAME.getCurrentQuestion().getTitle());
        score.setText("0");
        confirmButton.setVisibility(View.INVISIBLE);
        adapter.setGame(QuizGame.GAME);

        onGameStatusChange(QuizGame.GAME, QuizGame.GAME.getStatus());
    }

    private void onGameStatusChange(QuizGame game, QuizGameStatus status) {
        switch (status) {
            case ANSWERING:
                title.setText(game.getCurrentQuestion().getTitle());
                confirmButton.setVisibility(View.INVISIBLE);
                adapter.prepareForNextQuestion();
                break;
            case ANSWERED:
                confirmButton.setVisibility(View.VISIBLE);
                adapter.showCorrectAnswers();
                score.setText(String.valueOf(game.getScore()));
                break;
            case FINISHED:
                Intent intent = new Intent(this, ScoreActivity.class);
                intent.putExtra("score", game.getScore());
                startActivity(intent);
                break;
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