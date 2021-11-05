package io.github.gaeqs.quiz.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.github.gaeqs.quiz.R;
import io.github.gaeqs.quiz.database.User;
import io.github.gaeqs.quiz.database.UserStorage;
import io.github.gaeqs.quiz.util.Validate;

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.RankingViewHolder> {

    public static Comparator<User> RANKING_COMPARATOR = (o1, o2) -> {
        if (o1.getMaximumScore() == o2.getMaximumScore()) {
            if(o1.getMaximumScoreTime() == o2.getMaximumScoreTime()) {
                return Long.compare(o2.getLastPlayed(), o1.getLastPlayed());
            }
            return Long.compare(o1.getMaximumScoreTime(), o2.getMaximumScoreTime());
        }
        return o2.getMaximumScore() - o1.getMaximumScore();
    };

    private final Context context;
    private final Map<User, Bitmap> userImages;
    private final LiveData<List<User>> liveData;
    private List<User> sortedList = Collections.emptyList();

    public RankingAdapter(Context context, ViewModelStoreOwner viewModelOwner, LifecycleOwner lifecycleOwner) {
        this.context = context;
        Validate.notNull(context, "Context cannot be null!");
        Validate.notNull(viewModelOwner, "Owner cannot be null!");
        Validate.notNull(lifecycleOwner, "Owner cannot be null!");

        liveData = new ViewModelProvider(viewModelOwner).get(UserStorage.class).getUsers();
        userImages = new ViewModelProvider(viewModelOwner).get(UserBitmapStorage.class).bitmaps;
        liveData.observe(lifecycleOwner, list -> {
            userImages.keySet().removeIf(it -> !list.contains(it));
            sortedList = list.stream().sorted(RANKING_COMPARATOR).collect(Collectors.toList());
            notifyDataSetChanged();
        });
    }

    @NonNull
    @Override
    public RankingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.user_ranking_item, parent, false);

        return new RankingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RankingViewHolder holder, int position) {
        User user = sortedList.get(position);
        holder.name.setText(user.getName());

        holder.bestScore.setText(context.getString(R.string.ranking_best_score,
                user.getMaximumScore(), user.getMaximumScoreTime() / 1000));
        holder.matches.setText(context.getString(R.string.ranking_matches, user.getMatches()));

        if (user.getLastPlayed() > 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(user.getLastPlayed());
            DateFormat format = SimpleDateFormat.getInstance();
            holder.lastPlayed.setText(context.getString(R.string.ranking_last_played,
                    format.format(calendar.getTime())));
        } else {
            holder.lastPlayed.setText("");
        }


        Bitmap bitmap = userImages.get(user);
        if (bitmap == null) {
            try {
                if (user.getImagePath() != null && !user.getImagePath().isEmpty()) {
                    Uri imageUri = Uri.fromFile(new File(user.getImagePath()));
                    bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
                    userImages.put(user, bitmap);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        holder.image.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return sortedList.size();
    }

    public static class RankingViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name;
        TextView bestScore;
        TextView matches;
        TextView lastPlayed;

        public RankingViewHolder(@NonNull View view) {
            super(view);
            image = view.findViewById(R.id.user_ranking_item_image);
            name = view.findViewById(R.id.user_ranking_item_name);
            bestScore = view.findViewById(R.id.user_ranking_item_best_score);
            matches = view.findViewById(R.id.user_ranking_item_matches);
            lastPlayed = view.findViewById(R.id.user_ranking_item_last_played);
        }
    }
}
