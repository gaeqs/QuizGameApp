package io.github.gaeqs.quiz.data;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import io.github.gaeqs.quiz.R;
import io.github.gaeqs.quiz.activity.ConfigurationActivity;
import io.github.gaeqs.quiz.database.UserStorage;
import io.github.gaeqs.quiz.util.Validate;

/**
 * The adapted used by a {@link RecyclerView} to display the {@link Answer} of the current {@link Question}.
 */
public class UserAdapter extends ArrayAdapter<User> {

    private final Spinner spinner;
    private final LiveData<List<User>> liveData;
    private final Map<User, Bitmap> userImages;
    private final User GUEST = new User(getContext().getString(R.string.guest), "");

    private String selectedUser;

    public UserAdapter(Spinner spinner, ViewModelStoreOwner viewModelOwner,
                       LifecycleOwner lifecycleOwner, boolean addGuest) {
        super(spinner.getContext(), R.layout.user_dropdown_item);
        this.spinner = spinner;
        Validate.notNull(viewModelOwner, "Owner cannot be null!");
        Validate.notNull(lifecycleOwner, "Owner cannot be null!");
        liveData = new ViewModelProvider(viewModelOwner).get(UserStorage.class).getUsers();
        userImages = new ViewModelProvider(viewModelOwner).get(UserBitmapStorage.class).bitmaps;
        liveData.observe(lifecycleOwner, list -> {
            clear();
            if (addGuest) {
                add(GUEST);
            }
            addAll(list);
            userImages.keySet().removeIf(it -> !list.contains(it));

            int index = 0;
            if (selectedUser != null) {
                for (User user : list) {
                    if (user.getName().equals(selectedUser)) break;
                    index++;
                }
                spinner.setSelection(index == list.size() ? 0 : index + 1);
            } else {
                spinner.setSelection(0);
            }
        });


        SharedPreferences preferences = getContext()
                .getSharedPreferences(ConfigurationActivity.PREFERENCES, 0);

        selectedUser = preferences.getString(ConfigurationActivity.PREFERENCES_USER, null);
    }

    public String getSelectedUser() {
        return selectedUser;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createView(position, convertView, parent, false);
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return createView(position, convertView, parent, true);
    }


    private View createView(int position, View convertView, ViewGroup parent, boolean dropdown) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.user_dropdown_item, parent, false);
        }

        User user = (User) getItem(position);
        Object tagUser = convertView.getTag();

        CardView cardView = convertView.findViewById(R.id.user_dropdown_item);
        if (tagUser == user) return cardView;

        TextView nameView = convertView.findViewById(R.id.user_dropdown_item_name);
        ImageView imageView = convertView.findViewById(R.id.user_dropdown_item_image);

        nameView.setText(user.getName());

        Bitmap bitmap = userImages.get(user);
        if (bitmap == null) {
            try {
                if (user.getImagePath() != null && !user.getImagePath().isEmpty()) {
                    Uri imageUri = Uri.fromFile(new File(user.getImagePath()));
                    bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);
                    userImages.put(user, bitmap);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return cardView;
            }
        }

        imageView.setImageBitmap(bitmap);

        if (dropdown) {
            cardView.setOnClickListener(v -> {
                spinner.setSelection(position);
                View root = cardView.getRootView();
                root.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
                root.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
                selectedUser = position == 0 ? null : getItem(position).getName();
            });
        } else {
            cardView.setOnClickListener(v -> spinner.performClick());
        }

        convertView.setTag(user);
        return cardView;
    }

}
