package io.github.gaeqs.quiz.data;

import android.graphics.Bitmap;

import androidx.lifecycle.ViewModel;

import java.util.HashMap;
import java.util.Map;

import io.github.gaeqs.quiz.database.User;

public class UserBitmapStorage extends ViewModel {
    public Map<User, Bitmap> bitmaps = new HashMap<>();
}