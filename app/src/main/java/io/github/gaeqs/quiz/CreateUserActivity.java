package io.github.gaeqs.quiz;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import io.github.gaeqs.quiz.data.User;
import io.github.gaeqs.quiz.database.UserStorage;

public class CreateUserActivity extends AppCompatActivity {

    private Button confirmButton;
    private EditText nameTextField;

    private File photoFile;
    private Uri photoUri;
    private boolean hasImage = false;

    private List<User> users = Collections.emptyList();
    private UserStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        storage = new ViewModelProvider(this).get(UserStorage.class);

        confirmButton = findViewById(R.id.create_user_confirm);
        nameTextField = findViewById(R.id.create_user_name);

        nameTextField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                refreshConfirmStatus();
            }
        });

        storage.getUsers().observe(this, users -> {
            this.users = users;
            refreshConfirmStatus();
        });

        refreshConfirmStatus();
    }

    public void confirm(View view) {
        User user = new User(sanitizeUsername(nameTextField.getText().toString()), photoFile.getAbsolutePath());
        if (storage.addNewUser(user)) {
            finish();
        }
    }

    public void takeImage(View view) {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        } else {
            openCamera();
        }
    }

    private void refreshConfirmStatus() {
        String name = sanitizeUsername(nameTextField.getText().toString());
        confirmButton.setEnabled(hasImage
                && !name.isEmpty()
                && users.stream().noneMatch(it -> it.getName().equalsIgnoreCase(name)));
    }


    private String sanitizeUsername(String name) {
        return name.trim();
    }

    // region camera

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                photoFile = createImageFile();
                photoUri = FileProvider.getUriForFile(
                        this, "io.github.gaeqs.quiz.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                takePictureIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                cameraLauncher.launch(takePictureIntent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                .format(System.currentTimeMillis());
        String imageFileName = "PHOTO_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) openCamera();
            });

    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    try {
                        InputStream in = getContentResolver().openInputStream(photoUri);
                        Bitmap bm = BitmapFactory.decodeStream(in);
                        in.close();

                        ImageView imageView = (ImageView) findViewById(R.id.create_user_image);
                        imageView.setImageBitmap(bm);
                        imageView.setRotation(90);
                        hasImage = true;
                        refreshConfirmStatus();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });

    // endregion
}