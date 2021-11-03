package io.github.gaeqs.quiz.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import io.github.gaeqs.quiz.R;
import io.github.gaeqs.quiz.data.UserAdapter;
import io.github.gaeqs.quiz.database.UserStorage;

public class DeleteUserActivity extends AppCompatActivity {

    private UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_user);

        Button confirmButton = findViewById(R.id.delete_user_confirm);
        Spinner spinner = findViewById(R.id.delete_user_combo_box);

        adapter = new UserAdapter(spinner, this, this, false);
        spinner.setAdapter(adapter);
    }

    public void confirm(View view) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.delete_user_confirm_title)
                .setMessage(getString(R.string.delete_user_confirm_message, adapter.getSelectedUser()))
                .setPositiveButton(R.string.general_yes, (dialog, which) -> {
                    new ViewModelProvider(this).get(UserStorage.class)
                            .removeUser(adapter.getSelectedUser());
                    finish();
                })
                .setNegativeButton(R.string.general_no, null)
                .show();
    }
}