package io.github.gaeqs.quiz.activity;

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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

import io.github.gaeqs.quiz.R;
import io.github.gaeqs.quiz.data.UserAdapter;
import io.github.gaeqs.quiz.database.AppDatabase;
import io.github.gaeqs.quiz.database.User;

public class EditUserActivity extends AppCompatActivity {

    private Button confirmButton;
    private Spinner spinner;
    private UserAdapter adapter;

    private File photoFile;
    private Uri photoUri;
    private boolean hasImage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        confirmButton = findViewById(R.id.edit_user_confirm);
        spinner = findViewById(R.id.edit_user_combo_box);

        adapter = new UserAdapter(spinner, this, this, false);
        spinner.setAdapter(adapter);

        refreshConfirmStatus();
    }

    public void confirm(View view) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            User user = AppDatabase.createInstance(this).userDao().getUser(adapter.getSelectedUser());
            if (user == null) return;
            user.setImagePath(photoFile.getAbsolutePath());
            AppDatabase.createInstance(this).userDao().updateUsers(user);
        });

        finish();
    }

    public void takeImage(View view) {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        } else {
            openCamera();
        }
    }

    private void refreshConfirmStatus() {
        confirmButton.setEnabled(hasImage);
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

                        ImageView imageView = (ImageView) findViewById(R.id.edit_user_image);
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