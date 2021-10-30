package io.github.gaeqs.quiz.display;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.function.BiConsumer;

import io.github.gaeqs.quiz.R;
import io.github.gaeqs.quiz.game.QuizGame;
import io.github.gaeqs.quiz.game.QuizGameStatus;

public class ImageDisplayFragment extends Fragment {

    private final BiConsumer<QuizGame, QuizGameStatus> listener = this::onGameStatusChange;

    private ImageView imageView;

    public ImageDisplayFragment() {
        super(R.layout.activity_game_image);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imageView = view.findViewById(R.id.activity_game_image_image);
        System.out.println("CREATING "+view);
    }

    @Override
    public void onStart() {
        super.onStart();
        QuizGame.GAME.addChangeListener(listener);
        setQuestionImage();
    }

    @Override
    public void onStop() {
        super.onStop();
        QuizGame.GAME.removeChangeListener(listener);
    }


    private void onGameStatusChange(QuizGame game, QuizGameStatus status) {
        switch (status) {
            case ANSWERING:
                setQuestionImage();
                break;
            case ANSWERED:
            case FINISHED:
                break;
        }
    }

    private void setQuestionImage() {
        String imgRes = QuizGame.GAME.getCurrentQuestion().getImage();
        if (imgRes != null) {
            imageView.setImageResource(getResources().getIdentifier(imgRes, "drawable",
                    getContext().getPackageName()));

        } else {
            imageView.setImageResource(0);
        }
    }
}
