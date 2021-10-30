package io.github.gaeqs.quiz.display;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.function.BiConsumer;

import io.github.gaeqs.quiz.R;
import io.github.gaeqs.quiz.game.QuizGame;
import io.github.gaeqs.quiz.game.QuizGameStatus;

public class VideoDisplayFragment extends Fragment {

    private final BiConsumer<QuizGame, QuizGameStatus> listener = this::onGameStatusChange;
    private VideoView videoView;

    public VideoDisplayFragment() {
        super(R.layout.activity_game_video);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        videoView = view.findViewById(R.id.activity_game_video_video);
    }

    @Override
    public void onStart() {
        super.onStart();
        QuizGame.GAME.addChangeListener(listener);
        setQuestionVideo();
    }

    @Override
    public void onStop() {
        super.onStop();
        QuizGame.GAME.removeChangeListener(listener);
    }


    private void onGameStatusChange(QuizGame game, QuizGameStatus status) {
        switch (status) {
            case ANSWERING:
                setQuestionVideo();
                break;
            case ANSWERED:
            case FINISHED:
                break;
        }
    }

    private void setQuestionVideo() {

        String vidRes = QuizGame.GAME.getCurrentQuestion().getVideo();
        if (vidRes != null) {
            videoView.setVideoURI(Uri.parse("android.resource://"
                    + getContext().getPackageName() + "/" + vidRes));
            videoView.setMediaController(new MediaController(getContext()));
            videoView.requestFocus();
            videoView.start();
        }
    }
}
