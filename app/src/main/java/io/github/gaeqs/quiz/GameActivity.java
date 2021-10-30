package io.github.gaeqs.quiz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.function.BiConsumer;

import io.github.gaeqs.quiz.display.ImageDisplayFragment;
import io.github.gaeqs.quiz.game.QuizGame;
import io.github.gaeqs.quiz.game.QuizGameStatus;

public class GameActivity extends AppCompatActivity {

    private TextView title;
    private TextView progress;
    private TextView results;

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
        QuizGame.GAME.addChangeListener(changeListener);
        title.setText(QuizGame.GAME.getCurrentQuestion().getTitle());

        results.setText("0 / 0");
        progress.setText("0 / " + QuizGame.GAME.getMaxQuestions());

        initFragment();
        onGameStatusChange(QuizGame.GAME, QuizGame.GAME.getStatus());
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
        }

        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.question_display, clazz, null)
                .addToBackStack(null)
                .commit();
    }


}