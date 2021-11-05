package io.github.gaeqs.quiz.util;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

import io.github.gaeqs.quiz.data.Question;

public class JSONUtils {


    /**
     * Reads the file that matches the given resource id as a text file.
     *
     * @param context the context of the application.
     * @param id      the id of the resource.
     * @return the text of the resource.
     * @throws IOException when something bad happens while reading the file.
     */
    public static String readString(Context context, int id) throws IOException {
        InputStream in = context.getResources().openRawResource(id);
        byte[] buffer = new byte[in.available()];
        in.read(buffer);
        in.close();
        return new String(buffer, StandardCharsets.UTF_8);
    }
}
