package io.github.gaeqs.quiz.data;

import android.graphics.Bitmap;

import androidx.lifecycle.ViewModel;

import java.util.HashMap;
import java.util.Map;

public class UserBitmapStorage extends ViewModel {
    public Map<User, Bitmap> bitmaps = new HashMap<>();
}