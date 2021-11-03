package io.github.gaeqs.quiz.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.github.gaeqs.quiz.R;
import io.github.gaeqs.quiz.data.RankingAdapter;

public class RankingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        RecyclerView view = findViewById(R.id.ranking_recycler_view);
        RankingAdapter adapter = new RankingAdapter(this, this, this);
        view.setLayoutManager(new LinearLayoutManager(view.getContext()));
        view.setAdapter(adapter);
    }
}