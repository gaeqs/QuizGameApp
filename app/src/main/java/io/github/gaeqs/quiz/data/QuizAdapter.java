package io.github.gaeqs.quiz.data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashSet;
import java.util.Set;

import io.github.gaeqs.quiz.R;
import io.github.gaeqs.quiz.game.QuizGame;
import io.github.gaeqs.quiz.game.QuizGameStatus;
import io.github.gaeqs.quiz.util.Validate;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder> {

    private final Context context;
    private final Set<QuizViewHolder> holders;
    private QuizGame game;


    public QuizAdapter(Context context) {
        Validate.notNull(context, "Context cannot be null!");
        this.context = context;
        this.holders = new HashSet<>();
        this.game = null;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setGame(QuizGame game) {
        this.game = game;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.answer_card, parent, false);

        QuizViewHolder holder = new QuizViewHolder(view);
        holders.add(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        Answer answer = game.getCurrentQuestion().getAnswers().get(position);

        if (answer.getImage() != null) {
            int imageId = context.getResources().getIdentifier(answer.getImage(),
                    "drawable", context.getPackageName());
            System.out.println(imageId);
            holder.image.setImageResource(imageId);
        } else {
            holder.image.setImageResource(0);
        }
        holder.text.setText(answer.getName());
        holder.correct = answer.isCorrect();
    }

    @Override
    public int getItemCount() {
        return game == null ? 0 : game.getCurrentQuestion().getAnswers().size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void prepareForNextQuestion() {
        holders.forEach(QuizViewHolder::resetCardColor);
        notifyDataSetChanged();
    }

    public void showCorrectAnswers() {
        holders.forEach(QuizViewHolder::showCorrectAnswer);
    }

    public class QuizViewHolder extends RecyclerView.ViewHolder {

        CardView card;
        ImageView image;
        TextView text;
        boolean correct;

        public QuizViewHolder(@NonNull View view) {
            super(view);
            card = view.findViewById(R.id.answer_card);
            image = view.findViewById(R.id.answer_image);
            text = view.findViewById(R.id.answer_text);
            card.setOnClickListener(it -> {
                game.answer(correct);
            });
            if(game.getStatus() == QuizGameStatus.ANSWERED) {
                showCorrectAnswers();
            }
        }

        public void resetCardColor() {
            TypedValue v = new TypedValue();
            context.getTheme().resolveAttribute(R.attr.cardBackgroundColor, v, true);
            card.setCardBackgroundColor(v.data);
        }

        public void showCorrectAnswer() {
            if (correct) {
                card.setCardBackgroundColor(context.getResources()
                        .getColor(R.color.correct_answer, context.getTheme()));
            }
        }
    }

}
