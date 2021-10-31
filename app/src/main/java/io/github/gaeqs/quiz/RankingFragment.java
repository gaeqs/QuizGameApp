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

import io.github.gaeqs.quiz.data.QuizAdapter;
import io.github.gaeqs.quiz.game.QuizGame;

public class RankingFragment extends Fragment {


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
            //QuizGame.GAME.addChangeListener(changeListener);
            //onGameStatusChange(QuizGame.GAME, QuizGame.GAME.getStatus());
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //QuizGame.GAME.removeChangeListener(changeListener);
    }
}