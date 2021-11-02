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

import io.github.gaeqs.quiz.activity.ConfigurationActivity;
import io.github.gaeqs.quiz.data.User;
import io.github.gaeqs.quiz.data.UserAdapter;
import io.github.gaeqs.quiz.database.UserStorage;

/**
 * A fragment representing a list of Items.
 */
public class UserListComboBox extends Fragment {

    private Spinner spinner;

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

        UserAdapter adapter = new UserAdapter(spinner, this, this);
        spinner.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}