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


    public static String readJSON(Context context, int id) throws IOException {
        InputStream in = context.getResources().openRawResource(id);
        byte[] buffer = new byte[in.available()];
        in.read(buffer);
        in.close();
        return new String(buffer, StandardCharsets.UTF_8);
    }

    public static List<Question> readQuestionsFromJSON(Context context, int id) throws IOException {
        String raw = readJSON(context, id);
        Gson gson = new Gson();

        Type listType = new TypeToken<List<Question>>() {
        }.getType();


        return gson.fromJson(raw, listType);
    }

}
