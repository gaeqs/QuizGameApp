package io.github.gaeqs.quiz.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.function.BiConsumer;

import io.github.gaeqs.quiz.R;
import io.github.gaeqs.quiz.display.AudioDisplayFragment;
import io.github.gaeqs.quiz.display.ImageDisplayFragment;
import io.github.gaeqs.quiz.display.VideoDisplayFragment;
import io.github.gaeqs.quiz.game.QuizGame;
import io.github.gaeqs.quiz.game.QuizGameStatus;

public class GameActivity extends AppCompatActivity {

    private TextView title;
    private TextView progress;
    private TextView results;

    private Class<? extends Fragment> currentDisplayFragment = null;

    private final BiConsumer<QuizGame, QuizGameStatus> changeListener = this::onGameStatusChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        title = findViewById(R.id.quiz_title);
        progress = findViewById(R.id.progressnum_text);
        results = findViewById(R.id.resultsnum_text);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onStart() {
        super.onStart();
        QuizGame game = QuizGame.GAME;
        game.addChangeListener(changeListener);
        title.setText(game.getCurrentQuestion().getTitle());

        progress.setText(game.getAnsweredQuestions() + " / "
                + game.getMaxQuestions());

        results.setText(game.getCorrectAnswers() + " / "
                + game.getWrongAnswers());

        initFragment();
        onGameStatusChange(game, game.getStatus());
    }

    @SuppressLint("SetTextI18n")
    private void onGameStatusChange(QuizGame game, QuizGameStatus status) {
        switch (status) {
            case ANSWERING:
                title.setText(game.getCurrentQuestion().getTitle());
                initFragment();
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
                intent.putExtra("username", game.getUsername().orElse(null));
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

    private void initFragment() {
        Class<? extends Fragment> clazz = Fragment.class;

        if (QuizGame.GAME.getCurrentQuestion().getImage() != null) {
            clazz = ImageDisplayFragment.class;
        } else if (QuizGame.GAME.getCurrentQuestion().getVideo() != null) {
            clazz = VideoDisplayFragment.class;
        } else if (QuizGame.GAME.getCurrentQuestion().getAudio() != null) {
            clazz = AudioDisplayFragment.class;
        }

        if (currentDisplayFragment == clazz) return;
        currentDisplayFragment = clazz;
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.question_display, clazz, null)
                .addToBackStack(null)
                .commit();
    }


}