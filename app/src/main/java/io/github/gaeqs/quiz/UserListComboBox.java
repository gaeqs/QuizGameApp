package io.github.gaeqs.quiz;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.Arrays;
import java.util.List;

import io.github.gaeqs.quiz.data.User;
import io.github.gaeqs.quiz.database.UserStorage;

/**
 * A fragment representing a list of Items.
 */
public class UserListComboBox extends Fragment {

    private Spinner spinner;
    private final Observer<List<User>> observer = this::onListChange;

    public UserListComboBox() {
        super(R.layout.user_list_combo_box);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinner = view.findViewById(R.id.user_combo_box);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences preferences = parent.getContext()
                        .getSharedPreferences(ConfigurationActivity.PREFERENCES, 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(ConfigurationActivity.PREFERENCES_USER,
                        parent.getSelectedItem().toString());
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                SharedPreferences preferences = parent.getContext()
                        .getSharedPreferences(ConfigurationActivity.PREFERENCES, 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove(ConfigurationActivity.PREFERENCES_USER);
                editor.apply();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        UserStorage storage = new ViewModelProvider(this).get(UserStorage.class);
        storage.getUsers().observe(this, observer);
    }

    @Override
    public void onStop() {
        super.onStop();
        UserStorage storage = new ViewModelProvider(this).get(UserStorage.class);
        storage.getUsers().removeObserver(observer);
    }


    private void onListChange(List<User> list) {
        String[] array = new String[list.size()];
        int i = 0;
        for (User user : list) {
            array[i] = user.getName();
            i++;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        SharedPreferences preferences = getContext()
                .getSharedPreferences(ConfigurationActivity.PREFERENCES, 0);
        String user = preferences.getString(ConfigurationActivity.PREFERENCES_USER, null);
        if (user != null) {
            int index = Arrays.asList(array).indexOf(user);
            if (index == -1) return;
            spinner.setSelection(index);
        }
    }

}