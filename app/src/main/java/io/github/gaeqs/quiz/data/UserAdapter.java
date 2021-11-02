package io.github.gaeqs.quiz.data;

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

import io.github.gaeqs.quiz.R;
import io.github.gaeqs.quiz.database.UserStorage;
import io.github.gaeqs.quiz.util.Validate;

/**
 * The adapted used by a {@link RecyclerView} to display the {@link Answer} of the current {@link Question}.
 */
public class UserAdapter extends ArrayAdapter<User> {

    private final Spinner spinner;
    private final LiveData<List<User>> liveData;

    public UserAdapter(Spinner spinner, ViewModelStoreOwner viewModelOwner, LifecycleOwner lifecycleOwner) {
        super(spinner.getContext(), R.layout.user_dropdown_item);
        this.spinner = spinner;
        Validate.notNull(viewModelOwner, "Owner cannot be null!");
        Validate.notNull(lifecycleOwner, "Owner cannot be null!");
        liveData = new ViewModelProvider(viewModelOwner).get(UserStorage.class).getUsers();
        liveData.observe(lifecycleOwner, list -> {
            clear();
            addAll(list);
        });
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.user_dropdown_item, parent, false);
        }

        User user = (User) getItem(position);
        CardView cardView = convertView.findViewById(R.id.user_dropdown_item);
        TextView nameView = convertView.findViewById(R.id.user_dropdown_item_name);
        ImageView imageView = convertView.findViewById(R.id.user_dropdown_item_image);

        nameView.setText(user.getName());

        //try {
        //    Uri imageUri = Uri.fromFile(new File(user.getImagePath()));
        //    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);
        //    imageView.setImageBitmap(bitmap);
        //} catch (IOException e) {
        //    e.printStackTrace();
        //}


        cardView.setOnClickListener(v -> {
            spinner.setSelection(position);
            View root = cardView.getRootView();
            root.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK));
            root.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_BACK));
        });

        return cardView;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

}
