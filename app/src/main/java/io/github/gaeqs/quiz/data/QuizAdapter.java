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

/**
 * The adapted used by a {@link RecyclerView} to display the {@link Answer} of the current {@link Question}.
 */
public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder> {

    private final Context context;
    private final Set<QuizViewHolder> holders;
    private QuizGame game;


    /**
     * Creates the adapter.
     *
     * @param context the context of the application.
     */
    public QuizAdapter(Context context) {
        Validate.notNull(context, "Context cannot be null!");
        this.context = context;
        this.holders = new HashSet<>();
        this.game = null;
    }

    /**
     * Changes the game of the adapter.
     *
     * @param game the game.
     */
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
        holder.image.setImageResource(answer.getImage() != null
                ? context.getResources().getIdentifier(answer.getImage(),
                "drawable", context.getPackageName())
                : 0);
        holder.text.setText(answer.getName());
        holder.correct = answer.isCorrect();
    }

    @Override
    public int getItemCount() {
        return game == null ? 0 : game.getCurrentQuestion().getAnswers().size();
    }

    /**
     * Invalidates the context of the {@link RecyclerView}, making it
     * reload the answers.
     */
    @SuppressLint("NotifyDataSetChanged")
    public void prepareForNextQuestion() {
        holders.forEach(QuizViewHolder::resetCardColor);
        notifyDataSetChanged();
    }

    /**
     * Turns green the correct answers.
     */
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
                game.answer(getAdapterPosition());
            });
            if (game.getStatus() == QuizGameStatus.ANSWERED) {
                showCorrectAnswers();
            }
        }

        /**
         * Resets the color of the card.
         */
        public void resetCardColor() {
            TypedValue v = new TypedValue();
            context.getTheme().resolveAttribute(R.attr.cardBackgroundColor, v, true);
            card.setCardBackgroundColor(v.data);
        }

        /**
         * Turns green the card if this answer is correct.
         */
        public void showCorrectAnswer() {
            boolean selected = getAdapterPosition() == game.getSelectedAnswer();
            int color;
            if (correct) {
                if (selected) {
                    color = R.color.selected_correct_answer;
                } else {
                    color = R.color.correct_answer;
                }
            } else {
                if (selected) {
                    color = R.color.selected_wrong_answer;
                } else {
                    return;
                }
            }
            card.setCardBackgroundColor(context.getResources().getColor(color, context.getTheme()));
        }
    }

}
