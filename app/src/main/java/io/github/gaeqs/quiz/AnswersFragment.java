package io.github.gaeqs.quiz;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.function.BiConsumer;

import io.github.gaeqs.quiz.data.QuizAdapter;
import io.github.gaeqs.quiz.game.QuizGame;
import io.github.gaeqs.quiz.game.QuizGameStatus;

/**
 * A fragment representing a list of Items.
 */
public class AnswersFragment extends Fragment {

    private final BiConsumer<QuizGame, QuizGameStatus> changeListener = this::onGameStatusChange;

    private RecyclerView list;
    private QuizAdapter adapter;
    private boolean lastQuestionIsImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_answers_list, container, false);

        if (view instanceof RecyclerView) {
            list = (RecyclerView) view;
            Context context = view.getContext();
            adapter = new QuizAdapter(context);
            RecyclerView recyclerView = (RecyclerView) view;

            list.setLayoutManager(QuizGame.GAME.getCurrentQuestion().isImageQuestion()
                    ? new GridLayoutManager(list.getContext(), 2)
                    : new LinearLayoutManager(list.getContext()));

            lastQuestionIsImage = QuizGame.GAME.getCurrentQuestion().isImageQuestion();

            adapter.setGame(QuizGame.GAME);
            recyclerView.setAdapter(adapter);
            QuizGame.GAME.addChangeListener(changeListener);
            onGameStatusChange(QuizGame.GAME, QuizGame.GAME.getStatus());
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        QuizGame.GAME.removeChangeListener(changeListener);
    }

    private void onGameStatusChange(QuizGame game, QuizGameStatus status) {
        switch (status) {
            case ANSWERING:
                changeLayoutIfNeeded(game.getCurrentQuestion().isImageQuestion());
                adapter.prepareForNextQuestion();
                break;
            case ANSWERED:
                adapter.showCorrectAnswers();
                break;
        }
    }

    private void changeLayoutIfNeeded(boolean newQuestionIsImage) {
        if (lastQuestionIsImage == newQuestionIsImage) return;
        lastQuestionIsImage = newQuestionIsImage;

        list.setLayoutManager(newQuestionIsImage
                ? new GridLayoutManager(list.getContext(), 2)
                : new LinearLayoutManager(list.getContext()));

        list.setAdapter(adapter);
    }
}