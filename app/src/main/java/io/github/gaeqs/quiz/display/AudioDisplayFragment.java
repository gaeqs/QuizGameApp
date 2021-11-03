package io.github.gaeqs.quiz.display;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.function.BiConsumer;

import io.github.gaeqs.quiz.R;
import io.github.gaeqs.quiz.game.QuizGame;
import io.github.gaeqs.quiz.game.QuizGameStatus;

public class AudioDisplayFragment extends Fragment {

    private final BiConsumer<QuizGame, QuizGameStatus> listener = this::onGameStatusChange;
    private ImageButton playStopButton;
    private MediaPlayer mediaPlayer;
    private boolean prepared;

    public AudioDisplayFragment() {
        super(R.layout.activity_game_audio);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        playStopButton = view.findViewById(R.id.activity_game_audio_play_stop_button);
    }

    @Override
    public void onStart() {
        super.onStart();
        QuizGame.GAME.addChangeListener(listener);
        setQuestionAudio();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        QuizGame.GAME.removeChangeListener(listener);
    }


    private void onGameStatusChange(QuizGame game, QuizGameStatus status) {
        switch (status) {
            case ANSWERING:
                setQuestionAudio();
                break;
            case ANSWERED:
            case FINISHED:
                break;
        }
    }

    private void setQuestionAudio() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        prepared = false;
        String audRes = QuizGame.GAME.getCurrentQuestion().getAudio();
        if (audRes != null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioAttributes(
                    new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
            );
            try {
                int audioId = getResources().getIdentifier(audRes, "raw", getContext().getPackageName());
                mediaPlayer.setDataSource(getContext(), Uri.parse("android.resource://"
                        + getContext().getPackageName() + "/" + audioId));
            } catch (Exception e) {
                e.printStackTrace();
                mediaPlayer = null;
                return;
            }

            mediaPlayer.setLooping(true);

            playStopButton.setOnClickListener(view -> pauseOrStart());

            mediaPlayer.setOnPreparedListener(mp -> {
                prepared = true;
                mp.start();
                changeButtonIcon(true);
            });

            pauseOrStart();
        }
    }

    private void pauseOrStart() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            changeButtonIcon(false);
        } else {
            if (!prepared) {
                mediaPlayer.prepareAsync();
            } else {
                mediaPlayer.start();
                changeButtonIcon(true);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            prepared = false;
        }
    }

    private void changeButtonIcon(boolean playing) {
        playStopButton.setImageResource(playing
                ? android.R.drawable.ic_media_pause
                : android.R.drawable.ic_media_play);
    }
}
